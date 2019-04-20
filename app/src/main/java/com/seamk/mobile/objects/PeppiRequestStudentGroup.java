package com.seamk.mobile.objects;

/**
 * Created by Juha Ala-Rantala on 17.11.2017.
 */

public class PeppiRequestStudentGroup {

    private String startTime;
    private String endTime;
    private String studentGroup;

    public PeppiRequestStudentGroup(String startTime, String endTime, String studentGroup) {
        String startTimeTime = "T00:00:00";
        String endTimeTime = "T23:59:59";
        this.startTime = startTime+startTimeTime;
        this.endTime = endTime+endTimeTime;
        this.studentGroup = "["+studentGroup+"]";
    }
}
