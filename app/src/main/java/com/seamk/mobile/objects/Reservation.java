package com.seamk.mobile.objects;

import com.seamk.mobile.elasticsearch.Attendee;
import com.seamk.mobile.elasticsearch.ExternalLocation;
import com.seamk.mobile.elasticsearch.ExternalPerson;
import com.seamk.mobile.elasticsearch.Location;
import com.seamk.mobile.elasticsearch.Realization;
import com.seamk.mobile.elasticsearch.ReservedFor;
import com.seamk.mobile.elasticsearch.SchedulingGroup;
import com.seamk.mobile.elasticsearch.StudentGroup;

import org.parceler.Parcel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Juha Ala-Rantala on 10.4.2017.
 */

@Parcel
public class Reservation {

    String reservationId;
    String subject;
    String description;

    String startDate;
    String endDate;

    List<Location> location;
    List<ExternalLocation> externalLocation;
    List<Realization> realization;
    List<StudentGroup> studentGroup;
    List<SchedulingGroup> schedulingGroup;
    List<ReservedFor> teacher;
    List<ExternalPerson> externalPerson;
    List<Attendee> attendee;

    public Reservation(){
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public List<Location> getLocation() {
        return location;
    }

    public void setLocation(List<Location> location) {
        this.location = location;
    }

    public List<ExternalLocation> getExternalLocation() {
        return externalLocation;
    }

    public void setExternalLocation(List<ExternalLocation> externalLocation) {
        this.externalLocation = externalLocation;
    }

    public List<Realization> getRealization() {
        return realization;
    }

    public void setRealization(List<Realization> realization) {
        this.realization = realization;
    }

    public List<StudentGroup> getStudentGroup() {
        return studentGroup;
    }

    public void setStudentGroup(List<StudentGroup> studentGroup) {
        this.studentGroup = studentGroup;
    }

    public List<SchedulingGroup> getSchedulingGroup() {
        return schedulingGroup;
    }

    public void setSchedulingGroup(List<SchedulingGroup> schedulingGroup) {
        this.schedulingGroup = schedulingGroup;
    }

    public List<ReservedFor> getTeacher() {
        return teacher;
    }

    public void setTeacher(List<ReservedFor> teacher) {
        this.teacher = teacher;
    }

    public List<ExternalPerson> getExternalPerson() {
        return externalPerson;
    }

    public void setExternalPerson(List<ExternalPerson> externalPerson) {
        this.externalPerson = externalPerson;
    }

    public List<Attendee> getAttendee() {
        return attendee;
    }

    public void setAttendee(List<Attendee> attendee) {
        this.attendee = attendee;
    }

    public String getLocationStringShort() {
        List<String> list = new ArrayList<>();
        for (Location location : location){
            list.add(location.getCode());
        }
        return listToShortString(list);
    }

    public String getRealizationCodeStringShort() {
        List<String> list = new ArrayList<>();
        for (Realization realization : realization){
            list.add(realization.getCode());
        }
        return listToShortString(list);
    }

    public String getRealizationNameStringShort() {
        List<String> list = new ArrayList<>();
        for (Realization realization : realization){
            list.add(realization.getLocalizedName().getValueFi());
        }
        return listToShortString(list);
    }

    public String getRealizationNameStringLong() {
        List<String> list = new ArrayList<>();
        for (Realization realization : realization){
            list.add(realization.getLocalizedName().getValueFi());
        }
        return listToLongString(list);
    }

    public String getRealizationCodeStringLong() {
        List<String> list = new ArrayList<>();
        for (Realization realization : realization){
            list.add(realization.getCode());
        }
        return listToLongString(list);
    }

    public String getRoomNameStringLong() {
        List<String> list = new ArrayList<>();
        for (Location location : location){
            list.add(location.getLocalizedName().getValueFi());
        }
        return listToLongString(list);
    }

    public String getRoomCodeStringLong() {
        List<String> list = new ArrayList<>();
        for (Location location : location){
            list.add(location.getCode());
        }
        return listToLongString(list);
    }

    public String getBuildingNameStringLong() {
        List<String> list = new ArrayList<>();
        for (Location location : location){
            list.add(location.getParent().getLocalizedName().getValueFi());
        }
        return listToLongString(list);
    }

    public String getStudentGroupStringLong() {
        List<String> list = new ArrayList<>();
        for (StudentGroup studentGroup : studentGroup){
            list.add(studentGroup.getCode());
        }
        return listToLongString(list);
    }

    public String getTeacherStringLong() {
        List<String> list = new ArrayList<>();
        for (ReservedFor reservedFor : teacher){
            list.add(reservedFor.getName());
        }
        return listToLongString(list);
    }

    public String getSchedulingGroupStringLong() {
        List<String> list = new ArrayList<>();
        for (SchedulingGroup schedulingGroup : schedulingGroup){
            list.add(schedulingGroup.getName());
        }
        return listToLongString(list);
    }

    public String getExternalStringLong() {
        List<String> list = new ArrayList<>();
        for (ExternalLocation externalLocation : externalLocation) {
            list.add(externalLocation.getCode());
        }
        return listToLongString(list);
    }

    public String getAttendeeStringLong() {
        List<String> list = new ArrayList<>();
        for (Attendee attendee : attendee){
            list.add(attendee.getName());
        }
        return listToLongString(list);
    }

    public long getLongStartDate() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        String tmpStartDate = this.startDate.replace("T", " ");
        Long longStartDate = 0L;
        try { longStartDate = format.parse(tmpStartDate).getTime(); } catch (ParseException e) { e.printStackTrace(); }
        return longStartDate;
    }

    public long getLongEndDate() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        String tmpStartDate = this.endDate.replace("T", " ");
        Long longEndDate = 0L;
        try { longEndDate = format.parse(tmpStartDate).getTime(); } catch (ParseException e) { e.printStackTrace(); }
        return longEndDate;
    }

    private String listToLongString(List<String> stringList) {
        String tmpString = "";
        if (stringList.size() > 0) {
            for (String s : stringList) {
                tmpString = tmpString + ", " + s;
            }
            tmpString = tmpString.substring(2);
        }
        return tmpString;
    }

    private String listToShortString(List<String> stringList) {
        String tmpString = "";
        if (stringList.size() > 0) {
            if (stringList.size() > 2) {
                tmpString = stringList.get(0) + " ja " + (stringList.size() - 1) + " muuta";
            } else if (stringList.size() == 1) {
                tmpString = stringList.get(0);
            } else if (stringList.size() == 2){
                tmpString = stringList.get(0) + " ja " + stringList.get(1);
            }
        }
        return tmpString;
    }
}