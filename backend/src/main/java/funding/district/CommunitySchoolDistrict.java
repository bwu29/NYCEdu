package main.java.funding.district;

import java.util.Collections;
import java.util.List;
import main.java.funding.district.DistrictIncomeHelpers;

public class CommunitySchoolDistrict {

  private List<DistrictIncomeHelpers> community_school_district;

  public List<DistrictIncomeHelpers> getDistrictNumber()
  {return Collections.unmodifiableList(this.community_school_district);}

  public List<DistrictIncomeHelpers> getDistrictIncome()
  {return Collections.unmodifiableList(this.community_school_district);}
}
