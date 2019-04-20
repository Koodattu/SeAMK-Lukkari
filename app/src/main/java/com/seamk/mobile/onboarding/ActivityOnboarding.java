package com.seamk.mobile.onboarding;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;

/**
 * Created by Juha Ala-Rantala on 17.9.2017.
 */

// onboarding sisältö: 1: tervehdys, 2: yleiskatsaus, 3: lukujärjestys 4: ruokalista, 5: opintokori, 6: työkaluja 7: aloitetaan
//
//

public class ActivityOnboarding extends AppIntro {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Note here that we DO NOT use setContentView();

        //setFadeAnimation();
        //setZoomAnimation();
        //setFlowAnimation();
        //setSlideOverAnimation();
        //setDepthAnimation();
        setColorTransitionsEnabled(true);

        // Add your slide fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.

        addSlide(new FragmentOnboardingFirst());
        addSlide(new FragmentOnboardingSecond());
        addSlide(new FragmentOnboardingThird());
        addSlide(new FragmentOnboardingFourth());
        addSlide(new FragmentOnboardingFifth());

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
        setProgressButtonEnabled(true);
        showSkipButton(true);
        setSkipText("Ohita");
        setDoneText("Valmis");

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permission in Manifest.
        /*
        setVibrate(true);
        setVibrateIntensity(30);
        */
    }

    @Override
    public void onBackPressed() {
        if (pager.isFirstSlide(fragments.size())){
            //sammutetaan
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        SharedPreferences settings = getSharedPreferences("ApplicationPreferences", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("tourComplete", true);
        editor.commit();
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        SharedPreferences settings = getSharedPreferences("ApplicationPreferences", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("tourComplete", true);
        editor.commit();
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}