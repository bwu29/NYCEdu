package main.java.teacherDemographics.district;

import org.w3c.dom.ls.LSOutput;

public class TeachDistDemsHelpers {


  private String American_Indian_Alaskan_Native;
  private String Asian;
  private String Black_African_American;
  private String Hawaiian_Pacific_Islander;
  private String Hispanic;
  private String White;
  private String Multiple;
  private String Unknown_Other;
  private String district_code;
  private String Teachers;

  public String getNumTeachers(){
    return this.Teachers;
  }

  public String getTeachDistrict(){
    return this.district_code;
  }

  public String getTeacherIndian() {
   return this.American_Indian_Alaskan_Native;
  }

  public String getTeacherAsian(){
    return this.Asian;
  }

  public String getTeacherBlack(){
    return this.Black_African_American;
  }

  public String getTeacherHawaiian(){
    return this.Hawaiian_Pacific_Islander;
  }

  public String getTeacherHispanic(){
    return this.Hispanic;
  }

  public String getTeacherWhite(){
    return this.White;
  }

  public String getTeacherMultiracial(){
    return this.Multiple;
  }

  public String getUnknownOther(){
    return this.Unknown_Other;
  }

}
