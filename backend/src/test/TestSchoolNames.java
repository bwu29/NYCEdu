import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.java.utilities.ServerState;
import main.java.schoolNameRetrieval.SchoolFromLocation;
import okio.Buffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import spark.Spark;

public class TestSchoolNames {

  @BeforeAll
  public static void setup_before_everything() {
    Spark.port(0);
    // remove logging spam during tests
    Logger.getLogger("").setLevel(Level.WARNING);
  }

  @AfterEach
  public void teardown() {
    Spark.unmap("schoolName");
    Spark.unmap("teacherDemographics");
    Spark.unmap("absences");
    Spark.unmap("graduationRates");
    Spark.unmap("funding");
    Spark.unmap("studentDemographics");
    Spark.stop();
    Spark.awaitStop();
  }

  private static HttpURLConnection tryRequest(String apiCall) throws IOException {

    URL requestURL = new URL("http://localhost:" + Spark.port() + "/" + apiCall);
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
    clientConnection.connect();
    return clientConnection;
  }

  private Map<String, Object> deserializationHelper(HttpURLConnection connection)
      throws IOException {

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map<String, Object>> adapter =
            moshi.adapter(Types.newParameterizedType(Map.class, String.class, Object.class));
    return adapter.fromJson(new Buffer().readFrom(connection.getInputStream()));
  }

  @Test
  public void testSuccessfulResponseMap() throws IOException {
    Spark.get("school", new SchoolFromLocation());
    Spark.init();
    Spark.awaitInitialization();
    HttpURLConnection connection = tryRequest("/school?x=40.674456&y=-73.912685");
    Map<String, Object> toCheck = this.deserializationHelper(connection);

    assertEquals(toCheck.get(ServerState.result), ServerState.success);
    assertEquals(toCheck.get(ServerState.data).toString(),
        "[Brooklyn Collegiate: A College Board School]");

    HttpURLConnection connection1 = tryRequest("/school?x=40.74651&y=-73.98107");
    Map<String, Object> toCheck1 = this.deserializationHelper(connection1);

    assertTrue(toCheck1.get(ServerState.data).toString().contains("MANHATTAN ACADEMY FOR ARTS & LANGUAGE")
        && toCheck1.get(ServerState.data).toString().contains("Murray Hill Academy")
            && toCheck1.get(ServerState.data).toString().contains("Unity Center for Urban Technologies"));

  }

}
