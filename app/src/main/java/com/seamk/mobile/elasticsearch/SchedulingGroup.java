
package com.seamk.mobile.elasticsearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class SchedulingGroup {

    @SerializedName("id")
    @Expose
    String id;
    @SerializedName("name")
    @Expose
    String name;
    @SerializedName("maxStudents")
    @Expose
    Integer maxStudents;
    @SerializedName("openMaxStudents")
    @Expose
    Integer openMaxStudents;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(Integer maxStudents) {
        this.maxStudents = maxStudents;
    }

    public Integer getOpenMaxStudents() {
        return openMaxStudents;
    }

    public void setOpenMaxStudents(Integer openMaxStudents) {
        this.openMaxStudents = openMaxStudents;
    }

}
