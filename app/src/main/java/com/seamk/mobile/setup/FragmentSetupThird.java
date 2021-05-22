package com.seamk.mobile.setup;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.seamk.mobile.R;
import com.seamk.mobile.eventbusevents.GoToStudyBasketEvent;
import com.seamk.mobile.eventbusevents.RestaurantSetupChosenEvent;
import com.seamk.mobile.eventbusevents.StudentGroupEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Juha Ala-Rantala on 5.3.2018.
 */

// yhteenveto valinnoista

public class FragmentSetupThird extends Fragment {

    @BindView(R.id.overview_studentgroup) TextView textViewSG;
    @BindView(R.id.overview_restaurant) TextView textViewR;
    @BindView(R.id.button_close_intro) Button button;
    @BindView(R.id.b_muuta_ryhma) Button buttonChangeGroup;
    @BindView(R.id.b_muuta_ravintola) Button buttonChangeRestaurant;
    @BindView(R.id.check_box_study_basket) AppCompatCheckBox checkBox;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_setup_third, container, false);
        ButterKnife.bind(this, v);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences settings = getActivity().getSharedPreferences("ApplicationPreferences", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("setupComplete", true);
                editor.apply();
                getActivity().finish();
            }
        });

        buttonChangeGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ActivitySetup)getActivity()).goBackSlides(2);
            }
        });

        buttonChangeRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ActivitySetup)getActivity()).goBackSlides(1);
            }
        });

        textViewR.setText(getResources().getString(R.string.setup_not_chosen));

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (checkBox.isChecked()) {
            EventBus.getDefault().post(new GoToStudyBasketEvent());
        }
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Subscribe
    public void onStudentGroupSelected(StudentGroupEvent event) {
        if (event.studentGroupCode.equals("")){
            textViewSG.setText(getResources().getString(R.string.setup_not_chosen));
        } else {
            textViewSG.setText(event.studentGroupCode);
        }
    }

    @Subscribe
    public void onRestaurantSetupChosen(RestaurantSetupChosenEvent event) {
        String restaurantName = "Ei valittu";
        switch (event.restaurantCode) {
            case "0":
                restaurantName = "Ei valittu";
                break;
            case "69":
                restaurantName = "Eventti";
                break;
            case "1404":
                restaurantName = "Ilmajoentie";
                break;
            case "1401":
                restaurantName = "Koskenalantie";
                break;
            case "873":
                restaurantName = "Frami F";
                break;
            case "874":
                restaurantName = "Kampustalo";
                break;
            default:
                throw new IllegalArgumentException("Invalid restaurant code: " + event.restaurantCode);
        }

        textViewR.setText(restaurantName);
    }
}
