package com.seamk.mobile.search;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.seamk.mobile.R;
import com.seamk.mobile.adapters.ClassroomsAdapter;
import com.seamk.mobile.objects.Building;
import com.seamk.mobile.objects.Classroom;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Juha Ala-Rantala on 18.4.2017.
 */

public class RootFragmentClassrooms extends Fragment implements SearchView.OnQueryTextListener {

    RecyclerView recyclerView;
    ClassroomsAdapter adapter;
    private List<Classroom> allClassrooms;
    private List<Classroom> classrooms;
    List<Building> buildingsCodesNames;
    List<Building> buildingsIdsCodes;
    List<Building> combinedBuildings;
    List<String> buildingsNamesStrings;
    Map<String, List<Classroom>> buildingsRoomObjectsMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_classrooms, container, false);

        //classrooms = allClassrooms;
        setHasOptionsMenu(true);
        getActivity().setTitle(getResources().getString(R.string.rooms));
        recyclerView = v.findViewById(R.id.rv);
        GridLayoutManager glm = new GridLayoutManager(v.getContext(),3);
        recyclerView.setLayoutManager(glm);
        recyclerView.setHasFixedSize(true);

        final Spinner spinner = v.findViewById(R.id.spinner_buildings);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, buildingsNamesStrings);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (spinner.getSelectedItemPosition() == 0){
                    classrooms = allClassrooms;
                } else {
                    classrooms = buildingsRoomObjectsMap.get(spinner.getSelectedItem().toString());
                }
                recyclerView.invalidate();
                adapter = new ClassroomsAdapter(classrooms, getContext());
                recyclerView.setAdapter(adapter);
                if (searchView != null){
                    adapter.filter(searchView.getQuery().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        adapter = new ClassroomsAdapter(classrooms, getContext());
        recyclerView.setAdapter(adapter);

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        classrooms = new ArrayList<>();
        buildingsCodesNames = new ArrayList<>();
        buildingsIdsCodes = new ArrayList<>();
        buildingsNamesStrings = new ArrayList<>();
        combinedBuildings = new ArrayList<>();
        buildingsRoomObjectsMap = new HashMap<>();

        getBuildingsCodeName();
        getBuildingsIdCode();
        getClassrooms();

        for (int i = 0; i < buildingsCodesNames.size(); i++){
            for (int j = 0; j < buildingsIdsCodes.size(); j++){
                if (buildingsCodesNames.get(i).getBuildingCode().equals(buildingsIdsCodes.get(j).getBuildingCode())){
                    combinedBuildings.add(new Building(buildingsCodesNames.get(i).getBuildingCode(), buildingsCodesNames.get(i).getBuildingName(), buildingsIdsCodes.get(j).getId()));
                }
            }
        }

        getBuildingIdRoomCodeName();

        buildingsNamesStrings.add("Kaikki");
        for (int i = 0; i < combinedBuildings.size(); i++){
            buildingsNamesStrings.add(combinedBuildings.get(i).getBuildingName());
        }
    }

    SearchView searchView;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.menu_search, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        adapter.filter(query);
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        searchView.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filter(newText);
        return true;
    }

    public void getClassrooms(){
        allClassrooms = new ArrayList<>();
        InputStream is;
        String json = null;
        JSONObject jsonObject = null;
        try {
            is = getActivity().getAssets().open("roomscodesnames");
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
        int x = 0;
        Iterator<String> keys = jsonObject.keys();
        while( keys.hasNext() ){
            String key = keys.next();
            String value = jsonObject.optString(key);
            Classroom classroom = new Classroom(key, value);
            allClassrooms.add(classroom);
            x++;
        }
        Collections.sort(allClassrooms, new Comparator<Classroom>(){
            public int compare(Classroom obj1, Classroom obj2) {
                return obj1.getClassRoomFullName().compareToIgnoreCase(obj2.getClassRoomFullName());
            }
        });
    }

    public void getBuildingsCodeName(){

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
            Building building = new Building(key, value);
            buildingsCodesNames.add(building);
        }
        Collections.sort(buildingsCodesNames, new Comparator<Building>(){
            public int compare(Building obj1, Building obj2) {
                return obj1.getBuildingCode().compareToIgnoreCase(obj2.getBuildingCode());
            }
        });
    }

    public void getBuildingsIdCode(){

        InputStream is;
        String json = null;
        JSONObject jsonObject = null;
        try {
            is = getActivity().getAssets().open("buildingidcode");
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
            Building building = new Building(value, Integer.parseInt(key));
            buildingsIdsCodes.add(building);
        }

        Collections.sort(buildingsIdsCodes, new Comparator<Building>(){
            public int compare(Building obj1, Building obj2) {
                return obj1.getBuildingCode().compareToIgnoreCase(obj2.getBuildingCode());
            }
        });
    }


    public void getBuildingIdRoomCodeName(){

        InputStream is;
        String json = null;
        JSONArray jsonArray = null;
        try {
            is = getActivity().getAssets().open("buildingidroomcodename");
            int size = 0;
            size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            jsonArray = new JSONArray(json);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int j = 0; j < jsonArray.length(); j++){
            JSONObject jsonObject = null;
            try {
                jsonObject = jsonArray.getJSONObject(j);
            } catch (JSONException e){
                e.printStackTrace();
            }
            Iterator<String> keys = jsonObject.keys();
            while( keys.hasNext() ){
                String key = keys.next();
                JSONObject jsonObject1 = jsonObject.optJSONObject(key);
                List<Classroom> tempClassrooms = new ArrayList<>();

                Iterator<String> keys2 = jsonObject1.keys();
                while( keys2.hasNext() ) {
                    String key2 = keys2.next();
                    String value2 = jsonObject1.optString(key2);
                    tempClassrooms.add(new Classroom(key2, value2));
                }
                Collections.sort(tempClassrooms, new Comparator<Classroom>(){
                    public int compare(Classroom obj1, Classroom obj2) {
                        return obj1.getClassRoomFullName().compareToIgnoreCase(obj2.getClassRoomFullName());
                    }
                });
                for (int i = 0; i < combinedBuildings.size(); i++){
                    if (combinedBuildings.get(i).getId().equals(key)){
                        buildingsRoomObjectsMap.put(combinedBuildings.get(i).getBuildingName(), tempClassrooms);
                    }
                }
            }
        }
    }
}