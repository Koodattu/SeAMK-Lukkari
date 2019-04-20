package com.seamk.mobile.eventbusevents;

/**
 * Created by Juha Ala-Rantala on 27.8.2017.
 */

public class DateEvent {

    int year;
    int month;
    int day;
    int tag;

    public DateEvent(int year, int month, int day, int tag) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.tag = tag;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getTag() {
        return tag;
    }
}
