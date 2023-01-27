package main.java.utilities;

import com.squareup.moshi.Moshi;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * This is our API Utilities class. We created this static class because we have a wide range of
 * classes that all use very similar methods. The methods in this class largely make use of Moshi to
 * retrieve, serialize, and deserialize data from various JSONs. This class is mostly used to avoid
 * duplicate code and handle Moshi.
 */

public class APIUtilities {

  /**
   * Because of the way the JSON for the school names was being returned from the API, we needed to reformat
   * the JSON for Moshi to be able to parse it. This method is specifically for reformatting the school name
   * JSON and then deserializing it using Moshi.
   * @param classType: allows this method to be extensible for use by various class types
   * @param httpResponseBody: data returned from http of type String
   * @return: deserialized JSON in form of a specified Java object
   * @param <T>: allows this method to be extensible for use by various class types
   * @throws IOException : if moshi deserialization fails
   */
  public static <T> T deserializeSchoolNames(Class<T> classType, String httpResponseBody) throws IOException {

    Moshi moshi = new Moshi.Builder().build();

    String reformatted = "{ \"list_of_schools\": "+httpResponseBody +"}";

    return moshi.adapter(classType).fromJson(reformatted);
  }

  /**
   * Because of the way the JSON is being returned specifically from the SODA API, we needed to reformat the
   * response body in order for Moshi to be able to deserialize it. This method is specifically for any data
   * from NYC Open Data and retrieved and filtered using the SODA API.
   * @param classType: allows this method to be extensible for use by various class types
   * @param httpResponseBody : data returned from http of type String
   * @return : deserialized JSON in form of a specified Java object
   * @param <T> : allows this method to be extensible for use by various class types
   * @throws IOException : if moshi deserialization fails
   */
  public static <T> T deserializeData(Class<T> classType, String httpResponseBody) throws IOException {
    Moshi moshi = new Moshi.Builder().build();

    String reformatted = httpResponseBody.split("\\[")[1].split("]")[0];

    return moshi.adapter(classType).fromJson(reformatted);
  }

  /**
   * If data is retrieved locally from our backend, we deserialize this data using Java's Files methods.
   * This method is used specifically for data stored locally in our file.
   * @param classType: allows this method to be extensible for use by various class types
   * @param jsonBody : body of JSON in backend
   * @return : deserialized JSON in form of a specified Java object
   * @param <T> : allows this method to be extensible for use by various class types
   * @throws IOException : if moshi deserialization fails
   */
  public static <T> T deserializeFromFilepath(Class<T> classType, String jsonBody) throws IOException {

    String localJson = new String(Files.readAllBytes(Paths.get(jsonBody)));

    Moshi moshi = new Moshi.Builder().build();
    return moshi.adapter(classType).fromJson(localJson);
  }


  /**
   * Defining this method as static in another class allows us to avoid code duplication when we
   * have to build for and send responses to multiple different URIs.
   *
   * @param uriToHandle: the URI that the user would like to send and receive data from
   * @return HttpResponse<String>: the response from building and sending an HTTP request.
   * @throws IOException: the IOException is thrown by HTTPClient when method .send() fails.
   * @throws InterruptedException: the InterruptedException is thrown by HTTPClient when method
   *     .send() fails.
   */
  public static HttpResponse<String> buildHTTPRequestAndResponse(URI uriToHandle)
      throws IOException, InterruptedException {

    HttpRequest httpRequest = HttpRequest.newBuilder().uri(uriToHandle).GET().build();

    return HttpClient.newBuilder().build().send(httpRequest, BodyHandlers.ofString());
  }

}
