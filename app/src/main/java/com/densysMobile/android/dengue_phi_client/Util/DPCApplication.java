package com.densysMobile.android.dengue_phi_client.Util;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.androidnetworking.AndroidNetworking;

/**
 * Created by Sineth on 1/26/2017.
 */

public class DPCApplication extends MultiDexApplication {
    private static DPCApplication dpcApplication = null;
    public static boolean isLoggedIn =false;

    public static Context getAppContext() {
        return dpcApplication.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dpcApplication = this;
        AndroidNetworking.initialize(getApplicationContext());
        AndroidNetworking.enableLogging("TAG");

    }
}
