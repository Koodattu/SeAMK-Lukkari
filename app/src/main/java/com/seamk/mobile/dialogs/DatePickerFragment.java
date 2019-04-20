package com.seamk.mobile.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import com.seamk.mobile.eventbusevents.DateEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

/**
 * Created by Juha Ala-Rantala on 27.8.2017.
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    int tag;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            tag = bundle.getInt("tag");
        }

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        EventBus.getDefault().post(new DateEvent(year, month + 1, day, tag));
    }
}