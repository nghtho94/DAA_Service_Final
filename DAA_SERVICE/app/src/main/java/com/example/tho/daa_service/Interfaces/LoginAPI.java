package com.example.tho.daa_service.Interfaces;


import com.example.tho.daa_service.Models.ResponseData.VerifyResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginAPI {

    @POST("/login")
    @FormUrlEncoded
    Call<VerifyResponse> login(@Field("app_id") Integer appID, @Field("m") String m);
}
