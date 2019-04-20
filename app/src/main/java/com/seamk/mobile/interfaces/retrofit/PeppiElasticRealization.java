package com.seamk.mobile.interfaces.retrofit;

import com.seamk.mobile.elasticsearch.ElasticRealization;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Juha Ala-Rantala on 26.2.2018.
 */

public interface PeppiElasticRealization {

    @POST("/r1/search/peppirealization/realization")
    Call<ElasticRealization> getRealizations(@Body RequestBody body);
}
