package com.seamk.mobile.onboarding;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.github.paolorotolo.appintro.ISlideBackgroundColorHolder;
import com.seamk.mobile.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Juha Ala-Rantala on 18.9.2017.
 */

public class FragmentOnboardingFirst extends Fragment implements ISlideBackgroundColorHolder {
    @Override
    public int getDefaultBackgroundColor() {
        // Return the default background color of the slide.
        return Color.parseColor("#3f51b5");
    }

    @Override
    public void setBackgroundColor(@ColorInt int backgroundColor) {
        // Set the background color of the view within your slide to which the transition should be applied.
        if (frameLayout != null) {
            frameLayout.setBackgroundColor(backgroundColor);
        }
    }

    @BindView(R.id.frame_layout_root)
    FrameLayout frameLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_onboarding_start, container, false);

        ButterKnife.bind(this, v);
        return v;
    }
    /*
    @Override
    public int getDefaultBackgroundColor() {
        // Return the default background color of the slide.
        return Color.parseColor("#000000");
    }

    @Override
    public void setBackgroundColor(@ColorInt int backgroundColor) {
        // Set the background color of the view within your slide to which the transition should be applied.
        if (layoutContainer != null) {
        layoutContainer.setBackgroundColor(backgroundColor);
    }
}*/
}
