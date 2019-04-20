package com.seamk.mobile.objects;

import java.util.List;

/**
 * Created by Juha Ala-Rantala on 12.3.2018.
 */

public class BasketTeacher {

    private String teacherName;
    private String teacherCode;
    private boolean isAdded = false;
    private boolean isShown = true;
    private List<BasketRealization> basketRealizations;

    public BasketTeacher() {
    }

    public BasketTeacher(String teacherName, String teacherCode) {
        this.teacherName = teacherName;
        this.teacherCode = teacherCode;
    }

    public BasketTeacher(String teacherName, String teacherCode, List<BasketRealization> basketRealizations) {
        this.teacherName = teacherName;
        this.teacherCode = teacherCode;
        this.basketRealizations = basketRealizations;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherCode() {
        return teacherCode;
    }

    public void setTeacherCode(String teacherCode) {
        this.teacherCode = teacherCode;
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

    public List<BasketRealization> getBasketRealizations() {
        return basketRealizations;
    }

    public void setBasketRealizations(List<BasketRealization> basketRealizations) {
        this.basketRealizations = basketRealizations;
    }
}
