package com.seamk.mobile;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.solovyev.android.checkout.Billing;

import static com.seamk.mobile.util.Common.isEmulator;
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
        // load the correct ad depending on whether app is running inside an emulator or not
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        /*
        if (isEmulator()){
            MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        }
        else{
            MobileAds.initialize(this, "ca-app-pub-6426182185430448~8323607887");
        }
         */
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
