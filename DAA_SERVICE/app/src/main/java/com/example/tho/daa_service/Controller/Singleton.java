package com.example.tho.daa_service.Controller;


import com.example.tho.daa_service.Models.ResponseData.Bean;
import com.example.tho.daa_service.Models.ResponseData.IdentitySPData;
import com.example.tho.daa_service.Models.crypto.BNCurve;

import java.util.ArrayList;

public class Singleton {

    private static final String TPM_ECC_BN_P256 = "TPM_ECC_BN_P256";
    private static Singleton singleton = new Singleton();
    private static IdentitySPData identitySPData;
    private static BNCurve curve;
    private static String sessionID;
    private static ArrayList<Bean> mList = new ArrayList<>();

    /* A private Constructor prevents any other
     * class from instantiating.
     */
    private Singleton() { }

    /* Static 'instance' method */
    public static Singleton getInstance( ) {
        return singleton;
    }

    protected static void initData(){
        curve = new BNCurve(BNCurve.BNCurveInstantiation.valueOf(TPM_ECC_BN_P256));
    }

    public static IdentitySPData getIdentitySPData() {
        return identitySPData;
    }

    public static void setCurve(BNCurve curve) {
        Singleton.curve = curve;
    }

    public static BNCurve getCurve() {
        return curve;
    }

    public static String getSessionID() {
        return sessionID;
    }

    public static void setSessionID(String sessionID) {
        Singleton.sessionID = sessionID;
    }

    public static void setIdentitySPData(IdentitySPData identitySPData) {
        Singleton.identitySPData = identitySPData;
    }

    public static void setmList(ArrayList<Bean> mList) {
        Singleton.mList = mList;
    }

    public static ArrayList<Bean> getmList() {
        return mList;
    }

    public void addLog(Bean bean){
        this.mList.add(bean);
    }

    /* Other methods protected by singleton-ness */
    protected static void demoMethod( ) {
        System.out.println("demoMethod for singleton");
    }
}
