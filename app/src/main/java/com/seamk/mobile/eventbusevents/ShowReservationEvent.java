package com.seamk.mobile.eventbusevents;

import com.seamk.mobile.objects.ReservationOld;

/**
 * Created by Juha Ala-Rantala on 6.9.2017.
 */

public class ShowReservationEvent {

    public ReservationOld reservationOld;
    public int type;

    public ShowReservationEvent(ReservationOld reservationOld, int type) {
        this.reservationOld = reservationOld;
        this.type = type;
    }
}
