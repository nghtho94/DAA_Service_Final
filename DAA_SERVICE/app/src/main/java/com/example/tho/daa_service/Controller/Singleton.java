package com.example.tho.daa_service.Controller;


import com.example.tho.daa_service.Models.ResponseData.IdentitySPData;
import com.example.tho.daa_service.Models.crypto.BNCurve;

public class Singleton {

    private static final String TPM_ECC_BN_P256 = "TPM_ECC_BN_P256";
    private static Singleton singleton = new Singleton();
    private static IdentitySPData AnonymousIdentity;
    private static BNCurve curve;
    private static String sessionID;

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

    public static IdentitySPData getAnonymousIdentity() {
        return AnonymousIdentity;
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

    /* Other methods protected by singleton-ness */
    protected static void demoMethod( ) {
        System.out.println("demoMethod for singleton");
    }
}
