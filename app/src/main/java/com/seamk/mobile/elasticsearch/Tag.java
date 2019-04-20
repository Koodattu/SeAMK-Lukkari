
package com.seamk.mobile.elasticsearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class Tag {

    @SerializedName("id")
    @Expose
    String id;
    @SerializedName("localizedName")
    @Expose
    LocalizedName localizedName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalizedName getLocalizedName() {
        return localizedName;
    }

    public void setLocalizedName(LocalizedName localizedName) {
        this.localizedName = localizedName;
    }

}
