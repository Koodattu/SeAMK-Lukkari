
package com.seamk.mobile.elasticsearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HitsReservation {

    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("max_score")
    @Expose
    private Double maxScore;
    @SerializedName("hits")
    @Expose
    private List<HitReservation> hitReservation = null;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Double getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(Double maxScore) {
        this.maxScore = maxScore;
    }

    public List<HitReservation> getHitReservation() {
        return hitReservation;
    }

    public void setHitReservation(List<HitReservation> hitReservation) {
        this.hitReservation = hitReservation;
    }

}
