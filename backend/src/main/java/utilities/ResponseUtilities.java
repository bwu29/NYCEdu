package main.java.utilities;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * This is our ResponseUtilities class. This class contains three static methods:
 * serialize(), success(), and failure(). These methods our useful for both of our handler classes,
 * and allow extensibility should a developer want to add an endpoint and use Moshi to
 * serialize data and/or send a response back to the user.
 */
public class ResponseUtilities {

  public ResponseUtilities(){}

  /**
   * serialize() uses Moshi to serialize a map with value of any type back into String that can
   * be read by the user.
   * @param response: we use type Map<String, Object> because this allows us to return a success
   *                or failure response to the user and any useful information such as the actual
   *                dataset or reason for failure.
   * @return The response ultimately sent back to the user in the form of a String.
   */
  public static String serialize(Map<String, Object> response) {
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map<String, Object>> adapter =
        moshi.adapter(Types.newParameterizedType(Map.class, String.class, Object.class));

    return adapter.toJson(response);
  }

  /**
   * success() sets the first key-value pair in the map to be result: success and then returns
   * the appropriate data back to the user. It uses the above serialize() method to return a string
   * of the successfully requested data
   * @param data: data from successful query
   * @return re-serialized form of data
   */
  public static String success(Object data) {
    Map<String, Object> successResponse = new HashMap<>();
    successResponse.put(ServerState.result, ServerState.success);
    successResponse.put(ServerState.data, data);
    return serialize(successResponse);
  }


  /**
   * failure() sets the first key-value pair in the map to be result: failure and then returns a
   * specific error message depending on what went wrong in the query. It uses the above serialize()
   * method to return a string back to the user
   * @param informativeMessage a message sent back to the user about what specifically went wrong
   * @return : string letting the user know their query failed
   */
  public static String failure(String informativeMessage) {
    Map<String, Object> failureResponse = new HashMap<>();
    failureResponse.put(ErrorState.result, ErrorState.fail);
    failureResponse.put(ErrorState.data, informativeMessage);
    return serialize(failureResponse);
  }

}
