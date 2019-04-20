
package com.seamk.mobile.elasticsearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BucketRooms {

    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("doc_count")
    @Expose
    private Integer docCount;
    @SerializedName("mostReservedBuildings")
    @Expose
    private MostReservedBuildings mostReservedBuildings;
    @SerializedName("mostReservedRoomsDescriptions")
    @Expose
    private MostReservedRoomsDescriptions mostReservedRoomsDescriptions;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getDocCount() {
        return docCount;
    }

    public void setDocCount(Integer docCount) {
        this.docCount = docCount;
    }

    public MostReservedBuildings getMostReservedBuildings() {
        return mostReservedBuildings;
    }

    public void setMostReservedBuildings(MostReservedBuildings mostReservedBuildings) {
        this.mostReservedBuildings = mostReservedBuildings;
    }

    public MostReservedRoomsDescriptions getMostReservedRoomsDescriptions() {
        return mostReservedRoomsDescriptions;
    }

    public void setMostReservedRoomsDescriptions(MostReservedRoomsDescriptions mostReservedRoomsDescriptions) {
        this.mostReservedRoomsDescriptions = mostReservedRoomsDescriptions;
    }

}
