package com.seamk.mobile;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.seamk.mobile.elasticsearch.ElasticReservation;
import com.seamk.mobile.elasticsearch.HitReservation;
import com.seamk.mobile.elasticsearch.SourceReservation;
import com.seamk.mobile.interfaces.callback.ElasticReservationCallback;
import com.seamk.mobile.objects.Reservation;
import com.seamk.mobile.retrofit.RetroFitters;
import com.seamk.mobile.util.Common;

import org.json.JSONException;
import org.parceler.Parcels;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentFindTimetableGrid extends Fragment implements WeekView.EventClickListener, MonthLoader.MonthChangeListener {

    @BindView(R.id.weekView) WeekView weekView;
    @BindView(R.id.progressbar_week_view) ProgressBar progressBar;
    @BindView(R.id.relative_layout_week_view) RelativeLayout relativeLayout;
    @BindView(R.id.linear_layout_week_view) LinearLayout linearLayout;
    @BindView(R.id.b_retry) Button buttonRetry;

    int weekViewType = 3;
    String searchWord;

    List<Reservation> reservations = new ArrayList<>();
    List<WeekViewEvent> weekViewReservations = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.item_timetable, container, false);
        ButterKnife.bind(this, v);

        getActivity().setTitle(searchWord);

        weekView.setOnEventClickListener(this);
        weekView.setMonthChangeListener(this);
        setupDateTimeInterpreter();

        weekView.setMinTime(8);
        weekView.setMaxTime(21);

        setHasOptionsMenu(true);

        weekView.setAlpha(0.25f);
        progressBar.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.GONE);

        weekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
        weekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
        weekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));

        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
            calendar.add(Calendar.WEEK_OF_YEAR, 1);
        }

        weekView.setHourHeight(120);
        weekView.goToDate(calendar);

        try {
            fetchReservations();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            searchWord = bundle.getString("word");
        }
    }

    private void setupDateTimeInterpreter() {
        weekView.setDateTimeInterpreter(new DateTimeInterpreter() {
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
        for (Reservation reservation : reservations){
            if (reservation.getReservationId().equals(event.getIdentifier())){
                Intent intent;
                intent = new Intent(getContext(), ActivityReservationDetails.class);
                intent.putExtra("reservation", Parcels.wrap(reservation));
                getContext().startActivity(intent);
            }
        }
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        return weekViewReservations;
    }

    void fetchReservations() throws IOException, JSONException {
        RetroFitters.fetchElasticReservations(getContext(), searchWord, 30, new ElasticReservationCallback() {
            @Override
            public void onSuccess(ElasticReservation elasticReservation) {
                finishedFetching(elasticReservation);
            }

            @Override
            public void onFailure(Throwable throwable) {
                // TODO
            }
        });
    }

    void finishedFetching(ElasticReservation elasticReservation) {
        reservations = new ArrayList<>();

        List<HitReservation> hitReservations = elasticReservation.getHitsReservation().getHitReservation();

        for (HitReservation hit : hitReservations) {
            SourceReservation s = hit.getSourceReservation();

            Reservation reservation = new Reservation();
            reservation.setReservationId(s.getId());
            reservation.setSubject(s.getSubject());
            reservation.setDescription(s.getDescription());
            reservation.setStartDate(s.getStartDate());
            reservation.setEndDate(s.getEndDate());
            reservation.setLocation(s.getLocation());
            reservation.setExternalLocation(s.getExternalLocation());
            reservation.setRealization(s.getRealization());
            reservation.setStudentGroup(s.getStudentGroup());
            reservation.setSchedulingGroup(s.getSchedulingGroup());
            reservation.setTeacher(s.getReservedFor());
            reservation.setExternalPerson(s.getExternalPerson());
            reservation.setAttendee(s.getAttendee());

            reservations.add(reservation);
        }

        WeekViewEvent event;
        for (Reservation reservation : reservations) {
            Date dateStartDate = null;
            Date dateEndDate = null;
            SimpleDateFormat simpleDateFormat0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String tmpStartDate = reservation.getStartDate().replace("T", " ");
            try {dateStartDate = simpleDateFormat0.parse(tmpStartDate);} catch (ParseException e) {e.printStackTrace();}
            String tmpEndDate = reservation.getEndDate().replace("T", " ");
            try {dateEndDate = simpleDateFormat0.parse(tmpEndDate);} catch (ParseException e) {e.printStackTrace();}

            Calendar tmpCalendarStart = Calendar.getInstance();
            tmpCalendarStart.setTime(dateStartDate);

            Calendar tmpCalendarEnd = Calendar.getInstance();
            tmpCalendarEnd.setTime(dateEndDate);

            String nameText;
            if (reservation.getRealizationCodeStringShort().equals("")){
                nameText = reservation.getSubject();
            } else {
                nameText = reservation.getRealizationNameStringShort();
            }

            //event = new WeekViewEvent(Long.parseLong(reservation.getReservationId()), nameText, tmpCalendarStart, tmpCalendarEnd);
            event = new WeekViewEvent();
            event.setIdentifier(reservation.getReservationId());
            event.setName(nameText);
            event.setStartTime(tmpCalendarStart);
            event.setEndTime(tmpCalendarEnd);
            event.setLocation(reservation.getLocationStringShort());
            if (reservation.getRealizationNameStringShort().equals("")) {
                event.setColor(Color.parseColor(Common.stringToHexColor(reservation.getSubject())));
            } else {
                event.setColor(Color.parseColor(Common.stringToHexColor(reservation.getRealizationCodeStringShort() + reservation.getRealizationNameStringShort())));
            }
            weekViewReservations.add(event);
        }
        progressBar.setVisibility(View.GONE);
        if (weekViewReservations.size() > 0){
            weekView.setAlpha(1f);
        } else {
            linearLayout.setVisibility(View.VISIBLE);
        }
        weekView.notifyDatasetChanged();
    }
}
