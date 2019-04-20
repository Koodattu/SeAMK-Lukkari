package com.seamk.mobile.roomsearch;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.seamk.mobile.R;
import com.seamk.mobile.dialogs.DatePickerFragment;
import com.seamk.mobile.dialogs.TimePickerFragment;
import com.seamk.mobile.eventbusevents.DateEvent;
import com.seamk.mobile.eventbusevents.TimeEvent;
import com.seamk.mobile.objects.Building;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Juha Ala-Rantala on 5.5.2017.
 */

public class FragmentSearchBasic extends Fragment {

    Dialog dialog;
    List<Building> buildings;
    List<String> buildingsStrings;
    String searchBuildingCode = "-";
    String searchBuildingRoomType = "atk";
    String searchStartDate = "-";
    String searchStartTime = "-";
    String searchLength = "-";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_basic_search, container, false);

        Button buttonChooseBuilding = v.findViewById(R.id.search_choose_building_basic);
        buttonChooseBuilding.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_select_building);
                RadioGroup rg = dialog.findViewById(R.id.radio_group);

                for(int i=0; i < buildingsStrings.size(); i++){
                    RadioButton rb = new RadioButton(getActivity());
                    rb.setText(buildingsStrings.get(i));
                    rb.setMaxLines(1);
                    rb.setTextSize(TypedValue.COMPLEX_UNIT_SP,24);
                    rb.setPadding(8,8,8,8);

                    rg.addView(rb);
                }

                rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        int childCount = group.getChildCount();
                        for (int x = 0; x < childCount; x++) {
                            RadioButton btn = (RadioButton) group.getChildAt(x);
                            if (btn.getId() == checkedId) {
                                searchBuildingCode = buildings.get(x).getBuildingName();

                                dialog.dismiss();
                                updateSearchTermsTexts();
                            }
                        }
                    }
                });

                dialog.show();
            }
        });

        Button buttonChooseDateStart = v.findViewById(R.id.search_choose_date_start_basic);
        buttonChooseDateStart.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("tag", 2);
                newFragment.setArguments(bundle);
                newFragment.show(getActivity().getSupportFragmentManager(), "startDatePicker");
            }
        });

        Button buttonChooseTimeStart = v.findViewById(R.id.search_choose_time_start_basic);
        buttonChooseTimeStart.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("tag", 2);
                newFragment.setArguments(bundle);
                newFragment.show(getActivity().getSupportFragmentManager(), "startTimePicker");
            }
        });

        Button setStartTimeToNow = v.findViewById(R.id.BSetTimeNowBasic);
        setStartTimeToNow.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();

                String hour = Integer.toString(calendar.get(Calendar.HOUR_OF_DAY));
                String minute = Integer.toString(calendar.get(Calendar.MINUTE));
                if (Integer.valueOf(hour) < 10){
                    hour = "0" + hour;
                }
                if (Integer.valueOf(minute) < 10){
                    minute = "0" + minute;
                }
                searchStartTime = hour + ":" + minute;

                String day = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
                String month = Integer.toString(calendar.get(Calendar.MONTH) + 1);
                String year = Integer.toString(calendar.get(Calendar.YEAR));
                int dayInt = calendar.get(Calendar.DAY_OF_MONTH);
                int monthInt = calendar.get(Calendar.MONTH) + 1;
                if (dayInt < 10){
                    day = "0" + day;
                }
                if (monthInt < 10){
                    month = "0" + month;
                }
                searchStartDate = day + "." + month + "." + year;
                updateSearchTermsTexts();
            }
        });

        RadioGroup rg = v.findViewById(R.id.rg_atk_theory);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioButtonID = group.getCheckedRadioButtonId();
                View radioButton = group.findViewById(radioButtonID);
                int idx = group.indexOfChild(radioButton);
                if (idx == 0){
                    searchBuildingRoomType = "all";
                } else if (idx == 1){
                    searchBuildingRoomType = "atk";
                } else {
                    searchBuildingRoomType = "teoria";
                }
            }
        });

        Button buttonDoSearch = v.findViewById(R.id.BSuoritaHakuBasic);
        buttonDoSearch.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent searchResults = new Intent(getContext(), RootActivityEmptyRoomSearchResults.class);
                Bundle b = new Bundle();
                b.putString("typeOfSearch", "basic");
                b.putString("searchBuildingCode", searchBuildingCode);
                b.putString("searchBuildingRoomType", searchBuildingRoomType);
                b.putString("searchStartDate", searchStartDate);
                b.putString("searchStartTime", searchStartTime);
                b.putString("searchLength", searchLength);
                searchResults.putExtras(b);
                startActivity(searchResults);
            }
        });

        return v;
    }
/*
    public void showTimePickerDialog()
    {
        final Dialog d = new Dialog(getContext());
        d.setTitle("Valitse kesto");
        d.setContentView(R.layout.dialog_number_picker);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(10);
        np.setMinValue(1);
        np.setWrapSelectorWheel(true);
        if (!searchLength.equals("-")){
            np.setValue(Integer.valueOf(searchLength));
        }
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                searchLength = String.valueOf(np.getValue());
                updateSearchTermsTexts();
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();
    }
*/


    @Subscribe
    public void onTimePicked(TimeEvent event) {
        String hour = Integer.toString(event.getHourOfDay());
        String minute = Integer.toString(event.getMinute());

        if (Integer.valueOf(hour) < 10){
            hour = "0" + hour;
        }
        if (Integer.valueOf(minute) < 10){
            minute = "0" + minute;
        }

        if (event.getTag() == 2){
            searchStartTime = hour + ":" + minute;
        }
        updateSearchTermsTexts();
    }

    @Subscribe
    public void onDatePicked(DateEvent event) {
        if (event.getTag() == 2){
            String day = Integer.toString(event.getDay());
            String month = Integer.toString(event.getMonth());
            String year = Integer.toString(event.getYear());
            if (event.getDay() < 10){
                day = "0" + day;
            }
            if (event.getMonth() < 10){
                month = "0" + month;
            }
            searchStartDate = day + "." + month + "." + year;
        }
        updateSearchTermsTexts();
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getBuildings();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }

    public void updateSearchTermsTexts(){
        TextView textViewChosenBuilding = getActivity().findViewById(R.id.TVChosenBuildingBasic);
        textViewChosenBuilding.setText(searchBuildingCode);
        TextView textViewChosenStartDate = getActivity().findViewById(R.id.TVChosenStartDateBasic);
        textViewChosenStartDate.setText(searchStartDate);
        TextView textViewChosenStartTime = getActivity().findViewById(R.id.TVChosenStartTimeBasic);
        textViewChosenStartTime.setText(searchStartTime);
    }

    public void getBuildings(){
        buildings = new ArrayList<>();
        buildingsStrings = new ArrayList<>();

        InputStream is;
        String json = null;
        JSONObject jsonObject = null;
        try {
            is = getActivity().getAssets().open("buildingscodesnames");
            int size = 0;
            size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            jsonObject = new JSONObject(json);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Iterator<String> keys = jsonObject.keys();
        while( keys.hasNext() ){
            String key = keys.next();
            String value = jsonObject.optString(key);
            Building building = new Building(value, key);
            buildings.add(building);
        }
        Collections.sort(buildings, new Comparator<Building>(){
            public int compare(Building obj1, Building obj2) {
                return obj1.getBuildingCode().compareToIgnoreCase(obj2.getBuildingCode());
            }
        });


        for (int i = 0; i < buildings.size(); i++){
            buildingsStrings.add(buildings.get(i).getBuildingCode());
        }
    }


}
