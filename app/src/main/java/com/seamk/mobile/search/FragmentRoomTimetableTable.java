package com.seamk.mobile.search;

import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.seamk.mobile.R;
import com.seamk.mobile.eventbusevents.ReservationRoomEvent;
import com.seamk.mobile.objects.ReservationOld;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Juha Ala-Rantala on 23.4.2017.
 */

public class FragmentRoomTimetableTable extends Fragment {

    ReservationOld reservationOld;
    private List<ReservationOld> reservationOlds;
    ArrayList<List<ReservationOld>> allReservations;
    ArrayList<List<Date>> allDates;
    ArrayList<Boolean[]> allBooleans;
    String[] days = new String[7];
    String[] times = { "08:00", "09:00", "10:00", "11:00", "12:15", "13:15", "14:15", "15:15", "16:15", "17:15", "18:15", "19:15", "20:15"};
    List<Date> datesMA;
    List<Date> datesTI;
    List<Date> datesKE;
    List<Date> datesTO;
    List<Date> datesPE;
    List<Date> datesLA;
    List<Date> datesSU;

    Boolean[] boolTableMA;
    Boolean[] boolTableTI;
    Boolean[] boolTableKE;
    Boolean[] boolTableTO;
    Boolean[] boolTablePE;
    Boolean[] boolTableLA;
    Boolean[] boolTableSU;

    List<ReservationOld> reservationsMA;
    List<ReservationOld> reservationsTI;
    List<ReservationOld> reservationsKE;
    List<ReservationOld> reservationsTO;
    List<ReservationOld> reservationsPE;
    List<ReservationOld> reservationsLA;
    List<ReservationOld> reservationsSU;

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

    public void calculateReservations() {
        Calendar calendar = Calendar.getInstance();

        Date dateStartDate = null;
        SimpleDateFormat simpleDateFormat0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String tmpStartDate = reservationOld.getStartDate().replace("T", " ");
        try {dateStartDate = simpleDateFormat0.parse(tmpStartDate);} catch (ParseException e) {e.printStackTrace();}

        Calendar tmpCalendar = Calendar.getInstance();
        tmpCalendar.setTime(dateStartDate);
        calendar.set(Calendar.WEEK_OF_YEAR, tmpCalendar.get(Calendar.WEEK_OF_YEAR));

        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

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

        allReservations = new ArrayList<>();
        allReservations.add(reservationsMA);
        allReservations.add(reservationsTI);
        allReservations.add(reservationsKE);
        allReservations.add(reservationsTO);
        allReservations.add(reservationsPE);
        allReservations.add(reservationsLA);
        allReservations.add(reservationsSU);

        for (int i = 0; i < allBooleans.size(); i++){
            for (int j = 0; j < times.length; j++){
                allBooleans.get(i)[j] = false;
            }
        }

        for (int i = 0; i < allReservations.size(); i++){
            for (int j = 0; j < allReservations.get(i).size(); j++){
                for (int k = 0; k < allDates.get(i).size(); k++){
                    Date compareTime = allDates.get(i).get(k);
                    Date refStartTime = allReservations.get(i).get(j).getDateStartDate();
                    Date refEndTime = allReservations.get(i).get(j).getDateEndDate();
                    if (refStartTime.before(compareTime) && refEndTime.after(compareTime)){
                        allBooleans.get(i)[k] = true;
                    }
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String tmpstartDate = reservationOld.getStartDate().replace("T", " ");
                    String tmpendDate = reservationOld.getEndDate().replace("T", " ");
                    try {refStartTime = simpleDateFormat.parse(tmpstartDate);} catch (ParseException e) {e.printStackTrace();}
                    try {refEndTime = simpleDateFormat.parse(tmpendDate);} catch (ParseException e) {e.printStackTrace();}
                    if (refStartTime.before(compareTime) && refEndTime.after(compareTime)){
                        allBooleans.get(i)[k] = null;
                    }
                }
            }
        }

        reservationsMA = new ArrayList<>();
        reservationsTI = new ArrayList<>();
        reservationsKE = new ArrayList<>();
        reservationsTO = new ArrayList<>();
        reservationsPE = new ArrayList<>();
        reservationsLA = new ArrayList<>();
        reservationsSU = new ArrayList<>();
    }

    TableLayout tableLayout;
    LinearLayout linearLayoutEmptyView;
    LinearLayout linearLayoutProgressBar;
    List<TableRow> tableRows;
    TableRow tableRow0;
    TableRow tableRow1;
    TableRow tableRow2;
    TableRow tableRow3;
    TableRow tableRow4;
    TableRow tableRow5;
    TableRow tableRow6;
    TableRow tableRow7;
    TableRow tableRow8;
    TableRow tableRow9;
    TableRow tableRow10;
    TableRow tableRow11;
    TableRow tableRow12;
    TableRow tableRow13;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_room_timetable_matrix, container, false);
        linearLayoutEmptyView = v.findViewById(R.id.LLemptyView);
        tableLayout = v.findViewById(R.id.tl);
        linearLayoutProgressBar = v.findViewById(R.id.LLprogressBar);

        tableLayout.setVisibility(View.INVISIBLE);
        linearLayoutEmptyView.setVisibility(View.INVISIBLE);
        linearLayoutProgressBar.setVisibility(View.VISIBLE);

        tableRow0 = v.findViewById(R.id.tr0);
        tableRow1 = v.findViewById(R.id.tr1);
        tableRow2 = v.findViewById(R.id.tr2);
        tableRow3 = v.findViewById(R.id.tr3);
        tableRow4 = v.findViewById(R.id.tr4);
        tableRow5 = v.findViewById(R.id.tr5);
        tableRow6 = v.findViewById(R.id.tr6);
        tableRow7 = v.findViewById(R.id.tr7);
        tableRow8 = v.findViewById(R.id.tr8);
        tableRow9 = v.findViewById(R.id.tr9);
        tableRow10 = v.findViewById(R.id.tr10);
        tableRow11 = v.findViewById(R.id.tr11);
        tableRow12 = v.findViewById(R.id.tr12);
        tableRow13 = v.findViewById(R.id.tr13);

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getArguments();
        reservationOld = Parcels.unwrap(extras.getParcelable("reservationOld"));

        reservationOlds = new ArrayList<>();
        DateFormat format = new SimpleDateFormat("dd.MM.");
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        Date dateStartDate = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String tmpstartDate = reservationOld.getStartDate().replace("T", " ");
        try {dateStartDate = simpleDateFormat.parse(tmpstartDate);} catch (ParseException e) {e.printStackTrace();}

        Calendar tmpCalendar = Calendar.getInstance();
        tmpCalendar.setTime(dateStartDate);
        calendar.set(Calendar.WEEK_OF_YEAR, tmpCalendar.get(Calendar.WEEK_OF_YEAR));

        days[0] = "Ma " + format.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        days[1] = "Ti " + format.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        days[2] = "Ke " + format.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        days[3] = "To " + format.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        days[4] = "Pe " + format.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        days[5] = "La " + format.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        days[6] = "Su " + format.format(calendar.getTime());

        reservationsMA = new ArrayList<>();
        reservationsTI = new ArrayList<>();
        reservationsKE = new ArrayList<>();
        reservationsTO = new ArrayList<>();
        reservationsPE = new ArrayList<>();
        reservationsLA = new ArrayList<>();
        reservationsSU = new ArrayList<>();
        tableRows = new ArrayList<>();
        datesMA = new ArrayList<>();
        datesTI = new ArrayList<>();
        datesKE = new ArrayList<>();
        datesTO = new ArrayList<>();
        datesPE = new ArrayList<>();
        datesLA = new ArrayList<>();
        datesSU = new ArrayList<>();


    }

    @Subscribe
    public void onNewReservationsEvent(ReservationRoomEvent event) {
        this.reservationOlds = event.reservationOlds;
        if (reservationOlds.size() == 0){
            tableLayout.setVisibility(View.INVISIBLE);
            linearLayoutEmptyView.setVisibility(View.VISIBLE);
            linearLayoutProgressBar.setVisibility(View.INVISIBLE);
        } else {
            tableLayout.setVisibility(View.VISIBLE);
            linearLayoutEmptyView.setVisibility(View.INVISIBLE);
            linearLayoutProgressBar.setVisibility(View.INVISIBLE);

            Calendar c = Calendar.getInstance();

            Date dateStartDate = null;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String tmpstartDate = reservationOld.getStartDate().replace("T", " ");
            try {dateStartDate = simpleDateFormat.parse(tmpstartDate);} catch (ParseException e) {e.printStackTrace();}

            Calendar tmpCalendar = Calendar.getInstance();
            tmpCalendar.setTime(dateStartDate);
            c.set(Calendar.WEEK_OF_YEAR, tmpCalendar.get(Calendar.WEEK_OF_YEAR));

            c.setFirstDayOfWeek(Calendar.MONDAY);
            c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            for (int i = 0; i < days.length; i++){
                for (int j = 0; j < times.length; j++){
                    int time = Integer.parseInt(times[j].substring(0,2));
                    int time2 = Integer.parseInt(times[j].substring(3,5));
                    c.set(Calendar.HOUR_OF_DAY, time);
                    c.set(Calendar.MINUTE, time2);

                    if (i == 0){
                        datesMA.add(c.getTime());
                    } else if (i == 1){
                        datesTI.add(c.getTime());
                    }else if (i == 2){
                        datesKE.add(c.getTime());
                    }else if (i == 3){
                        datesTO.add(c.getTime());
                    }else if (i == 4){
                        datesPE.add(c.getTime());
                    }else if (i == 5){
                        datesLA.add(c.getTime());
                    }else if (i == 6){
                        datesSU.add(c.getTime());
                    }
                }
                c.add(Calendar.DAY_OF_YEAR, 1);
            }


            boolTableMA = new Boolean[datesMA.size()];
            boolTableTI = new Boolean[datesTI.size()];
            boolTableKE = new Boolean[datesKE.size()];
            boolTableTO = new Boolean[datesTO.size()];
            boolTablePE = new Boolean[datesPE.size()];
            boolTableLA = new Boolean[datesLA.size()];
            boolTableSU = new Boolean[datesSU.size()];

            allDates = new ArrayList<>();
            allDates.add(datesMA);
            allDates.add(datesTI);
            allDates.add(datesKE);
            allDates.add(datesTO);
            allDates.add(datesPE);
            allDates.add(datesLA);
            allDates.add(datesSU);

            allBooleans = new ArrayList<>();
            allBooleans.add(boolTableMA);
            allBooleans.add(boolTableTI);
            allBooleans.add(boolTableKE);
            allBooleans.add(boolTableTO);
            allBooleans.add(boolTablePE);
            allBooleans.add(boolTableLA);
            allBooleans.add(boolTableSU);

            calculateReservations();

            tableRows.add(tableRow0);
            tableRows.add(tableRow1);
            tableRows.add(tableRow2);
            tableRows.add(tableRow3);
            tableRows.add(tableRow4);
            tableRows.add(tableRow5);
            tableRows.add(tableRow6);
            tableRows.add(tableRow7);
            tableRows.add(tableRow8);
            tableRows.add(tableRow9);
            tableRows.add(tableRow10);
            tableRows.add(tableRow11);
            tableRows.add(tableRow12);
            tableRows.add(tableRow13);

            View view0 = getActivity().getLayoutInflater().inflate(R.layout.item_classroom_text, null, false);
            TextView textView0 = view0.findViewById(R.id.DateOrTimeText);
            textView0.setText("");
            textView0.setTextColor(Color.BLACK);
            tableRow0.addView(view0);
            for (int i = 0; i < days.length - 2; i++){
                View view = getActivity().getLayoutInflater().inflate(R.layout.item_classroom_text, null, false);
                TextView textView = view.findViewById(R.id.DateOrTimeText);
                textView.setText(days[i]);
                textView.setTextColor(Color.BLACK);
                tableRow0.addView(view);
            }
            for (int i = 0; i < times.length; i++){
                View view = getActivity().getLayoutInflater().inflate(R.layout.item_classroom_text, null, false);
                TextView textView = view.findViewById(R.id.DateOrTimeText);
                textView.setText(times[i]);
                textView.setTextColor(Color.BLACK);
                tableRows.get(i+1).addView(view);
            }
            for (int i = 0; i < allBooleans.size() - 2; i++){
                for (int j = 0; j < allBooleans.get(i).length; j++){
                    View vr = getActivity().getLayoutInflater().inflate(R.layout.card_reserved, null, false);
                    View va = getActivity().getLayoutInflater().inflate(R.layout.card_available, null, false);
                    View vrtr = getActivity().getLayoutInflater().inflate(R.layout.card_reserved_this_reservation, null, false);

                    if (allBooleans.get(i)[j] == null){
                        tableRows.get(j+1).addView(vrtr);
                    } else if (allBooleans.get(i)[j]){
                        tableRows.get(j+1).addView(vr);
                    } else {
                        tableRows.get(j+1).addView(va);
                    }
                }
            }

            tableLayout.setStretchAllColumns(true);
            tableLayout.setShrinkAllColumns(true);


        }
    }
}
