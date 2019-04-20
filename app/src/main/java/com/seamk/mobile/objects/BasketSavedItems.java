package com.seamk.mobile.objects;

import java.util.List;

/**
 * Created by Juha Ala-Rantala on 2.10.2017.
 */

public class BasketSavedItems {


    private List<BasketStudentGroup> basketStudentGroups;
    private List<BasketRealization> basketStudentGroupsRealizations;

    private List<BasketTeacher> basketTeachers;
    private List<BasketRealization> basketTeacherRealizations;

    private List<BasketRealization> basketRealizations;

    public BasketSavedItems() {
    }

    public List<BasketStudentGroup> getBasketStudentGroups() {
        return basketStudentGroups;
    }

    public void setBasketStudentGroups(List<BasketStudentGroup> basketStudentGroups) {
        this.basketStudentGroups = basketStudentGroups;
    }

    public List<BasketRealization> getBasketStudentGroupsRealizations() {
        return basketStudentGroupsRealizations;
    }

    public void setBasketStudentGroupsRealizations(List<BasketRealization> basketStudentGroupsRealizations) {
        this.basketStudentGroupsRealizations = basketStudentGroupsRealizations;
    }

    public List<BasketTeacher> getBasketTeachers() {
        return basketTeachers;
    }

    public void setBasketTeachers(List<BasketTeacher> basketTeachers) {
        this.basketTeachers = basketTeachers;
    }

    public List<BasketRealization> getBasketTeacherRealizations() {
        return basketTeacherRealizations;
    }

    public void setBasketTeacherRealizations(List<BasketRealization> basketTeacherRealizations) {
        this.basketTeacherRealizations = basketTeacherRealizations;
    }

    public List<BasketRealization> getBasketRealizations() {
        return basketRealizations;
    }

    public void setBasketRealizations(List<BasketRealization> basketRealizations) {
        this.basketRealizations = basketRealizations;
    }
}
