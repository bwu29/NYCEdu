package main.java.utilities;

/**
 * This is our public final static ErrorState class. Specific error messages that we want to send
 * to the user are written here.
 * Having these defined in a separate class as static, final variables practices defensive programming
 * and also makes our code more readable.
 */
public class ErrorState {

  public static String noSchoolData(String type){
    return "No "+type+" data available for this school. ";
  }

  public static final int dataDNE = 3;
  public static final String schoolDNE = "This school cannot be found. Please ensure school name is complete and spelled correctly.";

  public static final String schoolNameError = "Please specify the school name";

  public static final String districtOutOfBoundsError = "Please search for a district that exists";
  public static final String noDistrictNumberGivenError = "Please specify a district number";
  public static final String type = "Please specify the type: school, district or city";
  public static final String fail = "fail";
  public static final String result = "result";
  public static final String coordinateErr = "Please provide two valid coordinates.";
  public static final String data = "data";


}
