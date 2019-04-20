package com.seamk.mobile.objects;

/**
 * Created by Juha Ala-Rantala on 21.9.2017.
 */

public class BasketRealization {

    private String realizationCode;
    private String realizationNameFi;
    private String realizationNameEn;
    private boolean isAdded = false;
    private boolean isShown = true;
    private boolean noReservations = false;
    private boolean duplicateWarning = false;
    private long lastSavedTime;

    public BasketRealization() {
    }

    public BasketRealization(String realizationCode, String realizationNameFi, String realizationNameEn) {
        this.realizationCode = realizationCode;
        this.realizationNameFi = realizationNameFi;
        this.realizationNameEn = realizationNameEn;
    }

    public String getRealizationCode() {
        return realizationCode;
    }

    public void setRealizationCode(String realizationCode) {
        this.realizationCode = realizationCode;
    }

    public String getRealizationNameFi() {
        return realizationNameFi;
    }

    public void setRealizationNameFi(String realizationNameFi) {
        this.realizationNameFi = realizationNameFi;
    }

    public String getRealizationNameEn() {
        return realizationNameEn;
    }

    public void setRealizationNameEn(String realizationNameEn) {
        this.realizationNameEn = realizationNameEn;
    }

    public boolean isAdded() {
        return isAdded;
    }

    public void setAdded(boolean added) {
        isAdded = added;
    }

    public boolean isShown() {
        return isShown;
    }

    public void setShown(boolean shown) {
        isShown = shown;
    }

    public long getLastSavedTime() {
        return lastSavedTime;
    }

    public void setLastSavedTime(long lastSavedTime) {
        this.lastSavedTime = lastSavedTime;
    }

    public boolean isNoReservations() {
        return noReservations;
    }

    public void setNoReservations(boolean noReservations) {
        this.noReservations = noReservations;
    }

    public boolean isDuplicateWarning() {
        return duplicateWarning;
    }

    public void setDuplicateWarning(boolean duplicateWarning) {
        this.duplicateWarning = duplicateWarning;
    }
}
