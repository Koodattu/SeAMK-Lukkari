package com.seamk.mobile.objects;

import android.text.format.DateUtils;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by Juha Ala-Rantala on 13.9.2017.
 */

public class EmptyRoom {
    private String roomFullName;
    private String roomCode;
    private String roomName;
    private String roomType;
    private String roomBuilding;
    private String roomStatus;
    public long dateWhenEmpty;
    public long dateWhenNextReservation;
    public long timeEmptyFor;

    public EmptyRoom(String roomCode, String roomFullName, String roomBuilding, long dateWhenEmpty, long dateWhenNextReservation, long searchStartDate) {
        this.roomCode = roomCode;
        this.roomFullName = roomFullName;
        this.roomBuilding = roomBuilding;
        this.dateWhenEmpty = dateWhenEmpty;
        this.dateWhenNextReservation = dateWhenNextReservation;

        String[] splitRoom;
        splitRoom = roomFullName.split(" \\(");
        if (splitRoom.length == 2){
            splitRoom[1] = splitRoom[1].substring(0, splitRoom[1].length() - 1);
            this.roomName = splitRoom[0];
            this.roomType =  splitRoom[1];
        } else {
            this.roomName = splitRoom[0];
            this.roomType = "";
        }

        Calendar calendarTimeNow = Calendar.getInstance();
        if (searchStartDate != 0){
            calendarTimeNow.setTimeInMillis(searchStartDate);
        }

        if (dateWhenEmpty == 0 && dateWhenNextReservation == 0){ // ei varauksia koko päivänä

            timeEmptyFor = TimeUnit.DAYS.toMillis(1); //collections.sort varten
            roomStatus = "Vapaana koko loppupäivän";

        } else if (dateWhenEmpty == 0){ // ei aiempia varauksia päivänä


            Calendar calendarWhenReserved = Calendar.getInstance();
            calendarWhenReserved.setTimeInMillis(dateWhenNextReservation);

            long difference =  calendarWhenReserved.getTimeInMillis() - calendarTimeNow.getTimeInMillis();
            long differenceInSeconds = difference / DateUtils.SECOND_IN_MILLIS;
            long minutes = differenceInSeconds / 60;
            long hours = minutes / 60;
            String timeFreeFor = hours % 24 + "h " + minutes % 60 + "min";

            timeEmptyFor = difference;
            roomStatus = "Vapaana nyt seuraavat " + timeFreeFor;

        } else if (dateWhenNextReservation == 0) { // ei tulevia varauksia päivänä

            Calendar calendarWhenEmpty = Calendar.getInstance();
            calendarWhenEmpty.setTimeInMillis(dateWhenEmpty);

            long difference =  calendarWhenEmpty.getTimeInMillis() - calendarTimeNow.getTimeInMillis();
            long differenceInSeconds = difference / DateUtils.SECOND_IN_MILLIS;
            long minutes = differenceInSeconds / 60;
            long hours = minutes / 60;
            String timeUntilFree = hours % 24 + "h " + minutes % 60 + "min";

            timeEmptyFor = TimeUnit.DAYS.toMillis(1);
            roomStatus = timeUntilFree + " päästä vapaana loppupäivän";

        } else { // varauksia on aiemmin ja jälkeen

            Calendar calendarWhenEmpty = Calendar.getInstance();
            calendarWhenEmpty.setTimeInMillis(dateWhenEmpty);
            Calendar calendarWhenReserved = Calendar.getInstance();
            calendarWhenReserved.setTimeInMillis(dateWhenNextReservation);

            long difference =  calendarWhenEmpty.getTimeInMillis() - calendarTimeNow.getTimeInMillis();
            long differenceInSeconds = difference / DateUtils.SECOND_IN_MILLIS;
            long minutes = differenceInSeconds / 60;
            long hours = minutes / 60;
            String timeUntilFree = hours % 24 + "h " + minutes % 60 + "min";

            difference = calendarWhenReserved.getTimeInMillis() - calendarWhenEmpty.getTimeInMillis();
            differenceInSeconds = difference / DateUtils.SECOND_IN_MILLIS;
            minutes = differenceInSeconds / 60;
            hours = minutes / 60;
            String timeFreeFor = hours % 24 + "h " + minutes % 60 + "min";

            timeEmptyFor = difference;
            roomStatus = "Vapautumiseen " + timeUntilFree + " sitten vapaana " + timeFreeFor;
        }
    }

    public String getRoomCode() {
        return roomCode;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomBuilding() {
        return roomBuilding;
    }

    public String getRoomStatus() {
        return roomStatus;
    }
}
