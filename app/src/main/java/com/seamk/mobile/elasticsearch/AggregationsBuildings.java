package com.seamk.mobile.elasticsearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AggregationsBuildings {

@SerializedName("mostReservedBuildings")
@Expose
private MostReservedBuildings mostReservedBuildings;

public MostReservedBuildings getMostReservedBuildings() {
return mostReservedBuildings;
}

public void setMostReservedBuildings(MostReservedBuildings mostReservedBuildings) {
this.mostReservedBuildings = mostReservedBuildings;
}

}