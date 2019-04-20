package com.seamk.mobile.objects;

/**
 * Created by Juha Ala-Rantala on 19.9.2017.
 */

public class RestaurantSetup {
    String name;
    String location;
    String IDNumber;
    private boolean isSelected = false;

    public RestaurantSetup(String name, String location, String IDNumber) {
        this.name = name;
        this.location = location;
        this.IDNumber = IDNumber;
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

    public String getIDNumber() {
        return IDNumber;
    }

    public void setIDNumber(String IDNumber) {
        this.IDNumber = IDNumber;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
