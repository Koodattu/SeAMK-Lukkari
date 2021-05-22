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
import com.seamk.mobile.adapters.RestaurantDayAdapter;
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
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FragmentRestaurantDayTab extends Fragment{

    @BindView(R.id.day_fragment_recycler) RecyclerView recyclerView;
    @BindView(R.id.empty_view) TextView emptyView;
    @BindView(R.id.progressbar) ProgressBar progressBar;

    int dayIndex;
    int placeIndex;

    GridLayoutManager gridLayoutManager;
    private RestaurantDayAdapter adapter;
    private List<Course> courses;

    public FragmentRestaurantDayTab() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_restaurant_day, container, false);

        ButterKnife.bind(this, v);

        gridLayoutManager = new GridLayoutManager(getContext(),1);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new RestaurantDayAdapter(getContext(), courses);
        recyclerView.setAdapter(adapter);

        recyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dayIndex = getArguments().getInt("dayIndex");
        placeIndex = getArguments().getInt("placeIndex");
        courses = new ArrayList<>();
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

                DateFormat format = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
                Calendar calendar = Calendar.getInstance();
                calendar.setFirstDayOfWeek(Calendar.MONDAY);
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                calendar.add(Calendar.DAY_OF_MONTH, dayIndex);
                String day = format.format(calendar.getTime());

                //int locationNumber = 873 + placeIndex;
                String slN = Integer.toString(placeIndex);

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
                    e.printStackTrace();
                } catch (JSONException e){
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid){
                if (courses.size() > 0){
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

                DateFormat format = new SimpleDateFormat("d.M.", Locale.ENGLISH);
                Calendar calendar = Calendar.getInstance();
                calendar.setFirstDayOfWeek(Calendar.MONDAY);
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                calendar.add(Calendar.DAY_OF_MONTH, dayIndex);
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
                    String[] tmpArray;

                    if (textArray[textArray.length - 1].contains("Lisähinnalla")){ //lisähinta on viimeisessä
                        tmpArray = new String[textArray.length + 1];

                        for (int k = 0; k < textArray.length; k++){
                            tmpArray[k] = textArray[k];
                        }

                        if (textArray[textArray.length - 1].contains("Lisähinnalla")){
                            tmpArray[textArray.length - 1] = textArray[textArray.length - 1].split("Lisähinnalla")[0];
                            tmpArray[textArray.length] = textArray[textArray.length - 1].split("Lisähinnalla")[1];
                            textArray = tmpArray.clone();
                        }
                    } else if (textArray[textArray.length - 2].contains("Lisähinnalla")){ //lisähinta on toiseksi viimeisessä
                        tmpArray = new String[textArray.length];

                        for (int k = 0; k < textArray.length; k++){
                            tmpArray[k] = textArray[k];
                        }

                        if (textArray[textArray.length - 2].contains("Lisähinnalla")){
                            tmpArray[textArray.length - 2] = textArray[textArray.length - 2].split("Lisähinnalla")[0];
                            tmpArray[textArray.length - 1] = textArray[textArray.length - 2].split("Lisähinnalla")[1];
                            textArray = tmpArray.clone();
                        }
                    } else { //lisähintaa ei ole

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
        protected void onPostExecute(Void aVoid){
            if (courses.size() > 0){
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