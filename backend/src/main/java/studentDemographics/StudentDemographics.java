package main.java.studentDemographics;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import main.java.studentDemographics.district.DistrictDemographicHelpers;
import main.java.studentDemographics.district.DistrictDemographics;
import main.java.utilities.CitywideData;
import main.java.utilities.ClassState;
import main.java.utilities.ErrorState;
import main.java.utilities.ResponseUtilities;
import main.java.utilities.ServerState;
import main.java.utilities.APIUtilities;
import main.java.studentDemographics.school.MoshiDemographics;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * This is our backend class for fetching student demographic data. Our school-level data is retrieved from
 * "<a href="https://data.cityofnewyork.us/Education/2013-2018-Demographic-Snapshot-School/s52a-8aq6">...</a>">School-Level Demographic Data</a>.
 * We used the SODA API specified on NYCOpenData to retrieve the information. We retrieve information specifically from the 2017-2018 school year.
 * /
 * Our district level data is retrieved from
 * "<a href="https://infohub.nyced.org/reports/school-quality/information-and-data-overview">...</a>">District-Level Demographic Data</a>, under the Demographic
 * Snapshot header. We converted the given Excel file into JSON format and stored the JSON locally in our data sources folder.
 * Because district level demographics are stored locally on our backend, we create a Hashmap that stores the necessary data for each district as soon
 * as the server is started to provide constant time lookup for our frontend.
 * /
 * Citywide data was retrieved from
 * "<a href="https://www.schools.nyc.gov/about-us/reports/doe-data-at-a-glance">...</a>">Citywide Demographic Data</a>
 */
public class StudentDemographics implements Route {

  String studentDemographicFilepath;
  HashMap<String, String> schoolDemographics;
  HashMap<String, Object> districtDemographics;

  public StudentDemographics() throws IOException {
    this.studentDemographicFilepath = ClassState.studentDemographicsFilepath;
    this.buildDistrictDemographicsMap();
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {

    if (!request.queryMap().hasKey(ServerState.type)){
      return ResponseUtilities.failure(ErrorState.type);
    }

    String type = request.queryParams(ServerState.type);

    switch(type){

      case ServerState.school:

        if (request.queryMap().hasKey(ServerState.schoolName)) {
          String schoolName = request.queryParams(ServerState.schoolName)
              .replaceAll(" ", "%20");
          return this.retrieveSchoolDemographics(schoolName);
        }
        else{return ResponseUtilities.failure(ErrorState.schoolDNE);}

      case ServerState.district:
        if (request.queryMap().hasKey(ServerState.districtNum)){
          String number = request.queryParams(ServerState.districtNum);
          if (Integer.parseInt(number) > ServerState.districtMax || Integer.parseInt(number) < ServerState.districtMin){
            return ResponseUtilities.failure(ErrorState.districtOutOfBoundsError);
          } else {

            return ResponseUtilities.success(this.districtDemographics.get(number));
          }
        }
        else{return ResponseUtilities.failure(ErrorState.noDistrictNumberGivenError);}

      case ServerState.city:
        return ResponseUtilities.success(CitywideData.citywideStudentDemographics());

      default:
        return ResponseUtilities.failure(ErrorState.type);
    }
  }

  private boolean invalidSchoolName(String httpRequest){
    return (httpRequest.length()==3);
  }

  public Map<String, String> buildDemographicStats(String httpBody) throws IOException {

    this.schoolDemographics = new HashMap<>();

    String percentFemale = APIUtilities.deserializeData(MoshiDemographics.class,
        httpBody).getPercentFemale();
    this.schoolDemographics.put(ClassState.female, percentFemale);

    String percentMale = APIUtilities.deserializeData(MoshiDemographics.class,
        httpBody).getPercentMale();
    this.schoolDemographics.put(ClassState.male, percentMale);

    String percentAsian = APIUtilities.deserializeData(MoshiDemographics.class,
        httpBody).getPercentAsian();
    this.schoolDemographics.put(ClassState.Asian, percentAsian);

    String percentBlack = APIUtilities.deserializeData(MoshiDemographics.class,
        httpBody).getPercentBlack();
    this.schoolDemographics.put(ClassState.Black, percentBlack);

    String percentHispanic = APIUtilities.deserializeData(MoshiDemographics.class,
        httpBody).getPercentHispanic();
    this.schoolDemographics.put(ClassState.LatinX, percentHispanic);

    String percentWhite = APIUtilities.deserializeData(MoshiDemographics.class,
        httpBody).getPercentWhite();
    this.schoolDemographics.put(ClassState.white, percentWhite);

    String percentWithDisability = APIUtilities.deserializeData(MoshiDemographics.class,
        httpBody).getPercentDisability();
    this.schoolDemographics.put(ClassState.disability, percentWithDisability);

    String percentELL = APIUtilities.deserializeData(MoshiDemographics.class,
        httpBody).getPercentELL();
    this.schoolDemographics.put(ClassState.ell, percentELL);

    String percentPoverty = APIUtilities.deserializeData(MoshiDemographics.class,
        httpBody).getPercentPoverty();
    this.schoolDemographics.put(ClassState.poverty, percentPoverty);

    return this.schoolDemographics;
  }

  public String retrieveSchoolDemographics(String schoolName)
      throws URISyntaxException, IOException, InterruptedException {

    URI accessDemographics = new URI(ClassState.schoolLevelDemographicsAPIURL+ schoolName);

    HttpResponse<String> demographicHTTP =
        APIUtilities.buildHTTPRequestAndResponse(accessDemographics);

    if(this.invalidSchoolName(demographicHTTP.body())){
      return ResponseUtilities.failure(ErrorState.noSchoolData("student demographics"));
    }
    else{
      return ResponseUtilities.success(this.buildDemographicStats(demographicHTTP.body()));
    }
  }
  public void buildDistrictDemographicsMap() throws IOException {

    this.districtDemographics = new HashMap<>();

    List<DistrictDemographicHelpers> districtNum = APIUtilities.deserializeFromFilepath(
        DistrictDemographics.class,
        this.studentDemographicFilepath).getDistrictNumber();

    List<DistrictDemographicHelpers> female = APIUtilities.deserializeFromFilepath(
        DistrictDemographics.class,
        this.studentDemographicFilepath).getFemale();

    List<DistrictDemographicHelpers> male = APIUtilities.deserializeFromFilepath(
        DistrictDemographics.class,
        this.studentDemographicFilepath).getMale();

    List<DistrictDemographicHelpers> Asian = APIUtilities.deserializeFromFilepath(
        DistrictDemographics.class,
        this.studentDemographicFilepath).getAsian();

    List<DistrictDemographicHelpers> Black = APIUtilities.deserializeFromFilepath(
        DistrictDemographics.class,
        this.studentDemographicFilepath).getBlack();

    List<DistrictDemographicHelpers> Hispanic = APIUtilities.deserializeFromFilepath(
        DistrictDemographics.class,
        this.studentDemographicFilepath).getHispanic();

    List<DistrictDemographicHelpers> Multiracial = APIUtilities.deserializeFromFilepath(
        DistrictDemographics.class,
        this.studentDemographicFilepath).getMultiracial();

    List<DistrictDemographicHelpers> white = APIUtilities.deserializeFromFilepath(
        DistrictDemographics.class,
        this.studentDemographicFilepath).getWhite();

    List<DistrictDemographicHelpers> Disabilities = APIUtilities.deserializeFromFilepath(
        DistrictDemographics.class,
        this.studentDemographicFilepath).getDisabilities();

    List<DistrictDemographicHelpers> ELL = APIUtilities.deserializeFromFilepath(
        DistrictDemographics.class,
        this.studentDemographicFilepath).getELL();

    List<DistrictDemographicHelpers> poverty = APIUtilities.deserializeFromFilepath(
        DistrictDemographics.class,
        this.studentDemographicFilepath).getPoverty();

    for(int i=0; i<districtNum.size(); i++){
      HashMap<String, Object> data = new HashMap<>();
      data.put(ClassState.female, female.get(i).getFemale());
      data.put(ClassState.male, male.get(i).getMale());
      data.put(ClassState.Asian, Asian.get(i).getAsian());
      data.put(ClassState.Black, Black.get(i).getBlack());
      data.put(ClassState.LatinX, Hispanic.get(i).getHispanic());
      data.put(ClassState.Multiracial, Multiracial.get(i).getMultiracial());
      data.put(ClassState.white, white.get(i).getWhite());
      data.put(ClassState.disability, Disabilities.get(i).getDisabilities());
      data.put(ClassState.ell, ELL.get(i).getELL());
      data.put(ClassState.poverty, poverty.get(i).getPoverty());

      this.districtDemographics.put(districtNum.get(i).getDistrict(), data);
    }
  }
}
