package com.seamk.mobile.timetable;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seamk.mobile.R;
import com.seamk.mobile.objects.ReservationOld;


/**
 * Created by Juha Ala-Rantala on 22.8.2017.
 */

public class FragmentReservationDetails extends Fragment {

    ReservationOld reservationOld;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_reservation_info, container, false);

        TextView subject = v.findViewById(R.id.TV_subject_data);
        TextView description = v.findViewById(R.id.TV_description_data);
        TextView startDate = v.findViewById(R.id.TV_start_date_data);
        TextView endDate = v.findViewById(R.id.TV_end_date_data);
        TextView roomCode = v.findViewById(R.id.TV_room_code_data);
        TextView roomName = v.findViewById(R.id.TV_room_name_data);
        TextView buildingCode = v.findViewById(R.id.TV_building_code_data);
        TextView buildingName = v.findViewById(R.id.TV_building_name_data);
        TextView realizationCode = v.findViewById(R.id.TV_realization_code_data);
        TextView realizationName = v.findViewById(R.id.TV_realization_name_data);

        subject.setText(reservationOld.getSubject());
        description.setText(reservationOld.getDescription());
        startDate.setText(reservationOld.getStartDate());
        endDate.setText(reservationOld.getEndDate());
        roomCode.setText(reservationOld.getRoomCode().get(0));
        roomName.setText(reservationOld.getRoomName().get(0));
        buildingCode.setText(reservationOld.getBuildingCode().get(0));
        buildingName.setText(reservationOld.getBuildingName().get(0));
        realizationCode.setText(reservationOld.getRealizationCode().get(0));
        realizationName.setText(reservationOld.getRealizationName().get(0));

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getArguments();
        reservationOld = extras.getParcelable("reservationOld");
    }
}
