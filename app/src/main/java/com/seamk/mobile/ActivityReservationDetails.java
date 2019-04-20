package com.seamk.mobile;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.seamk.mobile.objects.Reservation;
import com.seamk.mobile.util.Common;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityReservationDetails extends AppCompatActivity {

    @BindView(R.id.tv_title_short) TextView titleShort;
    @BindView(R.id.tv_title_full) TextView titleFull;
    @BindView(R.id.iv_subject_color) ImageView colorSubject;
    @BindView(R.id.tv_details_subject) TextView subject;
    @BindView(R.id.tv_details_description) TextView description;
    @BindView(R.id.tv_details_time_start) TextView timeStart;
    @BindView(R.id.tv_details_time_end) TextView timeEnd;
    @BindView(R.id.tv_details_room_code) TextView roomCode;
    @BindView(R.id.tv_details_room_name) TextView roomName;
    @BindView(R.id.tv_details_building_name) TextView buildingName;
    @BindView(R.id.tv_details_realization_code) TextView realizationCode;
    @BindView(R.id.tv_details_realization_name) TextView realizationName;
    @BindView(R.id.tv_details_student_group) TextView studentGroupCode;
    @BindView(R.id.tv_details_teacher) TextView teacher;
    @BindView(R.id.tv_details_scheduling_group) TextView schedulingGroup;
    @BindView(R.id.tv_details_external_location) TextView externalLocation;
    @BindView(R.id.tv_details_attendee) TextView attendee;
    @BindView(R.id.toolbar) Toolbar toolbar;

    Reservation reservation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reservation_details);
        ButterKnife.bind(this);

        setTitle(getResources().getString(R.string.reservation_details));

        Bundle extras = getIntent().getExtras();
        reservation = Parcels.unwrap(extras.getParcelable("reservation"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (reservation.getRealizationNameStringShort().equals("")) {
            titleShort.setText(Common.getFirstLetters(reservation.getSubject()));
            titleFull.setText(reservation.getSubject());
            colorSubject.setBackgroundColor(Color.parseColor(Common.stringToHexColor(reservation.getSubject())));
        } else {
            titleShort.setText(Common.getFirstLetters(reservation.getRealizationNameStringShort()));
            titleFull.setText(reservation.getRealizationNameStringShort());
            colorSubject.setBackgroundColor(Color.parseColor(Common.stringToHexColor(reservation.getRealizationCodeStringShort() + reservation.getRealizationNameStringShort())));
        }

        subject.setText(reservation.getSubject());
        description.setText(reservation.getDescription());
        timeStart.setText(reservation.getStartDate().replace("T", " "));
        timeEnd.setText(reservation.getEndDate().replace("T", " "));
        roomCode.setText(reservation.getRoomCodeStringLong());
        roomName.setText(reservation.getRoomNameStringLong());
        buildingName.setText(reservation.getBuildingNameStringLong());
        realizationCode.setText(reservation.getRealizationCodeStringLong());
        realizationName.setText(reservation.getRealizationNameStringLong());
        studentGroupCode.setText(reservation.getStudentGroupStringLong());
        teacher.setText(reservation.getTeacherStringLong());
        schedulingGroup.setText(reservation.getSchedulingGroupStringLong());
        externalLocation.setText(reservation.getExternalStringLong());
        attendee.setText(reservation.getAttendeeStringLong());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}