package com.seamk.mobile.eventbusevents;

import com.seamk.mobile.objects.BasketStudentGroup;

/**
 * Created by Juha Ala-Rantala on 1.10.2017.
 */

public class StudentGroupBasketEvent {
    BasketStudentGroup studentGroupBasket;

    public StudentGroupBasketEvent(BasketStudentGroup studentGroupBasket) {
        this.studentGroupBasket = studentGroupBasket;
    }

    public BasketStudentGroup getStudentGroupBasket() {
        return studentGroupBasket;
    }
}
