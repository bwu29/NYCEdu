package main.java.teacherDemographics.district;

import java.util.Collections;
import java.util.List;

public class TeachDistrictDemographics {
  private List<TeachDistDemsHelpers> Ethnicity_x_Dist;
//
//
//  public List<TeachDistDemsHelpers> getData(){
//    return Collections.unmodifiableList(this.district_code);
//  }
  public List<TeachDistDemsHelpers> getDistrictTeacherNumber(){return Collections.unmodifiableList(this.Ethnicity_x_Dist); }
  public List<TeachDistDemsHelpers> getTeacherAsian(){return Collections.unmodifiableList(this.Ethnicity_x_Dist);}
  public List<TeachDistDemsHelpers> getTeacherIndian(){return Collections.unmodifiableList(this.Ethnicity_x_Dist);}
  public List<TeachDistDemsHelpers> getTeacherBlack(){return Collections.unmodifiableList(this.Ethnicity_x_Dist);}
  public List<TeachDistDemsHelpers> getTeacherHispanic(){return Collections.unmodifiableList(this.Ethnicity_x_Dist);}
  public List<TeachDistDemsHelpers> getTeacherHawaiian(){return Collections.unmodifiableList(this.Ethnicity_x_Dist);}
  public List<TeachDistDemsHelpers> getTeacherWhite(){return Collections.unmodifiableList(this.Ethnicity_x_Dist);}
  public List<TeachDistDemsHelpers> getTeacherMultiracial(){return Collections.unmodifiableList(this.Ethnicity_x_Dist);}
  public List<TeachDistDemsHelpers> getTeacherUnknownOther(){return Collections.unmodifiableList(this.Ethnicity_x_Dist);}
  public List<TeachDistDemsHelpers> getNumTeachers(){return Collections.unmodifiableList(this.Ethnicity_x_Dist);}
}
