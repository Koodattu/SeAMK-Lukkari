
package com.seamk.mobile.elasticsearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class LocalizedCuEvaluationCriteria3 {

    @SerializedName("valueFi")
    @Expose
    String valueFi;
    @SerializedName("valueEn")
    @Expose
    String valueEn;
    @SerializedName("valueSv")
    @Expose
    String valueSv;

    public String getValueFi() {
        return valueFi;
    }

    public void setValueFi(String valueFi) {
        this.valueFi = valueFi;
    }

    public String getValueEn() {
        return valueEn;
    }

    public void setValueEn(String valueEn) {
        this.valueEn = valueEn;
    }

    public String getValueSv() {
        return valueSv;
    }

    public void setValueSv(String valueSv) {
        this.valueSv = valueSv;
    }

}
