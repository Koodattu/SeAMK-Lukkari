
package com.seamk.mobile.elasticsearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AggregationsRooms {

    @SerializedName("mostReservedRooms")
    @Expose
    private MostReservedRooms mostReservedRooms;

    public MostReservedRooms getMostReservedRooms() {
        return mostReservedRooms;
    }

    public void setMostReservedRooms(MostReservedRooms mostReservedRooms) {
        this.mostReservedRooms = mostReservedRooms;
    }

}
