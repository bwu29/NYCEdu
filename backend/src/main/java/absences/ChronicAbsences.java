package main.java.absences;

import java.io.IOException;
import java.net.URISyntaxException;
import main.java.absences.district.DistrictAbsences;
import main.java.absences.school.SchoolAbsences;
import main.java.utilities.CitywideData;
import main.java.utilities.ClassState;
import main.java.utilities.ErrorState;
import main.java.utilities.APIUtilities;
import main.java.utilities.ResponseUtilities;
import main.java.utilities.ServerState;
import spark.Request;
import spark.Response;
import spark.Route;

import java.net.URI;
import java.net.http.HttpResponse;

/**
 * This is our backend class for fetching chronic absence data. This data is sourced from
 *
 * "<a href="https://infohub.nyced.org/reports/school-quality/information-and-data-overview/end-of-year-attendance-and-chronic-absenteeism-data">...</a>">Chronic Absenteeism Data</a>.
 *
 * We used the SODA API specified on NYCOpenData to retrieve the information. We retrieve information specifically from the 2017-2018 school year.
 */

public class ChronicAbsences implements Route {


  @Override
  public Object handle(Request request, Response response) throws Exception {

    if (!request.queryMap().hasKey(ServerState.type)){
      return ResponseUtilities.failure(ErrorState.type);
    }

    String type = request.queryParams(ServerState.type);

    switch(type){

      case ServerState.school:

        if (request.queryMap().hasKey(ServerState.schoolName)){

          String schoolName = request.queryParams(ServerState.schoolName)
              .replaceAll(" ","%20");

          String retrieval = this.retrieveSchoolAbsences(schoolName);

          if (retrieval.equals(ErrorState.fail)){
            return ResponseUtilities.failure(ErrorState.schoolDNE);
          }

          return ResponseUtilities.success(retrieval);}
        else{

          return ResponseUtilities.failure(ErrorState.schoolDNE);}

      case ServerState.district:
        if (request.queryMap().hasKey(ServerState.districtNum)) {

          String district = request.queryParams(ServerState.districtNum);
          if (Integer.parseInt(district) > ServerState.districtMax || Integer.parseInt(district) < ServerState.districtMin) {
            return ResponseUtilities.failure(ErrorState.districtOutOfBoundsError);
          } else {
            return ResponseUtilities.success(this.retrieveDistrictAbsences(district));
          }
        }
        else{
          return ResponseUtilities.failure(ErrorState.noDistrictNumberGivenError);
        }

      case ServerState.city:
        return ResponseUtilities.success(CitywideData.citywideAbsenteeism);

      default:

        return ResponseUtilities.failure(ErrorState.type);
    }
  }
  public String retrieveSchoolAbsences(String schoolName)
      throws URISyntaxException, IOException, InterruptedException {

    URI accessSchool = new URI(ClassState.schoolLevelAbsenteeAPIURL + schoolName);

    HttpResponse<String> chronicAbsencesHTTP =
        APIUtilities.buildHTTPRequestAndResponse(accessSchool);

    if (chronicAbsencesHTTP.body().length() == ErrorState.dataDNE){
      return ErrorState.fail;
    }
    else {
      return (APIUtilities.deserializeData(SchoolAbsences.class,
          chronicAbsencesHTTP.body()).getAbsences() + ClassState.specifyAbsences);
    }
  }

  public String retrieveDistrictAbsences(String districtNumber)
      throws URISyntaxException, IOException, InterruptedException {

    URI accessDistrict = new URI(ClassState.districtLevelAbsenteeAPIURL+districtNumber);

    HttpResponse<String> chronicAbsencesHTTP =
        APIUtilities.buildHTTPRequestAndResponse(accessDistrict);

    return (APIUtilities.deserializeData(DistrictAbsences.class,
        chronicAbsencesHTTP.body()).getDistrictAbsences()+ClassState.specifyAbsences);
  }
}
