package com.seamk.mobile.objects;

/**
 * Created by Juha Ala-Rantala on 20.4.2017.
 */

public class Classroom {
    String classRoomName;
    String classRoomDescription;
    String classRoomFullName;
    String classRoomCode;

    public Classroom(String classRoomCode, String classRoomFullName) {
        this.classRoomCode = classRoomCode;
        this.classRoomFullName = classRoomFullName;

        String[] splitRoom;
        splitRoom = classRoomFullName.split(" \\(");
        if (splitRoom.length == 2){
            splitRoom[1] = splitRoom[1].substring(0, splitRoom[1].length() - 1);
            this.classRoomName = splitRoom[0];
            this.classRoomDescription =  splitRoom[1];
        } else {
            this.classRoomName = splitRoom[0];
            this.classRoomDescription = "";
        }
    }

    public Classroom() {
    }

    public String getClassRoomName() {
        return classRoomName;
    }

    public void setClassRoomName(String classRoomName) {
        this.classRoomName = classRoomName;
    }

    public String getClassRoomDescription() {
        return classRoomDescription;
    }

    public void setClassRoomDescription(String classRoomDescription) {
        this.classRoomDescription = classRoomDescription;
    }

    public String getClassRoomFullName() {
        return classRoomFullName;
    }

    public void setClassRoomFullName(String classRoomFullName) {
        this.classRoomFullName = classRoomFullName;
    }

    public String getClassRoomCode() {
        return classRoomCode;
    }

    public void setClassRoomCode(String classRoomCode) {
        this.classRoomCode = classRoomCode;
    }

}
