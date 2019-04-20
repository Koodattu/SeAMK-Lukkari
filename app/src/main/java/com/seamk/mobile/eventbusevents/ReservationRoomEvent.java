package com.seamk.mobile.eventbusevents;

import com.seamk.mobile.objects.ReservationOld;

import java.util.List;

/**
 * Created by Juha Ala-Rantala on 12.9.2017.
 */

public class ReservationRoomEvent {
    public final List<ReservationOld> reservationOlds;

    public ReservationRoomEvent(List<ReservationOld> reservationOlds) {
        this.reservationOlds = reservationOlds;
    }
}
