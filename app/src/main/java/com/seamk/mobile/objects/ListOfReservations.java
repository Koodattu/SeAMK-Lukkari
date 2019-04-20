package com.seamk.mobile.objects;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Juha Ala-Rantala on 27.3.2018.
 */

@Parcel
public class ListOfReservations {

    List<ReservationOld> reservationOldList;

    public ListOfReservations(){}

    public ListOfReservations(List<ReservationOld> reservationOldList){
        this.reservationOldList = reservationOldList;
    }

    public List<ReservationOld> getReservationOldList() {
        return reservationOldList;
    }

    public void setReservationOldList(List<ReservationOld> reservationOldList) {
        this.reservationOldList = reservationOldList;
    }
}
