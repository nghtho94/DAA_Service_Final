package com.example.tho.daa_service.Models.ResponseData;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IdentitySPData {
    @SerializedName("curve")
    @Expose
    String curve;

    @SerializedName("appId")
    @Expose
    String appId;

    @SerializedName("ipk")
    @Expose
    String ipk;


    @SerializedName("permission")
    @Expose
    String permission;

    @SerializedName("esk_permission")
    @Expose
    String esk_permission;

    @SerializedName("epk_permission")
    @Expose
    String epk_permission;

    @SerializedName("gsk_permission")
    @Expose
    String gsk_permission;

    @SerializedName("credential_permission")
    @Expose
    String credential_permission;


    @SerializedName("level_customer")
    @Expose
    String level_customer;

    @SerializedName("credential_level_customer")
    @Expose
    String credential_level_customer;

    @SerializedName("gsk_level_customer")
    @Expose
    String gsk_level_customer;

    @SerializedName("epk_level_customer")
    @Expose
    String epk_level_customer;

    @SerializedName("esk_level_customer")
    @Expose
    String esk_level_customer;


    public String getCredential_permission() {
        return credential_permission;
    }

    public String getEpk_permission() {
        return epk_permission;
    }

    public String getEsk_permission() {
        return esk_permission;
    }

    public String getGsk_permission() {
        return gsk_permission;
    }

    public String getPermission() {
        return permission;
    }


    public String getAppId() {
        return appId;
    }

    public String getCredential_level_customer() {
        return credential_level_customer;
    }



    public String getCurve() {
        return curve;
    }

    public String getEpk_level_customer() {
        return epk_level_customer;
    }


    public String getEsk_level_customer() {
        return esk_level_customer;
    }

    public String getGsk_level_customer() {
        return gsk_level_customer;
    }

    public String getIpk() {
        return ipk;
    }

    public String getLevel_customer() {
        return level_customer;
    }

}
