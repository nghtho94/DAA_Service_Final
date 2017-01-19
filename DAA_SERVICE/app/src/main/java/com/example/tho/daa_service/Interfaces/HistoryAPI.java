package com.example.tho.daa_service.Interfaces;

import com.example.tho.daa_service.Models.ResponseData.Bean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by tho on 1/18/17.
 */

public interface HistoryAPI {

    @GET
    Call<Bean> getHistory(@Url String url);
}
