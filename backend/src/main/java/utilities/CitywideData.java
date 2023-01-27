package main.java.utilities;

import java.util.HashMap;

/**
 * This is our public final static CitywideData class. Because information about the city is specific
 * for each endpoint and only requires one particular piece of information, we define it here. This
 * allows for constant time lookup for our frontend and avoids any unnecessary data processing.
 *
 * Having these defined in a separate class as static, final variables practices defensive programming and also makes our code more readable.
 *
 */

public class CitywideData {

  public static final String citywideFunding = "$188,762,935.00";
  public static final String citywideAbsenteeism = "26.8% of students";
  public static final String citywideGraduationRate = "74.3% of students";

  public static HashMap<String, String> citywideStudentDemographics(){

    HashMap<String, String> studentDemographics = new HashMap<>();

    studentDemographics.put("Female: ", "48.6%");
    studentDemographics.put("Male: ", "51.4%");
    studentDemographics.put("Asian: ", "16.6%");
    studentDemographics.put("Black: ", "24.4%");
    studentDemographics.put("Hispanic: ", "41.1%");
    studentDemographics.put("White: ", "14.7%");
    studentDemographics.put("Students with Disabilities: ", "20.6%");
    studentDemographics.put("English Language Learners: ", "13.9%");
    studentDemographics.put("Economically Disadvantaged", "71.9%");

    return studentDemographics;
  }

  public static HashMap<String, String> citywideTeacherDemographics(){
    HashMap<String, String> data = new HashMap<>();

    data.put("American Indian/Alaskan Native", "0.035");
    data.put("Asian", "6.44");
    data.put("Black/African American", "21.25");
    data.put("Hawaiian/Pacific Islander", "0");
    data.put("Hispanic", "18.12");
    data.put("Multiple", "0.48");
    data.put("White", "50.82");
    data.put("Unknown/Other", "2.60");

    return data;
  }

}
