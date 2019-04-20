
package com.seamk.mobile.elasticsearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AggregationsStudentGroups {

    @SerializedName("mostReservedStudentGroups")
    @Expose
    private MostReservedStudentGroups mostReservedStudentGroups;

    public MostReservedStudentGroups getMostReservedStudentGroups() {
        return mostReservedStudentGroups;
    }

    public void setMostReservedStudentGroups(MostReservedStudentGroups mostReservedStudentGroups) {
        this.mostReservedStudentGroups = mostReservedStudentGroups;
    }

}
