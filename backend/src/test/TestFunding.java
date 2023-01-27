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
import main.java.funding.Funding;
import main.java.utilities.CitywideData;
import main.java.utilities.ErrorState;
import main.java.utilities.ServerState;
import okio.Buffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

public class TestFunding {


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

  @BeforeEach
  public void setup() throws IOException {
    Spark.get("funding", new Funding());
    Spark.init();
    Spark.awaitInitialization();
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
  public void testSchoolLevelStandardSuccess() throws IOException {

    HttpURLConnection connection = tryRequest("funding?type=school&&school_name=Brooklyn%20Collegiate:%20A%20College%20Board%20School");
    Map<String, Object> toCheck = this.deserializationHelper(connection);

    assertEquals(toCheck.get(ErrorState.result), ServerState.success);
    assertTrue(toCheck.get(ServerState.data).toString().contains("$0.00"));

    HttpURLConnection connection1 = tryRequest("funding?type=school&&school_name=New%20Explorations%20into%20Science,%20Technology%20and%20Math%20High%20School");
    Map<String, Object> toCheck1 = this.deserializationHelper(connection1);

    assertEquals(toCheck1.get(ErrorState.result), ServerState.success);
    assertTrue(toCheck1.get(ServerState.data).toString().contains("$1,479,000.00"));

  }

  @Test
  public void testDistrictLevelStandardSuccess() throws IOException{

    HttpURLConnection connection = tryRequest("funding?type=district&&number=3");
    Map<String, Object> toCheck = this.deserializationHelper(connection);


    assertEquals(toCheck.get(ErrorState.result), ServerState.success);
    assertTrue(toCheck.get(ServerState.data).toString().contains("$10,289,076.12"));
  }

  @Test
  public void testCitywideStandardSuccess() throws IOException{

    HttpURLConnection connection = tryRequest("funding?type=city");
    Map<String, Object> toCheck = this.deserializationHelper(connection);

    assertEquals(toCheck.get(ErrorState.result), ServerState.success);
    assertEquals(toCheck.get(ServerState.data), CitywideData.citywideFunding);
  }

  @Test
  public void testAbsencesTypeFailure() throws IOException{

    HttpURLConnection connection = tryRequest("funding?type=nonexistent&&school_name=Brooklyn%20Collegiate:%20A%20College%20Board%20School");
    Map<String, Object> toCheck = this.deserializationHelper(connection);

    assertEquals(toCheck.get(ErrorState.result), ErrorState.fail);
    assertEquals(toCheck.get(ErrorState.data), ErrorState.type);

  }

  @Test
  public void testSchoolDNEError() throws IOException{

    HttpURLConnection connection = tryRequest("funding?type=school&&school_name=schoolDNE");
    Map<String, Object> toCheck = this.deserializationHelper(connection);

    assertEquals(toCheck.get(ErrorState.result), ErrorState.fail);
    assertEquals(toCheck.get(ErrorState.data), ErrorState.schoolDNE);

  }

  @Test
  public void testDistrictDNEError() throws IOException{

    HttpURLConnection connection = tryRequest("funding?type=district&&number=42");
    Map<String, Object> toCheck = this.deserializationHelper(connection);

    assertEquals(toCheck.get(ErrorState.result), ErrorState.fail);
    assertEquals(toCheck.get(ErrorState.data), ErrorState.districtOutOfBoundsError);

  }

  @Test
  public void testInvalidParameters() throws IOException{

    HttpURLConnection connection = tryRequest("funding?parameter=DNE&&number=3");
    Map<String, Object> toCheck = this.deserializationHelper(connection);

    assertEquals(toCheck.get(ErrorState.result), ErrorState.fail);
    assertEquals(toCheck.get(ErrorState.data), ErrorState.type);
  }

}
