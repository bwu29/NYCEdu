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
import main.java.teacherDemographics.TeacherDemographics;
import main.java.utilities.CitywideData;
import main.java.utilities.ErrorState;
import main.java.utilities.ServerState;
import okio.Buffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

public class TestTeacherDemographics {



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
    Spark.get("teacherDemographics", new TeacherDemographics());
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

    HttpURLConnection connection = tryRequest("teacherDemographics?type=school&&school_name=Orchard%20Collegiate%20Academy");
    Map<String, Object> toCheck = this.deserializationHelper(connection);

    assertEquals(toCheck.get(ErrorState.result), ServerState.success);
    assertTrue(toCheck.get(ServerState.data).toString().contains("White=42.90, LatinX=No Data, Black=No Data, Hawaiian/ Pacific Islander=0.00, Asian=23.80, Native American=0.00"));

    HttpURLConnection connection1 = tryRequest("teacherDemographics?type=school&&school_name=New%20Explorations%20into%20Science,%20Technology%20and%20Math");
    Map<String, Object> toCheck1 = this.deserializationHelper(connection1);

    assertEquals(toCheck1.get(ErrorState.result), ServerState.success);
    assertTrue(toCheck1.get(ServerState.data).toString().contains("White=69.20, LatinX=7.50, Black=5.60, Hawaiian/ Pacific Islander=0.00, Asian=14.00, Native American=0.00"));

  }

  @Test
  public void testDistrictLevelStandardSuccess() throws IOException{

    HttpURLConnection connection = tryRequest("teacherDemographics?type=district&&number=3");
    Map<String, Object> toCheck = this.deserializationHelper(connection);

    assertEquals(toCheck.get(ErrorState.result), ServerState.success);
    assertTrue(toCheck.get(ServerState.data).toString().contains("White=61.98, Multiracial=1.05, Unknown/ other=2.85, LatinX=15.87, Black=11.03, Hawaiian/ Pacific Islander=none, Asian=6.84, Native American=none"));
  }

  @Test
  public void testCitywideStandardSuccess() throws IOException{

    HttpURLConnection connection = tryRequest("teacherDemographics?type=city");
    Map<String, Object> toCheck = this.deserializationHelper(connection);

    assertEquals(toCheck.get(ErrorState.result), ServerState.success);
    assertEquals(toCheck.get(ServerState.data), CitywideData.citywideTeacherDemographics());
  }

  @Test
  public void testAbsencesTypeFailure() throws IOException{

    HttpURLConnection connection = tryRequest("teacherDemographics?type=nonexistent&&school_name=Brooklyn%20Collegiate:%20A%20College%20Board%20School");
    Map<String, Object> toCheck = this.deserializationHelper(connection);

    assertEquals(toCheck.get(ErrorState.result), ErrorState.fail);
    assertEquals(toCheck.get(ErrorState.data), ErrorState.type);

  }

  @Test
  public void testSchoolDNEError() throws IOException{

    HttpURLConnection connection = tryRequest("teacherDemographics?type=school&&school_name=schoolDNE");
    Map<String, Object> toCheck = this.deserializationHelper(connection);

    assertEquals(toCheck.get(ErrorState.result), ErrorState.fail);
    assertEquals(toCheck.get(ErrorState.data), ErrorState.noSchoolData("teacher demographics"));

  }

  @Test
  public void testDistrictDNEError() throws IOException{

    HttpURLConnection connection = tryRequest("teacherDemographics?type=district&&number=42");
    Map<String, Object> toCheck = this.deserializationHelper(connection);

    assertEquals(toCheck.get(ErrorState.result), ErrorState.fail);
    assertEquals(toCheck.get(ErrorState.data), ErrorState.districtOutOfBoundsError);

  }

  @Test
  public void testInvalidParameters() throws IOException{

    HttpURLConnection connection = tryRequest("teacherDemographics?parameter=DNE&&number=3");
    Map<String, Object> toCheck = this.deserializationHelper(connection);

    assertEquals(toCheck.get(ErrorState.result), ErrorState.fail);
    assertEquals(toCheck.get(ErrorState.data), ErrorState.type);
  }

}
