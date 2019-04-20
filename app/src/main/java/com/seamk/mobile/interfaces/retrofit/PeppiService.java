package com.seamk.mobile.interfaces.retrofit;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Juha Ala-Rantala on 17.11.2017.
 */

public interface PeppiService {
    //@Headers({"Content-Type: application/json"})
    @POST("/r1/reservation/search?l=fi")
    Call<ResponseBody> createRequest(@Body RequestBody body);
}
