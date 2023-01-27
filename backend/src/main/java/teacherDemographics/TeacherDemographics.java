package main.java.teacherDemographics;

import main.java.teacherDemographics.district.TeachDistDemsHelpers;
import main.java.teacherDemographics.district.TeachDistrictDemographics;
import main.java.teacherDemographics.school.MoshiTeacherDemographics;
import main.java.utilities.APIUtilities;
import main.java.utilities.CitywideData;
import main.java.utilities.ClassState;
import main.java.utilities.ErrorState;
import main.java.utilities.ResponseUtilities;
import main.java.utilities.ServerState;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
/**
 * This is our backend class for fetching teacher demographic data. Our school-level data is retrieved from
 * "<a href="https://infohub.nyced.org/reports/government-reports/report-on-school-based-staff-demographics">...</a>">School-Level Teacher Demographic Data</a>.
 * We used the SODA API specified on NYCOpenData to retrieve the information. We retrieve information specifically from the 2017-2018 school year.
 * /
 * Our district level data is retrieved from
 * "<a href="https://data.cityofnewyork.us/Education/2019-2020-School-Year-Local-Law-226-Report-for-the/2jg5-6hqv">...</a>">District-Level Teacher Demographic Data</a>, under the Attachment section. We converted the given Excel file into JSON format and stored the JSON locally in our data sources folder.
 * Because district level demographics are stored locally on our backend, we create a Hashmap that stores the necessary data for each district as soon
 * as the server is started to provide constant time lookup for our frontend.
 * /
 * Citywide data was also retrieved from the Excel above.
 */
public class TeacherDemographics implements Route {

  HashMap<String, Object> districtTeacherDemographics;
  String teacherDemographicFilepath;

  public TeacherDemographics() throws IOException {
    this.teacherDemographicFilepath = ClassState.teacherDemographicsFilepath;
    this.buildDistrictTeacherDemographicsMap();
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
          return this.retrieveSchoolTeacherDemographics(schoolName);
        }
        else{return ResponseUtilities.failure(ErrorState.noSchoolData("teacher demographics"));}

      case ServerState.district:
        if (request.queryMap().hasKey(ServerState.districtNum)){
          String number = request.queryParams(ServerState.districtNum);
          if (Integer.parseInt(number) > 32 || Integer.parseInt(number) < 1){
            return ResponseUtilities.failure(ErrorState.districtOutOfBoundsError);
          } else {
            return ResponseUtilities.success(this.districtTeacherDemographics.get(number));
          }
        }
        else{return ResponseUtilities.failure(ErrorState.noDistrictNumberGivenError);}

      case ServerState.city:
        return ResponseUtilities.success(CitywideData.citywideTeacherDemographics());

      default:
        return ResponseUtilities.failure(ErrorState.type);
    }

  }

  private String retrieveSchoolTeacherDemographics(String schoolName)
          throws URISyntaxException, IOException, InterruptedException {

    URI accessSchool = new URI(ClassState.schoolLevelTeacherDemographicsAPIURL + schoolName);

    HttpResponse<String> demographicHTTP =
            APIUtilities.buildHTTPRequestAndResponse(accessSchool);

    if(this.invalidSchoolName(demographicHTTP.body())){
      return ResponseUtilities.failure(ErrorState.noSchoolData("teacher demographics"));
    }
    else{
      return ResponseUtilities.success(this.buildDemographicStats(demographicHTTP.body()));
    }
  }

  private boolean invalidSchoolName(String httpRequest){
    return (httpRequest.length()==3);
  }

  public HashMap<String, String> buildDemographicStats(String httpBody) throws IOException {

    DecimalFormat df = new DecimalFormat("0.00");

    HashMap<String,String> demographics = new HashMap<>();

    String percentAsian = APIUtilities.deserializeData(MoshiTeacherDemographics.class,
            httpBody).getPercentAsianTeacher();
    try{
      double percent = Double.parseDouble(percentAsian)*100;
      demographics.put(ClassState.Asian, String.valueOf(df.format(percent)));

    } catch (NumberFormatException e) {
      demographics.put(ClassState.Asian, percentAsian);
    }

    String percentBlack = APIUtilities.deserializeData(MoshiTeacherDemographics.class,
            httpBody).getPercentBlackTeacher();
    try{
      double percent = Double.parseDouble(percentBlack)*100;
      demographics.put(ClassState.Black, String.valueOf(df.format(percent)));

    } catch (NumberFormatException e) {
      demographics.put(ClassState.Black, percentBlack);
    }

    String percentHispanic = APIUtilities.deserializeData(MoshiTeacherDemographics.class,
            httpBody).getPercentHispanicTeacher();
    try{
      double percent = Double.parseDouble(percentHispanic)*100;
      demographics.put(ClassState.LatinX, String.valueOf(df.format(percent)));

    } catch (NumberFormatException e) {
      demographics.put(ClassState.LatinX, percentHispanic);
    }

    String percentWhite = APIUtilities.deserializeData(MoshiTeacherDemographics.class,
            httpBody).getPercentWhiteTeacher();
    try{
      double percent = Double.parseDouble(percentWhite)*100;
      demographics.put(ClassState.white, String.valueOf(df.format(percent)));

    } catch (NumberFormatException e) {
      demographics.put(ClassState.white, percentWhite);
    }

    String percentAmerindian = APIUtilities.deserializeData(MoshiTeacherDemographics.class,
            httpBody).getPercentAmerindianTeacher();
    try{
      double percent = Double.parseDouble(percentAmerindian)*100;
      demographics.put(ClassState.NativeAmerican, String.valueOf(df.format(percent)));

    } catch (NumberFormatException e) {
      demographics.put(ClassState.NativeAmerican, percentAmerindian);
    }

    String percentPacific = APIUtilities.deserializeData(MoshiTeacherDemographics.class,
            httpBody).getPercentPacificTeacher();
    try{
      double percent = Double.parseDouble(percentPacific)*100;
      demographics.put(ClassState.PI, String.valueOf(df.format(percent)));

    } catch (NumberFormatException e) {
      demographics.put(ClassState.PI, percentPacific);
    }

    return demographics;
  }

  private void buildDistrictTeacherDemographicsMap() throws IOException{
    this.districtTeacherDemographics = new HashMap<>();

    List<TeachDistDemsHelpers> districtNum = APIUtilities.deserializeFromFilepath(
            TeachDistrictDemographics.class,
            this.teacherDemographicFilepath).getDistrictTeacherNumber();

    List<TeachDistDemsHelpers> indian = APIUtilities.deserializeFromFilepath(
            TeachDistrictDemographics.class,
            this.teacherDemographicFilepath).getTeacherIndian();

    List<TeachDistDemsHelpers> asian = APIUtilities.deserializeFromFilepath(
            TeachDistrictDemographics.class,
            this.teacherDemographicFilepath).getTeacherAsian();

    List<TeachDistDemsHelpers> white = APIUtilities.deserializeFromFilepath(
            TeachDistrictDemographics.class,
            this.teacherDemographicFilepath).getTeacherWhite();

    List<TeachDistDemsHelpers> black = APIUtilities.deserializeFromFilepath(
            TeachDistrictDemographics.class,
            this.teacherDemographicFilepath).getTeacherBlack();

    List<TeachDistDemsHelpers> hispanic = APIUtilities.deserializeFromFilepath(
            TeachDistrictDemographics.class,
            this.teacherDemographicFilepath).getTeacherHispanic();

    List<TeachDistDemsHelpers> hawaiian = APIUtilities.deserializeFromFilepath(
            TeachDistrictDemographics.class,
            this.teacherDemographicFilepath).getTeacherHawaiian();

    List<TeachDistDemsHelpers> multiracial = APIUtilities.deserializeFromFilepath(
            TeachDistrictDemographics.class,
            this.teacherDemographicFilepath).getTeacherMultiracial();

    List<TeachDistDemsHelpers> unknownOther = APIUtilities.deserializeFromFilepath(
            TeachDistrictDemographics.class,
            this.teacherDemographicFilepath).getTeacherUnknownOther();

    List<TeachDistDemsHelpers> numberTeachers = APIUtilities.deserializeFromFilepath(
            TeachDistrictDemographics.class,
            this.teacherDemographicFilepath).getNumTeachers();


    for(int i=1; i<districtNum.size(); i++){
      HashMap<String, Object> data = new HashMap<>();

      String teachersInDistrict = numberTeachers.get(i).getNumTeachers();

      data.put(ClassState.NativeAmerican, this.getAverageNumber(indian.get(i).getTeacherIndian(), teachersInDistrict));
      data.put(ClassState.Asian, this.getAverageNumber(asian.get(i).getTeacherAsian(),teachersInDistrict));
      data.put(ClassState.Black, this.getAverageNumber(black.get(i).getTeacherBlack(),teachersInDistrict));
      data.put(ClassState.PI, this.getAverageNumber(hawaiian.get(i).getTeacherHawaiian(), teachersInDistrict));
      data.put(ClassState.LatinX, this.getAverageNumber(hispanic.get(i).getTeacherHispanic(),teachersInDistrict));
      data.put(ClassState.Multiracial, this.getAverageNumber(multiracial.get(i).getTeacherMultiracial(), teachersInDistrict));
      data.put(ClassState.white, this.getAverageNumber(white.get(i).getTeacherWhite(), teachersInDistrict));
      data.put(ClassState.unknown, this.getAverageNumber(unknownOther.get(i).getUnknownOther(), teachersInDistrict));

      this.districtTeacherDemographics.put(districtNum.get(i).getTeachDistrict(), data);
    }
  }

  private String getAverageNumber(String fraction, String totalTeachers){
    if (Objects.equals(fraction, "*")){
      return "none";
    } else {
      double race = Double.parseDouble(fraction);
      double teachers = Double.parseDouble(totalTeachers);
      Double average = ((race / teachers)*100);

      DecimalFormat df = new DecimalFormat("0.00");

      return df.format(average);
    }
  }
}
