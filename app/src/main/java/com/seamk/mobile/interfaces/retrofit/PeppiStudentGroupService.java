package com.seamk.mobile.interfaces.retrofit;

import com.seamk.mobile.objects.PeppiRequestStudentGroup;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Juha Ala-Rantala on 17.11.2017.
 */

public interface PeppiStudentGroupService {
    @POST("/r1/reservation/search?l=fi")
    Call<PeppiRequestStudentGroup> createRequest(@Body PeppiRequestStudentGroup peppiRequestStudentGroup);
}
