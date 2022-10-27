package com.seamk.mobile.setup;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.appintro.AppIntro2;

public class SetupActivity extends AppIntro2 {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 5 slides: 0: welcome, 1: agreement, 2: student/teacher & group/name 3: restaurant, 4: recap and done

        // welcome
        addSlide(new SetupWelcomeFragment());
        // agreement
        addSlide(new SetupAgreementFragment());
        // student/teacher + group/name
        addSlide(new SetupBasketFragment());
        // restaurant
        //addSlide(AppIntroCustomLayoutFragment.newInstance(R.layout.intro_custom_layout1));
        // recap and done
        //addSlide(AppIntroCustomLayoutFragment.newInstance(R.layout.intro_custom_layout1));

        //TODO play with values
        //setTransformer(new AppIntroPageTransformerType.Parallax());

        setWizardMode(true);
        setIndicatorEnabled(true);

        //TODO do we want to change colors here?
        //setIndicatorColor(getResources().getColor(R.color.errorRed), getResources().getColor(R.color.defaultButton));
        showStatusBar(true);
        //setStatusBarColorRes(R.color.seamkBlue);
        //setNavBarColorRes(R.color.seamkGreen);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCanRequestNextPage() {
        return super.onCanRequestNextPage();
    }

    @Override
    protected void onDonePressed(@Nullable Fragment currentFragment) {
        super.onDonePressed(currentFragment);
    }

    @Override
    public void onIllegallyRequestedNextPage() {
        super.onIllegallyRequestedNextPage();
    }

    @Override
    protected void onNextPressed(@Nullable Fragment currentFragment) {
        super.onNextPressed(currentFragment);
    }

    @Override
    protected void onSkipPressed(@Nullable Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
    }

    @Override
    protected void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
    }
}