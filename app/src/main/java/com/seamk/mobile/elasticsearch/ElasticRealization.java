
package com.seamk.mobile.elasticsearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ElasticRealization {

    @SerializedName("took")
    @Expose
    private Integer took;
    @SerializedName("timed_out")
    @Expose
    private Boolean timedOut;
    @SerializedName("_shards")
    @Expose
    private Shards shards;
    @SerializedName("hits")
    @Expose
    private HitsRealization hitsRealization;

    public Integer getTook() {
        return took;
    }

    public void setTook(Integer took) {
        this.took = took;
    }

    public Boolean getTimedOut() {
        return timedOut;
    }

    public void setTimedOut(Boolean timedOut) {
        this.timedOut = timedOut;
    }

    public Shards getShards() {
        return shards;
    }

    public void setShards(Shards shards) {
        this.shards = shards;
    }

    public HitsRealization getHitsRealization() {
        return hitsRealization;
    }

    public void setHitsRealization(HitsRealization hitsRealization) {
        this.hitsRealization = hitsRealization;
    }

}
