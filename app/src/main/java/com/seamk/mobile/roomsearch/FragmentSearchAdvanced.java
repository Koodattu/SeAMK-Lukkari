package com.seamk.mobile.roomsearch;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Juha Ala-Rantala on 5.5.2017.
 */

public class FragmentSearchAdvanced extends Fragment{

    Dialog dialog;
    List<Building> buildings;
    List<String> buildingsStrings;
    String searchBuildingCode = "-";
    String searchBuildingRoomType = "-";
    String searchStartDate = "-";
    String searchStartTime = "-";
    String searchEndDate = "-";
    String searchEndTime = "-";
    Map<String, List<String>> buildingsRoomTypesMap = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_advanced_search, container, false);

        final Button buttonChooseRoomType = v.findViewById(R.id.search_choose_room_type);
        buttonChooseRoomType.setEnabled(false);
        buttonChooseRoomType.getBackground().setColorFilter(0x837e7e7e, PorterDuff.Mode.MULTIPLY);
        buttonChooseRoomType.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_select_building);
                RadioGroup rg = dialog.findViewById(R.id.radio_group);

                for(int i=0; i < buildingsRoomTypesMap.get(searchBuildingCode).size(); i++){
                    RadioButton rb = new RadioButton(getActivity());
                    rb.setText(buildingsRoomTypesMap.get(searchBuildingCode).get(i));
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
                                searchBuildingRoomType = buildingsRoomTypesMap.get(searchBuildingCode).get(x);
                                dialog.dismiss();
                                updateSearchTermsTexts();
                            }
                        }
                    }
                });

                dialog.show();
            }
        });

        Button buttonChooseBuilding = v.findViewById(R.id.search_choose_building);
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

                                if(buildingsRoomTypesMap.get(searchBuildingCode).size() == 0){
                                    buttonChooseRoomType.setEnabled(false);
                                    buttonChooseRoomType.getBackground().setColorFilter(0x837e7e7e, PorterDuff.Mode.MULTIPLY);
                                } else {
                                    buttonChooseRoomType.setEnabled(true);
                                    buttonChooseRoomType.getBackground().setColorFilter(0xFFD5D6D6, PorterDuff.Mode.MULTIPLY);
                                }

                                if (!searchBuildingRoomType.equals("-")){
                                    if(!buildingsRoomTypesMap.get(searchBuildingCode).contains(searchBuildingRoomType))
                                        searchBuildingRoomType = "-";
                                }

                                dialog.dismiss();
                                updateSearchTermsTexts();
                            }
                        }
                    }
                });

                dialog.show();
            }
        });

        Button buttonChooseDateStart = v.findViewById(R.id.search_choose_date_start);
        buttonChooseDateStart.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("tag", 0);
                newFragment.setArguments(bundle);
                newFragment.show(getActivity().getSupportFragmentManager(), "startDatePicker");
            }
        });

        Button buttonChooseTimeStart = v.findViewById(R.id.search_choose_time_start);
        buttonChooseTimeStart.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("tag", 0);
                newFragment.setArguments(bundle);
                newFragment.show(getActivity().getSupportFragmentManager(), "startTimePicker");
            }
        });

        Button buttonChooseDateEnd = v.findViewById(R.id.search_choose_date_end);
        buttonChooseDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("tag", 1);
                newFragment.setArguments(bundle);
                newFragment.show(getActivity().getSupportFragmentManager(), "endDatePicker");
            }
        });

        Button buttonChooseTimeEnd = v.findViewById(R.id.search_choose_time_end);
        buttonChooseTimeEnd.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("tag", 1);
                newFragment.setArguments(bundle);
                newFragment.show(getActivity().getSupportFragmentManager(), "endTimePicker");
            }
        });

        Button setStartTimeToNow = v.findViewById(R.id.BSetTimeNowAdvanced);
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

        Button setLength = v.findViewById(R.id.BSetLengthAdvanced);
        setLength.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        Button buttonDoSearch = v.findViewById(R.id.BSuoritaHakuAdvanced);
        buttonDoSearch.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent searchResults = new Intent(getContext(), RootActivityEmptyRoomSearchResults.class);
                Bundle b = new Bundle();
                b.putString("typeOfSearch", "advanced");
                b.putString("searchBuildingCode", searchBuildingCode);
                b.putString("searchBuildingRoomType", searchBuildingRoomType);
                b.putString("searchStartDate", searchStartDate);
                b.putString("searchStartTime", searchStartTime);
                b.putString("searchEndDate", searchEndDate);
                b.putString("searchEndTime", searchEndTime);
                searchResults.putExtras(b);
                startActivity(searchResults);
            }
        });

        return v;
    }

    public void showTimePickerDialog()
    {
        final Dialog d = new Dialog(getContext());
        d.setTitle(getResources().getString(R.string.choose_length));
        d.setContentView(R.layout.dialog_number_picker);
        Button b1 = d.findViewById(R.id.button1);
        Button b2 = d.findViewById(R.id.button2);
        final NumberPicker np = d.findViewById(R.id.numberPicker1);
        np.setMaxValue(100);
        np.setMinValue(1);
        np.setWrapSelectorWheel(false);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (!searchStartDate.equals("-") && !searchStartTime.equals("-")){
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(searchStartDate.split("\\.")[0]));
                    calendar.set(Calendar.MONTH, Integer.valueOf(searchStartDate.split("\\.")[1]));
                    calendar.set(Calendar.YEAR, Integer.valueOf(searchStartDate.split("\\.")[2]));
                    calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(searchStartTime.split(":")[0]));
                    calendar.set(Calendar.MINUTE, Integer.valueOf(searchStartTime.split(":")[1]));

                    calendar.add(Calendar.HOUR, np.getValue());

                    String hour = Integer.toString(calendar.get(Calendar.HOUR_OF_DAY));
                    String minute = Integer.toString(calendar.get(Calendar.MINUTE));

                    if (Integer.valueOf(hour) < 10){
                        hour = "0" + hour;
                    }
                    if (Integer.valueOf(minute) < 10){
                        minute = "0" + minute;
                    }
                    searchEndTime = hour + ":" + minute;

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
                    searchEndDate = day + "." + month + "." + year;

                    updateSearchTermsTexts();
                }
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

        if (event.getTag() == 0){
            searchStartTime = hour + ":" + minute;
        } else if (event.getTag() == 1){
            searchEndTime = hour + ":" + minute;
        }
        updateSearchTermsTexts();
    }

    @Subscribe
    public void onDatePicked(DateEvent event) {
        if (event.getTag() == 0){
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
        } else if (event.getTag() == 1){
            String day = Integer.toString(event.getDay());
            String month = Integer.toString(event.getMonth());
            String year = Integer.toString(event.getYear());
            if (event.getDay() < 10){
                day = "0" + day;
            }
            if (event.getMonth() < 10){
                month = "0" + month;
            }
            searchEndDate = day + "." + month + "." + year;
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
        getBuildingsRoomsTypes();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }

    public void updateSearchTermsTexts(){
        TextView textViewChosenBuilding = getActivity().findViewById(R.id.TVChosenBuilding);
        textViewChosenBuilding.setText(searchBuildingCode);
        TextView textViewChosenRoomType = getActivity().findViewById(R.id.TVChosenRoomType);
        textViewChosenRoomType.setText(searchBuildingRoomType);
        TextView textViewChosenStartDate = getActivity().findViewById(R.id.TVChosenStartDate);
        textViewChosenStartDate.setText(searchStartDate);
        TextView textViewChosenStartTime = getActivity().findViewById(R.id.TVChosenStartTime);
        textViewChosenStartTime.setText(searchStartTime);
        TextView textViewChosenEndDate = getActivity().findViewById(R.id.TVChosenEndDate);
        textViewChosenEndDate.setText(searchEndDate);
        TextView textViewChosenEndTime = getActivity().findViewById(R.id.TVChosenEndTime);
        textViewChosenEndTime.setText(searchEndTime);
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

    public void getBuildingsRoomsTypes(){
        InputStream is;
        String json = null;
        JSONObject jsonObject = null;
        try {
            is = getActivity().getAssets().open("allbuildingswithtypes");
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
            JSONArray jsonArray = jsonObject.optJSONArray(key);
            ArrayList<String> list = new ArrayList<String>();
            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int i = 0; i < len; i++) {
                    try {
                        list.add(jsonArray.get(i).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            Collections.sort(list, new Comparator<String>(){
                public int compare(String obj1, String obj2) {
                    return obj1.compareToIgnoreCase(obj2);
                }
            });

            buildingsRoomTypesMap.put(key, list);
        }

    }
}
