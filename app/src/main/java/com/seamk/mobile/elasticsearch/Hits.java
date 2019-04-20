package com.seamk.mobile.elasticsearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Hits {

@SerializedName("total")
@Expose
private Integer total;
@SerializedName("max_score")
@Expose
private Double maxScore;
@SerializedName("hits")
@Expose
private List<Object> hits = null;

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

public List<Object> getHits() {
return hits;
}

public void setHits(List<Object> hits) {
this.hits = hits;
}

}