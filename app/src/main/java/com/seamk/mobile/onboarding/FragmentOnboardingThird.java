package com.seamk.mobile.onboarding;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.paolorotolo.appintro.ISlideBackgroundColorHolder;
import com.seamk.mobile.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Juha Ala-Rantala on 18.9.2017.
 */

public class FragmentOnboardingThird  extends Fragment implements ISlideBackgroundColorHolder {
    @Override
    public int getDefaultBackgroundColor() {
        // Return the default background color of the slide.
        return Color.parseColor("#00bce1");
    }

    @Override
    public void setBackgroundColor(@ColorInt int backgroundColor) {
        // Set the background color of the view within your slide to which the transition should be applied.
        if (frameLayout != null) {
            frameLayout.setBackgroundColor(backgroundColor);
        }
    }
    @BindView(R.id.image_view_onboarding)
    ImageView imageView;
    @BindView(R.id.text_view_onboarding_title)
    TextView textViewTitle;
    @BindView(R.id.text_view_onboarding_description)
    TextView textViewDesc;
    @BindView(R.id.frame_layout_root)
    FrameLayout frameLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_onboarding_tour, container, false);

        ButterKnife.bind(this, v);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);

        imageView.setImageDrawable(getResources().getDrawable(R.drawable.image_onboarding_first));
        imageView.getLayoutParams().height = (int)(outMetrics.heightPixels * 0.55);
        imageView.getLayoutParams().width = outMetrics.widthPixels;
        imageView.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.image_onboarding_third));
        textViewTitle.setText(getResources().getString(R.string.intro_basket_title));
        textViewDesc.setText(getResources().getString(R.string.intro_basket_desc));
        frameLayout.setBackgroundColor(getResources().getColor(R.color.seamkBlue));
        return v;
    }
}
