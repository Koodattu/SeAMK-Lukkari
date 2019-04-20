
package com.seamk.mobile.elasticsearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AggregationsTeachers {

    @SerializedName("mostReservedTeachers")
    @Expose
    private MostReservedTeachers mostReservedTeachers;

    public MostReservedTeachers getMostReservedTeachers() {
        return mostReservedTeachers;
    }

    public void setMostReservedTeachers(MostReservedTeachers mostReservedTeachers) {
        this.mostReservedTeachers = mostReservedTeachers;
    }

}
