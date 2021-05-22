package com.seamk.mobile.roomsearch;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.seamk.mobile.R;
import com.seamk.mobile.adapters.EmptyRoomAdapter;
import com.seamk.mobile.objects.Building;
import com.seamk.mobile.objects.Classroom;
import com.seamk.mobile.objects.EmptyRoom;
import com.seamk.mobile.objects.ReservationOld;
import com.seamk.mobile.util.Common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.seamk.mobile.util.Common.stringToString;

/**
 * Created by Juha Ala-Rantala on 7.9.2017.
 */

public class RootActivityEmptyRoomSearchResults extends AppCompatActivity {

    String typeOfSearch;
    String searchBuildingCode = "-";
    String searchBuildingRoomType = "-";
    String searchStartDate = "-";
    String searchStartTime = "-";
    String searchEndDate = "-";
    String searchEndTime = "-";
    String searchLength = "-";

    List<ReservationOld> reservationOlds = new ArrayList<>();
    FetchReservations fetchReservations;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    TextView textView;
    EmptyRoomAdapter adapter;

    List<Classroom> allClassrooms;
    List<Building> buildingsCodesNames;
    List<Building> buildingsIdsCodes;
    List<Building> combinedBuildings;
    Map<String, List<Classroom>> buildingsRoomObjectsMap;
    Map<String, List<ReservationOld>> roomsReservationsMap;
    List<EmptyRoom> emptyRooms;

    long longStartDate;
    long longEndDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getResources().getString(R.string.search_results));
        setContentView(R.layout.activity_empty_room_search_results);

        buildingsCodesNames = new ArrayList<>();
        buildingsIdsCodes = new ArrayList<>();
        combinedBuildings = new ArrayList<>();
        buildingsRoomObjectsMap = new HashMap<>();
        roomsReservationsMap = new HashMap<>();
        emptyRooms = new ArrayList<>();

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

        for ( String key : buildingsRoomObjectsMap.keySet() ) {
            for (int i = 0; i < buildingsRoomObjectsMap.get(key).size(); i++){
                roomsReservationsMap.put(buildingsRoomObjectsMap.get(key).get(i).getClassRoomCode(), new ArrayList<ReservationOld>());
            }
        }

        recyclerView = findViewById(R.id.rv);
        progressBar = findViewById(R.id.progressbar);
        textView = findViewById(R.id.empty_view);

        Bundle b = getIntent().getExtras();
        typeOfSearch = b.getString("typeOfSearch");

        if (typeOfSearch == null){
            Toast.makeText(this, "Haku epäonnistui.", Toast.LENGTH_SHORT).show();
        } else if (typeOfSearch.equals("quick")){
            searchBuildingCode = b.getString("searchBuildingCode");
        } else if (typeOfSearch.equals("basic")){
            searchBuildingCode = b.getString("searchBuildingCode");
            searchBuildingRoomType = b.getString("searchBuildingRoomType");
            searchStartDate = b.getString("searchStartDate");
            searchStartTime = b.getString("searchStartTime");
            searchLength = b.getString("searchLength");
        } else if (typeOfSearch.equals("advanced")){
            searchBuildingCode = b.getString("searchBuildingCode");
            searchBuildingRoomType = b.getString("searchBuildingRoomType");
            searchStartDate = b.getString("searchStartDate");
            searchStartTime = b.getString("searchStartTime");
            searchEndDate = b.getString("searchEndDate");
            searchEndTime = b.getString("searchEndTime");
        } else {
            Toast.makeText(this, "Näin ei voi tapahtua.", Toast.LENGTH_SHORT).show();
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(RootActivityEmptyRoomSearchResults.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

        if (searchBuildingRoomType.equals("all")){
            searchBuildingRoomType = "-";
        }

        DateFormat formatDateFull = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
        String stringStartDate = null;
        String stringEndDate = null;
        String endTime = "23:59:59";
        Calendar now = Calendar.getInstance();

        if (searchStartDate.equals("-")){
            stringStartDate = formatDate.format(now.getTime());
        } else {
            stringStartDate = searchStartDate.split("\\.")[2] + "-" + searchStartDate.split("\\.")[1] + "-" + searchStartDate.split("\\.")[0];
        }
        if (searchStartTime.equals("-")){
            stringStartDate = stringStartDate + " " + formatTime.format(now.getTime());
        } else {
            stringStartDate = stringStartDate + " " + searchStartTime + ":00";
        }
        if (searchEndDate.equals("-")){
            stringEndDate = formatDate.format(now.getTime());
        } else {
            stringEndDate = searchEndDate.split("\\.")[2] + "-" + searchEndDate.split("\\.")[1] + "-" + searchEndDate.split("\\.")[0];
        }
        if (searchEndTime.equals("-")){
            stringEndDate = stringEndDate + " " + endTime;
        } else {
            stringEndDate = stringEndDate + " " + searchEndTime + ":00";
        }
        Date dateStartDate = null;
        Date dateEndDate = null;
        try {dateStartDate = formatDateFull.parse(stringStartDate);} catch (ParseException e) {e.printStackTrace();}
        try {dateEndDate = formatDateFull.parse(stringEndDate);} catch (ParseException e) {e.printStackTrace();}
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateStartDate);
        longStartDate = calendar.getTimeInMillis();
        calendar.setTime(dateEndDate);
        longEndDate = calendar.getTimeInMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (fetchReservations != null){
            fetchReservations.cancel(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchReservations = new FetchReservations();
        fetchReservations.execute();
    }

    private class FetchReservations extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            MediaType JSONData = MediaType.parse("application/json; charset=utf-8");
            OkHttpClient client = new OkHttpClient();

            Calendar calendar = Calendar.getInstance();
            calendar.setFirstDayOfWeek(Calendar.MONDAY);

            DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat formatTime = new SimpleDateFormat("HH:mm:ss");

            String startTime = "00:00:00";
            String endTime = "23:59:59";
            String startDate;
            String endDate;

            String url = "https://opendata.seamk.fi:443/r1/reservationOld/search?l=fi";

            if (typeOfSearch.equals("quick")){
                startDate = formatDate.format(calendar.getTime()) + "T" + formatTime.format(calendar.getTime());
                endDate = formatDate.format(calendar.getTime()) + "T" + endTime;
            } else if (typeOfSearch.equals("basic")) {
                if (searchStartDate.equals("-")){
                    startDate = formatDate.format(calendar.getTime()) + "T";
                    endDate = formatDate.format(calendar.getTime()) + "T";
                } else {
                    startDate = searchStartDate.split("\\.")[2] + "-" + searchStartDate.split("\\.")[1] + "-" + searchStartDate.split("\\.")[0] + "T";
                    endDate = searchStartDate.split("\\.")[2] + "-" + searchStartDate.split("\\.")[1] + "-" + searchStartDate.split("\\.")[0] + "T";
                }
                if (searchStartTime.equals("-")){
                    startDate = startDate + formatTime.format(calendar.getTime());
                    endDate = endDate + endTime;
                } else {
                    startDate = startDate + searchStartTime + ":00";
                    endDate = endDate + endTime;
                }
            } else  if (typeOfSearch.equals("advanced")) {
                if (searchStartDate.equals("-")){
                    startDate = formatDate.format(calendar.getTime()) + "T";
                } else {
                    startDate = searchStartDate.split("\\.")[2] + "-" + searchStartDate.split("\\.")[1] + "-" + searchStartDate.split("\\.")[0] + "T";
                }
                if (searchStartTime.equals("-")){
                    startDate = startDate + formatTime.format(calendar.getTime());
                } else {
                    startDate = startDate + searchStartTime + ":00";
                }
                if (searchEndDate.equals("-")){
                    endDate = formatDate.format(calendar.getTime()) + "T";
                } else {
                    endDate = searchEndDate.split("\\.")[2] + "-" + searchEndDate.split("\\.")[1] + "-" + searchEndDate.split("\\.")[0] + "T";
                }
                if (searchEndTime.equals("-")){
                    endDate = endDate + endTime;
                } else {
                    endDate = endDate + endTime;
                }
            } else {
                startDate = formatDate.format(calendar.getTime()) + "T" + startTime;
                endDate = formatDate.format(calendar.getTime()) + "T" + endTime;
            }

            String JSONPost;

            if (searchBuildingCode.equals("-")){
                JSONPost = "{'rangeStart':'" + startDate + "'," + "'rangeEnd':'" + endDate + "'}";
            } else {
                JSONPost = "{'rangeStart':'" + startDate + "'," + "'rangeEnd':'" + endDate + "'," + "'building':[" + "'" + searchBuildingCode + "'" + "]}";
            }

            RequestBody body = RequestBody.create(JSONData, JSONPost);

            String credential = Credentials.basic(stringToString(getApplicationContext()),"");

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .header("Authorization", credential)
                    .build();

            try {
                Response response = client.newCall(request).execute();

                JSONObject jsonObject = new JSONObject(response.body().string());
                JSONArray JSONReservations = jsonObject.getJSONArray("reservationOlds");

                reservationOlds = Common.reservationsFromJson(jsonObject);
            } catch (IOException e){
                e.printStackTrace();
            } catch (JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            reservationOlds.clear();
            textView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            int minutes = 45;
            long fortyfive = minutes * 60 * 1000;
            minutes = 15;
            long fifteen = minutes * 60 * 1000;

            for (int i = 0; i < reservationOlds.size(); i++){
                if (!reservationOlds.get(i).getRoomCode().get(0).equals("")){
                    for (int j = 0; j < reservationOlds.get(i).getRoomCode().size(); j++){
                        if (roomsReservationsMap.containsKey(reservationOlds.get(i).getRoomCode().get(j))){ // jos mapista löytyy jo kyseinen huone
                            roomsReservationsMap.get(reservationOlds.get(i).getRoomCode().get(j)).add(reservationOlds.get(i));
                        } else { // jos mapista ei vielä löydy kyseistä huonetta
                            roomsReservationsMap.put(reservationOlds.get(i).getRoomCode().get(j), new ArrayList<ReservationOld>());
                            roomsReservationsMap.get(reservationOlds.get(i).getRoomCode().get(j)).add(reservationOlds.get(i));
                        }
                    }
                }
            }
            Map<String, List<ReservationOld>> sortedRoomsReservationsMap = new TreeMap<>(roomsReservationsMap);
            for ( String key : sortedRoomsReservationsMap.keySet() ) {
                if (sortedRoomsReservationsMap.get(key).size() == 0){
                    Classroom classroom = new Classroom();
                    for (int i = 0; i < allClassrooms.size(); i++){
                        if (key.equals(allClassrooms.get(i).getClassRoomCode())){
                            classroom = allClassrooms.get(i);
                        }
                    }
                    String buildingCode = "";
                    for ( String rax : buildingsRoomObjectsMap.keySet() ) {
                        for (int i = 0; i < buildingsRoomObjectsMap.get(rax).size(); i++){
                            if (classroom.getClassRoomCode().equals(buildingsRoomObjectsMap.get(rax).get(i).getClassRoomCode())){
                                buildingCode = rax;
                            }
                        }
                    }
                    if (searchBuildingCode.equals("-")){
                        if (!searchBuildingRoomType.equals("-")){
                            if (classroom.getClassRoomFullName().contains(searchBuildingRoomType)){
                                emptyRooms.add(new EmptyRoom(classroom.getClassRoomCode(), classroom.getClassRoomFullName(), buildingCode, 0, 0, longStartDate));
                            }
                        } else {
                            emptyRooms.add(new EmptyRoom(classroom.getClassRoomCode(), classroom.getClassRoomFullName(), buildingCode, 0, 0, longStartDate));
                        }
                    } else {
                        if (buildingCode.equals(searchBuildingCode)){
                            if (!searchBuildingRoomType.equals("-")){
                                if (classroom.getClassRoomFullName().contains(searchBuildingRoomType)){
                                    emptyRooms.add(new EmptyRoom(classroom.getClassRoomCode(), classroom.getClassRoomFullName(), buildingCode, 0, 0, longStartDate));
                                }
                            } else {
                                emptyRooms.add(new EmptyRoom(classroom.getClassRoomCode(), classroom.getClassRoomFullName(), buildingCode, 0, 0, longStartDate));
                            }
                        }
                    }
                } else {
                    List<ReservationOld> reservationOlds = sortedRoomsReservationsMap.get(key);
                    ReservationOld reservationOld = sortedRoomsReservationsMap.get(key).get(0);
                    long dateWhenEmpty = 0;
                    long dateWhenNextReservation = 0;

                    if (reservationOld.getLongStartDate() - fifteen <= longStartDate && longStartDate <= (reservationOld.getLongEndDate() + fifteen)) { //juuri nyt on varaus
                        for (int i = 0; i < reservationOlds.size(); i++) {
                            if (i != reservationOlds.size() - 1) { // jos ei ole viimeinen varaus
                                if ((reservationOlds.get(i + 1).getLongStartDate()) - reservationOlds.get(i).getLongEndDate() > fortyfive) {
                                    dateWhenEmpty = reservationOlds.get(i).getLongEndDate();
                                    dateWhenNextReservation = reservationOlds.get(i + 1).getLongStartDate();
                                    break;
                                }
                            } else { // jos on viimeinen varaus
                                dateWhenEmpty = reservationOlds.get(i).getLongEndDate();
                                dateWhenNextReservation = 0;
                            }
                        }
                    } else { //juuri nyt ei ole varausta
                            dateWhenEmpty = 0;
                            dateWhenNextReservation = reservationOld.getLongStartDate();
                        }

                        Classroom classroom = new Classroom();
                        for (int x = 0; x < allClassrooms.size(); x++){
                            if (key.equals(allClassrooms.get(x).getClassRoomCode())){
                                classroom = allClassrooms.get(x);
                            }
                        }
                        String buildingCode = "";
                        for ( String rax : buildingsRoomObjectsMap.keySet() ) {
                            for (int x = 0; x < buildingsRoomObjectsMap.get(rax).size(); x++){
                                if (classroom.getClassRoomCode().equals(buildingsRoomObjectsMap.get(rax).get(x).getClassRoomCode())){
                                    buildingCode = rax;
                                }
                            }
                        }
                        if (searchBuildingCode.equals("-")){
                            if (!searchBuildingRoomType.equals("-")){
                                if (classroom.getClassRoomFullName().contains(searchBuildingRoomType)){
                                    emptyRooms.add(new EmptyRoom(classroom.getClassRoomCode(), classroom.getClassRoomFullName(), buildingCode, dateWhenEmpty, dateWhenNextReservation, longStartDate));
                                }
                            } else {
                                emptyRooms.add(new EmptyRoom(classroom.getClassRoomCode(), classroom.getClassRoomFullName(), buildingCode, dateWhenEmpty, dateWhenNextReservation, longStartDate));
                            }
                        } else {
                            if (buildingCode.equals(searchBuildingCode)){
                                if (!searchBuildingRoomType.equals("-")){
                                    if (classroom.getClassRoomFullName().contains(searchBuildingRoomType)){
                                        emptyRooms.add(new EmptyRoom(classroom.getClassRoomCode(), classroom.getClassRoomFullName(), buildingCode, dateWhenEmpty, dateWhenNextReservation, longStartDate));
                                    }
                                } else {
                                    emptyRooms.add(new EmptyRoom(classroom.getClassRoomCode(), classroom.getClassRoomFullName(), buildingCode, dateWhenEmpty, dateWhenNextReservation, longStartDate));
                                }
                            }
                        }
                }
            }

            Collections.sort(emptyRooms, new Comparator<EmptyRoom>(){
                public int compare(EmptyRoom obj1, EmptyRoom obj2) {
                    if (obj1.dateWhenEmpty == obj2.dateWhenEmpty) {
                        if (obj1.timeEmptyFor == obj2.timeEmptyFor) {
                            return obj1.getRoomBuilding().compareToIgnoreCase(obj2.getRoomBuilding());
                        } else {
                            return obj1.timeEmptyFor > obj2.timeEmptyFor ? -1 : 1;
                        }
                    } else {
                        return obj1.dateWhenEmpty < obj2.dateWhenEmpty ? -1 : 1;
                    }
                }
            });

            adapter = new EmptyRoomAdapter(RootActivityEmptyRoomSearchResults.this, emptyRooms);
            recyclerView.setAdapter(adapter);
            if (emptyRooms.size() == 0){
                textView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            } else {
                textView.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void getClassrooms(){
        allClassrooms = new ArrayList<>();
        InputStream is;
        String json = null;
        JSONObject jsonObject = null;
        try {
            is = getAssets().open("roomscodesnames");
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
            is = getAssets().open("buildingscodesnames");
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
            is = getAssets().open("buildingidcode");
            int size = 0;
            size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            jsonObject = new JSONObject(json);
        } catch (IOException | JSONException e) {
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
            is = getAssets().open("buildingidroomcodename");
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
                        buildingsRoomObjectsMap.put(combinedBuildings.get(i).getBuildingCode(), tempClassrooms);
                    }
                }
            }
        }
    }
}
