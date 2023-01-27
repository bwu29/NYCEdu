package main.java.studentDemographics.district;

import java.util.Collections;
import java.util.List;

public class DistrictDemographics {

  private List<DistrictDemographicHelpers> District;

  public List<DistrictDemographicHelpers> getDistrictNumber(){return Collections.unmodifiableList(this.District); }
  public List<DistrictDemographicHelpers> getFemale(){return Collections.unmodifiableList(this.District); }
  public List<DistrictDemographicHelpers> getMale(){return Collections.unmodifiableList(this.District); }
  public List<DistrictDemographicHelpers> getAsian(){return Collections.unmodifiableList(this.District); }
  public List<DistrictDemographicHelpers> getBlack(){return Collections.unmodifiableList(this.District); }
  public List<DistrictDemographicHelpers> getHispanic(){return Collections.unmodifiableList(this.District); }
  public List<DistrictDemographicHelpers> getMultiracial(){return Collections.unmodifiableList(this.District); }
  public List<DistrictDemographicHelpers> getWhite(){return Collections.unmodifiableList(this.District); }
  public List<DistrictDemographicHelpers> getDisabilities(){return Collections.unmodifiableList(this.District); }
  public List<DistrictDemographicHelpers> getELL(){return Collections.unmodifiableList(this.District); }
  public List<DistrictDemographicHelpers> getPoverty(){return Collections.unmodifiableList(this.District); }
  public List<DistrictDemographicHelpers> getEconomicNeedIndex(){return Collections.unmodifiableList(this.District); }

}
