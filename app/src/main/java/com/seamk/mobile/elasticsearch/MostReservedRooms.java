
package com.seamk.mobile.elasticsearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MostReservedRooms {

    @SerializedName("doc_count_error_upper_bound")
    @Expose
    private Integer docCountErrorUpperBound;
    @SerializedName("sum_other_doc_count")
    @Expose
    private Integer sumOtherDocCount;
    @SerializedName("buckets")
    @Expose
    private List<BucketRooms> buckets = null;

    public Integer getDocCountErrorUpperBound() {
        return docCountErrorUpperBound;
    }

    public void setDocCountErrorUpperBound(Integer docCountErrorUpperBound) {
        this.docCountErrorUpperBound = docCountErrorUpperBound;
    }

    public Integer getSumOtherDocCount() {
        return sumOtherDocCount;
    }

    public void setSumOtherDocCount(Integer sumOtherDocCount) {
        this.sumOtherDocCount = sumOtherDocCount;
    }

    public List<BucketRooms> getBuckets() {
        return buckets;
    }

    public void setBuckets(List<BucketRooms> buckets) {
        this.buckets = buckets;
    }

}
