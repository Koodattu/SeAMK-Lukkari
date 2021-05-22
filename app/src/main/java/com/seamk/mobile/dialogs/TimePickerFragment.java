package com.seamk.mobile.dialogs;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.widget.TimePicker;

import com.seamk.mobile.eventbusevents.TimeEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

/**
 * Created by Juha Ala-Rantala on 27.8.2017.
 */

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    int tag;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            tag = bundle.getInt("tag");
        }

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute, true);
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the date chosen by the user
        EventBus.getDefault().post(new TimeEvent(hourOfDay, minute, tag));
    }


}
