package com.seamk.mobile.objects;

import java.util.List;

/**
 * Created by Juha Ala-Rantala on 21.9.2017.
 */

public class BasketStudentGroup {

    private String studentGroupCode;
    private String studentGroupID;
    private boolean isAdded = false;
    private boolean isShown = true;
    private List<BasketRealization> basketRealizations;

    public BasketStudentGroup(String studentGroupCode, String studentGroupID) {
        this.studentGroupCode = studentGroupCode;
        this.studentGroupID = studentGroupID;
    }

    public BasketStudentGroup(String studentGroupCode, String studentGroupID, List<BasketRealization> basketRealizations) {
        this.studentGroupCode = studentGroupCode;
        this.studentGroupID = studentGroupID;
        this.basketRealizations = basketRealizations;
    }

    public BasketStudentGroup() {
    }

    public String getStudentGroupCode() {
        return studentGroupCode;
    }

    public void setStudentGroupCode(String studentGroupCode) {
        this.studentGroupCode = studentGroupCode;
    }

    public String getStudentGroupID() {
        return studentGroupID;
    }

    public void setStudentGroupID(String studentGroupID) {
        this.studentGroupID = studentGroupID;
    }

    public boolean isAdded() {
        return isAdded;
    }

    public void setAdded(boolean selected) {
        isAdded = selected;
    }

    public List<BasketRealization> getBasketRealizations() {
        return basketRealizations;
    }

    public void setBasketRealizations(List<BasketRealization> basketRealizations) {
        this.basketRealizations = basketRealizations;
    }

    public boolean isShown() {
        return isShown;
    }

    public void setShown(boolean shown) {
        isShown = shown;
    }
}
