package main.java.teacherDemographics.school;

public class MoshiTeacherDemographics {

  private String teacher_ethnicity_asian_pct;

  private String teacher_ethnicity_black_pct;

  private String teacher_ethnicity_hispanic;

  private String teacher_ethnicity_white_pct;

  private String teacher_ethnicity_amerindian;

  private String teacher_ethnicity_pacific;

  public String getPercentAsianTeacher(){
    return this.teacher_ethnicity_asian_pct;}

  public String getPercentBlackTeacher(){
    return this.teacher_ethnicity_black_pct;}

  public String getPercentHispanicTeacher(){return this.teacher_ethnicity_hispanic;}

  public String getPercentWhiteTeacher(){return this.teacher_ethnicity_white_pct;}

  public String getPercentAmerindianTeacher(){return this.teacher_ethnicity_amerindian;}

  public String getPercentPacificTeacher(){return this.teacher_ethnicity_pacific;}

}
