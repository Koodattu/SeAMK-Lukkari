package com.seamk.mobile.elasticsearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SourceReservation {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("startDate")
    @Expose
    private String startDate;
    @SerializedName("endDate")
    @Expose
    private String endDate;
    @SerializedName("modifiedDate")
    @Expose
    private String modifiedDate;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("currentStatus")
    @Expose
    private String currentStatus;
    @SerializedName("location")
    @Expose
    private List<Location> location = null;
    @SerializedName("externalLocation")
    @Expose
    private List<ExternalLocation> externalLocation = null;
    @SerializedName("studentGroup")
    @Expose
    private List<StudentGroup> studentGroup = null;
    @SerializedName("schedulingGroup")
    @Expose
    private List<SchedulingGroup> schedulingGroup = null;
    @SerializedName("externalPerson")
    @Expose
    private List<ExternalPerson> externalPerson = null;
    @SerializedName("attendee")
    @Expose
    private List<Attendee> attendee = null;
    @SerializedName("reservedFor")
    @Expose
    private List<ReservedFor> reservedFor = null;
    @SerializedName("realization")
    @Expose
    private List<Realization> realization = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
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

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
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

    public List<ReservedFor> getReservedFor() {
        return reservedFor;
    }

    public void setReservedFor(List<ReservedFor> reservedFor) {
        this.reservedFor = reservedFor;
    }

    public List<Realization> getRealization() {
        return realization;
    }

    public void setRealization(List<Realization> realization) {
        this.realization = realization;
    }

}