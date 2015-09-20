package uk.co.trotus.workrecordspro;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;

/**
 * Created by Iustin on 01/08/2015.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    String tag;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        DateTimeFormatter formatter;
        formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
        String str = bundle.getString("date");
        DateTime dt = formatter.parseDateTime(str);
        tag = bundle.getString("tag");

        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();

        c.set(dt.getYear(), dt.getMonthOfYear() - 1, dt.getDayOfMonth());

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        if (tag == "punchin")
            new MainActivity().CatchReturnedDate(year, month, day);
        else if (tag == "job_PayRate_StartDate")
            new EditJob().CatchReturnedDate(year, month, day);
        else
            new NewShift().CatchReturnedDate(year, month, day);
    }
}