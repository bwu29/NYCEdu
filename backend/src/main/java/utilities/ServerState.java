package main.java.utilities;

/**
 * This is our public static final ServerState class. If a variable is used more than once throughout
 * our program, or communicates information about the state of our server, we define it here.
 *
 * Having these defined in a separate class as static, final variables practices defensive programming
 * and also makes our code more readable.
 */
public final class ServerState {

  public static final String type = "type";
  public static final String school = "school";
  public static final String schoolName = "school_name";

  public static final String district = "district";
  public static final String districtNum = "number";
  public static final int districtMax = 32;
  public static final int districtMin = 1;

  public static final String city = "city";

  public static final String result = "result";
  public static final String data = "data";
  public static final String success = "success";

}
