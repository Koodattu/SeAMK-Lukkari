package com.seamk.mobile.retrofit;

import android.content.Context;
import androidx.annotation.NonNull;

import com.seamk.mobile.R;
import com.seamk.mobile.elasticsearch.ElasticBuildings;
import com.seamk.mobile.elasticsearch.ElasticRealization;
import com.seamk.mobile.elasticsearch.ElasticReservation;
import com.seamk.mobile.elasticsearch.ElasticRooms;
import com.seamk.mobile.elasticsearch.ElasticStudentGroups;
import com.seamk.mobile.elasticsearch.ElasticTeachers;
import com.seamk.mobile.interfaces.callback.ElasticBuildingsCallback;
import com.seamk.mobile.interfaces.callback.ElasticRealizationCallback;
import com.seamk.mobile.interfaces.callback.ElasticReservationCallback;
import com.seamk.mobile.interfaces.callback.ElasticRoomsCallback;
import com.seamk.mobile.interfaces.callback.ElasticStudentGroupsCallback;
import com.seamk.mobile.interfaces.callback.ElasticTeachersCallback;
import com.seamk.mobile.interfaces.retrofit.PeppiElasticBuildings;
import com.seamk.mobile.interfaces.retrofit.PeppiElasticRealization;
import com.seamk.mobile.interfaces.retrofit.PeppiElasticReservation;
import com.seamk.mobile.interfaces.retrofit.PeppiElasticRooms;
import com.seamk.mobile.interfaces.retrofit.PeppiElasticStudentGroups;
import com.seamk.mobile.interfaces.retrofit.PeppiElasticTeachers;
import com.seamk.mobile.objects.BasketRealization;
import com.seamk.mobile.objects.BasketSavedItems;
import com.seamk.mobile.objects.BasketStudentGroup;
import com.seamk.mobile.objects.BasketTeacher;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.seamk.mobile.util.Common.stringToString;

/**
 * Created by Juha Ala-Rantala on 16.3.2018.
 */

public class RetroFitters {

    public static void fetchElasticStudentGroups(Context context, int days, final ElasticStudentGroupsCallback elasticStudentGroupsCallback) throws IOException, JSONException {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        calendar.add(Calendar.DAY_OF_YEAR, -days);
        String startDate = format.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_YEAR, 2 * days);
        String endDate = format.format(calendar.getTime());

        InputStream inputStream = context.getResources().openRawResource(R.raw.query_student_groups);

        String ESString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

        JSONObject jsonObjectES = new JSONObject(ESString);
        JSONObject jsonText;

        jsonText = jsonObjectES.getJSONObject("query").getJSONObject("filtered").getJSONObject("filter").getJSONArray("and").getJSONObject(0).getJSONObject("range").getJSONObject("startDate");
        jsonText.put("gte", startDate);

        jsonText = jsonObjectES.getJSONObject("query").getJSONObject("filtered").getJSONObject("filter").getJSONArray("and").getJSONObject(0).getJSONObject("range").getJSONObject("startDate");
        jsonText.put("lte", endDate);

        ESString = jsonObjectES.toString();

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), ESString);

        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(new BasicAuthInterceptor(stringToString(context), "")).build();

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl("https://opendata.seamk.fi:443").addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.client(httpClient).build();

        PeppiElasticStudentGroups peppiElasticStudentGroups =  retrofit.create(PeppiElasticStudentGroups.class);
        Call<ElasticStudentGroups> elasticStudentGroupsCall = peppiElasticStudentGroups.getStudentGroups(requestBody);
        elasticStudentGroupsCall.enqueue(new Callback<ElasticStudentGroups>() {
            @Override
            public void onResponse(@NonNull Call<ElasticStudentGroups> call, @NonNull Response<ElasticStudentGroups> response) {
                elasticStudentGroupsCallback.onSuccess(response.body());
            }
            @Override
            public void onFailure(@NonNull Call<ElasticStudentGroups> call, @NonNull Throwable throwable) {
                elasticStudentGroupsCallback.onFailure(throwable);
            }
        });
    }

    public static void fetchElasticRooms(Context context, int days, final ElasticRoomsCallback elasticRoomsCallback) throws IOException, JSONException {

        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        calendar.add(Calendar.DAY_OF_YEAR, -days);
        String startDate = format.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_YEAR, 2 * days);
        String endDate = format.format(calendar.getTime());

        InputStream inputStream = context.getResources().openRawResource(R.raw.query_rooms);

        String ESString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

        JSONObject jsonObjectES = new JSONObject(ESString);
        JSONObject jsonText;

        jsonText = jsonObjectES.getJSONObject("query").getJSONObject("filtered").getJSONObject("filter").getJSONArray("and").getJSONObject(0).getJSONObject("range").getJSONObject("startDate");
        jsonText.put("gte", startDate);

        jsonText = jsonObjectES.getJSONObject("query").getJSONObject("filtered").getJSONObject("filter").getJSONArray("and").getJSONObject(0).getJSONObject("range").getJSONObject("startDate");
        jsonText.put("lte", endDate);

        ESString = jsonObjectES.toString();

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), ESString);

        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(new BasicAuthInterceptor(stringToString(context), "")).build();

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl("https://opendata.seamk.fi:443").addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.client(httpClient).build();

        PeppiElasticRooms peppiElasticRooms =  retrofit.create(PeppiElasticRooms.class);
        Call<ElasticRooms> elasticRoomsCall = peppiElasticRooms.getRooms(requestBody);
        elasticRoomsCall.enqueue(new Callback<ElasticRooms>() {
            @Override
            public void onResponse(@NonNull Call<ElasticRooms> call, @NonNull Response<ElasticRooms> response) {
                elasticRoomsCallback.onSuccess(response.body());
            }
            @Override
            public void onFailure(@NonNull Call<ElasticRooms> call, @NonNull Throwable throwable) {
                elasticRoomsCallback.onFailure(throwable);
            }
        });
    }

    public static void fetchElasticTeachers(Context context, int days, final ElasticTeachersCallback elasticTeachersCallback) throws IOException, JSONException {

        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        calendar.add(Calendar.DAY_OF_YEAR, -days);
        String startDate = format.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_YEAR, 2 * days);
        String endDate = format.format(calendar.getTime());

        InputStream inputStream = context.getResources().openRawResource(R.raw.query_teachers);

        String ESString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

        JSONObject jsonObjectES = new JSONObject(ESString);
        JSONObject jsonText;

        jsonText = jsonObjectES.getJSONObject("query").getJSONObject("filtered").getJSONObject("filter").getJSONArray("and").getJSONObject(0).getJSONObject("range").getJSONObject("startDate");
        jsonText.put("gte", startDate);

        jsonText = jsonObjectES.getJSONObject("query").getJSONObject("filtered").getJSONObject("filter").getJSONArray("and").getJSONObject(0).getJSONObject("range").getJSONObject("startDate");
        jsonText.put("lte", endDate);

        ESString = jsonObjectES.toString();

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), ESString);

        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(new BasicAuthInterceptor(stringToString(context), "")).build();

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl("https://opendata.seamk.fi:443").addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.client(httpClient).build();

        PeppiElasticTeachers peppiElasticTeachers =  retrofit.create(PeppiElasticTeachers.class);
        Call<ElasticTeachers> elasticTeachersCall = peppiElasticTeachers.getTeachers(requestBody);
        elasticTeachersCall.enqueue(new Callback<ElasticTeachers>() {
            @Override
            public void onResponse(@NonNull Call<ElasticTeachers> call, @NonNull Response<ElasticTeachers> response) {
                elasticTeachersCallback.onSuccess(response.body());
            }
            @Override
            public void onFailure(@NonNull Call<ElasticTeachers> call, @NonNull Throwable throwable) {
                elasticTeachersCallback.onFailure(throwable);
            }
        });
    }

    public static void fetchElasticReservations(Context context, BasketSavedItems basketSavedItems, int days, final ElasticReservationCallback elasticReservationCallback) throws IOException, JSONException {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        calendar.add(Calendar.DAY_OF_YEAR, -days);
        String startDate = format.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_YEAR, 2 * days);
        String endDate = format.format(calendar.getTime());

        InputStream inputStream = context.getResources().openRawResource(R.raw.query_study_basket_bool);

        String ESString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

        JSONObject jsonObjectES = new JSONObject(ESString);
        JSONObject jsonObject;
        JSONArray jsonArray;

        //setting the start time in the search string
        jsonObject = jsonObjectES.getJSONObject("query").getJSONObject("constant_score").getJSONObject("filter").getJSONObject("bool").getJSONArray("must").getJSONObject(0).getJSONObject("range").getJSONObject("startDate");
        jsonObject.put("gte", startDate);

        //setting the end time in the search string
        jsonObject = jsonObjectES.getJSONObject("query").getJSONObject("constant_score").getJSONObject("filter").getJSONObject("bool").getJSONArray("must").getJSONObject(0).getJSONObject("range").getJSONObject("startDate");
        jsonObject.put("lte", endDate);

        //setting the teachers
        jsonArray = jsonObjectES.getJSONObject("query").getJSONObject("constant_score").getJSONObject("filter").getJSONObject("bool").getJSONArray("should").getJSONObject(0).getJSONObject("terms").getJSONArray("reservedFor.name.raw");
        for (BasketTeacher basketTeacher : basketSavedItems.getBasketTeachers()){
            if (basketTeacher.isShown()){
                jsonArray.put(basketTeacher.getTeacherName());
            }
        }

        //setting the student groups
        jsonArray = jsonObjectES.getJSONObject("query").getJSONObject("constant_score").getJSONObject("filter").getJSONObject("bool").getJSONArray("should").getJSONObject(1).getJSONObject("terms").getJSONArray("studentGroup.code.raw");
        for (BasketStudentGroup studentGroupBasket : basketSavedItems.getBasketStudentGroups()){
            if (studentGroupBasket.isShown()){
                jsonArray.put(studentGroupBasket.getStudentGroupCode());
            }
        }

        //setting the realizations
        jsonArray = jsonObjectES.getJSONObject("query").getJSONObject("constant_score").getJSONObject("filter").getJSONObject("bool").getJSONArray("should").getJSONObject(2).getJSONObject("terms").getJSONArray("realization.code.raw");
        for (BasketRealization basketRealization : basketSavedItems.getBasketRealizations()){
            if (basketRealization.isShown()){
                jsonArray.put(basketRealization.getRealizationCode());
            }
        }

        ESString = jsonObjectES.toString();

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), ESString);

        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(new BasicAuthInterceptor(stringToString(context), "")).build();

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl("https://opendata.seamk.fi:443").addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.client(httpClient).build();

        PeppiElasticReservation peppiElasticReservation =  retrofit.create(PeppiElasticReservation.class);
        Call<ElasticReservation> elasticReservationCall = peppiElasticReservation.getReservations(requestBody);
        elasticReservationCall.enqueue(new Callback<ElasticReservation>() {
            @Override
            public void onResponse(@NonNull Call<ElasticReservation> call, @NonNull Response<ElasticReservation> response) {
                elasticReservationCallback.onSuccess(response.body());
            }
            @Override
            public void onFailure(@NonNull Call<ElasticReservation> call, @NonNull Throwable throwable) {
                elasticReservationCallback.onFailure(throwable);
            }
        });
    }

    public static void fetchElasticReservations(Context context, String searchWord, int days, final ElasticReservationCallback elasticReservationCallback) throws IOException, JSONException {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        calendar.add(Calendar.DAY_OF_YEAR, -days);
        String startDate = format.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_YEAR, 2 * days);
        String endDate = format.format(calendar.getTime());

        InputStream inputStream = context.getResources().openRawResource(R.raw.query_study_basket_bool);

        String ESString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

        JSONObject jsonObjectES = new JSONObject(ESString);
        JSONObject jsonObject;
        JSONArray jsonArray;

        //setting the start time in the search string
        jsonObject = jsonObjectES.getJSONObject("query").getJSONObject("constant_score").getJSONObject("filter").getJSONObject("bool").getJSONArray("must").getJSONObject(0).getJSONObject("range").getJSONObject("startDate");
        jsonObject.put("gte", startDate);

        //setting the end time in the search string
        jsonObject = jsonObjectES.getJSONObject("query").getJSONObject("constant_score").getJSONObject("filter").getJSONObject("bool").getJSONArray("must").getJSONObject(0).getJSONObject("range").getJSONObject("startDate");
        jsonObject.put("lte", endDate);

        //setting the teachers
        jsonArray = jsonObjectES.getJSONObject("query").getJSONObject("constant_score").getJSONObject("filter").getJSONObject("bool").getJSONArray("should").getJSONObject(0).getJSONObject("terms").getJSONArray("reservedFor.name.raw");
        jsonArray.put(searchWord);

        //setting the student groups
        jsonArray = jsonObjectES.getJSONObject("query").getJSONObject("constant_score").getJSONObject("filter").getJSONObject("bool").getJSONArray("should").getJSONObject(1).getJSONObject("terms").getJSONArray("studentGroup.code.raw");
        jsonArray.put(searchWord);

        //setting the realizations
        jsonArray = jsonObjectES.getJSONObject("query").getJSONObject("constant_score").getJSONObject("filter").getJSONObject("bool").getJSONArray("should").getJSONObject(2).getJSONObject("terms").getJSONArray("location.code.raw");
        jsonArray.put(searchWord);

        ESString = jsonObjectES.toString();

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), ESString);

        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(new BasicAuthInterceptor(stringToString(context), "")).build();

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl("https://opendata.seamk.fi:443").addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.client(httpClient).build();

        PeppiElasticReservation peppiElasticReservation =  retrofit.create(PeppiElasticReservation.class);
        Call<ElasticReservation> elasticReservationCall = peppiElasticReservation.getReservations(requestBody);
        elasticReservationCall.enqueue(new Callback<ElasticReservation>() {
            @Override
            public void onResponse(@NonNull Call<ElasticReservation> call, @NonNull Response<ElasticReservation> response) {
                elasticReservationCallback.onSuccess(response.body());
            }
            @Override
            public void onFailure(@NonNull Call<ElasticReservation> call, @NonNull Throwable throwable) {
                elasticReservationCallback.onFailure(throwable);
            }
        });
    }

    public static void fetchElasticRealization(Context context, String searchWord, final ElasticRealizationCallback elasticRealizationCallback) throws IOException, JSONException {
        searchWord = ".*" + searchWord + ".*";

        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        calendar.add(Calendar.MONTH, -3);
        String startDate = format.format(calendar.getTime());
        calendar.add(Calendar.MONTH, 6);
        String endDate = format.format(calendar.getTime());

        InputStream inputStream = context.getResources().openRawResource(R.raw.query_timetable_bool_realization_basket);

        String ESString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

        JSONObject jsonObjectES = new JSONObject(ESString);
        JSONObject jsonObject;

        //setting the start time in the search string
        jsonObject = jsonObjectES.getJSONObject("query").getJSONObject("constant_score").getJSONObject("filter").getJSONObject("bool").getJSONArray("must").getJSONObject(0).getJSONObject("range").getJSONObject("startDate");
        jsonObject.put("gte", startDate);

        //setting the end time in the search string
        jsonObject = jsonObjectES.getJSONObject("query").getJSONObject("constant_score").getJSONObject("filter").getJSONObject("bool").getJSONArray("must").getJSONObject(0).getJSONObject("range").getJSONObject("startDate");
        jsonObject.put("lte", endDate);

        //setting the search word
        jsonObject = jsonObjectES.getJSONObject("query").getJSONObject("constant_score").getJSONObject("filter").getJSONObject("bool").getJSONArray("should").getJSONObject(0).getJSONObject("regexp");
        jsonObject.put("code", searchWord);

        jsonObject = jsonObjectES.getJSONObject("query").getJSONObject("constant_score").getJSONObject("filter").getJSONObject("bool").getJSONArray("should").getJSONObject(1).getJSONObject("regexp");
        jsonObject.put("code.raw", searchWord);

        jsonObject = jsonObjectES.getJSONObject("query").getJSONObject("constant_score").getJSONObject("filter").getJSONObject("bool").getJSONArray("should").getJSONObject(2).getJSONObject("regexp");
        jsonObject.put("localizedName.valueFi", searchWord);

        jsonObject = jsonObjectES.getJSONObject("query").getJSONObject("constant_score").getJSONObject("filter").getJSONObject("bool").getJSONArray("should").getJSONObject(3).getJSONObject("regexp");
        jsonObject.put("localizedName.valueFi.raw", searchWord);

        jsonObject = jsonObjectES.getJSONObject("query").getJSONObject("constant_score").getJSONObject("filter").getJSONObject("bool").getJSONArray("should").getJSONObject(4).getJSONObject("regexp");
        jsonObject.put("localizedName.valueEn", searchWord);

        jsonObject = jsonObjectES.getJSONObject("query").getJSONObject("constant_score").getJSONObject("filter").getJSONObject("bool").getJSONArray("should").getJSONObject(5).getJSONObject("regexp");
        jsonObject.put("localizedName.valueEn.raw", searchWord);

        ESString = jsonObjectES.toString();

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), ESString);

        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(new BasicAuthInterceptor(stringToString(context), "")).build();

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl("https://opendata.seamk.fi:443").addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.client(httpClient).build();

        PeppiElasticRealization peppiElasticRealization =  retrofit.create(PeppiElasticRealization.class);
        Call<ElasticRealization> elasticRealizationCall = peppiElasticRealization.getRealizations(requestBody);
        elasticRealizationCall.enqueue(new Callback<ElasticRealization>() {
            @Override
            public void onResponse(@NonNull Call<ElasticRealization> call, @NonNull Response<ElasticRealization> response) {
                elasticRealizationCallback.onSuccess(response.body());
            }
            @Override
            public void onFailure(@NonNull Call<ElasticRealization> call, @NonNull Throwable throwable) {
                elasticRealizationCallback.onFailure(throwable);
            }
        });
    }


    public static void fetchElasticRealizationFind(Context context, String searchWord, final ElasticRealizationCallback elasticRealizationCallback) throws IOException, JSONException {
        searchWord = ".*" + searchWord + ".*";

        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        calendar.add(Calendar.MONTH, -3);
        String startDate = format.format(calendar.getTime());
        calendar.add(Calendar.MONTH, 6);
        String endDate = format.format(calendar.getTime());

        InputStream inputStream = context.getResources().openRawResource(R.raw.query_timetable_bool_realization_find);

        String ESString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

        JSONObject jsonObjectES = new JSONObject(ESString);
        JSONObject jsonObject;

        //setting the start time in the search string
        jsonObject = jsonObjectES.getJSONObject("query").getJSONObject("constant_score").getJSONObject("filter").getJSONObject("bool").getJSONArray("must").getJSONObject(0).getJSONObject("range").getJSONObject("startDate");
        jsonObject.put("gte", startDate);

        //setting the end time in the search string
        jsonObject = jsonObjectES.getJSONObject("query").getJSONObject("constant_score").getJSONObject("filter").getJSONObject("bool").getJSONArray("must").getJSONObject(0).getJSONObject("range").getJSONObject("startDate");
        jsonObject.put("lte", endDate);

        //setting the search word
        jsonObject = jsonObjectES.getJSONObject("query").getJSONObject("constant_score").getJSONObject("filter").getJSONObject("bool").getJSONArray("should").getJSONObject(0).getJSONObject("regexp");
        jsonObject.put("code", searchWord);

        jsonObject = jsonObjectES.getJSONObject("query").getJSONObject("constant_score").getJSONObject("filter").getJSONObject("bool").getJSONArray("should").getJSONObject(1).getJSONObject("regexp");
        jsonObject.put("code.raw", searchWord);

        jsonObject = jsonObjectES.getJSONObject("query").getJSONObject("constant_score").getJSONObject("filter").getJSONObject("bool").getJSONArray("should").getJSONObject(2).getJSONObject("regexp");
        jsonObject.put("localizedName.valueFi", searchWord);

        jsonObject = jsonObjectES.getJSONObject("query").getJSONObject("constant_score").getJSONObject("filter").getJSONObject("bool").getJSONArray("should").getJSONObject(3).getJSONObject("regexp");
        jsonObject.put("localizedName.valueFi.raw", searchWord);

        jsonObject = jsonObjectES.getJSONObject("query").getJSONObject("constant_score").getJSONObject("filter").getJSONObject("bool").getJSONArray("should").getJSONObject(4).getJSONObject("regexp");
        jsonObject.put("localizedName.valueEn", searchWord);

        jsonObject = jsonObjectES.getJSONObject("query").getJSONObject("constant_score").getJSONObject("filter").getJSONObject("bool").getJSONArray("should").getJSONObject(5).getJSONObject("regexp");
        jsonObject.put("localizedName.valueEn.raw", searchWord);

        ESString = jsonObjectES.toString();

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), ESString);

        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(new BasicAuthInterceptor(stringToString(context), "")).build();

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl("https://opendata.seamk.fi:443").addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.client(httpClient).build();

        PeppiElasticRealization peppiElasticRealization =  retrofit.create(PeppiElasticRealization.class);
        Call<ElasticRealization> elasticRealizationCall = peppiElasticRealization.getRealizations(requestBody);
        elasticRealizationCall.enqueue(new Callback<ElasticRealization>() {
            @Override
            public void onResponse(@NonNull Call<ElasticRealization> call, @NonNull Response<ElasticRealization> response) {
                elasticRealizationCallback.onSuccess(response.body());
            }
            @Override
            public void onFailure(@NonNull Call<ElasticRealization> call, @NonNull Throwable throwable) {
                elasticRealizationCallback.onFailure(throwable);
            }
        });
    }

    public static void fetchElasticRealizations(Context context, final ElasticRealizationCallback elasticRealizationCallback, BasketSavedItems basketSavedItems, int days) throws IOException, JSONException {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        calendar.add(Calendar.DAY_OF_YEAR, -days);
        String startDate = format.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_YEAR, 2 * days);
        String endDate = format.format(calendar.getTime());

        InputStream inputStream = context.getResources().openRawResource(R.raw.query_bool_realizations);

        String ESString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

        JSONObject jsonObjectES = new JSONObject(ESString);
        JSONObject jsonObject;
        JSONArray jsonArray;

        //setting the start time in the search string
        jsonObject = jsonObjectES.getJSONObject("query").getJSONObject("constant_score").getJSONObject("filter").getJSONObject("bool").getJSONArray("must").getJSONObject(0).getJSONObject("range").getJSONObject("startDate");
        jsonObject.put("gte", startDate);

        //setting the end time in the search string
        jsonObject = jsonObjectES.getJSONObject("query").getJSONObject("constant_score").getJSONObject("filter").getJSONObject("bool").getJSONArray("must").getJSONObject(0).getJSONObject("range").getJSONObject("startDate");
        jsonObject.put("lte", endDate);

        //setting the teachers
        jsonArray = jsonObjectES.getJSONObject("query").getJSONObject("constant_score").getJSONObject("filter").getJSONObject("bool").getJSONArray("should").getJSONObject(0).getJSONObject("terms").getJSONArray("teacher.name.raw");
        for (BasketTeacher basketTeacher : basketSavedItems.getBasketTeachers()){
                jsonArray.put(basketTeacher.getTeacherName());
        }

        //setting the student groups
        jsonArray = jsonObjectES.getJSONObject("query").getJSONObject("constant_score").getJSONObject("filter").getJSONObject("bool").getJSONArray("should").getJSONObject(1).getJSONObject("terms").getJSONArray("studentGroups.code.raw");
        for (BasketStudentGroup studentGroupBasket : basketSavedItems.getBasketStudentGroups()){
                jsonArray.put(studentGroupBasket.getStudentGroupCode());
        }

        //setting the realizations
        jsonArray = jsonObjectES.getJSONObject("query").getJSONObject("constant_score").getJSONObject("filter").getJSONObject("bool").getJSONArray("should").getJSONObject(2).getJSONObject("terms").getJSONArray("code.raw");
        for (BasketRealization basketRealization : basketSavedItems.getBasketRealizations()){
                jsonArray.put(basketRealization.getRealizationCode());
        }

        ESString = jsonObjectES.toString();

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), ESString);

        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(new BasicAuthInterceptor(stringToString(context), "")).build();

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl("https://opendata.seamk.fi:443").addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.client(httpClient).build();

        PeppiElasticRealization peppiElasticRealization =  retrofit.create(PeppiElasticRealization.class);
        Call<ElasticRealization> elasticRealizationCall = peppiElasticRealization.getRealizations(requestBody);
        elasticRealizationCall.enqueue(new Callback<ElasticRealization>() {
            @Override
            public void onResponse(@NonNull Call<ElasticRealization> call, @NonNull Response<ElasticRealization> response) {
                elasticRealizationCallback.onSuccess(response.body());
            }
            @Override
            public void onFailure(@NonNull Call<ElasticRealization> call, @NonNull Throwable throwable) {
                elasticRealizationCallback.onFailure(throwable);
            }
        });
    }

    public static void fetchElasticBuildings(Context context, int days, final ElasticBuildingsCallback elasticBuildingsCallback) throws IOException, JSONException {

        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        calendar.add(Calendar.DAY_OF_YEAR, -days);
        String startDate = format.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_YEAR, 2 * days);
        String endDate = format.format(calendar.getTime());

        InputStream inputStream = context.getResources().openRawResource(R.raw.query_buildings);

        String ESString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

        JSONObject jsonObjectES = new JSONObject(ESString);
        JSONObject jsonText;

        jsonText = jsonObjectES.getJSONObject("query").getJSONObject("filtered").getJSONObject("filter").getJSONArray("and").getJSONObject(0).getJSONObject("range").getJSONObject("startDate");
        jsonText.put("gte", startDate);

        jsonText = jsonObjectES.getJSONObject("query").getJSONObject("filtered").getJSONObject("filter").getJSONArray("and").getJSONObject(0).getJSONObject("range").getJSONObject("startDate");
        jsonText.put("lte", endDate);

        ESString = jsonObjectES.toString();

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), ESString);

        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(new BasicAuthInterceptor(stringToString(context), "")).build();

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl("https://opendata.seamk.fi:443").addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.client(httpClient).build();

        PeppiElasticBuildings peppiElasticBuildings =  retrofit.create(PeppiElasticBuildings.class);
        Call<ElasticBuildings> elasticBuildingsCall = peppiElasticBuildings.getBuildings(requestBody);
        elasticBuildingsCall.enqueue(new Callback<ElasticBuildings>() {
            @Override
            public void onResponse(@NonNull Call<ElasticBuildings> call, @NonNull Response<ElasticBuildings> response) {
                elasticBuildingsCallback.onSuccess(response.body());
            }
            @Override
            public void onFailure(@NonNull Call<ElasticBuildings> call, @NonNull Throwable throwable) {
                elasticBuildingsCallback.onFailure(throwable);
            }
        });
    }
}
