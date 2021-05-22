package com.seamk.mobile.restaurant;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.seamk.mobile.R;
import com.seamk.mobile.adapters.RestaurantWeekAdapter;
import com.seamk.mobile.objects.Course;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FragmentRestaurantWeekTab extends Fragment{

    int dayIndex;
    int placeIndex;

    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private RestaurantWeekAdapter adapter;
    private TextView emptyView;
    private ProgressBar progressBar;

    public FragmentRestaurantWeekTab() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_restaurant_week, container, false);
        recyclerView = v.findViewById(R.id.week_fragment_recycler);
        emptyView = v.findViewById(R.id.empty_view);
        progressBar = v.findViewById(R.id.progressbar);

        gridLayoutManager = new GridLayoutManager(getContext(),1);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new RestaurantWeekAdapter(allDaysCourses);
        recyclerView.setAdapter(adapter);

        recyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        return v;
    }


    List<Object> coursesMA;
    List<Object> coursesTI;
    List<Object> coursesKE;
    List<Object> coursesTO;
    List<Object> coursesPE;
    List<Object> allDaysCourses;
    String[] days = new String[5];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dayIndex = getArguments().getInt("dayIndex");
        placeIndex = getArguments().getInt("placeIndex");

        allDaysCourses = new ArrayList<>();
        coursesMA = new ArrayList<>();
        coursesTI = new ArrayList<>();
        coursesKE = new ArrayList<>();
        coursesTO = new ArrayList<>();
        coursesPE = new ArrayList<>();

        DateFormat format = new SimpleDateFormat("dd.MM.");
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.add(Calendar.WEEK_OF_YEAR, dayIndex);

        days[0] = getString(R.string.monday) + " " + format.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        days[1] = getString(R.string.tuesday) + " " + format.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        days[2] = getString(R.string.wednesday) + " " + format.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        days[3] = getString(R.string.thursday) + " " + format.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        days[4] = getString(R.string.friday) + " " + format.format(calendar.getTime());

        if (placeIndex == 69){
            fetchAreenaCourses = new FetchAreenaCourses();
            fetchAreenaCourses.execute();
        } else {
            fetchSodexoCourses = new FetchSodexoCourses();
            fetchSodexoCourses.execute();
        }
    }

    @Override
    public void onDestroy() {
        if (fetchSodexoCourses != null){
            fetchSodexoCourses.cancel(true);
        }
        if (fetchAreenaCourses != null){
            fetchAreenaCourses.cancel(true);
        }
        super.onDestroy();
    }

    private FetchSodexoCourses fetchSodexoCourses;

    private class FetchSodexoCourses extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();
            String slN = Integer.toString(placeIndex);
            DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            Calendar calendar = Calendar.getInstance();
            calendar.setFirstDayOfWeek(Calendar.MONDAY);
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            calendar.add(Calendar.WEEK_OF_YEAR, dayIndex);
            String day;

            calendar.add(Calendar.DAY_OF_MONTH, 0);
            day = format.format(calendar.getTime());
            Request requestMA = new Request.Builder().url("http://www.sodexo.fi/ruokalistat/output/daily_json/"+ slN +"/"+ day +"/fi").build();

            calendar.add(Calendar.DAY_OF_MONTH, 1);
            day = format.format(calendar.getTime());
            Request requestTI = new Request.Builder().url("http://www.sodexo.fi/ruokalistat/output/daily_json/"+ slN +"/"+ day +"/fi").build();

            calendar.add(Calendar.DAY_OF_MONTH, 1);
            day = format.format(calendar.getTime());
            Request requestKE = new Request.Builder().url("http://www.sodexo.fi/ruokalistat/output/daily_json/"+ slN +"/"+ day +"/fi").build();

            calendar.add(Calendar.DAY_OF_MONTH, 1);
            day = format.format(calendar.getTime());
            Request requestTO = new Request.Builder().url("http://www.sodexo.fi/ruokalistat/output/daily_json/"+ slN +"/"+ day +"/fi").build();

            calendar.add(Calendar.DAY_OF_MONTH, 1);
            day = format.format(calendar.getTime());
            Request requestPE = new Request.Builder().url("http://www.sodexo.fi/ruokalistat/output/daily_json/"+ slN +"/"+ day +"/fi").build();

            try {

                Response responseMA = client.newCall(requestMA).execute();
                Response responseTI = client.newCall(requestTI).execute();
                Response responseKE = client.newCall(requestKE).execute();
                Response responseTO = client.newCall(requestTO).execute();
                Response responsePE = client.newCall(requestPE).execute();


                JSONObject jsonObjectMA = new JSONObject(responseMA.body().string());
                JSONObject jsonObjectTI = new JSONObject(responseTI.body().string());
                JSONObject jsonObjectKE = new JSONObject(responseKE.body().string());
                JSONObject jsonObjectTO = new JSONObject(responseTO.body().string());
                JSONObject jsonObjectPE = new JSONObject(responsePE.body().string());


                JSONArray jsonArrayMA = jsonObjectMA.getJSONArray("courses");
                JSONArray jsonArrayTI = jsonObjectTI.getJSONArray("courses");
                JSONArray jsonArrayKE = jsonObjectKE.getJSONArray("courses");
                JSONArray jsonArrayTO = jsonObjectTO.getJSONArray("courses");
                JSONArray jsonArrayPE = jsonObjectPE.getJSONArray("courses");

                for (int i=0; i<jsonArrayMA.length(); i++)
                {
                    JSONObject object = jsonArrayMA.getJSONObject(i);
                    Course course = new Course(object.optString("title_fi"),object.optString("title_en"),object.optString("category"),object.optString("price"),object.optString("properties"));
                    coursesMA.add(course);
                }
                for (int i=0; i<jsonArrayTI.length(); i++)
                {
                    JSONObject object = jsonArrayTI.getJSONObject(i);
                    Course course = new Course(object.optString("title_fi"),object.optString("title_en"),object.optString("category"),object.optString("price"),object.optString("properties"));
                    coursesTI.add(course);
                }
                for (int i=0; i<jsonArrayKE.length(); i++)
                {
                    JSONObject object = jsonArrayKE.getJSONObject(i);
                    Course course = new Course(object.optString("title_fi"),object.optString("title_en"),object.optString("category"),object.optString("price"),object.optString("properties"));
                    coursesKE.add(course);
                }
                for (int i=0; i<jsonArrayTO.length(); i++)
                {
                    JSONObject object = jsonArrayTO.getJSONObject(i);
                    Course course = new Course(object.optString("title_fi"),object.optString("title_en"),object.optString("category"),object.optString("price"),object.optString("properties"));
                    coursesTO.add(course);
                }
                for (int i=0; i<jsonArrayPE.length(); i++)
                {
                    JSONObject object = jsonArrayPE.getJSONObject(i);
                    Course course = new Course(object.optString("title_fi"),object.optString("title_en"),object.optString("category"),object.optString("price"),object.optString("properties"));
                    coursesPE.add(course);
                }

            } catch (IOException e){
                e.printStackTrace();
            } catch (JSONException e){
                System.out.println("End of content");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid){
            if (coursesMA.size() > 0) {
                allDaysCourses.add(days[0]);
                allDaysCourses.addAll(coursesMA);
            }

            if (coursesTI.size() > 0) {
                allDaysCourses.add(days[1]);
                allDaysCourses.addAll(coursesTI);
            }

            if (coursesKE.size() > 0) {
                allDaysCourses.add(days[2]);
                allDaysCourses.addAll(coursesKE);
            }

            if (coursesTO.size() > 0) {
                allDaysCourses.add(days[3]);
                allDaysCourses.addAll(coursesTO);
            }

            if (coursesPE.size() > 0) {
                allDaysCourses.add(days[4]);
                allDaysCourses.addAll(coursesPE);
            }

            if (allDaysCourses.size() > 0){
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            }
            else {
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }

            adapter.notifyDataSetChanged();
        }
    }
    private FetchAreenaCourses fetchAreenaCourses;
    private class FetchAreenaCourses extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            String url = "http://www.seinajokiareena.fi/ravintolapalvelut/lounaslista";

            try {
                Document document = Jsoup.connect(url).get();

                String text = document.html();
/*
                text = text.substring(text.indexOf("Lounas vko") + "Lounas vko".length() + 1);
                text = text.substring(0, 2);
                int weekNo = Integer.valueOf(text);
*/
                Calendar c = Calendar.getInstance();
                c.setFirstDayOfWeek(Calendar.MONDAY);
                c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                //int weekNo2 = c.get(Calendar.WEEK_OF_YEAR);
                //if (weekNo2 == weekNo) {
                    DateFormat format = new SimpleDateFormat("d.M.");
                    Calendar calendar = Calendar.getInstance();
                    calendar.setFirstDayOfWeek(Calendar.MONDAY);
                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                    for (int x = 0; x < 5; x++) {
                        allDaysCourses.add(days[x]);
                        String today = format.format(calendar.getTime());
                        calendar.add(Calendar.DAY_OF_MONTH, 1);
                        String tomorrow = format.format(calendar.getTime());
                        text = document.html();
                        text = text.substring(text.indexOf("<h2>Ravintola Eventti (B-halli)</h2>"));
                        //text = text.substring(0, text.indexOf("<p>&nbsp;</p> <p>&nbsp;</p> <p>&nbsp;</p> <p>&nbsp;</p> <p>&nbsp;</p> <p>&nbsp;</p> <p>&nbsp;</p>"));
                        text = text.substring(0, text.indexOf("</td>"));
                        text = text.substring(text.indexOf(today) + 11);
                        if (text.contains(tomorrow)) {
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
                        if (!extra.equals("")) {
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
                                            if ((j + k) == textArray[i].length()) {
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
                                allDaysCourses.add(course);
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
                                            if ((j + k) == extra.length()) {
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
                                allDaysCourses.add(course);
                            }
                        }
                    }
            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid){
            if (allDaysCourses.size() > 0){
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            }
            else {
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }

            adapter.notifyDataSetChanged();

        }
    }
}