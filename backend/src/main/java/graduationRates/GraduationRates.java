package main.java.graduationRates;

import java.io.IOException;
import java.net.URISyntaxException;
import main.java.absences.school.SchoolAbsences;
import main.java.graduationRates.district.DistrictGraduationRates;
import main.java.graduationRates.school.MoshiGradRates;
import main.java.utilities.APIUtilities;
import main.java.utilities.CitywideData;
import main.java.utilities.ClassState;
import main.java.utilities.ErrorState;
import main.java.utilities.ResponseUtilities;
import main.java.utilities.ServerState;
import spark.Request;
import spark.Response;
import spark.Route;

import java.net.URI;
import java.net.http.HttpResponse;

/**
 * This is our backend class for fetching graduation rates. This data is sourced from
 *
 * "<a href="https://infohub.nyced.org/reports/academics/graduation-results">...</a>">Graduation Rates Data</a>.
 *
 * We used the SODA API specified on NYCOpenData to retrieve the information. We retrieve information specifically from the cohort class of 2014, and specify those who graduate in four years by the August of their senior year.
 */
public class GraduationRates implements Route {

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
              .replaceAll(" ","%20").toUpperCase();

          String retrieval = this.schoolGradRates(schoolName);

          if (retrieval.equals(ErrorState.fail)){
            return ResponseUtilities.failure(ErrorState.schoolDNE);
          }

          return ResponseUtilities.success(retrieval);}

        else{return ResponseUtilities.failure(ErrorState.schoolDNE);}

      case ServerState.district:
        if (request.queryMap().hasKey(ServerState.districtNum)){

          String district = request.queryParams(ServerState.districtNum);

          if (Integer.parseInt(district) > ServerState.districtMax || Integer.parseInt(district) < ServerState.districtMin) {
            return ResponseUtilities.failure(ErrorState.districtOutOfBoundsError);
          } else {
            return ResponseUtilities.success(this.districtGradRates(district));
          }
        }

      case ServerState.city:
        return ResponseUtilities.success(CitywideData.citywideGraduationRate);

      default:

        return ResponseUtilities.failure(ErrorState.type);
    }

  }
  public String schoolGradRates(String schoolName)
      throws IOException, InterruptedException, URISyntaxException {

    URI accessSchool = new URI(ClassState.schoolLevelGraduationRatesAPIURL + schoolName);

    HttpResponse<String> gradHTTP =
        APIUtilities.buildHTTPRequestAndResponse(accessSchool);

    if (gradHTTP.body().length() == ErrorState.dataDNE){
      return ErrorState.fail;
    }
    else {
      return (APIUtilities.deserializeData(MoshiGradRates.class,
          gradHTTP.body()).FourYearGradRate() + ClassState.specifyGradRate);
    }
  }

  public String districtGradRates(String district)
      throws URISyntaxException, IOException, InterruptedException {

    URI accessDistrict = new URI(ClassState.districtLevelGraduationRatesAPIURL+district);

    HttpResponse<String> gradHTTP =
        APIUtilities.buildHTTPRequestAndResponse(accessDistrict);

    return (APIUtilities.deserializeData(DistrictGraduationRates.class,
        gradHTTP.body()).getDistrictGradRate()+ClassState.specifyGradRate);

  }
}
