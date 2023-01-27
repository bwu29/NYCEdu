package main.java.funding.school;

import java.util.Collections;
import java.util.List;
import main.java.funding.school.SchoolFundingHelpers;

public class MoshiSchoolFunding {
  private List<SchoolFundingHelpers> School;

  public List<SchoolFundingHelpers> getIncomeList(){return Collections.unmodifiableList(this.School);}

  public List<SchoolFundingHelpers> getSchoolList(){return Collections.unmodifiableList(this.School);}

}
