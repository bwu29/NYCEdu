package main.java;

import static spark.Spark.after;

import java.io.IOException;
import main.java.absences.ChronicAbsences;
import main.java.funding.Funding;
import main.java.graduationRates.GraduationRates;
import main.java.studentDemographics.StudentDemographics;
import main.java.schoolNameRetrieval.SchoolFromLocation;
import main.java.teacherDemographics.TeacherDemographics;
import spark.Spark;

/**
 * This Server class is our main class. To run our program, we run this Server class. We use Spark
 * to establish a port number and set up our endpoints for the user and developer.
 */

public class Server {

  public static void main(String[] args) throws Exception {

    Spark.port(32);

    after(
        (request, response) -> {
          response.header("Access-Control-Allow-Origin", "*");
          response.header("Access-Control-Allow-Methods", "*");
        });

    Spark.get("schoolName", new SchoolFromLocation());
    Spark.get("teacherDemographics", new TeacherDemographics());
    Spark.get("absences", new ChronicAbsences());
    Spark.get("graduationRates", new GraduationRates());

    try{
      Spark.get("funding", new Funding());
      Spark.get("studentDemographics", new StudentDemographics());

    } catch (IOException e) {
      System.out.println(e.getMessage());
      throw new RuntimeException(e);
    }

    Spark.init();
    Spark.awaitInitialization();
    System.out.println("Server started.");
  }
}
