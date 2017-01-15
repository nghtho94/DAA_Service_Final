package com.example.tho.daa_service.Models.ResponseData;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyResponse {
//    {
//        "link": "http://localhost:8090/loginResult/7",
//            "status": "login success"
//    }

    @SerializedName("link")
    @Expose
    String link;

    @SerializedName("status")
    @Expose
    String status;

    public String getLink() {
        return link;
    }

    public String getStatus() {
        return status;
    }
}
