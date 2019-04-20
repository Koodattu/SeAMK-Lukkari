package com.seamk.mobile.objects;

import org.parceler.Parcel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Created by Juha Ala-Rantala on 10.4.2017.
 */

@Parcel
public class ReservationOld {

    String reservationId;
    String description;
    String subject;

    String startDate;
    String endDate;
    String modifiedDate;

    List<String> roomId;
    List<String> roomCode;
    List<String> roomName;

    List<String> buildingId;
    List<String> buildingCode;
    List<String> buildingName;

    List<String> realizationId;
    List<String> realizationCode;
    List<String> realizationName;

    List<String> studentGroupId;
    List<String> studentGroupCode;
    List<String> studentGroupName;

    List<String> teacherID;
    List<String> teacherCode;
    List<String> teacherName;

    List<String> schedulingGroupId;
    List<String> schedulingGroupCode;
    List<String> schedulingGroupName;

    List<String> movableId;
    List<String> movableCode;
    List<String> movableName;

    List<String> externalId;
    List<String> externalCode;
    List<String> externalName;

    Date dateStartDate;
    Date dateEndDate;
    List<String> classroomTypes;

    long longStartDate;
    long longEndDate;

    public ReservationOld(){
    }

    public ReservationOld(String reservationId, String description, String subject,
                          String startDate, String endDate, String modifiedDate,
                          List<String> roomId, List<String> roomCode, List<String> roomName,
                          List<String> buildingId, List<String> buildingCode, List<String> buildingName,
                          List<String> realizationId, List<String> realizationCode, List<String> realizationName,
                          List<String> studentGroupId, List<String> studentGroupCode, List<String> studentGroupName,
                          List<String> teacherID, List<String> teacherCode, List<String> teacherName,
                          List<String> schedulingGroupId, List<String> schedulingGroupCode, List<String> schedulingGroupName,
                          List<String> movableId, List<String> movableCode, List<String> movableName, List<String> externalId, List<String> externalCode, List<String> externalName) {
        this.reservationId = reservationId;
        this.description = description;
        this.subject = subject;
        this.startDate = startDate;
        this.endDate = endDate;
        this.modifiedDate = modifiedDate;
        this.roomId = removeDuplicates(roomId);
        this.roomCode = removeDuplicates(roomCode);
        this.roomName = removeDuplicates(roomName);
        this.buildingId = removeDuplicates(buildingId);
        this.buildingCode = removeDuplicates(buildingCode);
        this.buildingName = removeDuplicates(buildingName);
        this.realizationId = removeDuplicates(realizationId);
        this.realizationCode = removeDuplicates(realizationCode);
        this.realizationName = removeDuplicates(realizationName);
        this.studentGroupId = removeDuplicates(studentGroupId);
        this.studentGroupCode = removeDuplicates(studentGroupCode);
        this.studentGroupName = removeDuplicates(studentGroupName);
        this.teacherID = removeDuplicates(teacherID);
        this.teacherCode = removeDuplicates(teacherCode);
        this.teacherName = removeDuplicates(teacherName);
        this.schedulingGroupId = removeDuplicates(schedulingGroupId);
        this.schedulingGroupCode = removeDuplicates(schedulingGroupCode);
        this.schedulingGroupName = removeDuplicates(schedulingGroupName);
        this.movableId = removeDuplicates(movableId);
        this.movableCode = removeDuplicates(movableCode);
        this.movableName = removeDuplicates(movableName);
        this.externalId = removeDuplicates(externalId);
        this.externalCode = removeDuplicates(externalCode);
        this.externalName = removeDuplicates(externalName);

        classroomTypes = new ArrayList<>();

        for (String s : roomName) {
            String[] splitRoom;
            splitRoom = s.split(" \\(");
            if (splitRoom.length == 2) {
                splitRoom[1] = splitRoom[1].substring(0, splitRoom[1].length() - 1);
                splitRoom[1] = splitRoom[1].replaceAll("\\s", "");
                splitRoom[1] = splitRoom[1].replaceAll("\\d", "");
                this.classroomTypes.add(splitRoom[1]);
            } else {
                this.classroomTypes.add("");
            }
        }

        if (roomName.size() == 0) {
            String tmpSubject = subject;
            for (String s : realizationName) {
                tmpSubject = tmpSubject.replace(s, "");
            }
            this.roomName.add(tmpSubject);
        }
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSubject() {
        String tmpSubject = this.subject;
        if (!getRealizationCode().get(0).equals("") && !getRealizationName().get(0).equals("")){
            tmpSubject = tmpSubject.replaceAll(getRealizationCode().get(0), "").replaceAll(getRealizationName().get(0), "");
        }

        return tmpSubject;
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

    public List<String> getRoomId() {
        return roomId;
    }

    public void setRoomId(List<String> roomId) {
        this.roomId = roomId;
    }

    public List<String> getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(List<String> roomCode) {
        this.roomCode = roomCode;
    }

    public List<String> getRoomName() {
        return roomName;
    }

    public void setRoomName(List<String> roomName) {
        this.roomName = roomName;
    }

    public List<String> getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(List<String> buildingId) {
        this.buildingId = buildingId;
    }

    public List<String> getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(List<String> buildingCode) {
        this.buildingCode = buildingCode;
    }

    public List<String> getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(List<String> buildingName) {
        this.buildingName = buildingName;
    }

    public List<String> getRealizationId() {
        return realizationId;
    }

    public void setRealizationId(List<String> realizationId) {
        this.realizationId = realizationId;
    }

    public List<String> getRealizationCode() {
        return realizationCode;
    }

    public void setRealizationCode(List<String> realizationCode) {
        this.realizationCode = realizationCode;
    }

    public List<String> getRealizationName() {
        return realizationName;
    }

    public void setRealizationName(List<String> realizationName) {
        this.realizationName = realizationName;
    }

    public List<String> getStudentGroupId() {
        return studentGroupId;
    }

    public void setStudentGroupId(List<String> studentGroupId) {
        this.studentGroupId = studentGroupId;
    }

    public List<String> getStudentGroupCode() {
        return studentGroupCode;
    }

    public void setStudentGroupCode(List<String> studentGroupCode) {
        this.studentGroupCode = studentGroupCode;
    }

    public List<String> getStudentGroupName() {
        return studentGroupName;
    }

    public void setStudentGroupName(List<String> studentGroupName) {
        this.studentGroupName = studentGroupName;
    }

    public List<String> getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(List<String> teacherID) {
        this.teacherID = teacherID;
    }

    public List<String> getTeacherCode() {
        return teacherCode;
    }

    public void setTeacherCode(List<String> teacherCode) {
        this.teacherCode = teacherCode;
    }

    public List<String> getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(List<String> teacherName) {
        this.teacherName = teacherName;
    }

    public List<String> getSchedulingGroupId() {
        return schedulingGroupId;
    }

    public void setSchedulingGroupId(List<String> schedulingGroupId) {
        this.schedulingGroupId = schedulingGroupId;
    }

    public List<String> getSchedulingGroupCode() {
        return schedulingGroupCode;
    }

    public void setSchedulingGroupCode(List<String> schedulingGroupCode) {
        this.schedulingGroupCode = schedulingGroupCode;
    }

    public List<String> getSchedulingGroupName() {
        return schedulingGroupName;
    }

    public void setSchedulingGroupName(List<String> schedulingGroupName) {
        this.schedulingGroupName = schedulingGroupName;
    }

    public List<String> getMovableId() {
        return movableId;
    }

    public void setMovableId(List<String> movableId) {
        this.movableId = movableId;
    }

    public List<String> getMovableCode() {
        return movableCode;
    }

    public void setMovableCode(List<String> movableCode) {
        this.movableCode = movableCode;
    }

    public List<String> getMovableName() {
        return movableName;
    }

    public void setMovableName(List<String> movableName) {
        this.movableName = movableName;
    }

    public List<String> getExternalId() {
        return externalId;
    }

    public void setExternalId(List<String> externalId) {
        this.externalId = externalId;
    }

    public List<String> getExternalCode() {
        return externalCode;
    }

    public void setExternalCode(List<String> externalCode) {
        this.externalCode = externalCode;
    }

    public List<String> getExternalName() {
        return externalName;
    }

    public void setExternalName(List<String> externalName) {
        this.externalName = externalName;
    }

    public Date getDateStartDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String tmpstartDate = startDate.replace("T", " ");
        try {
            dateStartDate = simpleDateFormat.parse(tmpstartDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateStartDate;
    }

    public void setDateStartDate(Date dateStartDate) {
        this.dateStartDate = dateStartDate;
    }

    public Date getDateEndDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String tmpendDate = endDate.replace("T", " ");
        try {
            dateEndDate = simpleDateFormat.parse(tmpendDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateEndDate;
    }

    public String getRoomNamesStringLong() {
        return listToLongString(roomName);
    }

    public String getRoomCodesStringLong() {
        return listToLongString(roomCode);
    }

    public String getStudentGroupsStringLong() {
        return listToLongString(studentGroupCode);
    }

    public String getBuildingsCodesStringLong() {
        return listToLongString(buildingCode);
    }

    public String getBuildingsNamesStringLong() {
        return listToLongString(buildingName);
    }

    public String getRealizationsNamesStringLong() {
        return listToLongString(realizationName);
    }

    public String getRealizationCodesStringLong() {
        return listToLongString(realizationCode);
    }

    public String getSchedulingGroupsStringLong() {
        return listToLongString(schedulingGroupCode);
    }

    public String getMovableStringLong() {
        return listToLongString(movableCode);
    }

    public String getExternalStringLong() {
        return listToLongString(externalCode);
    }

    public void setDateEndDate(Date dateEndDate) {
        this.dateEndDate = dateEndDate;
    }

    public List<String> getClassroomTypes() {
        return classroomTypes;
    }

    public void setClassroomTypes(List<String> classroomTypes) {
        this.classroomTypes = classroomTypes;
    }

    public long getLongStartDate() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        String tmpStartDate = this.startDate.replace("T", " ");
        try { this.longStartDate = format.parse(tmpStartDate).getTime(); } catch (ParseException e) { e.printStackTrace(); }
        return longStartDate;
    }

    public void setLongStartDate(long longStartDate) {
        this.longStartDate = longStartDate;
    }

    public long getLongEndDate() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        String tmpStartDate = this.endDate.replace("T", " ");
        try { this.longEndDate = format.parse(tmpStartDate).getTime(); } catch (ParseException e) { e.printStackTrace(); }
        return longEndDate;
    }

    private List<String> removeDuplicates(List<String> stringList){
        Set<String> stringSet = new LinkedHashSet<>(stringList);
        List<String> tmpList = new ArrayList<>(stringSet);
        if (stringList.size() == 0){
            tmpList.add("");
        }
        return new ArrayList<>(tmpList);
    }

    public void setLongEndDate(long longEndDate) {
        this.longEndDate = longEndDate;
    }

    public String getRoomNamesStringShort() {
        return listToShortString(roomName);
    }

    public String getRoomCodesStringShort() {
        return listToShortString(roomCode);
    }

    public String getStudentGroupsStringShort() {
        return listToShortString(studentGroupCode);
    }

    public String getBuildingsCodesStringShort() {
        return listToShortString(buildingCode);
    }

    public String getBuildingsNamesStringShort() {
        return listToShortString(buildingName);
    }

    public String getRealizationsNamesStringShort() {
        return listToShortString(realizationName);
    }

    public String getRealizationCodesStringShort() {
        return listToShortString(realizationCode);
    }

    public String getTeachersStringShort() {
        return listToShortString(teacherName);
    }

    public String getSchedulingGroupsStringShort() {
        return listToShortString(schedulingGroupCode);
    }

    public String getMovableStringShort() {
        return listToShortString(movableCode);
    }

    public String getExternalStringShort() {
        return listToShortString(externalCode);
    }

    private String listToLongString(List<String> stringList) {
        String tmpString = "";
        for (String s : stringList) {
            tmpString = tmpString + ", " + s;
        }
        tmpString = tmpString.substring(2);
        return tmpString;
    }

    private String listToShortString(List<String> stringList) {
        String tmpString = "";
        if (!stringList.get(0).equals("")) {
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