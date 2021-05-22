package com.seamk.mobile.setup;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.seamk.mobile.eventbusevents.NextSlideEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Juha Ala-Rantala on 17.9.2017.
 */

public class ActivitySetup extends AppIntro {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Note here that we DO NOT use setContentView();
        pager.setOffscreenPageLimit(4);

        //setFadeAnimation();
        //setZoomAnimation();
        //setFlowAnimation();
        //setSlideOverAnimation();
        setDepthAnimation();
        //setColorTransitionsEnabled(true);

        // Add your slide fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.

        addSlide(new FragmentSetupFirst());
        addSlide(new FragmentSetupSecond());
        addSlide(new FragmentSetupThird());

        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.
        //addSlide(AppIntroFragment.newInstance("Tervetuloa SeAMK mobiililukujärjestykseen!", "Pyyhkäise vasemmalle pikakatsausta varten", R.drawable.image_swipe_left, getResources().getColor(R.color.colorPrimaryDark)));
        //addSlide(AppIntroFragment.newInstance("Lukujärjestys ja ruokalista", "SeAMK Lukkarilla voit nopeasti tarkastella päivän lukujärjestystä ja ruokalistaa.", R.drawable.image_onboarding_first, getResources().getColor(R.color.seamkGreen)));
        //addSlide(AppIntroFragment.newInstance("Haku-ominaisuudet", "Haku välilehdeltä löydät mm. tyhjän tilan sekä tilojen haut.", R.drawable.image_onboarding_second, getResources().getColor(R.color.seamkBlue)));

        // OPTIONAL METHODS
        // Override bar/separator color.
        setBarColor(Color.parseColor("#303f9f"));
        setSeparatorColor(Color.parseColor("#000000"));

        // Hide Skip/Done button.
        showSkipButton(false);
        setProgressButtonEnabled(false);
        setSkipText("");
        setDoneText("");
        setImageNextButton(null);

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permission in Manifest.
        /*
        setVibrate(true);
        setVibrateIntensity(30);
        */
    }

    @Override
    public void onBackPressed() {
        if (pager.getCurrentItem() == 0){
            //TODO sammutetaan
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        //EventBus.getDefault().post(new StudentGroupEvent(""));
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        if (pager.getCurrentItem() == 0){
            setProgressButtonEnabled(true);
        } else {
            setProgressButtonEnabled(false);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onNextSlideEvent(NextSlideEvent event) {
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences = getSharedPreferences("ApplicationPreferences", 0);
                if (preferences.contains("restaurantCode")){
                    pager.setCurrentItem(pager.getCurrentItem()+2, true);
                } else {
                    pager.setCurrentItem(pager.getCurrentItem()+1, true);
                }
            }
        }, 250);
    }

    public void goBackSlides(final int slides) {
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (slides == 1){
                    pager.setCurrentItem(pager.getCurrentItem()-1, true);
                } else {
                    pager.setCurrentItem(pager.getCurrentItem()-2, true);
                }
            }
        }, 100);
    }
}