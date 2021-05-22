package com.seamk.mobile;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.seamk.mobile.elasticsearch.BucketBuildings;
import com.seamk.mobile.elasticsearch.ElasticBuildings;
import com.seamk.mobile.interfaces.callback.ElasticBuildingsCallback;
import com.seamk.mobile.objects.Building;
import com.seamk.mobile.retrofit.RetroFitters;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class FragmentFindEmptyRoom extends UtilityFragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener  {

    @BindView(R.id.RBAll) RadioButton RBAll;
    @BindView(R.id.RBAtk) RadioButton RBAtk;
    @BindView(R.id.RBTheory) RadioButton RBTheory;
    @BindView(R.id.RBBox) RadioButton RBBox;
    @BindView(R.id.TVChosenBuilding) TextView TVChosenBuilding;
    @BindView(R.id.TVChosenDate) TextView TVChosenDate;
    @BindView(R.id.TVChosenTime) TextView TVChosenTime;
    @BindView(R.id.BChooseBuilding) Button BChooseBuilding;
    @BindView(R.id.BChooseDate) Button BChooseDate;
    @BindView(R.id.BChooseTime) Button BChooseTime;
    @BindView(R.id.BNow) Button BNow;
    @BindView(R.id.BSearch) Button BSearch;
    @BindView(R.id.PBBuilding) ProgressBar PBBuilding;

    String buildingCode = "-";
    String roomType = "all";
    String startDate = "-";
    String startTime = "-";

    List<Building> buildings = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_find_empty_room, container, false);
        ButterKnife.bind(this, v);

        getActivity().setTitle(getString(R.string.find_empty_room));

        BChooseBuilding.setOnClickListener(this);
        RBAll.setOnClickListener(this);
        RBAtk.setOnClickListener(this);
        RBTheory.setOnClickListener(this);
        RBBox.setOnClickListener(this);
        BChooseDate.setOnClickListener(this);
        BChooseTime.setOnClickListener(this);
        BNow.setOnClickListener(this);
        BSearch.setOnClickListener(this);

        RBAll.setChecked(true);
        BChooseBuilding.setVisibility(View.GONE);
        PBBuilding.setVisibility(View.VISIBLE);

        try {
            RetroFitters.fetchElasticBuildings(getContext(), 180, new ElasticBuildingsCallback() {
                @Override
                public void onSuccess(ElasticBuildings elasticBuildings) {
                    if (elasticBuildings.getAggregations().getMostReservedBuildings().getBuckets().size() == 0) {
                        onError("TODO");
                    } else {
                        for (BucketBuildings bucket : elasticBuildings.getAggregations().getMostReservedBuildings().getBuckets()) {
                            String code = bucket.getKey();
                            String name = "";
                            if (bucket.getMostReservedBuildingsNames().getBuckets().size() > 0) {
                                name = bucket.getMostReservedBuildingsNames().getBuckets().get(0).getKey();
                            }

                            buildings.add(new Building(code, name));
                        }

                        Collections.sort(buildings, new Comparator<Object>() {
                            public int compare(Object obj1, Object obj2) {
                                return ((Building) obj1).getBuildingName().compareToIgnoreCase(((Building) obj2).getBuildingName());
                            }
                        });

                        BChooseBuilding.setVisibility(View.VISIBLE);
                        PBBuilding.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Throwable throwable) {
                    onError(throwable.toString());
                }
            });
        } catch (IOException | JSONException e) {
            onError(e.toString());
        }

        return v;
    }

    void onError(String message) {
        BChooseBuilding.setEnabled(false);
        BChooseBuilding.setVisibility(View.VISIBLE);
        PBBuilding.setVisibility(View.GONE);
        if (message.contains("SocketTimeoutException")) {
            Toasty.error(getContext(), getString(R.string.error_time_out_buildings), Toast.LENGTH_LONG, true).show();
        } else if (message.contains("UnknownHostException")) {
            Toasty.error(getContext(), getString(R.string.error_no_internet_buildings), Toast.LENGTH_LONG, true).show();
        } else {
            Toasty.error(getContext(), getString(R.string.error_unknown_buildings), Toast.LENGTH_LONG, true).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.BChooseBuilding:
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_select_building);
                RadioGroup rg = dialog.findViewById(R.id.radio_group);

                for(int i = 0; i < buildings.size(); i++){
                    RadioButton rb = new RadioButton(getActivity());
                    rb.setText(buildings.get(i).getBuildingName());
                    rb.setMaxLines(1);
                    rb.setTextSize(TypedValue.COMPLEX_UNIT_SP,24);
                    rb.setPadding(8,8,8,8);

                    rg.addView(rb);
                }

                rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        int childCount = group.getChildCount();
                        for (int i = 0; i < childCount; i++) {
                            RadioButton btn = (RadioButton) group.getChildAt(i);
                            if (btn.getId() == checkedId) {
                                buildingCode = buildings.get(i).getBuildingName();
                                dialog.dismiss();
                                updateSearchTermsTexts();
                            }
                        }
                    }
                });

                dialog.show();
                break;
            case R.id.RBAll:
                roomType = "all";
                RBAll.setChecked(true);
                RBAtk.setChecked(false);
                RBTheory.setChecked(false);
                RBBox.setChecked(false);
                break;
            case R.id.RBAtk:
                roomType = "atk";
                RBAll.setChecked(false);
                RBAtk.setChecked(true);
                RBTheory.setChecked(false);
                RBBox.setChecked(false);
                break;
            case R.id.RBTheory:
                roomType = "theory";
                RBAll.setChecked(false);
                RBAtk.setChecked(false);
                RBTheory.setChecked(true);
                RBBox.setChecked(false);
                break;
            case R.id.RBBox:
                roomType = "box";
                RBAll.setChecked(false);
                RBAtk.setChecked(false);
                RBTheory.setChecked(false);
                RBBox.setChecked(true);
                break;
            case R.id.BChooseDate:
                Calendar cDate = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, cDate.get(Calendar.YEAR), cDate.get(Calendar.MONTH), cDate.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
                break;
            case R.id.BChooseTime:
                Calendar cTime = Calendar.getInstance();
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), this, cTime.get(Calendar.HOUR_OF_DAY), cTime.get(Calendar.MINUTE), true);
                timePickerDialog.show();
                break;
            case R.id.BNow:
                Calendar calendar = Calendar.getInstance();

                String hour = Integer.toString(calendar.get(Calendar.HOUR_OF_DAY));
                String minute = Integer.toString(calendar.get(Calendar.MINUTE));
                if (Integer.valueOf(hour) < 10){
                    hour = "0" + hour;
                }
                if (Integer.valueOf(minute) < 10){
                    minute = "0" + minute;
                }
                startTime = hour + ":" + minute;

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
                startDate = day + "." + month + "." + year;
                updateSearchTermsTexts();
                break;
            case R.id.BSearch:
                Fragment fragment = new FragmentFindEmptyRoomResults();
                Bundle bundle = new Bundle();
                bundle.putString("buildingCode", buildingCode);
                bundle.putString("roomType", roomType);
                bundle.putString("startDate", startDate);
                bundle.putString("startTime", startTime);
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                transaction.replace(R.id.frame, fragment, "FIND_EMPTY_ROOM_RESULTS");
                transaction.addToBackStack("TAG");
                transaction.commit();
                break;
        }
    }

    void updateSearchTermsTexts(){
        TVChosenBuilding.setText(buildingCode);
        TVChosenDate.setText(startDate);
        TVChosenTime.setText(startTime);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String sDay = Integer.toString(dayOfMonth);
        String sMonth = Integer.toString(month+1);
        String sYear = Integer.toString(year);
        if (dayOfMonth < 10){
            sDay = "0" + sDay;
        }
        if (month < 10){
            sMonth = "0" + sMonth;
        }

        startDate = sDay + "." + sMonth + "." + sYear;

        updateSearchTermsTexts();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String hour = Integer.toString(hourOfDay);
        String min = Integer.toString(minute);

        if (Integer.valueOf(hour) < 10){
            hour = "0" + hour;
        }
        if (Integer.valueOf(min) < 10){
            min = "0" + min;
        }

        startTime = hour + ":" + min;
        updateSearchTermsTexts();
    }
}
