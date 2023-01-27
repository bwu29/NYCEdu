package main.java.utilities;

/**
 * This is our public final static class ClassState. If we are trying to communicate specific
 * information about a class, or are retrieving  information, such as datasources and APIs
 * specific to a particular class, we define it here. In other words, if information is only retrieved in one class,
 * we define it here.
 *
 * Having these defined in a separate class as static, final variables practices defensive programming and also makes our code more readable.
 */
public final class ClassState {

  public static final String fundraisingFilepath = "backend/src/main/datasources/pta_fundraising_data.json";

  public static final String schoolLevelAbsenteeAPIURL = "https://data.cityofnewyork.us/resource/26je-vkp6.json?school_name=";

  public static final String specifyAbsences = " % of students were chronically absent";

  public static final String districtLevelAbsenteeAPIURL = "https://data.cityofnewyork.us/resource/hags-jh3e.json?year=2017-18&&grade=All%20Grades&&district=";
  public static final String schoolLevelGraduationRatesAPIURL = "https://data.cityofnewyork.us/resource/kybe-9iex.json?cohort=4%20year%20August&&cohort_year=2014&&demographic_category=All%20Students&&school_name=";
  public static final String districtLevelGraduationRatesAPIURL = "https://data.cityofnewyork.us/resource/qvdv-dru4.json?demographic_category=All%20Students&cohort_year=2014&&cohort=4%20year%20August&&district=";
  public static final String specifyGradRate = " % of students graduated in 4 years";

  public static String schoolCoordinates(Double x, Double y){
    return "https://data.cityofnewyork.us/resource/p6h4-mpyy.json?$where=within_circle(location_1,"
        + x+","+y+",10)&location_category_description=High%20school";}

  public static final int schoolNameQueryLim = 2;
  public static final int latMax = 90;
  public static final int lonMax = 180;
  public static final String xCoord = "x";
  public static final String yCoord = "y";

  public static final String studentDemographicsFilepath = "backend/src/main/datasources/student_ethnicity.json";

  public static final String female = "Female";
  public static final String male = "Male";
  public static final String Asian = "Asian";
  public static final String Black = "Black";
  public static final String LatinX = "LatinX";
  public static final String Multiracial = "Multiracial";
  public static final String white = "White";
  public static final String PI = "Hawaiian/ Pacific Islander";
  public static final String NativeAmerican = "Native American";
  public static final String unknown = "Unknown/ other";
  public static final String disability = "Students with Disabilities";
  public static final String ell = "English Language Learner";
  public static final String poverty = "Students in poverty";
  public static final String schoolLevelDemographicsAPIURL = "https://data.cityofnewyork.us/resource/s52a-8aq6.json?year=2017-18&&school_name=";
  public static final String teacherDemographicsFilepath = "backend/src/main/datasources/teacher_ethnicity.json";
  public static final String schoolLevelTeacherDemographicsAPIURL = "https://data.cityofnewyork.us/resource/26je-vkp6.json?school_name=";


}
