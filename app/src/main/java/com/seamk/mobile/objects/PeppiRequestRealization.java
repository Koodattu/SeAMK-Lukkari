package com.seamk.mobile.objects;


/**
 * Created by Juha Ala-Rantala on 17.11.2017.
 */

public class PeppiRequestRealization {

    String startTime;
    String endTime;
    String realization;

    public PeppiRequestRealization(String startTime, String endTime, String realization) {
        String startTimeTime = "T00:00:00";
        String endTimeTime = "T23:59:59";
        this.startTime = startTime+startTimeTime;
        this.endTime = endTime+endTimeTime;
        this.realization = "["+realization+"]";
    }
}
