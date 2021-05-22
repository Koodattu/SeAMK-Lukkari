package com.seamk.mobile.timetable;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.seamk.mobile.R;
import com.seamk.mobile.eventbusevents.ExtraChosenEvent;
import com.seamk.mobile.objects.ReservationOld;
import com.seamk.mobile.objects.StringColorIconItem;
import com.seamk.mobile.util.Common;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

/**
 * Created by Juha Ala-Rantala on 14.11.2017.
 */

public class ActivityReservationDetailsOld extends AppCompatActivity {

    @BindView(R.id.tv_title_short) TextView titleShort;
    @BindView(R.id.tv_title_full) TextView titleFull;
    @BindView(R.id.iv_subject_color) ImageView colorSubject;
    @BindView(R.id.tv_details_subject) TextView subject;
    @BindView(R.id.tv_details_description) TextView description;
    @BindView(R.id.tv_details_time_start) TextView timeStart;
    @BindView(R.id.tv_details_time_end) TextView timeEnd;
    @BindView(R.id.tv_details_room_code) TextView roomCode;
    @BindView(R.id.tv_details_room_name) TextView roomName;
    @BindView(R.id.tv_details_building_code) TextView buildingCode;
    @BindView(R.id.tv_details_building_name) TextView buildingName;
    @BindView(R.id.tv_details_realization_code) TextView realizationCode;
    @BindView(R.id.tv_details_realization_name) TextView realizationName;
    @BindView(R.id.tv_details_student_group) TextView studentGroupCode;
    @BindView(R.id.tv_details_teacher) TextView teacher;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.toolbar) Toolbar toolbar;

    ReservationOld reservationOld;
    List<IItem> items;
    ItemAdapter<IItem> itemAdapter;
    FastAdapter<IItem> fastAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.item_reservation_details);
        ButterKnife.bind(this);

        setTitle(getResources().getString(R.string.reservation_details));

        Bundle extras = getIntent().getExtras();
        reservationOld = Parcels.unwrap(extras.getParcelable("reservationOld"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (reservationOld.getRealizationName().get(0).equals("")){
            titleShort.setText(Common.getFirstLetters(reservationOld.getSubject()));
            titleFull.setText(reservationOld.getSubject());
            colorSubject.setBackgroundColor(Color.parseColor(Common.stringToHexColor(reservationOld.getSubject())));
        } else {
            titleShort.setText(Common.getFirstLetters(reservationOld.getRealizationName().get(0)));
            titleFull.setText(reservationOld.getRealizationName().get(0));
            colorSubject.setBackgroundColor(Color.parseColor(Common.stringToHexColor(reservationOld.getRealizationName().get(0) + reservationOld.getRealizationCode().get(0))));
        }

        subject.setText(reservationOld.getSubject());
        description.setText(reservationOld.getDescription());
        timeStart.setText(reservationOld.getStartDate().replace("T", " "));
        timeEnd.setText(reservationOld.getEndDate().replace("T", " "));
        roomCode.setText(reservationOld.getRoomCodesStringShort());
        roomName.setText(reservationOld.getRoomNamesStringShort());
        buildingCode.setText(reservationOld.getBuildingsCodesStringShort());
        buildingName.setText(reservationOld.getBuildingsNamesStringShort());
        realizationCode.setText(reservationOld.getRealizationCodesStringShort());
        realizationName.setText(reservationOld.getRealizationsNamesStringShort());
        studentGroupCode.setText(reservationOld.getStudentGroupsStringShort());
        teacher.setText(reservationOld.getTeachersStringShort());

        final GridLayoutManager glm = new GridLayoutManager(this,1000);
        recyclerView.setLayoutManager(glm);
        recyclerView.setHasFixedSize(true);

        items = new ArrayList<>();
        items.add(new StringColorIconItem("Piilota", "", "", "ic_block", "colorPrimary", 1, 3));
        items.add(new StringColorIconItem("Sijainti", "", "", "ic_map", "colorPrimary", 3, 3));
        items.add(new StringColorIconItem("OPS", "", "", "ic_link_white", "colorPrimary", 4, 3));
        items.add(new StringColorIconItem("Tilan Lukujärjestys", "", "", "ic_calendar_dark", "colorPrimary", 2, 2));
        items.add(new StringColorIconItem("Tarkat Tiedot", "", "", "ic_info_light", "colorPrimary", 5, 2));

        recyclerView.setLayoutManager(glm);
        itemAdapter = new ItemAdapter<>();
        fastAdapter = FastAdapter.with(itemAdapter);
        recyclerView.setAdapter(fastAdapter);
        itemAdapter.clear();
        itemAdapter.add(items);

        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return glm.getSpanCount() / ((StringColorIconItem)items.get(position)).getSpanSize();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        return true;
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
    public void onSearchChosen(ExtraChosenEvent event) {
        int type = event.getItemType();

        switch (type){
            case 1:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityReservationDetailsOld.this);
                alertDialogBuilder.setTitle(getResources().getString(R.string.notification));

                alertDialogBuilder
                        .setMessage(getResources().getString(R.string.confirm_reservation_hide) + "\n\n" + getResources().getString(R.string.help_hide_full_realization) + "\n\n" + getResources().getString(R.string.help_restore_reservation))
                        .setNeutralButton(getResources().getString(R.string.cancel),new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.dismiss();
                            }
                        })
                        .setCancelable(true)
                        .setNegativeButton(getResources().getString(R.string.hide),new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                Toasty.info(getApplicationContext(), getString(R.string.under_construction), Toast.LENGTH_SHORT, true).show();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();

                alertDialog.show();
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(16);
                alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextSize(16);
                break;
            case 2:
                if (!reservationOld.getRoomCode().get(0).equals("")){
                    Toasty.info(this, getString(R.string.under_construction), Toast.LENGTH_SHORT, true).show();
                } else {
                    Toasty.info(this, getString(R.string.no_room_code), Toast.LENGTH_SHORT, true).show();
                }
                break;
            case 3:
                Toasty.info(this, getString(R.string.under_construction), Toast.LENGTH_SHORT, true).show();
                break;
            case 4:
                Toasty.info(this, getString(R.string.under_construction), Toast.LENGTH_SHORT, true).show();
                break;
            case 5:
                Toasty.info(this, getString(R.string.under_construction), Toast.LENGTH_SHORT, true).show();
                break;
        }
    }
}
