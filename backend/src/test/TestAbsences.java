package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.java.absences.ChronicAbsences;
import main.java.utilities.CitywideData;
import main.java.utilities.ErrorState;
import main.java.utilities.ServerState;
import okio.Buffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

public class TestAbsences {

  @BeforeAll
  public static void setup_before_everything() {
    Spark.port(0);
    // remove logging spam during tests
    Logger.getLogger("").setLevel(Level.WARNING);
  }

  @BeforeEach
  public void setUp(){
    Spark.get("absences", new ChronicAbsences());
    Spark.init();
    Spark.awaitInitialization();
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
  public void testSchoolLevelStandardSuccess() throws IOException {

    HttpURLConnection connection = tryRequest("absences?type=school&&school_name=Brooklyn%20Collegiate:%20A%20College%20Board%20School");
    Map<String, Object> toCheck = this.deserializationHelper(connection);

    assertEquals(toCheck.get(ErrorState.result), ServerState.success);
    assertEquals(toCheck.get(ServerState.data), "0.486 % of students were chronically absent");

  }

  @Test
  public void testDistrictLevelStandardSuccess() throws IOException{

    HttpURLConnection connection = tryRequest("absences?type=district&&number=3");
    Map<String, Object> toCheck = this.deserializationHelper(connection);

    assertEquals(toCheck.get(ErrorState.result), ServerState.success);
    assertEquals(toCheck.get(ServerState.data), "23.5 % of students were chronically absent");
  }

  @Test
  public void testCitywideStandardSuccess() throws IOException{

    HttpURLConnection connection = tryRequest("absences?type=city");
    Map<String, Object> toCheck = this.deserializationHelper(connection);

    assertEquals(toCheck.get(ErrorState.result), ServerState.success);
    assertEquals(toCheck.get(ServerState.data), CitywideData.citywideAbsenteeism);
  }

  @Test
  public void testAbsencesTypeFailure() throws IOException{

    HttpURLConnection connection = tryRequest("absences?type=nonexistent&&school_name=Brooklyn%20Collegiate:%20A%20College%20Board%20School");
    Map<String, Object> toCheck = this.deserializationHelper(connection);

    assertEquals(toCheck.get(ErrorState.result), ErrorState.fail);
    assertEquals(toCheck.get(ErrorState.data), ErrorState.type);

  }

  @Test
  public void testSchoolDNEError() throws IOException{

    HttpURLConnection connection = tryRequest("absences?type=school&&school_name=schoolDNE");
    Map<String, Object> toCheck = this.deserializationHelper(connection);

    assertEquals(toCheck.get(ErrorState.result), ErrorState.fail);
    assertEquals(toCheck.get(ErrorState.data), ErrorState.schoolDNE);

  }

  @Test
  public void testDistrictDNEError() throws IOException{

    HttpURLConnection connection = tryRequest("absences?type=district&&number=42");
    Map<String, Object> toCheck = this.deserializationHelper(connection);

    assertEquals(toCheck.get(ErrorState.result), ErrorState.fail);
    assertEquals(toCheck.get(ErrorState.data), ErrorState.districtOutOfBoundsError);

  }

  @Test
  public void testInvalidParameters() throws IOException{

    HttpURLConnection connection = tryRequest("absences?parameter=DNE&&number=3");
    Map<String, Object> toCheck = this.deserializationHelper(connection);

    assertEquals(toCheck.get(ErrorState.result), ErrorState.fail);
    assertEquals(toCheck.get(ErrorState.data), ErrorState.type);
  }


}
