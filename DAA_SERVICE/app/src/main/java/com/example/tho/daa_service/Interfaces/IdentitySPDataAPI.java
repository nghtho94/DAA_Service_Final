package com.example.tho.daa_service.Interfaces;


import com.example.tho.daa_service.Models.ResponseData.IdentitySPData;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IdentitySPDataAPI {

    @POST("app")
    @FormUrlEncoded
    Call<IdentitySPData> downloadFile(@Field("appId") Integer appID);
}
