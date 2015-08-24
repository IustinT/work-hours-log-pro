package uk.co.trotus.workrecordspro;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.sql.Date;

public class BaseActivity extends Activity {

    static Context context;

    static DialogFragment datePickerDialog;
    static DialogFragment timePickerDialog;

    String DateToString(DateTime dateTime){
        return dateTime.toString("dd/MM/yyyy HH:mm");
    }

    DateTime StringToDateTime(String string_DateTime){
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
        return formatter.parseDateTime(string_DateTime);
    }

    //region Prepare text for buttons and labels
    String MakeTextForDateButtons(DateTime date) {
        DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("E d MMM yyyy");

        return dateFormatter.print(date);
    }

    String MakeTextForHourButtons(DateTime date) {
        DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("H : m");

        return dateFormatter.print(date);
    }

    //endregion

    //region Select Date

    public void showDatePickerDialog(String tag, DateTime date) {
        Bundle arguments = new Bundle();
        arguments.putString("date", DateToString(date));
        arguments.putString("tag", tag);

        datePickerDialog = new DatePickerFragment();
        datePickerDialog.setArguments(arguments);
        datePickerDialog.show(getFragmentManager(), tag);
    }
    //endregion

    //region Select Time


    public void showTimePickerDialog(String tag, DateTime date) {
        Bundle arguments = new Bundle();
        arguments.putString("date", DateToString(date));
        arguments.putString("tag", tag);

        timePickerDialog = new TimePickerFragment();
        timePickerDialog.setArguments(arguments);
        timePickerDialog.show(getFragmentManager(), tag);
    }

    //endregion

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else if (id == R.id.action_new_shift) {
            startActivity(new Intent(this, NewShift.class));
            return true;
        } else if (id == R.id.action_view_shift) {
            //startActivity(new Intent(this, ViewShift.class));
            return true;
        } else if (id == R.id.action_manage_jobs) {
            startActivity(new Intent(this, ManageJobs.class));
            return true;
        } else if (id == R.id.action_new_job) {
            startActivity(new Intent(this, EditJob.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void ShowToast(CharSequence message, Context context) {
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }

    public void OpenShiftActivity(View v) {
        startActivity(new Intent(this, NewShift.class));
    }
    public void OpenSettingsActivity(View v) {
        startActivity(new Intent(this, SettingsActivity.class));
    }
    public void OpenNewJobActivity(View v) {
        startActivity(new Intent(this, EditJob.class));
    }
}