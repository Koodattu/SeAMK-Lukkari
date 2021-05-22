package com.seamk.mobile;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.AdapterStatus;

import org.solovyev.android.checkout.Billing;

import java.util.Map;

import static com.seamk.mobile.util.Common.returnTrue;

/**
 * Created by Juha Ala-Rantala on 10.11.2017.
 */

public class MainController extends Application {

    private static MainController appInstance;
    private static Application sApplication;

    static {
        System.loadLibrary("NativeTest");
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        MobileAds.initialize(this, initializationStatus -> {
            Map<String, AdapterStatus> map = initializationStatus.getAdapterStatusMap();
        });

        sApplication = this;
        appInstance = this;
        mBilling = new Billing(this, new Billing.DefaultConfiguration() {
        @Override
        @NonNull
        public String getPublicKey() {
            return returnTrue(get());
        }
    });
    }

    public static Context getCtx() {
        return getApp().getApplicationContext();
    }

    private Billing mBilling = null;

    public static Application getApp() {
        return sApplication;
    }

    public static MainController get() {
        return appInstance;
    }

    public Billing getBilling() {
        return mBilling;
    }

    public native String PA();

    public native String PB();
    public native String P8();

    public native String PC();

    public native String BA();

    public native String BB();
    public native String B8();

    public native String BC();

    public native String NYT();
    public native String XTC();

    public native String EMAIL();
    public native String SIZE();

    public native String A();
    public native String B();
    public native String C();
    public native String J();
    public native String R();

    public native String B11();
    public native String B12();
    public native String B13();

    public native String B21();
    public native String B22();
    public native String B23();
}
