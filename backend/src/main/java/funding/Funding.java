package main.java.funding;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import main.java.funding.district.CommunitySchoolDistrict;
import main.java.funding.district.DistrictIncomeHelpers;
import main.java.funding.school.SchoolFundingHelpers;
import main.java.funding.school.MoshiSchoolFunding;
import main.java.utilities.CitywideData;
import main.java.utilities.ClassState;
import main.java.utilities.ErrorState;
import main.java.utilities.APIUtilities;
import main.java.utilities.ResponseUtilities;
import main.java.utilities.ServerState;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;

/**
 * This class handles the retrieval of school funding numbers. We have our constructor build three
 * maps where Key = School/District/City identifier and Value = Income because we have this data
 * locally. Because we don't send any specific requests to an external API, we build the map
 * ourselves as soon as the server is loaded because maps maintain constant look-up time and
 * are therefore more efficient for retrieving data.
 * This data is retrieved from
 * "<a href="https://infohub.nyced.org/reports/government-reports/local-law-171-of-2018">...</a>">Funding Data</a>
 * We downloaded this data as an Excel file, converted it to JSON format and directly stored the JSON in our datasources folder.
 */

public class Funding implements Route {

  String incomeFilepath;
  HashMap<String, String> schoolToIncomeMap;
  HashMap<String, String> districtToIncomeMap;

  public Funding() throws IOException {

    this.incomeFilepath = ClassState.fundraisingFilepath;

    this.buildSchoolIncomeMap();
    this.buildDistrictIncomeMap();

  }

  @Override
  public Object handle(Request request, Response response) throws Exception {

    if (!request.queryMap().hasKey(ServerState.type)){
      return ResponseUtilities.failure(ErrorState.type);
    }

    String type = request.queryParams(ServerState.type);

    switch(type){

      case ServerState.school:

        String schoolName = request.queryParams(ServerState.schoolName).replaceAll("%20", " ");

        if (request.queryMap().hasKey(ServerState.schoolName) &&
            this.schoolToIncomeMap.containsKey(schoolName)){

          if ((Objects.equals(this.schoolToIncomeMap.get(request.queryParams(ServerState.schoolName)), "N/A"))){

            return ResponseUtilities.failure(ErrorState.noSchoolData("funding"));

        } else{

          return (ResponseUtilities.success
              (this.schoolToIncomeMap.get(schoolName)));

        }}
        else{return ResponseUtilities.failure(ErrorState.schoolDNE);}

      case ServerState.district:
        if (request.queryMap().hasKey(ServerState.districtNum)) {
          String district = request.queryParams(ServerState.districtNum);
          if (Integer.parseInt(district) > ServerState.districtMax || Integer.parseInt(district) < ServerState.districtMin) {
            return ResponseUtilities.failure(ErrorState.districtOutOfBoundsError);
          } else {
            return ResponseUtilities.success
                (this.districtToIncomeMap.get(request.queryParams(ServerState.districtNum)));
          }
        }
        else{return ResponseUtilities.failure(ErrorState.noDistrictNumberGivenError);}

      case ServerState.city:
        return ResponseUtilities.success(CitywideData.citywideFunding);

      default:
        return ResponseUtilities.failure(ErrorState.type);
    }

  }
  public void buildSchoolIncomeMap() throws IOException {

    this.schoolToIncomeMap = new HashMap<>();

    List<SchoolFundingHelpers> schoolNames = APIUtilities.deserializeFromFilepath(
        MoshiSchoolFunding.class,
        this.incomeFilepath).getSchoolList();
    List<SchoolFundingHelpers> incomes = APIUtilities.deserializeFromFilepath(MoshiSchoolFunding.class,
        this.incomeFilepath).getIncomeList();

    for(int i=0; i<schoolNames.size(); i++){

      this.schoolToIncomeMap.put(schoolNames.get(i).getSchoolName(), incomes.get(i).getTotalIncome());
    }

  }

  public void buildDistrictIncomeMap() throws IOException {

    this.districtToIncomeMap = new HashMap<>();

    List<DistrictIncomeHelpers> districts = APIUtilities.deserializeFromFilepath(
        CommunitySchoolDistrict.class,
        this.incomeFilepath).getDistrictNumber();
    List<DistrictIncomeHelpers> districtIncomes = APIUtilities.deserializeFromFilepath(
        CommunitySchoolDistrict.class, this.incomeFilepath).getDistrictIncome();

    for(int i=0; i<districts.size(); i++){

      this.districtToIncomeMap.put
          (districts.get(i).getDistrictNumber(),
              districtIncomes.get(i).getDistrictIncome());
    }
  }
}
