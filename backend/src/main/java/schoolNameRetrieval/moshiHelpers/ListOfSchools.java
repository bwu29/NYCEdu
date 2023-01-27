package main.java.schoolNameRetrieval.moshiHelpers;

import java.util.Collections;
import java.util.List;

public class ListOfSchools {

  List<LocationName> list_of_schools;

  public List<LocationName> getLocationNames(){return Collections.unmodifiableList(this.list_of_schools);}
}