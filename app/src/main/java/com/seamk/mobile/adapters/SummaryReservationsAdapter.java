package com.seamk.mobile.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.seamk.mobile.R;
import com.seamk.mobile.objects.ReservationOld;
import com.seamk.mobile.timetable.ActivityReservationDetailsOld;
import com.seamk.mobile.util.Common;

import org.parceler.Parcels;

import java.util.Date;
import java.util.List;

/**
 * Created by Juha Ala-Rantala on 5.4.2017.
 */

public class SummaryReservationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> items;
    private Context context;
    private static final int ITEM_TYPE_RESERVATION = 0;
    private static final int ITEM_TYPE_DATE = 1;
    private static final int ITEM_TYPE_DIVIDER = 2;


    public SummaryReservationsAdapter(Context context, List<Object> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == ITEM_TYPE_RESERVATION) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reservation,parent,false);
            return new ReservationViewHolder(itemView);
        } else if (viewType == ITEM_TYPE_DATE) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_summary_day,parent,false);
            return new DateViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_divider,parent,false);
            return new DividerViewHolder(itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof ReservationOld) {
            return ITEM_TYPE_RESERVATION;
        } else {
            if (items.get(position).equals("")){
                return ITEM_TYPE_DIVIDER;
            } else {
                return ITEM_TYPE_DATE;
            }
        }
    }

    public int getItemType(int position) {
        Date startDate, endDate, refStartDate, refEndDate;

        int span = 1;
        int lastViewType = 1;

            if (position == 0) {
                span = 1;
            } else if (position == items.size() - 1) {
                if (items.get(position - 1) instanceof String || items.get(position) instanceof String) {
                    span = 1;
                } else {
                    startDate = ((ReservationOld) items.get(position)).getDateStartDate();
                    endDate = ((ReservationOld) items.get(position)).getDateEndDate();
                    refStartDate = ((ReservationOld) items.get(position - 1)).getDateStartDate();
                    refEndDate = ((ReservationOld) items.get(position - 1)).getDateEndDate();
                    if (startDate.equals(refStartDate) && endDate.equals(refEndDate)) {
                        lastViewType = getItemType(position - 1);
                        span = lastViewType;
                    } else {
                        lastViewType = 1;
                        span = 1;
                    }
                }
            } else if (items.get(position) instanceof String) {
                span = 1;
            } else if (items.get(position - 1) instanceof String) {
                span = 1;
            } else if (items.get(position + 1) instanceof String) {
                startDate = ((ReservationOld) items.get(position)).getDateStartDate();
                endDate = ((ReservationOld) items.get(position)).getDateEndDate();
                refStartDate = ((ReservationOld) items.get(position - 1)).getDateStartDate();
                refEndDate = ((ReservationOld) items.get(position - 1)).getDateEndDate();
                if (!(startDate.equals(refStartDate) && endDate.equals(refEndDate))) {
                    span = 1;
                } else {
                    span = getItemType(position - 1);
                }
            } else if (items.get(position) instanceof ReservationOld) {
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
                            lastViewType = getItemType(position - 1);
                            span = lastViewType;
                        } else {
                            lastViewType = 1;
                            span = 1;
                        }
                    } else {
                        lastViewType = getItemType(position - 1);
                        span = lastViewType;
                    }
                } else {
                    startDate = ((ReservationOld) items.get(position)).getDateStartDate();
                    endDate = ((ReservationOld) items.get(position)).getDateEndDate();
                    refStartDate = ((ReservationOld) items.get(position - 1)).getDateStartDate();
                    refEndDate = ((ReservationOld) items.get(position - 1)).getDateEndDate();
                    if (startDate.equals(refStartDate) && endDate.equals(refEndDate)) {
                        lastViewType = getItemType(position - 1);
                        span = lastViewType;
                    } else {
                        lastViewType = 1;
                        span = 1;
                    }
                }
            }
            if (lastViewType == 1) {
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

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {

        Object item = items.get(position);

        if (viewHolder instanceof ReservationViewHolder) {
            ((ReservationViewHolder) viewHolder).bind((ReservationOld)item, context);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    intent = new Intent(context, ActivityReservationDetailsOld.class);
                    intent.putExtra("reservationOld", Parcels.wrap((ReservationOld)items.get(viewHolder.getAdapterPosition())));
                    context.startActivity(intent);
                }
            });
        } else if (viewHolder instanceof DateViewHolder) {
            ((DateViewHolder) viewHolder).bind((String) item);
        } else if (viewHolder instanceof DividerViewHolder){
            ((DividerViewHolder) viewHolder).bind();
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private static class DividerViewHolder extends RecyclerView.ViewHolder {

        public DividerViewHolder(View itemView){
            super(itemView);
        }

        public void bind() {
        }
    }

    private static class DateViewHolder extends RecyclerView.ViewHolder {

        public TextView datePvm;

        public DateViewHolder(View itemView){
            super(itemView);
            datePvm = itemView.findViewById(R.id.tv_text);
        }

        public void bind(String date) {
            datePvm.setText(date);
        }
    }

    public static class ReservationViewHolder extends RecyclerView.ViewHolder{

        public TextView start_end_time;
        public TextView subject;
        public TextView room;
        public TextView scheduling_group;
        public TextView teacher;
        ImageView subject_color;

        public ReservationViewHolder(View itemView){
            super(itemView);
            start_end_time = itemView.findViewById(R.id.tv_time);
            subject = itemView.findViewById(R.id.tv_subject);
            room = itemView.findViewById(R.id.tv_room);
            scheduling_group = itemView.findViewById(R.id.tv_scheduling_group);
            teacher = itemView.findViewById(R.id.tv_teacher);
            subject_color = itemView.findViewById(R.id.iv_subject_color);
        }
        public void bind(ReservationOld reservationOld, Context context) {

            SharedPreferences settings = context.getSharedPreferences("ApplicationPreferences", 0);

            if (reservationOld.getRealizationName().get(0).equals("")){
                subject.setText(reservationOld.getSubject());
            }
            else {
                subject.setText(reservationOld.getRealizationName().get(0));
            }

            // TODO jos nimi tai koodi löytyy, käytä sitä, käyttäjän asetus huomioon ottaen, muulloin käytä description

            // TODO jos asetuksissa on päällä koodin käyttö ja koodi ei ole tyhjä => näytä koodi
            // TODO jos asetuksissa on päällä koodin käyttö ja koodi on tyhjä => näytä nimi
            // TODO jos asetuksissa on päällä koodin käyttö ja koodin on tyhjä ja nimi on tyhjä => näytä desc
            // TODO jos asetuksissa ei ole päällä koodin käyttö ja nimi ei ole tyhjä => näytä nimi
            // TODO jos asetuksissa ei ole päällä koodin käyttö ja nimi on tyhjä => näytä koodi
            // TODO jos asetuksissa ei ole päällä koodin käyttö ja nimi on tyhjä ja koodi on tyhjä => näytä desc

            if (settings.getBoolean("useAlternativeLocation", false)){ // jos asetuksissa on päällä koodin käyttö
                if (reservationOld.getRoomCode().get(0).equals("")){ // jos koodi on tyhjä
                    if (reservationOld.getRoomName().get(0).equals("")){ // jos nimi on tyhjä
                        if (reservationOld.getDescription().isEmpty()) {
                            room.setText(reservationOld.getSubject());
                        } else {
                            room.setText(reservationOld.getDescription());
                        }
                    } else { // nimi ei ole tyhjä
                        if (reservationOld.getRoomName().size() > 1){ // jos on useampi nimi
                            room.setText(reservationOld.getRoomNamesStringShort());
                        } else { // jos on vain yksi nimi
                            room.setText(reservationOld.getRoomName().get(0));
                        }
                    }
                } else { // jos koodi ei ole tyhjä
                    if (reservationOld.getRoomCode().size() > 1){ // jos on useampi koodi
                        room.setText(reservationOld.getRoomCodesStringShort());
                    } else { // jos on vain yksi koodi
                        room.setText(reservationOld.getRoomCode().get(0));
                    }
                }
            } else { // jos asetuksissa ei ole päällä koodin käyttö
                if (reservationOld.getRoomName().get(0).equals("")){ // jos nimi on tyhjä
                    if (reservationOld.getRoomCode().get(0).equals("")){ // jos koodi on tyhjä
                        if (reservationOld.getDescription().isEmpty()) {
                            room.setText(reservationOld.getSubject());
                        } else {
                            room.setText(reservationOld.getDescription());
                        }
                    } else { // koodi ei ole tyhjä
                        if (reservationOld.getRoomCode().size() > 1){ // jos on useampi koodi
                            room.setText(reservationOld.getRoomCodesStringShort());
                        } else { // jos on vain yksi koodi
                            room.setText(reservationOld.getRoomCode().get(0));
                        }
                    }
                } else { // jos nimi ei ole tyhjä
                    if (reservationOld.getRoomCode().size() > 1){ // jos on useampi nimi
                        room.setText(reservationOld.getRoomNamesStringShort());
                    } else { // jos on vain yksi nimi
                        room.setText(reservationOld.getRoomName().get(0));
                    }
                }
            }

            if (settings.getBoolean("showSchedulingGroups", true)) {
                if (!reservationOld.getSchedulingGroupName().get(0).equals("")) {
                    scheduling_group.setVisibility(View.VISIBLE);
                    scheduling_group.setText(reservationOld.getSchedulingGroupName().get(0));
                }
            }

            if (reservationOld.getRealizationName().get(0).equals("")){
                subject_color.setBackgroundColor(Color.parseColor(Common.stringToHexColor(reservationOld.getSubject())));
            } else {
                subject_color.setBackgroundColor(Color.parseColor(Common.stringToHexColor(reservationOld.getRealizationName().get(0) + reservationOld.getRealizationCode().get(0))));
            }

            start_end_time.setText(reservationOld.getStartDate().substring(11, reservationOld.getStartDate().length() - 3) + " - " + reservationOld.getEndDate().substring(11, reservationOld.getEndDate().length() - 3));
        }
    }
}
