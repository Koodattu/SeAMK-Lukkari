package com.seamk.mobile.objects;

/**
 * Created by Juha Ala-Rantala on 5.4.2017.
 */

public class Restaurant {
    public String name;
    public String location;
    public String openingHours;
    public int IDNO;

    public Restaurant(String name, String location, String openingHours, int IDNO) {
        this.name = name;
        this.location = location;
        this.openingHours = openingHours;
        this.IDNO = IDNO;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public int getIDNO() {
        return IDNO;
    }

    public void setIDNO(int IDNO) {
        this.IDNO = IDNO;
    }
}
