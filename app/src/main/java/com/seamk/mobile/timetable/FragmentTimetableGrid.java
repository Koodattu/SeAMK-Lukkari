package com.seamk.mobile.timetable;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.seamk.mobile.R;
import com.seamk.mobile.UtilityFragment;
import com.seamk.mobile.interfaces.retrofit.PeppiService;
import com.seamk.mobile.objects.BasketRealization;
import com.seamk.mobile.objects.BasketSavedItems;
import com.seamk.mobile.objects.BasketStudentGroup;
import com.seamk.mobile.objects.ReservationOld;
import com.seamk.mobile.retrofit.ServiceGenerator;
import com.seamk.mobile.util.Common;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.seamk.mobile.util.Common.stringToString;


/**
 * Created by Juha Ala-Rantala on 16.11.2017.
 */

public class FragmentTimetableGrid extends UtilityFragment implements WeekView.EventClickListener, MonthLoader.MonthChangeListener {

    @BindView(R.id.weekView) WeekView mWeekView;
    @BindView(R.id.progressbar_week_view) ProgressBar mProgressBar;
    @BindView(R.id.relative_layout_week_view) RelativeLayout mRelativeLayout;
    @BindView(R.id.linear_layout_week_view) LinearLayout mLinearLayout;
    @BindView(R.id.b_retry) Button mButtonRetry;

    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private static final int TYPE_WEEK_VIEW = 3;
    private int mWeekViewType = TYPE_WEEK_VIEW;
    boolean calledNetwork = false;

    BasketSavedItems basketSavedItems = new BasketSavedItems();
    List<BasketStudentGroup> studentGroupBaskets = new ArrayList<>();
    List<BasketRealization> basketsToSearchRealizations = new ArrayList<>();
    List<BasketRealization> basketRealizations = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.item_timetable, container, false);
        ButterKnife.bind(this, v);

        getActivity().setTitle(getResources().getString(R.string.timetable));

        mWeekView.setOnEventClickListener(this);
        mWeekView.setMonthChangeListener(this);
        setupDateTimeInterpreter(true);

        mWeekView.setMinTime(8);
        mWeekView.setMaxTime(20);

        setHasOptionsMenu(true);

        mWeekView.setAlpha(0.25f);
        mProgressBar.setVisibility(View.VISIBLE);
        mLinearLayout.setVisibility(View.GONE);

        mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
        mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
        mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));

        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
            calendar.add(Calendar.WEEK_OF_YEAR, 1);
        }

        mWeekView.setHourHeight(120);
        mWeekView.goToDate(calendar);

        mButtonRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchedGroups = false;
                fetchedRealizations = false;
                mProgressBar.setVisibility(View.VISIBLE);
                mLinearLayout.setVisibility(View.GONE);
                mWeekView.setAlpha(0.25f);
                getReservations();
            }
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        if (callSG != null && callSG.isExecuted()){
            callSG.cancel();
        }
        if (callR != null && callR.isExecuted()) {
            callR.cancel();
        }
        super.onStop();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.menu_week_view, menu);
    }

    Toast mToast;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_refresh:
                if (calledNetwork && fetchedGroups && fetchedRealizations){
                    fetchedGroups = false;
                    fetchedRealizations = false;
                    mProgressBar.setVisibility(View.VISIBLE);
                    mLinearLayout.setVisibility(View.GONE);
                    mWeekView.setAlpha(0.25f);
                    getReservations();
                } else {
                    if (mToast != null) {
                        mToast.cancel();
                    }
                    mToast = Toast.makeText(getContext(), getString(R.string.please_wait_a_moment), Toast.LENGTH_SHORT);
                    mToast.show();
                }
                return true;
            case R.id.change_view_goto_today:
                mWeekView.goToToday();
                return true;
            case R.id.change_view_one_day:
                if (mWeekViewType != TYPE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(1);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                }
                return true;
            case R.id.change_view_three_days:
                if (mWeekViewType != TYPE_THREE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_THREE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(3);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                }
                return true;
            case R.id.change_view_five_days:
                if (mWeekViewType != TYPE_WEEK_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_WEEK_VIEW;
                    mWeekView.setNumberOfVisibleDays(5);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupDateTimeInterpreter(final boolean shortDate) {
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                String day = "";
                date.setFirstDayOfWeek(Calendar.MONDAY);
                int dayOfWeek = date.get(Calendar.DAY_OF_WEEK);

                switch (dayOfWeek){
                    case 2: day = getString(R.string.mon); break;
                    case 3: day = getString(R.string.tue); break;
                    case 4: day = getString(R.string.wed); break;
                    case 5: day = getString(R.string.thu); break;
                    case 6: day = getString(R.string.fri); break;
                    case 7: day = getString(R.string.sat); break;
                    case 1: day = getString(R.string.sun); break;
                }

                return day + " " + String.format("%02d", date.get(Calendar.DAY_OF_MONTH)) + "/" + String.format("%02d", date.get(Calendar.MONTH)+1);
            }

            @Override
            public String interpretTime(int hour, int minutes) {
                return String.format("%02d", hour) + ":" + String.format("%02d", minutes);
            }
        });
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        for (ReservationOld reservationOld : reservationOlds){
            if (Long.parseLong(reservationOld.getReservationId()) == event.getId()){
                Intent intent;
                intent = new Intent(getContext(), ActivityReservationDetailsOld.class);
                intent.putExtra("reservationOld", Parcels.wrap(reservationOld));
                getContext().startActivity(intent);
            }
        }
        // TODO
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {

        if (!calledNetwork) {
            getBasketItems();

            getReservations();

            calledNetwork = true;
        }
        return weekViewReservations;
    }

    List<ReservationOld> reservationOlds = new ArrayList<>();
    List<WeekViewEvent> weekViewReservations = new ArrayList<>();
    boolean fetchedRealizations = false;
    boolean fetchedGroups = false;

    Call<ResponseBody> callSG;
    Call<ResponseBody> callR;

    void getReservations(){

        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        calendar.add(Calendar.MONTH, -4);
        String startDate = format.format(calendar.getTime());
        calendar.add(Calendar.MONTH, 8);
        String endDate = format.format(calendar.getTime());

        String studentGroup = "";
        String realization = "";

        for (BasketStudentGroup studentGroupBasket : studentGroupBaskets) {
            if (studentGroupBasket.isShown()) {
                studentGroup = studentGroup + ", " + "'" + studentGroupBasket.getStudentGroupCode() + "'";
            }
        }
        if (studentGroup.length() != 0) {
            studentGroup = studentGroup.substring(2);
        }

        for (BasketRealization basketRealization : basketsToSearchRealizations) {
            realization = realization + ", " + "'" + basketRealization.getRealizationCode() + "'";
        }
        if (realization.length() != 0) {
            realization = realization.substring(2);
        }
        String size = "1000";
        String startTime = "T00:00:00";
        String endTime = "T23:59:59";
        String JSONPostStudentGroups = "{'startDate':'" + startDate + startTime + "'," + "'endDate':'" + endDate + endTime + "'," + "'studentGroup':[" + studentGroup + "]," + "'size':" + size + "}";
        String JSONPostRealizations = "{'startDate':'" + startDate + startTime + "'," + "'endDate':'" + endDate + endTime + "'," + "'realization':[" + realization + "]," + "'size':" + size + "}";

        RequestBody bodySG = RequestBody.create(MediaType.parse("application/json"), JSONPostStudentGroups);
        RequestBody bodyR = RequestBody.create(MediaType.parse("application/json"), JSONPostRealizations);
        if (studentGroupBaskets.size() > 0) {
            PeppiService peppiService = ServiceGenerator.createService(PeppiService.class, stringToString(getContext()), "");
            callSG = peppiService.createRequest(bodySG);
            callSG.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        JSONObject jsonObjectStudentGroup = new JSONObject(response.body().string());
                        reservationOlds.addAll(Common.reservationsFromJson(jsonObjectStudentGroup));
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    fetchedGroups = true;
                    finishedFetching();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    fetchedGroups = true;
                    finishedFetching();
                }
            });
        } else {
            fetchedGroups = true;
            finishedFetching();
        }
        if (basketsToSearchRealizations.size() > 0) {
            PeppiService peppiService = ServiceGenerator.createService(PeppiService.class, stringToString(getContext()), "");
            callR = peppiService.createRequest(bodyR);
            callR.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        JSONObject jsonObjectStudentGroup = new JSONObject(response.body().string());
                        reservationOlds.addAll(Common.reservationsFromJson(jsonObjectStudentGroup));
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    fetchedRealizations = true;
                    finishedFetching();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    fetchedRealizations = true;
                    finishedFetching();
                }
            });
        } else {
            fetchedRealizations = true;
            finishedFetching();
        }

    }

    void finishedFetching() {
        if (fetchedRealizations && fetchedGroups) {

            if (reservationOlds.size() > 0){
                weekViewReservations.clear();
            }

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

            WeekViewEvent event;
            for (ReservationOld reservationOld : reservationOlds) {
                Date dateStartDate = null;
                Date dateEndDate = null;
                SimpleDateFormat simpleDateFormat0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String tmpStartDate = reservationOld.getStartDate().replace("T", " ");
                try {dateStartDate = simpleDateFormat0.parse(tmpStartDate);} catch (ParseException e) {e.printStackTrace();}
                String tmpEndDate = reservationOld.getEndDate().replace("T", " ");
                try {dateEndDate = simpleDateFormat0.parse(tmpEndDate);} catch (ParseException e) {e.printStackTrace();}

                Calendar tmpCalendarStart = Calendar.getInstance();
                tmpCalendarStart.setTime(dateStartDate);

                Calendar tmpCalendarEnd = Calendar.getInstance();
                tmpCalendarEnd.setTime(dateEndDate);

                String nameText;
                if (reservationOld.getRealizationCode().get(0).equals("")){
                    nameText = reservationOld.getSubject();
                } else {
                    nameText = reservationOld.getRealizationName().get(0);
                }

                event = new WeekViewEvent(Long.parseLong(reservationOld.getReservationId()), nameText, tmpCalendarStart, tmpCalendarEnd);
                event.setLocation(reservationOld.getRoomCodesStringShort());
                if (reservationOld.getRealizationName().get(0).equals("")) {
                    event.setColor(Color.parseColor(Common.stringToHexColor(reservationOld.getSubject())));
                } else {
                    event.setColor(Color.parseColor(Common.stringToHexColor(reservationOld.getRealizationName().get(0) + reservationOld.getRealizationCode().get(0))));
                }
                weekViewReservations.add(event);
            }
            mProgressBar.setVisibility(View.GONE);
            if (weekViewReservations.size() > 0){
                mWeekView.setAlpha(1f);
            } else {
                mLinearLayout.setVisibility(View.VISIBLE);
            }
            mWeekView.notifyDatasetChanged();
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
}
