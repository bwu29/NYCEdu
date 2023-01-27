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
import main.java.studentDemographics.StudentDemographics;
import main.java.utilities.CitywideData;
import main.java.utilities.ErrorState;
import main.java.utilities.ServerState;
import okio.Buffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

public class TestStudentDemographics {


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
    Spark.get("studentDemographics", new StudentDemographics());
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

    HttpURLConnection connection = tryRequest("studentDemographics?type=school&&school_name=Orchard%20Collegiate%20Academy");
    Map<String, Object> toCheck = this.deserializationHelper(connection);

    assertEquals(toCheck.get(ErrorState.result), ServerState.success);
    assertTrue(toCheck.get(ServerState.data).toString().contains("Students in poverty=91.2, White=4.7, English Language Learner=13.5, Students with Disabilities=24.6, Female=47.4, Male=52.6, LatinX=56.7, Black=25.1, Asian=11.1"));

    HttpURLConnection connection1 = tryRequest("studentDemographics?type=school&&school_name=New%20Explorations%20into%20Science,%20Technology%20and%20Math");
    Map<String, Object> toCheck1 = this.deserializationHelper(connection1);

    assertEquals(toCheck1.get(ErrorState.result), ServerState.success);
    assertTrue(toCheck1.get(ServerState.data).toString().contains("Students in poverty=22.8, White=39.9, English Language Learner=0.8, Students with Disabilities=8.0, Female=47.9, Male=52.1, LatinX=11.8, Black=7.7, Asian=33.2"));

  }

  @Test
  public void testDistrictLevelStandardSuccess() throws IOException{

    HttpURLConnection connection = tryRequest("studentDemographics?type=district&&number=3");
    Map<String, Object> toCheck = this.deserializationHelper(connection);

    assertEquals(toCheck.get(ErrorState.result), ServerState.success);
    assertTrue(toCheck.get(ServerState.data).toString().contains("Students in poverty=48.1%, White=33.0%, English Language Learner=4.7%, Multiracial=5.8%, Students with Disabilities=17.7%, Female=53.9%, Male=46.1%, LatinX=31.8%, Black=20.9%, Asian=8.6%"));
  }

  @Test
  public void testCitywideStandardSuccess() throws IOException{

    HttpURLConnection connection = tryRequest("studentDemographics?type=city");
    Map<String, Object> toCheck = this.deserializationHelper(connection);

    assertEquals(toCheck.get(ErrorState.result), ServerState.success);
    assertEquals(toCheck.get(ServerState.data), CitywideData.citywideStudentDemographics());
  }

  @Test
  public void testAbsencesTypeFailure() throws IOException{

    HttpURLConnection connection = tryRequest("studentDemographics?type=nonexistent&&school_name=Brooklyn%20Collegiate:%20A%20College%20Board%20School");
    Map<String, Object> toCheck = this.deserializationHelper(connection);

    assertEquals(toCheck.get(ErrorState.result), ErrorState.fail);
    assertEquals(toCheck.get(ErrorState.data), ErrorState.type);

  }

  @Test
  public void testSchoolDNEError() throws IOException{

    HttpURLConnection connection = tryRequest("studentDemographics?type=school&&school_name=schoolDNE");
    Map<String, Object> toCheck = this.deserializationHelper(connection);

    assertEquals(toCheck.get(ErrorState.result), ErrorState.fail);
    assertEquals(toCheck.get(ErrorState.data), ErrorState.noSchoolData("student demographics"));

  }

  @Test
  public void testDistrictDNEError() throws IOException{

    HttpURLConnection connection = tryRequest("studentDemographics?type=district&&number=42");
    Map<String, Object> toCheck = this.deserializationHelper(connection);

    assertEquals(toCheck.get(ErrorState.result), ErrorState.fail);
    assertEquals(toCheck.get(ErrorState.data), ErrorState.districtOutOfBoundsError);

  }

  @Test
  public void testInvalidParameters() throws IOException{

    HttpURLConnection connection = tryRequest("studentDemographics?parameter=DNE&&number=3");
    Map<String, Object> toCheck = this.deserializationHelper(connection);

    assertEquals(toCheck.get(ErrorState.result), ErrorState.fail);
    assertEquals(toCheck.get(ErrorState.data), ErrorState.type);
  }
}
