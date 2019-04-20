
package com.seamk.mobile.elasticsearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Bucket {

    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("doc_count")
    @Expose
    private Integer docCount;

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

}
