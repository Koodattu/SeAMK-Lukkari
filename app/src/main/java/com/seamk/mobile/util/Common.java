package com.seamk.mobile.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.seamk.mobile.MainController;
import com.seamk.mobile.objects.BasketSavedItems;
import com.seamk.mobile.objects.ReservationOld;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import se.simbio.encryption.Encryption;

/**
 * Created by Juha Ala-Rantala on 8.11.2017.
 */

public final class Common {

    private static MainController instance;

    public static boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String intToARGB(String text){
        text = text.replaceAll(" ", "");
        text = text.replaceAll("[^a-zA-Z]", "");
        int i = text.hashCode();
        String s = Integer.toHexString(((i>>24)&0xFF))+Integer.toHexString(((i>>16)&0xFF))+Integer.toHexString(((i>>8)&0xFF))+Integer.toHexString((i&0xFF));

        if (s.length() == 7 || s.length() == 5){
            s = "0" + s;
        }

        if (s.length() == 8){
            s = s.substring(2);
        }

        return "#" + s;
    }

    public static BasketSavedItems getStudyBasket(Context context){
        SharedPreferences settings = context.getSharedPreferences("ApplicationPreferences", Context.MODE_PRIVATE);

        String loadValue = settings.getString("basketSavedItems", "");
        Type listType = new TypeToken<BasketSavedItems>(){}.getType();

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        return gson.fromJson(loadValue, listType);
    }

    public static void saveStudyBasket(BasketSavedItems basketSavedItems, Context context) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        String writeValue = gson.toJson(basketSavedItems);
        saveSettingStringValue("basketSavedItems", writeValue, context);
    }

    public static void saveSettingStringValue(String settingName, String writeValue, Context context){
        SharedPreferences settings = context.getSharedPreferences("ApplicationPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = settings.edit();
        mEditor.putString(settingName, writeValue);
        mEditor.apply();
    }

    public static String getSettingStringValue(String settingName, String defaultValue, Context context){
        SharedPreferences settings = context.getSharedPreferences("ApplicationPreferences", Context.MODE_PRIVATE);
        return settings.getString(settingName, defaultValue);
    }

    public static void saveSettingBoolValue(String settingName, boolean writeValue, Context context){
        SharedPreferences settings = context.getSharedPreferences("ApplicationPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = settings.edit();
        mEditor.putBoolean(settingName, writeValue);
        mEditor.apply();
    }

    public boolean getSettingBoolValue(String settingName, boolean defaultValue, Context context){
        SharedPreferences settings = context.getSharedPreferences("ApplicationPreferences", Context.MODE_PRIVATE);
        return settings.getBoolean(settingName, defaultValue);
    }

    public static String stringToHexColor(String text){
        text = text.replaceAll(" ", "");
        text = text.replaceAll("[^a-zA-Z]", "");

        text = Integer.toString(Math.abs(text.hashCode()));

        char[] chars = text.toCharArray();
        List<String> stringList = new ArrayList<>();
        for (char c : chars) {
            stringList.add(Character.toString(c));
        }

        Collections.shuffle(stringList, new Random(text.hashCode()));

        String listString = "";

        for (String s : stringList)
        {
            listString += s;
        }

        text = listString;

        // create random object - reuse this as often as possible
        //Random random = new Random(Long.parseLong(text));
        Random random = new Random(text.hashCode());

        // create a big random number - maximum is ffffff (hex) = 16777215 (dez)
        int nextInt = random.nextInt(256*256*256);

        // format it as hexadecimal string (with hashtag and leading zeros)
        String colorCode = String.format("#%06x", nextInt);

        // print it
        return colorCode;
    }

    public static int intToRGBImproved(String text){
        text = text.replaceAll(" ", "");
        text = text.replaceAll("[^a-zA-Z]", "");
        char[] chars = text.toCharArray();
        List<String> stringList = new ArrayList<>();
        for (char c : chars) {
            stringList.add(Character.toString(c));
        }

        Collections.shuffle(stringList, new Random(text.hashCode()));

        String listString = "";

        for (String s : stringList)
        {
            listString += s;
        }

        int hash = text.hashCode();
        int r = (hash & 0xFF0000) >> 16;
        int g = (hash & 0x00FF00) >> 8;
        int b = hash & 0x0000FF;

        int Red = (r << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
        int Green = (g << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
        int Blue = b & 0x000000FF; //Mask out anything not blue.

        return 0xFF000000 | Red | Green | Blue; //0xFF000000 for 100% Alpha. Bitwise OR everything together.
    }

    public static int randomNextInt(int seed){
        Random firstRandom = new Random(seed);
        Random secondRandom = new Random(seed * firstRandom.nextInt(256)+768);

        return secondRandom.nextInt(512)+512;
    }

    public static String getFirstLetters(String text)
    {
        String firstLetters = "";
        //text = text.replaceAll("[.,]", " "); // Replace dots, etc (optional)
        text = text.replaceAll("[^a-zA-ZäÄåÅöÖ]", " ");
        text = text.trim().replaceAll(" +", " ");
        for(String s : text.split(" "))
        {
            firstLetters += s.charAt(0);
        }
        firstLetters = firstLetters.toUpperCase();
        return firstLetters;
    }

    public static String returnTrue(MainController controller){
        instance = controller;

        String a = Integer.toString(randomNextInt(Character.getNumericValue(instance.A().charAt(0))));
        String b = Integer.toString(randomNextInt(Character.getNumericValue(instance.B().charAt(0))));
        String c = Integer.toString(randomNextInt(Character.getNumericValue(instance.C().charAt(0))));

        byte[] b1 = ByteBuffer.allocate(Integer.parseInt(instance.SIZE())).putInt(randomNextInt(Integer.parseInt(instance.B21()))).array();
        byte[] b2 = ByteBuffer.allocate(Integer.parseInt(instance.SIZE())).putInt(randomNextInt(Integer.parseInt(instance.B22()))).array();
        byte[] b3 = ByteBuffer.allocate(Integer.parseInt(instance.SIZE())).putInt(randomNextInt(Integer.parseInt(instance.B23()))).array();

        Encryption e1 = Encryption.getDefault(instance.EMAIL(), a, b1);
        Encryption e2 = Encryption.getDefault(instance.EMAIL(), b, b2);
        Encryption e3 = Encryption.getDefault(instance.EMAIL(), c, b3);

        return e1.decryptOrNull(instance.BA()) + e1.decryptOrNull(instance.B8()) + e3.decryptOrNull(instance.BC());
    }

    public static String stringToString(Context context){
        instance = ((MainController)context.getApplicationContext());

        String j = Integer.toString(randomNextInt(Character.getNumericValue(instance.J().charAt(0))));
        String a = Integer.toString(randomNextInt(Character.getNumericValue(instance.A().charAt(0))));
        String r = Integer.toString(randomNextInt(Character.getNumericValue(instance.R().charAt(0))));

        byte[] b1 = ByteBuffer.allocate(Integer.parseInt(instance.SIZE())).putInt(randomNextInt(Integer.parseInt(instance.B11()))).array();
        byte[] b2 = ByteBuffer.allocate(Integer.parseInt(instance.SIZE())).putInt(randomNextInt(Integer.parseInt(instance.B12()))).array();
        byte[] b3 = ByteBuffer.allocate(Integer.parseInt(instance.SIZE())).putInt(randomNextInt(Integer.parseInt(instance.B13()))).array();

        Encryption e1 = Encryption.getDefault(instance.EMAIL(), a, b1);
        Encryption e2 = Encryption.getDefault(instance.EMAIL(), r, b2);
        Encryption e3 = Encryption.getDefault(instance.EMAIL(), j, b3);

        return e1.decryptOrNull(instance.PA()) + e2.decryptOrNull(instance.P8()) + e2.decryptOrNull(instance.PC());
    }

    public static int getItemType(int position, List<Object> items) {
        Date startDate, endDate, refStartDate, refEndDate;

        int span = 1;
        int lastViewType = 1;

        if (position == 0) { // jos on ensimmäinen listassa
                span = 1;
            } else if (position == items.size() - 1) { // jos on viimeinen listassa
                if (items.get(position - 1) instanceof String) {
                    span = 1;
                } else {
                    startDate = ((ReservationOld) items.get(position)).getDateStartDate();
                    endDate = ((ReservationOld) items.get(position)).getDateEndDate();
                    refStartDate = ((ReservationOld) items.get(position - 1)).getDateStartDate();
                    refEndDate = ((ReservationOld) items.get(position - 1)).getDateEndDate();
                    if (startDate.equals(refStartDate) && endDate.equals(refEndDate)) {
                        lastViewType = getItemType(position - 1, items);
                        span = lastViewType;
                    } else {
                        lastViewType = 1;
                        span = 1;
                    }
                }
            } else if (items.get(position) instanceof String) { // jos on päivänmäärä
                span = 1;
            } else if (items.get(position - 1) instanceof String) { // jos edellinen on päivänmäärä
                span = 1;
            } else if (items.get(position + 1) instanceof String) { // jos seuraava on päivänmäärä
                startDate = ((ReservationOld) items.get(position)).getDateStartDate();
                endDate = ((ReservationOld) items.get(position)).getDateEndDate();
                refStartDate = ((ReservationOld) items.get(position - 1)).getDateStartDate();
                refEndDate = ((ReservationOld) items.get(position - 1)).getDateEndDate();
                if (!(startDate.equals(refStartDate) && endDate.equals(refEndDate))) {
                    span = 1;
                } else {
                    span = getItemType(position - 1, items);
                }
            } else if (items.get(position) instanceof ReservationOld) { // jos on varaus
                if (position != items.size() - 1) {
                    startDate = ((ReservationOld) items.get(position)).getDateStartDate();
                    endDate = ((ReservationOld) items.get(position)).getDateEndDate();
                    refStartDate = ((ReservationOld) items.get(position + 1)).getDateStartDate();
                    refEndDate = ((ReservationOld) items.get(position + 1)).getDateEndDate();
                    if (!(startDate.equals(refStartDate) && endDate.equals(refEndDate))) {
                        startDate = ((ReservationOld) items.get(position)).getDateStartDate();
                        endDate = ((ReservationOld) items.get(position)).getDateEndDate();
                        refStartDate = ((ReservationOld) items.get(position - 1)).getDateStartDate();
                        refEndDate = ((ReservationOld) items.get(position - 1)).getDateEndDate();
                        if (startDate.equals(refStartDate) && endDate.equals(refEndDate)) {
                            lastViewType = getItemType(position - 1, items);
                            span = lastViewType;
                        } else {
                            lastViewType = 1;
                            span = 1;
                        }
                    } else {
                        lastViewType = getItemType(position - 1, items);
                        span = lastViewType;
                    }
                } else {
                    startDate = ((ReservationOld) items.get(position)).getDateStartDate();
                    endDate = ((ReservationOld) items.get(position)).getDateEndDate();
                    refStartDate = ((ReservationOld) items.get(position - 1)).getDateStartDate();
                    refEndDate = ((ReservationOld) items.get(position - 1)).getDateEndDate();
                    if (startDate.equals(refStartDate) && endDate.equals(refEndDate)) {
                        lastViewType = getItemType(position - 1, items);
                        span = lastViewType;
                    } else {
                        lastViewType = 1;
                        span = 1;
                    }
                }
            }

            if (lastViewType == 1) { // jos edellinen oli kokonainen

                for (int i = position; i < items.size() - 1; i++) {
                    if (items.get(i) instanceof String || items.get(i + 1) instanceof String) {
                        break;
                    }
                    startDate = ((ReservationOld) items.get(i)).getDateStartDate();
                    endDate = ((ReservationOld) items.get(i)).getDateEndDate();
                    refStartDate = ((ReservationOld) items.get(i + 1)).getDateStartDate();
                    refEndDate = ((ReservationOld) items.get(i + 1)).getDateEndDate();
                    if (startDate.equals(refStartDate) && endDate.equals(refEndDate)) {
                        span++;
                    } else {
                        break;
                    }
                }
            }
        return span;
    }


    public static List<ReservationOld> reservationsFromJson(JSONObject jsonObject) {
        List<ReservationOld> reservations = new ArrayList<>();
        try {
            JSONArray JSONReservations = jsonObject.getJSONArray("reservations");
            String reservationId;
            String description;
            String subject;
            String startDate;
            String endDate;
            String modifiedDate;
            List<String> roomId;
            List<String> roomCode;
            List<String> roomName;
            List<String> buildingId;
            List<String> buildingCode;
            List<String> buildingName;
            List<String> realizationId;
            List<String> realizationCode;
            List<String> realizationName;
            List<String> studentGroupId;
            List<String> studentGroupCodeList;
            List<String> studentGroupName;
            List<String> schedulingGroupId;
            List<String> schedulingGroupCode;
            List<String> schedulingGroupName;
            List<String> movableId;
            List<String> movableCode;
            List<String> movableName;
            List<String> externalId;
            List<String> externalCode;
            List<String> externalName;

            for (int i = 0; i < JSONReservations.length(); i++) {
                JSONObject reservationsObject = JSONReservations.getJSONObject(i);
                startDate = reservationsObject.optString("startDate");
                endDate = reservationsObject.optString("endDate");
                modifiedDate = reservationsObject.optString("modifiedDate");
                description = reservationsObject.optString("description");
                reservationId = reservationsObject.optString("id");
                subject = reservationsObject.optString("subject");
                roomId = new ArrayList<>();
                roomCode = new ArrayList<>();
                roomName = new ArrayList<>();
                buildingId = new ArrayList<>();
                buildingCode = new ArrayList<>();
                buildingName = new ArrayList<>();
                realizationId = new ArrayList<>();
                realizationCode = new ArrayList<>();
                realizationName = new ArrayList<>();
                studentGroupId = new ArrayList<>();
                studentGroupCodeList = new ArrayList<>();
                studentGroupName = new ArrayList<>();
                schedulingGroupId = new ArrayList<>();
                schedulingGroupCode = new ArrayList<>();
                schedulingGroupName = new ArrayList<>();
                movableId = new ArrayList<>();
                movableCode = new ArrayList<>();
                movableName = new ArrayList<>();
                externalId = new ArrayList<>();
                externalCode = new ArrayList<>();
                externalName = new ArrayList<>();

                JSONArray JSONResources = reservationsObject.getJSONArray("resources");

                for (int j = 0; j < JSONResources.length(); j++) {
                    JSONObject resourcesObject = JSONResources.getJSONObject(j);

                    if (resourcesObject.getString("type").equals("room")) {
                        roomId.add(resourcesObject.getString("id"));
                        roomCode.add(resourcesObject.getString("code"));
                        roomName.add(resourcesObject.getString("name"));

                        if (resourcesObject.has("parent")) {
                            JSONObject parentObject = resourcesObject.getJSONObject("parent");

                            if (parentObject.getString("type").equals("building")) {
                                buildingId.add(parentObject.getString("id"));
                                buildingCode.add(parentObject.getString("code"));
                                buildingName.add(parentObject.getString("name"));
                            }
                        }
                    } else if (resourcesObject.getString("type").equals("building")) {
                        buildingId.add(resourcesObject.getString("id"));
                        buildingCode.add(resourcesObject.getString("code"));
                        buildingName.add(resourcesObject.getString("name"));
                    } else if (resourcesObject.getString("type").equals("realization")) {
                        realizationId.add(resourcesObject.getString("id"));
                        realizationCode.add(resourcesObject.getString("code"));
                        realizationName.add(resourcesObject.getString("name"));
                    } else if (resourcesObject.getString("type").equals("student_group")) {
                        studentGroupId.add(resourcesObject.getString("id"));
                        studentGroupCodeList.add(resourcesObject.getString("code"));
                        studentGroupName.add(resourcesObject.getString("name"));
                    } else if (resourcesObject.getString("type").equals("scheduling_group")) {
                        schedulingGroupId.add(resourcesObject.getString("id"));
                        schedulingGroupCode.add(resourcesObject.getString("code"));
                        schedulingGroupName.add(resourcesObject.getString("name"));
                    } else if (resourcesObject.getString("type").equals("movable")) {
                        movableId.add(resourcesObject.getString("id"));
                        movableCode.add(resourcesObject.getString("code"));
                        movableName.add(resourcesObject.getString("name"));
                    } else if (resourcesObject.getString("type").equals("external")) {
                        externalId.add(resourcesObject.getString("id"));
                        externalCode.add(resourcesObject.getString("code"));
                        externalName.add(resourcesObject.getString("name"));
                    }
                }
                ReservationOld reservationOld = new ReservationOld(reservationId, description, subject, startDate, endDate,
                        modifiedDate, roomId, roomCode, roomName, buildingId,
                        buildingCode, buildingName, realizationId, realizationCode,
                        realizationName, studentGroupId, studentGroupCodeList, studentGroupName,
                        new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>(),
                        schedulingGroupId, schedulingGroupCode, schedulingGroupName, movableId,
                        movableCode, movableName, externalId, externalCode, externalName);
                reservations.add(reservationOld);
            }
        } catch(JSONException e){
            e.printStackTrace();
        }
        return reservations;
    }
}
