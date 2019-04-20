package com.seamk.mobile.objects;

/**
 * Created by Juha Ala-Rantala on 26.8.2017.
 */

public class Building {
    String buildingCode;
    String buildingName;
    String id;

    public Building(String buildingCode, String buildingName) {
        this.buildingCode = buildingCode;
        this.buildingName = buildingName;
    }

    public Building(String buildingCode, String buildingName, String id) {
        this.buildingCode = buildingCode;
        this.buildingName = buildingName;
        this.id = id;
    }

    public Building(String buildingCode, int id) {
        this.buildingCode = buildingCode;
        this.id = Integer.toString(id);
    }

    public Building() {
    }

    public String getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(String buildingCode) {
        this.buildingCode = buildingCode;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
