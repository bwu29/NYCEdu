package main.java.schoolNameRetrieval;

import java.net.URI;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import main.java.utilities.ClassState;
import main.java.utilities.ErrorState;
import main.java.utilities.ResponseUtilities;
import main.java.utilities.ServerState;
import main.java.utilities.APIUtilities;
import main.java.schoolNameRetrieval.moshiHelpers.ListOfSchools;
import main.java.schoolNameRetrieval.moshiHelpers.LocationName;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * This class retrieves the school name based on the lat and lon the user selected, communicated from the front end.
 * This is the first point of contact between the front end and the back end. After the latitude and longitude, everything
 * else retrieved is from the backend based on school name, district number, or city.
 *
 * The data for this class is sourced from:
 * "<a href="https://data.cityofnewyork.us/Education/2017-2018-School-Locations/p6h4-mpyy">...</a>">School Location Data</a>.
 *
 * We used the SODA API specified on NYCOpenData to retrieve the information. We retrieve information based on latitude and longitude coordinates
 * from user clicks in the front end. In some cases, more than one high school exists at one location. In this case, we return a list
 * of schools for the user to then further specify.
 */
public class SchoolFromLocation implements Route {
  private Double x;
  private Double y;

  @Override
  public Object handle(Request request, Response response) throws Exception {

    if(request.queryMap().toMap().size() != ClassState.schoolNameQueryLim){ //makes sure query is size 2
      return ResponseUtilities.failure(ErrorState.coordinateErr);
    }
    try{ //makes sure its given actual coordinates
      this.checkDouble(request);
    }
    catch(NumberFormatException e){
      return ResponseUtilities.failure(ErrorState.coordinateErr);
    }

    URI accessSchoolName = new URI(ClassState.schoolCoordinates(this.x, this.y));

    HttpResponse<String> schoolNameAPIResponse =
        APIUtilities.buildHTTPRequestAndResponse(accessSchoolName);

     List<LocationName> schoolNames = APIUtilities.deserializeSchoolNames(ListOfSchools.class,
            schoolNameAPIResponse.body())
        .getLocationNames();

    List<String> schools = new ArrayList<>();

    for(LocationName school: schoolNames){
      schools.add(school.getLocationName());
    }
    return ResponseUtilities.success(schools);
  }


  private void checkDouble(Request request) throws NumberFormatException{

    this.x = Double.parseDouble(request.queryParams(ClassState.xCoord));
    this.y = Double.parseDouble(request.queryParams(ClassState.yCoord));

    if (Math.abs(this.x) > ClassState.latMax
        || Math.abs(this.y) > ClassState.lonMax) {
      throw new NumberFormatException();
    }

  }


}
