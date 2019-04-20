package com.seamk.mobile.eventbusevents;

import com.seamk.mobile.objects.ReservationOld;

import java.util.List;

/**
 * Created by Juha Ala-Rantala on 15.4.2017.
 */

public class MessageEvent {

    public final List<ReservationOld> reservationOlds;

    public MessageEvent(List<ReservationOld> reservationOlds) {
        this.reservationOlds = reservationOlds;
    }
}
