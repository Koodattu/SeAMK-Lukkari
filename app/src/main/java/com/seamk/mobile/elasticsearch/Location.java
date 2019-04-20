
package com.seamk.mobile.elasticsearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class Location {

    @SerializedName("id")
    @Expose
    String id;
    @SerializedName("code")
    @Expose
    String code;
    @SerializedName("localizedName")
    @Expose
    LocalizedName localizedName;
    @SerializedName("parent")
    @Expose
    Parent parent;
    @SerializedName("type")
    @Expose
    String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalizedName getLocalizedName() {
        return localizedName;
    }

    public void setLocalizedName(LocalizedName localizedName) {
        this.localizedName = localizedName;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
