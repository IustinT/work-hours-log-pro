package uk.co.trotus.workrecordspro;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;

/**
 * Created by Iustin on 01/08/2015.
 */
public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {
    String tag;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
        String str = bundle.getString("date");
        DateTime dt = formatter.parseDateTime(str);
        tag = bundle.getString("tag");

        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();

        c.setTime(dt.toDate());

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        if (tag == "punchin") //// TODO: 08/08/2015 try catch
            new MainActivity().CatchReturnedHour(hourOfDay, minute);
        else
            new NewShift().CatchReturnedHour(hourOfDay, minute);
    }
}