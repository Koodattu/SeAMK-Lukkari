package com.seamk.mobile.search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.seamk.mobile.R;
import com.seamk.mobile.eventbusevents.ReservationRoomEvent;
import com.seamk.mobile.objects.ReservationOld;
import com.seamk.mobile.objects.StringItem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FragmentRoomTimetableWeek extends Fragment {

    RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    //private LukkariWeekAdapter adapter;
    private List<ReservationOld> reservationOlds;
    private List<Object> datesReservations;
    TextView emptyView;
    ProgressBar progressBar;
    String[] days = new String[7];
    List<Object> reservationsMA;
    List<Object> reservationsTI;
    List<Object> reservationsKE;
    List<Object> reservationsTO;
    List<Object> reservationsPE;
    List<Object> reservationsLA;
    List<Object> reservationsSU;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_room_timetable_reservations, container, false);

        recyclerView = v.findViewById(R.id.week_fragment_recycler);
        emptyView = v.findViewById(R.id.empty_view_week);
        progressBar = v.findViewById(R.id.progressbar_room_timetable);

        gridLayoutManager = new GridLayoutManager(getContext(), 100);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
/*
        adapter = new LukkariWeekAdapter(getContext(), datesReservations);
        recyclerView.setAdapter(adapter);

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return gridLayoutManager.getSpanCount() / adapter.getItemType(position);
            }
        });
*/
        recyclerView.setVisibility(View.INVISIBLE);
        emptyView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        return v;
    }

    ReservationOld reservationOld;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getArguments();
        reservationOld = extras.getParcelable("reservationOld");

        Date dateStartDate = null;
        SimpleDateFormat simpleDateFormat0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String tmpStartDate = reservationOld.getStartDate().replace("T", " ");
        try {dateStartDate = simpleDateFormat0.parse(tmpStartDate);} catch (ParseException e) {e.printStackTrace();}

        Calendar tmpCalendar = Calendar.getInstance();
        tmpCalendar.setTime(dateStartDate);

        DateFormat format = new SimpleDateFormat("dd.MM.");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.WEEK_OF_YEAR, tmpCalendar.get(Calendar.WEEK_OF_YEAR));
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        days[0] = "Maanantai " + format.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        days[1] = "Tiistai " + format.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        days[2] = "Keskiviikko " + format.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        days[3] = "Torstai " + format.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        days[4] = "Perjantai " + format.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        days[5] = "Lauantai " + format.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        days[6] = "Sunnuntai " + format.format(calendar.getTime());

        reservationOlds = new ArrayList<>();
        reservationsMA = new ArrayList<>();
        reservationsTI = new ArrayList<>();
        reservationsKE = new ArrayList<>();
        reservationsTO = new ArrayList<>();
        reservationsPE = new ArrayList<>();
        reservationsLA = new ArrayList<>();
        reservationsSU = new ArrayList<>();
        datesReservations = new ArrayList<>();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onNewReservationsEvent(ReservationRoomEvent event) {
        this.reservationOlds = event.reservationOlds;

        if (reservationOlds.size() == 0){
            recyclerView.setVisibility(View.INVISIBLE);
            emptyView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }

        datesReservations = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Date dateStartDate = null;
        SimpleDateFormat simpleDateFormat0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String tmpStartDate = reservationOld.getStartDate().replace("T", " ");
        try {dateStartDate = simpleDateFormat0.parse(tmpStartDate);} catch (ParseException e) {e.printStackTrace();}

        Calendar tmpCalendar = Calendar.getInstance();
        tmpCalendar.setTime(dateStartDate);

        calendar.set(Calendar.WEEK_OF_YEAR, tmpCalendar.get(Calendar.WEEK_OF_YEAR));
        Date startDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date endDate = calendar.getTime();

        for (int i = 0; i < reservationOlds.size(); i++){
            if (reservationOlds.get(i).getDateStartDate().after(startDate) && reservationOlds.get(i).getDateEndDate().before(endDate)){
                reservationsMA.add(this.reservationOlds.get(i));
            }
        }

        startDate = endDate;
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        endDate = calendar.getTime();

        for (int i = 0; i < reservationOlds.size(); i++){
            if (reservationOlds.get(i).getDateStartDate().after(startDate) && reservationOlds.get(i).getDateEndDate().before(endDate)){
                reservationsTI.add(this.reservationOlds.get(i));
            }
        }

        startDate = endDate;
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        endDate = calendar.getTime();

        for (int i = 0; i < reservationOlds.size(); i++){
            if (reservationOlds.get(i).getDateStartDate().after(startDate) && reservationOlds.get(i).getDateEndDate().before(endDate)){
                reservationsKE.add(this.reservationOlds.get(i));
            }
        }

        startDate = endDate;
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        endDate = calendar.getTime();

        for (int i = 0; i < reservationOlds.size(); i++){
            if (reservationOlds.get(i).getDateStartDate().after(startDate) && reservationOlds.get(i).getDateEndDate().before(endDate)){
                reservationsTO.add(this.reservationOlds.get(i));
            }
        }

        startDate = endDate;
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        endDate = calendar.getTime();

        for (int i = 0; i < reservationOlds.size(); i++){
            if (reservationOlds.get(i).getDateStartDate().after(startDate) && reservationOlds.get(i).getDateEndDate().before(endDate)){
                reservationsPE.add(this.reservationOlds.get(i));
            }
        }

        startDate = endDate;
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        endDate = calendar.getTime();

        for (int i = 0; i < reservationOlds.size(); i++){
            if (reservationOlds.get(i).getDateStartDate().after(startDate) && reservationOlds.get(i).getDateEndDate().before(endDate)){
                reservationsLA.add(this.reservationOlds.get(i));
            }
        }

        startDate = endDate;
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        endDate = calendar.getTime();

        for (int i = 0; i < reservationOlds.size(); i++){
            if (reservationOlds.get(i).getDateStartDate().after(startDate) && reservationOlds.get(i).getDateEndDate().before(endDate)){
                reservationsSU.add(this.reservationOlds.get(i));
            }
        }

        StringItem stringItem = new StringItem("Ei varauksia tälle päivälle.");

        datesReservations.add(days[0]);
        if (reservationsMA.size() > 0) {
            datesReservations.addAll(reservationsMA);
        } else {
            datesReservations.add(stringItem);
        }

        datesReservations.add(days[1]);
        if (reservationsTI.size() > 0) {
            datesReservations.addAll(reservationsTI);
        } else {
            datesReservations.add(stringItem);
        }

        datesReservations.add(days[2]);
        if (reservationsKE.size() > 0) {
            datesReservations.addAll(reservationsKE);
        } else {
            datesReservations.add(stringItem);
        }

        datesReservations.add(days[3]);
        if (reservationsTO.size() > 0) {
            datesReservations.addAll(reservationsTO);
        } else {
            datesReservations.add(stringItem);
        }

        datesReservations.add(days[4]);
        if (reservationsPE.size() > 0) {
            datesReservations.addAll(reservationsPE);
        } else {
            datesReservations.add(stringItem);
        }

        datesReservations.add(days[5]);
        if (reservationsLA.size() > 0) {
            datesReservations.addAll(reservationsLA);
        } else {
            datesReservations.add(stringItem);
        }

        datesReservations.add(days[6]);
        if (reservationsSU.size() > 0) {
            datesReservations.addAll(reservationsSU);
        } else {
            datesReservations.add(stringItem);
        }
/*
        //recyclerView.invalidate();
        adapter = new LukkariWeekAdapter(getContext(), datesReservations);
        recyclerView.setAdapter(adapter);
*/
        reservationsMA = new ArrayList<>();
        reservationsTI = new ArrayList<>();
        reservationsKE = new ArrayList<>();
        reservationsTO = new ArrayList<>();
        reservationsPE = new ArrayList<>();
        reservationsLA = new ArrayList<>();
        reservationsSU = new ArrayList<>();
    }
}
