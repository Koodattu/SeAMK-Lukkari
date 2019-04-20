package com.seamk.mobile.onboarding;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.github.paolorotolo.appintro.ISlideBackgroundColorHolder;
import com.seamk.mobile.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Juha Ala-Rantala on 18.9.2017.
 */

public class FragmentOnboardingFifth extends Fragment implements ISlideBackgroundColorHolder {
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
    @BindView(R.id.button_close_intro)
    Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_onboarding_last, container, false);

        ButterKnife.bind(this, v);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences settings = getActivity().getSharedPreferences("ApplicationPreferences", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("tourComplete", true);
                editor.commit();
                getActivity().finish();
            }
        });

        return v;
    }
}
