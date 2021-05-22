package com.seamk.mobile.timetable;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.seamk.mobile.MainActivity;
import com.seamk.mobile.R;
import com.seamk.mobile.UtilityFragment;
import com.seamk.mobile.adapters.NewSummaryCoursesAdapter;
import com.seamk.mobile.adapters.SummaryReservationsAdapter;
import com.seamk.mobile.objects.BasketRealization;
import com.seamk.mobile.objects.BasketSavedItems;
import com.seamk.mobile.objects.BasketStudentGroup;
import com.seamk.mobile.objects.Course;
import com.seamk.mobile.objects.ReservationOld;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.seamk.mobile.util.Common.getItemType;
import static com.seamk.mobile.util.Common.reservationsFromJson;
import static com.seamk.mobile.util.Common.stringToString;

/**
 * Created by Juha Ala-Rantala on 2.8.2017.
 */

public class RootFragmentSummary extends UtilityFragment {

    BasketSavedItems basketSavedItems = new BasketSavedItems();
    List<BasketStudentGroup> studentGroupBaskets = new ArrayList<>();
    List<BasketRealization> basketsToSearchRealizations = new ArrayList<>();
    List<BasketRealization> basketRealizations = new ArrayList<>();

    ScrollView sv;

    RecyclerView recyclerViewCourses;
    RecyclerView recyclerViewReservations;
    List<Course> courses;
    List<ReservationOld> reservationOlds;
    SummaryReservationsAdapter summaryReservationsAdapter;
    NewSummaryCoursesAdapter newSummaryCoursesAdapter;
    String[] days = new String[7];
    String[] daysRestaurant = new String[7];
    List<Object> allDaysReservations;
    List<Object> allDaysCourses;

    private FetchSodexoCourses fetchSodexoCourses;
    private FetchReservations fetchReservations;
    private FetchAreenaCourses fetchAreenaCourses;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        courses = new ArrayList<>();
        reservationOlds = new ArrayList<>();
        allDaysReservations = new ArrayList<>();
        allDaysCourses = new ArrayList<>();

        daysRestaurant[0] = getString(R.string.monday);
        daysRestaurant[1] = getString(R.string.tuesday);
        daysRestaurant[2] = getString(R.string.wednesday);
        daysRestaurant[3] = getString(R.string.thursday);
        daysRestaurant[4] = getString(R.string.friday);
        daysRestaurant[5] = getString(R.string.saturday);
        daysRestaurant[6] = getString(R.string.sunday);

        Handler h = new Handler();
        h.postDelayed(new Runnable(){
            @Override
            public void run(){
                canRefresh = true;
            }
        }, 1000);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.root_fragment_summary_page, container, false);

        sv = v.findViewById(R.id.scroll);
        setHasOptionsMenu(true);

        SharedPreferences settings = getActivity().getSharedPreferences("ApplicationPreferences", 0);
        final RelativeLayout relativeLayout = v.findViewById(R.id.adSpace);
        if (settings.getBoolean("showAdverts", true)){
            final AdView adView = getActivity().findViewById(R.id.adView);
            adView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    int height = adView.getHeight();
                    if (height > 0) {
                        relativeLayout.getLayoutParams().height = height;
                    }
                }
            });
        } else {
            relativeLayout.getLayoutParams().height = 0;
        }
        relativeLayout.requestLayout();

        getActivity().setTitle(getResources().getString(R.string.summary));

        String studentGroup = getResources().getString(R.string.personal);
        if (studentGroupBaskets.size() == 1){
            studentGroup = studentGroupBaskets.get(0).getStudentGroupCode();
        }

        TextView textView0 = v.findViewById(R.id.lukkari_bar_text_view);
        textView0.setText(getResources().getString(R.string.timetable) + ", " + studentGroup);
        int locationIndex = Integer.valueOf(settings.getString("restaurantCode", "0"));
        String locationName = getRestaurantName(locationIndex);
        TextView textView1 = v.findViewById(R.id.ruokalista_bar_text_view);
        textView1.setText(getResources().getString(R.string.menu) + ", " + locationName);

        recyclerViewCourses = v.findViewById(R.id.rv_courses);
        GridLayoutManager gridLayoutManagerC = new GridLayoutManager(getActivity(), 1);
        recyclerViewCourses.setLayoutManager(gridLayoutManagerC);
        recyclerViewCourses.setHasFixedSize(true);

        recyclerViewReservations = v.findViewById(R.id.rv_reservations);
        final GridLayoutManager gridLayoutManagerR = new GridLayoutManager(getActivity(), 100);
        recyclerViewReservations.setLayoutManager(gridLayoutManagerR);
        recyclerViewReservations.setHasFixedSize(true);

        newSummaryCoursesAdapter = new NewSummaryCoursesAdapter(getContext(), allDaysCourses);
        recyclerViewCourses.setAdapter(newSummaryCoursesAdapter);

        summaryReservationsAdapter = new SummaryReservationsAdapter(getContext(), allDaysReservations);
        recyclerViewReservations.setAdapter(summaryReservationsAdapter);

        gridLayoutManagerR.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return gridLayoutManagerR.getSpanCount() / summaryReservationsAdapter.getItemType(position);
            }
        });

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        if (fetchReservations != null){
            fetchReservations.cancel(true);
        }
        if (fetchSodexoCourses != null){
            fetchSodexoCourses.cancel(true);
        }
        if (fetchAreenaCourses != null){
            fetchAreenaCourses.cancel(true);
        }
        super.onDestroy();
    }

    @Override
    public void onResume(){
        super.onResume();
        SharedPreferences preferences = getContext().getSharedPreferences("ApplicationPreferences", 0);
        if (studentGroupBaskets.size() == 0 && basketsToSearchRealizations.size() == 0 && preferences.getBoolean("tourComplete", false) && preferences.getBoolean("setupComplete", false)){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
            alertDialogBuilder.setTitle(getResources().getString(R.string.notification));

            alertDialogBuilder
                    .setMessage(getResources().getString(R.string.study_basket_empty) + "\n\n" + getResources().getString(R.string.question_go_to_study_basket))
                    .setNeutralButton(getResources().getString(R.string.close) ,new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            dialog.dismiss();
                        }
                    })
                    .setCancelable(true)
                    .setNegativeButton(getResources().getString(R.string.go_to_study_basket),new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            ((MainActivity)getActivity()).goToStudyBasket();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();

            alertDialog.show();
            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(16);
            alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextSize(16);
        }

        String studentGroup = getResources().getString(R.string.personal);
        if (studentGroupBaskets.size() == 1){
            studentGroup = studentGroupBaskets.get(0).getStudentGroupCode();
        }
        TextView textView = getActivity().findViewById(R.id.lukkari_bar_text_view);
        textView.setText(getResources().getString(R.string.timetable) + ", " + studentGroup);
        int locationIndex = Integer.valueOf(preferences.getString("restaurantCode", "0"));
        String locationName = getRestaurantName(locationIndex);
        TextView textView1 = getActivity().findViewById(R.id.ruokalista_bar_text_view);
        textView1.setText(getResources().getString(R.string.menu) + ", " + locationName);
    }

    private String getRestaurantName(int locationIndex){
        String locationName = "";

        switch(locationIndex){
            case 873:
                locationName = "Frami F";
                break;

            case 69:
                locationName = "Eventti";
                break;

            case 874:
                locationName = "Kampustalo";
                break;

            case 1401:
                locationName = "Koskenalantie";
                break;

            case 1404:
                locationName = "Ilmajoentie";
                break;
        }

        return locationName;
    }

    @Override
    public void onStart() {
        super.onStart();

        getBasketItems();

        SharedPreferences settings = getActivity().getSharedPreferences("ApplicationPreferences", Context.MODE_PRIVATE);
        if (settings.contains("restaurantCode")){
            if (Integer.valueOf(settings.getString("restaurantCode", "")) == 0){
                TextView emptyView = getActivity().findViewById(R.id.empty_view_restaurant_no_code);
                emptyView.setVisibility(View.VISIBLE);
            } else {
                if (Integer.valueOf(settings.getString("restaurantCode", "")) == 69) {
                    fetchAreenaCourses = new FetchAreenaCourses();
                    fetchAreenaCourses.execute();
                } else {
                    fetchSodexoCourses = new FetchSodexoCourses();
                    fetchSodexoCourses.execute();
                }
            }
        }
        if (basketRealizations.size() > 0 || studentGroupBaskets.size() > 0){
            fetchReservations = new FetchReservations();
            fetchReservations.execute();
        }
    }


    public void getBasketItems(){
        SharedPreferences settings = getActivity().getSharedPreferences("ApplicationPreferences", Context.MODE_PRIVATE);

        if (!settings.contains("basketSavedItems")){
            SharedPreferences.Editor mEditor = settings.edit();
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            try {
                basketSavedItems.setBasketRealizations(new ArrayList<BasketRealization>());
                basketSavedItems.setBasketStudentGroups(new ArrayList<BasketStudentGroup>());
                basketSavedItems.setBasketStudentGroupsRealizations(new ArrayList<BasketRealization>());
                String writeValue = gson.toJson(basketSavedItems);
                mEditor.putString("basketSavedItems", writeValue);
                mEditor.apply();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        String loadValue = settings.getString("basketSavedItems", "");
        Type listType = new TypeToken<BasketSavedItems>(){}.getType();

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        studentGroupBaskets = new ArrayList<>();
        basketRealizations = new ArrayList<>();
        basketsToSearchRealizations = new ArrayList<>();

        basketSavedItems = gson.fromJson(loadValue, listType);
        studentGroupBaskets = basketSavedItems.getBasketStudentGroups();
        basketsToSearchRealizations = basketSavedItems.getBasketRealizations();
        basketRealizations = basketSavedItems.getBasketStudentGroupsRealizations();
    }

    public void loadSavedReservations(){
        allDaysReservations = new ArrayList<>();
        SharedPreferences settings = getActivity().getSharedPreferences("ApplicationPreferences", Context.MODE_PRIVATE);
        String loadValue = settings.getString("summaryReservations", "");
        Type listType = new TypeToken<ArrayList<ReservationOld>>(){}.getType();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        reservationOlds = gson.fromJson(loadValue, listType);

        ReservationOld firstReservationOld = reservationOlds.get(0);
        Date firstDay = firstReservationOld.getDateStartDate();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(firstDay);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        DateFormat format = new SimpleDateFormat("dd.MM.");

        days[0] = getString(R.string.monday) + " " + format.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        days[1] = getString(R.string.tuesday) + " " + format.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        days[2] = getString(R.string.wednesday) + " " + format.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        days[3] = getString(R.string.thursday) + " " + format.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        days[4] = getString(R.string.friday) + " " + format.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        days[5] = getString(R.string.saturday) + " " + format.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        days[6] = getString(R.string.sunday) + " " + format.format(calendar.getTime());

        calendar.setTime(firstDay);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        Calendar cal3 = Calendar.getInstance();
        cal3.setFirstDayOfWeek(Calendar.MONDAY);

        settings = getActivity().getSharedPreferences("ApplicationPreferences", Context.MODE_PRIVATE);
        boolean hidePastDays = settings.getBoolean("hidePastDays", true);

        int startDay = 0;
        if (hidePastDays){
            if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
                startDay = 0;
            } else {
                startDay = cal3.get(Calendar.DAY_OF_WEEK) - 2;
                calendar = Calendar.getInstance();
                calendar.setFirstDayOfWeek(Calendar.MONDAY);
            }
        }

        boolean dayAdded;

        for (int i = startDay; i < days.length; i++){
            dayAdded = false;
            for (int j = 0; j < reservationOlds.size(); j++) {

                Date date1 = reservationOlds.get(j).getDateStartDate();
                Date date2 = calendar.getTime();

                cal1.setTime(date1);
                cal2.setTime(date2);

                boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);

                if (sameDay) {
                    if (!dayAdded) {
                        allDaysReservations.add(days[i]);
                        dayAdded = true;
                    }
                    allDaysReservations.add(reservationOlds.get(j));
                }

            }
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        if (summaryReservationsAdapter != null){
            summaryReservationsAdapter.notifyDataSetChanged();
        }
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.menu_summary, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_refresh:
                refreshSummary();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    boolean canRefresh = false;

    public void refreshSummary() {
        if (canRefresh){
            canRefresh = false;
            SharedPreferences settings = getActivity().getSharedPreferences("ApplicationPreferences", Context.MODE_PRIVATE);
            if (settings.contains("restaurantCode")){
                if (Integer.valueOf(settings.getString("restaurantCode", "")) == 0){
                    TextView emptyView = getActivity().findViewById(R.id.empty_view_restaurant_no_code);
                    emptyView.setVisibility(View.VISIBLE);
                } else {
                    if (Integer.valueOf(settings.getString("restaurantCode", "")) == 69) {
                        fetchAreenaCourses = new FetchAreenaCourses();
                        fetchAreenaCourses.execute();
                    } else {
                        fetchSodexoCourses = new FetchSodexoCourses();
                        fetchSodexoCourses.execute();
                    }
                }
            }
            if (basketRealizations.size() > 0 || studentGroupBaskets.size() > 0){
                fetchReservations = new FetchReservations();
                fetchReservations.execute();
            }

            Handler h = new Handler();
            h.postDelayed(new Runnable(){
                @Override
                public void run(){
                    canRefresh = true;
                }
            }, 5000);
        }
    }

        private class FetchSodexoCourses extends AsyncTask<Void, Void, Void> {
            boolean errorOccurred = false;

            @Override
            protected Void doInBackground(Void... params) {
                OkHttpClient client = new OkHttpClient();

                DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                Calendar calendar = Calendar.getInstance();

                if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
                    calendar.add(Calendar.DAY_OF_YEAR, 2);
                }
                if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                }

                String day = format.format(calendar.getTime());

                SharedPreferences settings = getActivity().getSharedPreferences("ApplicationPreferences", Context.MODE_PRIVATE);
                String slN = settings.getString("restaurantCode", "");

                Request request = new Request.Builder()
                        .url("http://www.sodexo.fi/ruokalistat/output/daily_json/"+ slN +"/"+ day +"/fi")
                        .build();

                try {
                    Response response = client.newCall(request).execute();

                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONArray jsonArray = jsonObject.getJSONArray("courses");

                    for (int i=0; i<jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Course course = new Course(object.optString("title_fi"),object.optString("title_en"),object.optString("category"),object.optString("price"),object.optString("properties"));
                        courses.add(course);
                    }

                } catch (IOException e){
                    errorOccurred = true;
                    e.printStackTrace();
                } catch (JSONException e){
                    System.out.println("End of content");
                }

                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                courses.clear();
                allDaysCourses.clear();
                TextView emptyView = getActivity().findViewById(R.id.empty_view_restaurant);
                if (emptyView.getVisibility() == View.VISIBLE){
                    emptyView.setVisibility(View.GONE);
                }
                ProgressBar progressBar = getActivity().findViewById(R.id.progressbarrestaurant);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(Void aVoid){
                ProgressBar progressBar = getActivity().findViewById(R.id.progressbarrestaurant);
                if (progressBar != null){
                    progressBar.setVisibility(View.GONE);
                }

                TextView emptyView = getActivity().findViewById(R.id.empty_view_restaurant);
                if (emptyView != null){
                    if (courses.size() > 0){
                        emptyView.setVisibility(View.GONE);
                    }
                    else {
                        emptyView.setVisibility(View.VISIBLE);
                    }
                }

                Calendar calendar = Calendar.getInstance();
                calendar.setFirstDayOfWeek(Calendar.MONDAY);

                DateFormat format = new SimpleDateFormat("dd.MM.");
                if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
                    calendar.add(Calendar.DAY_OF_YEAR, 2);
                }
                if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                }
                String day = daysRestaurant[calendar.get(Calendar.DAY_OF_WEEK) - 2] + " " + format.format(calendar.getTime());

                if (allDaysCourses.size() == 0 && courses.size() > 0){
                    allDaysCourses.add(day);
                    allDaysCourses.addAll(courses);
                }

                newSummaryCoursesAdapter.notifyDataSetChanged();
            }
        }

    private class FetchReservations extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... params) {
                MediaType JSONData = MediaType.parse("application/json; charset=utf-8");
                OkHttpClient client = new OkHttpClient();

                String credential = Credentials.basic(stringToString(getContext()),"");

                Calendar calendar = Calendar.getInstance();
                calendar.setFirstDayOfWeek(Calendar.MONDAY);
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                String startTime = "T00:00:00";
                String endTime = "T23:59:59";

                String url = "https://opendata.seamk.fi:443/r1/reservationOld/search?l=fi";
                SharedPreferences settings = getActivity().getSharedPreferences("ApplicationPreferences", Context.MODE_PRIVATE);

                if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
                    calendar.add(Calendar.WEEK_OF_YEAR, 1);
                } else {
                    calendar.add(Calendar.MONTH, 0);
                }
                String startDate = format.format(calendar.getTime()) + startTime;
                calendar.add(Calendar.MONTH, 4);
                String endDate = format.format(calendar.getTime()) + endTime;

                String size = "1000";

                String studentGroup = "";
                String realization = "";

                for (BasketStudentGroup studentGroupBasket : studentGroupBaskets){
                    if (studentGroupBasket.isShown()){
                        studentGroup = studentGroup + ", " + "'" + studentGroupBasket.getStudentGroupCode() + "'";
                    }
                }
                if (studentGroup.length() != 0){
                    studentGroup = studentGroup.substring(2);
                }

                for (BasketRealization basketRealization : basketsToSearchRealizations){
                    realization = realization + ", " + "'" + basketRealization.getRealizationCode() + "'";
                }
                if (realization.length() != 0){
                    realization = realization.substring(2);
                }

                String JSONPostStudentGroups = "{'startDate':'" + startDate + "'," + "'endDate':'" + endDate + "'," + "'studentGroup':[" + studentGroup + "]," + "'size':" + size + "}";
                RequestBody bodyStudentGroup = RequestBody.create(JSONData, JSONPostStudentGroups);
                Request requestStudentGroup = new Request.Builder().url(url).post(bodyStudentGroup).header("Authorization", credential).build();

                String JSONPostRealizations = "{'startDate':'" + startDate + "'," + "'endDate':'" + endDate + "'," + "'realization':[" + realization + "]," + "'size':" + size + "}";
                RequestBody bodyRealization = RequestBody.create(JSONData, JSONPostRealizations);
                Request requestRealization = new Request.Builder().url(url).post(bodyRealization).header("Authorization", credential).build();

                try {
                    if (studentGroupBaskets.size() > 0 && !studentGroup.equals("")){
                        Response responseStudentGroup = client.newCall(requestStudentGroup).execute();
                        JSONObject jsonObjectStudentGroup = new JSONObject(responseStudentGroup.body().string());
                        reservationOlds.addAll(reservationsFromJson(jsonObjectStudentGroup));
                    }

                    if (basketsToSearchRealizations.size() > 0 && !realization.equals("")){
                        Response responseRealization = client.newCall(requestRealization).execute();
                        JSONObject jsonObjectRealization = new JSONObject(responseRealization.body().string());
                        reservationOlds.addAll(reservationsFromJson(jsonObjectRealization));
                    }
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
                allDaysReservations.clear();
                reservationOlds.clear();
                TextView emptyView = getActivity().findViewById(R.id.empty_view_reservations);
                if (emptyView.getVisibility() == View.VISIBLE){
                    emptyView.setVisibility(View.GONE);
                }
                ProgressBar progressBar = getActivity().findViewById(R.id.progressbarreservations);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(Void aVoid){

                if (reservationOlds.size() > 0 && allDaysReservations.size() == 0) {

                    Map<Integer, ReservationOld> map = new LinkedHashMap<>();

                    for (ReservationOld reservationOld : reservationOlds) {
                        map.put(Integer.valueOf(reservationOld.getReservationId()), reservationOld);
                    }

                    reservationOlds.clear();
                    reservationOlds.addAll(map.values());

                    List<ReservationOld> tmpReservationOlds = new ArrayList<>();

                    for (ReservationOld reservationOld : reservationOlds) {
                        boolean savedRealization = false;
                        boolean shownRealization = false;
                        for (BasketRealization basketRealization : basketRealizations) {
                            if (basketRealization.getRealizationCode().equals(reservationOld.getRealizationCode().get(0))) {
                                savedRealization = true;
                                if (basketRealization.isShown()) {
                                    shownRealization = true;
                                }
                            }
                        }
                        if (savedRealization) {
                            if (shownRealization) {
                                tmpReservationOlds.add(reservationOld);
                            }
                        } else {
                            tmpReservationOlds.add(reservationOld);
                        }
                    }

                    reservationOlds.clear();
                    reservationOlds.addAll(tmpReservationOlds);

                    if (reservationOlds.size() != 0) {
                        Collections.sort(reservationOlds, new Comparator<ReservationOld>() {
                            public int compare(ReservationOld obj1, ReservationOld obj2) {
                                if (obj1.getLongStartDate() == obj2.getLongStartDate()) {
                                    if (obj1.getLongEndDate() == obj2.getLongEndDate()) {
                                        return Integer.valueOf(obj1.getReservationId()) < Integer.valueOf(obj2.getReservationId()) ? -1 : 1;
                                    } else {
                                        return obj1.getLongEndDate() < obj2.getLongEndDate() ? -1 : 1;
                                    }
                                } else {
                                    return obj1.getLongStartDate() < obj2.getLongStartDate() ? -1 : 1;
                                }
                            }
                        });
                    }

                    if (reservationOlds.size() != 0) {

                        SharedPreferences mSettings = getActivity().getSharedPreferences("ApplicationPreferences", Context.MODE_PRIVATE);
                        SharedPreferences.Editor mEditor = mSettings.edit();

                        GsonBuilder gsonb = new GsonBuilder();
                        Gson mGson = gsonb.create();

                        try {
                            String writeValue = mGson.toJson(reservationOlds);
                            mEditor.putString("summaryReservations", writeValue);
                            mEditor.apply();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        ReservationOld firstReservationOld = reservationOlds.get(0);
                        Date firstDay = firstReservationOld.getDateStartDate();

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(firstDay);
                        calendar.setFirstDayOfWeek(Calendar.MONDAY);
                        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

                        DateFormat format = new SimpleDateFormat("dd.MM.");

                        days[0] = getString(R.string.monday) + " " + format.format(calendar.getTime());
                        calendar.add(Calendar.DAY_OF_MONTH, 1);
                        days[1] = getString(R.string.tuesday) + " " + format.format(calendar.getTime());
                        calendar.add(Calendar.DAY_OF_MONTH, 1);
                        days[2] = getString(R.string.wednesday) + " " + format.format(calendar.getTime());
                        calendar.add(Calendar.DAY_OF_MONTH, 1);
                        days[3] = getString(R.string.thursday) + " " + format.format(calendar.getTime());
                        calendar.add(Calendar.DAY_OF_MONTH, 1);
                        days[4] = getString(R.string.friday) + " " + format.format(calendar.getTime());
                        calendar.add(Calendar.DAY_OF_MONTH, 1);
                        days[5] = getString(R.string.saturday) + " " + format.format(calendar.getTime());
                        calendar.add(Calendar.DAY_OF_MONTH, 1);
                        days[6] = getString(R.string.sunday) + " " + format.format(calendar.getTime());

                        calendar.setTime(firstDay);
                        calendar.setFirstDayOfWeek(Calendar.MONDAY);
                        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                        Calendar cal1 = Calendar.getInstance();
                        Calendar cal2 = Calendar.getInstance();

                        Calendar cal3 = Calendar.getInstance();
                        cal3.setFirstDayOfWeek(Calendar.MONDAY);

                        SharedPreferences settings = getActivity().getSharedPreferences("ApplicationPreferences", Context.MODE_PRIVATE);
                        boolean hidePastDays = settings.getBoolean("hidePastDays", true);

                        int startDay = 0;
                        if (hidePastDays) {
                            if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || firstReservationOld.getDateStartDate().after(Calendar.getInstance().getTime())) {
                                startDay = 0;
                            } else {
                                startDay = cal3.get(Calendar.DAY_OF_WEEK) - 2;
                                calendar = Calendar.getInstance();
                                calendar.setFirstDayOfWeek(Calendar.MONDAY);
                            }
                        }

                        boolean dayAdded;

                        for (int i = startDay; i < days.length; i++) {
                            dayAdded = false;
                            for (int j = 0; j < reservationOlds.size(); j++) {

                                Date date1 = reservationOlds.get(j).getDateStartDate();
                                Date date2 = calendar.getTime();

                                cal1.setTime(date1);
                                cal2.setTime(date2);

                                boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);

                                if (sameDay) {
                                    if (!dayAdded) {
                                        allDaysReservations.add(days[i]);
                                        dayAdded = true;
                                    }
                                    allDaysReservations.add(reservationOlds.get(j));
                                }

                            }
                            calendar.add(Calendar.DAY_OF_MONTH, 1);
                        }
                    }
                }

                for (int i = 0; i < allDaysReservations.size(); i++){
                    if (allDaysReservations.get(i) instanceof ReservationOld){
                        int span = getItemType(i, allDaysReservations);
                        if (span == 1){
                            allDaysReservations.add(i + 1, "");
                        } else {
                            int full = 100;
                            int counter = 0;
                            int value = 0;
                            for (int j = i; j > 0; j--){
                                if (span != getItemType(j, allDaysReservations)){
                                    value = full / counter;
                                    break;
                                } else {
                                    counter++;
                                }
                            }
                            if ((((span * value) + 99) / 100 ) * 100 == full){
                                allDaysReservations.add(i + 1, "");
                            }
                        }
                    }
                }

                summaryReservationsAdapter.notifyDataSetChanged();
                ProgressBar progressBar = getActivity().findViewById(R.id.progressbarreservations);
                if (progressBar != null){
                    progressBar.setVisibility(View.GONE);
                }
                TextView emptyView = getActivity().findViewById(R.id.empty_view_reservations);
                if (emptyView != null){
                    if (allDaysReservations.size() > 0){
                        emptyView.setVisibility(View.GONE);
                    }
                    else {
                        emptyView.setVisibility(View.VISIBLE);
                    }
                }
            }
        }

    private class FetchAreenaCourses extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            String url = "http://www.seinajokiareena.fi/ravintolapalvelut/lounaslista";

            try {
                Document document = Jsoup.connect(url).get();

                String text = document.html();

                DateFormat format = new SimpleDateFormat("d.M.");
                Calendar calendar = Calendar.getInstance();
                calendar.setFirstDayOfWeek(Calendar.MONDAY);
                if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
                    calendar.add(Calendar.DAY_OF_YEAR, 2);
                }
                if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                }
                String today = format.format(calendar.getTime());
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                String tomorrow = format.format(calendar.getTime());

                if (text.contains(today)) {
                    text = text.substring(text.indexOf("<h2>Ravintola Eventti (B-halli)</h2>"));
                    //text = text.substring(0, text.indexOf("<p>&nbsp;</p> <p>&nbsp;</p> <p>&nbsp;</p> <p>&nbsp;</p> <p>&nbsp;</p> <p>&nbsp;</p> <p>&nbsp;</p>"));
                    text = text.substring(0, text.indexOf("</td>"));

                    text = text.substring(text.indexOf(today) + 11);
                    if (text.contains(tomorrow)){
                        text = text.substring(0, text.indexOf(tomorrow) - 7);
                    }

                    text = text.replace("<p>&nbsp;</p>", "");
                    String[] textArray = text.split("> <");
                    String[] tmpArray = new String[textArray.length + 1];

                    for (int k = 0; k < textArray.length; k++){
                        tmpArray[k] = textArray[k];
                    }

                    if (textArray[textArray.length - 1].contains(">  <")){
                        tmpArray[textArray.length - 1] = textArray[textArray.length - 1].split(">  <")[0];
                        tmpArray[textArray.length] = textArray[textArray.length - 1].split(">  <")[1];
                        textArray = tmpArray.clone();
                    }

                    String extra = "";

                    for (int i = 0; i < textArray.length; i++) {
                        if (textArray[i].contains("Lisähinnalla")) {
                            extra = textArray[i];
                            extra = extra.replace("</strong>", "");
                            extra = extra.replace("<strong>", "");
                            extra = extra.replace("</br", "");
                            extra = extra.replace("</br>", "");
                            extra = extra.replace("<br>", "");
                            extra = extra.replace("/br", "");
                            extra = extra.replace("br>", "");
                            extra = extra.replace("<", "");
                            extra = extra.replace("<p>", "");
                            extra = extra.replace("</p", "");
                            extra = extra.replace("/p", "");
                            extra = extra.replace("/", "");
                            extra = extra.replace("p>", "");
                            extra = extra.replace("</", "");
                            extra = extra.replace("</p>", "");
                            extra = extra.replace("> ", "");
                            extra = extra.replace("&nbsp;", "");
                        }
                        textArray[i] = textArray[i].replace("</strong>", "");
                        textArray[i] = textArray[i].replace("<strong>", "");
                        textArray[i] = textArray[i].replace(" <p>", "");
                        textArray[i] = textArray[i].replace("</br", "");
                        textArray[i] = textArray[i].replace("</br>", "");
                        textArray[i] = textArray[i].replace("<br>", "");
                        textArray[i] = textArray[i].replace("/br", "");
                        textArray[i] = textArray[i].replace("br>", "");
                        textArray[i] = textArray[i].replace("<br", "");
                        textArray[i] = textArray[i].replace("<", "");
                        textArray[i] = textArray[i].replace("<p>", "");
                        textArray[i] = textArray[i].replace("</p", "");
                        textArray[i] = textArray[i].replace("/p", "");
                        textArray[i] = textArray[i].replace("/", "");
                        textArray[i] = textArray[i].replace("p>", "");
                        textArray[i] = textArray[i].replace("</", "");
                        textArray[i] = textArray[i].replace("</p>", "");
                        textArray[i] = textArray[i].replace("> ", "");
                        textArray[i] = textArray[i].replace("&nbsp;", "");
                    }

                    int tmpNo = 0;
                    if (!extra.equals("")){
                        tmpNo = 1;
                    }

                    for (int i = 0; i < textArray.length - tmpNo; i++) {
                        if (textArray[i].matches(".*[a-zA-Z]+.*")) {
                            int index = 0;
                            for (int j = 2; j < textArray[i].length(); j++) {
                                int k = 0;
                                if (Character.isUpperCase(textArray[i].charAt(j)) || textArray[i].charAt(j) == '*') {
                                    while (Character.isLetter(textArray[i].charAt(j + k))) {
                                        k++;
                                        if ((j + k) == textArray[i].length()){
                                            break;
                                        }
                                    }
                                    if (k < 4) {
                                        index = j - 1;
                                        break;
                                    }
                                }
                            }
                            String sub = "";
                            String remainder = "";
                            if (index != 0) {
                                sub = textArray[i].substring(0, index);
                                remainder = textArray[i].substring(index);
                            } else {
                                sub = textArray[i].substring(0, textArray[i].length());
                            }
                            Course course = new Course(sub, "", remainder, "2,60", "");
                            courses.add(course);
                        }
                    }

                    if (!extra.equals("")) {
                        extra = extra.replaceAll("Lisähinnalla", "");
                        if (extra.matches(".*[a-z].*")) {
                            int index = 0;
                            for (int j = 4; j < extra.length(); j++) {
                                int k = 0;
                                if (Character.isUpperCase(extra.charAt(j)) || extra.charAt(j) == '*') {
                                    while (Character.isLetter(extra.charAt(j + k))) {
                                        k++;
                                        if ((j + k) == extra.length()){
                                            break;
                                        }
                                    }
                                    if (k < 4) {
                                        index = j - 1;
                                        break;
                                    }
                                }
                            }
                            String sub = "";
                            String remainder = "";
                            if (index != 0) {
                                sub = extra.substring(0, index);
                                remainder = extra.substring(index);
                            }
                            Course course = new Course(sub, "", remainder, "4,95", "");
                            courses.add(course);
                        }
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            courses.clear();
            allDaysCourses.clear();
            TextView emptyView = getActivity().findViewById(R.id.empty_view_restaurant);
            if (emptyView.getVisibility() == View.VISIBLE){
                emptyView.setVisibility(View.GONE);
            }
            ProgressBar progressBar = getActivity().findViewById(R.id.progressbarrestaurant);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid){
            ProgressBar progressBar = getActivity().findViewById(R.id.progressbarrestaurant);
            if (progressBar != null){
                progressBar.setVisibility(View.GONE);
            }

            TextView emptyView = getActivity().findViewById(R.id.empty_view_restaurant_eventti);
            if (emptyView != null){
                if (courses.size() > 0){
                    emptyView.setVisibility(View.GONE);
                }
                else {
                    emptyView.setVisibility(View.VISIBLE);
                }
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setFirstDayOfWeek(Calendar.MONDAY);

            DateFormat format = new SimpleDateFormat("dd.MM.");
            if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
                calendar.add(Calendar.DAY_OF_YEAR, 2);
            }
            if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
                calendar.add(Calendar.DAY_OF_YEAR, 1);
            }
            String day = daysRestaurant[calendar.get(Calendar.DAY_OF_WEEK) - 2] + " " + format.format(calendar.getTime());

            if (allDaysCourses.size() == 0 && courses.size() > 0){
                allDaysCourses.add(day);
                allDaysCourses.addAll(courses);
            }

            newSummaryCoursesAdapter.notifyDataSetChanged();
        }
    }
}