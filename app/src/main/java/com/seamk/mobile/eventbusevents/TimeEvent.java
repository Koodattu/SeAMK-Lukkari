package com.seamk.mobile.eventbusevents;

/**
 * Created by Juha Ala-Rantala on 27.8.2017.
 */

public class TimeEvent {

    int hourOfDay;
    int minute;
    int tag;

    public TimeEvent(int hourOfDay, int minute, int tag) {
        this.hourOfDay = hourOfDay;
        this.minute = minute;
        this.tag = tag;
    }

    public int getHourOfDay() {
        return hourOfDay;
    }

    public int getMinute() {
        return minute;
    }

    public int getTag() {
        return tag;
    }
}
