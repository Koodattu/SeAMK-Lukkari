package com.seamk.mobile.roomsearch;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.seamk.mobile.objects.Building;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Juha Ala-Rantala on 5.5.2017.
 */

public class FragmentSearchInstant extends Fragment{

    Dialog dialog;
    List<Building> buildings;
    List<String> buildingsStrings;
    String searchBuildingCode = "-";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search_instant, container, false);

        Button buttonChooseBuilding = v.findViewById(R.id.search_choose_building_instant);
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

        Button buttonDoSearch = v.findViewById(R.id.BSuoritaHakuInstant);
        buttonDoSearch.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent searchResults = new Intent(getContext(), RootActivityEmptyRoomSearchResults.class);
                Bundle b = new Bundle();
                b.putString("typeOfSearch", "quick");
                b.putString("searchBuildingCode", searchBuildingCode);
                searchResults.putExtras(b);
                startActivity(searchResults);
            }
        });

        return v;
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
        TextView textViewChosenBuilding = getActivity().findViewById(R.id.TVChosenBuildingInstant);
        textViewChosenBuilding.setText(searchBuildingCode);
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
