package uk.co.trotus.workrecordspro;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class NewShift extends BaseActivity implements AdapterView.OnItemSelectedListener {

    //region Declare Variables

    static TextView hoursLabel;
    static EditText shiftNotes;
    static Spinner jobsSpinner;

    static Button startDateBtn;
    static Button endDateBtn;
    static Button saveShiftBtn;

    static Button startTimeBtn;
    static Button endTimeBtn;
    DatabaseHelper db;

    //initialising the dates here is redundant but is probably safer
    static DateTime startDate = new DateTime().withSecondOfMinute(0).withMillisOfSecond(0);
    static DateTime endDate = new DateTime(startDate);

    static Job job;
    static Shift shift;

    SpinnerAdapter jobsSpinnerAdapter;
    ArrayList<Job> jobs;
    //endregion

    void Stuff() {
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        job=(jobs.get(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parentView) {
        ShowToast("Spinner On Nothing Selected", getApplicationContext()); //TODO analytics
    }

    public void SaveShift(View v) {
        SaveShift(shift);
    }

    void SaveShift(Shift shift) {

        int shiftId = db.createShift(shift);

    }

    void AddJobsToSpinner(List<Job> listOfJobs) {
        int counter = 0;
        String[] jobsNames = new String[listOfJobs.size()];

        for (Job job : listOfJobs) {
            jobsNames[counter] = job.getName();
            counter++;
        }
        jobsSpinnerAdapter = new ArrayAdapter<>
                (getApplicationContext(),
                        R.layout.spinner_item,
                        jobsNames);
        jobsSpinner.setAdapter(jobsSpinnerAdapter);
    }

    //region Update the Text on buttons and labels
    void UpdateTextOnButtonsAndLabels() {
        startDateBtn.setText(MakeTextForDateButtons(shift.getStartDate()));
        endDateBtn.setText(MakeTextForDateButtons(shift.getEndDate()));

        startTimeBtn.setText(MakeTextForHourButtons(shift.getStartDate()));
        endTimeBtn.setText(MakeTextForHourButtons(shift.getEndDate()));

        if (shift.getStartDate().isBefore(shift.getEndDate().plusSeconds(1)))
            UpdateHoursLabel(shift.getMinutesInWork());
        else UpdateHoursLabel(0);
    }

    void UpdateHoursLabel(int minutes) {
        // TODO: 23/08/2015 make this method in 2 differit methods
        int hours = 0;
        String errorText; //= context.getResources().getString(R.string.warning_shift_start_date_is_after_end_date);
        String hoursText;
        String minutesText;
        String text = "0m";

        if (minutes > 0) {
            text = "";

            hours = minutes / 60;
            hoursText = hours + "h ";

            minutes %= 60;
            minutesText = minutes + "m";

            if (hours > 0)
                text = hoursText;
            if (minutes > 0)
                text += minutesText;

            EnableSaveButton(true);

        } else if (minutes < 0) {
            EnableSaveButton(false);

            errorText = context.getResources().getString(R.string.warning_shift_start_date_is_after_end_date);
            ShowToast(errorText, context);

            text = 0 + " m";
        } else if (minutes == 0) {
            text = minutes + " m";
            EnableSaveButton(false);
        } // TODO: 05/08/2015 unpredicted situation

        hoursLabel.setText(text);
    }

    //endregion
    void EnableSaveButton(boolean newState) {
        boolean state = saveShiftBtn.isEnabled();

        if (state != newState)
            saveShiftBtn.setEnabled(newState);
    }

    //region Select Date
    public void ShiftSelectDate(View v) {
        int id = v.getId();

        if (id == (R.id.shiftStartDateBtn))
            showDatePickerDialog("StartDate", startDate);
        else if (id == (R.id.shiftEndDateBtn))
            showDatePickerDialog("EndDate", endDate);
         // TODO: 08/08/2015 Analytics error
    }

    public void CatchReturnedDate(int year, int month, int day) {
        String tag = datePickerDialog.getTag();

        if (tag.equals("StartDate")) {
            startDate = startDate.withDate(year, month + 1, day);
            shift.setStartDate(startDate);
        } else if (tag.equals("EndDate")) {
            endDate = endDate.withDate(year, month + 1, day);
            shift.setEndDate(endDate);
        }

        UpdateTextOnButtonsAndLabels();
    }
    //endregion Select Date


    //region Select Time
    public void ShiftSelectTime(View v) {
        int id = v.getId();

        if (id == (R.id.shiftStartTimeBtn))
            showTimePickerDialog("StartTime", startDate);

        else if (id == (R.id.shiftEndTimeBtn))
            showTimePickerDialog("EndTime", endDate);
    }

    public void CatchReturnedHour(int hour, int minute) {
        String tag = timePickerDialog.getTag();

        if (tag.equals("StartTime")) {
            startDate = startDate.withTime(hour, minute, 0, 0);
            shift.setStartDate(startDate);
        } else if (tag.equals("EndTime")) {
            endDate = endDate.withTime(hour, minute, 0, 0);
            shift.setEndDate(endDate);
        }
        UpdateTextOnButtonsAndLabels();
    }
    //endregion


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_shift);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                if (extras.getBoolean("makeNewShiftFromPunch")) {

                    startDate = new DateTime(
                            StringToDateTime(
                                    extras.getString("punchInDate")
                            ));
                    endDate = new DateTime(
                            StringToDateTime(
                                    extras.getString("punchOutDate")
                            ));
                }

            } else {
                startDate = new DateTime().withSecondOfMinute(0).withMillisOfSecond(0);
                endDate = new DateTime(startDate);
            }
        }

        InitialiseVariables();
        UpdateTextOnButtonsAndLabels();

        AddJobsToSpinner(jobs);
        Stuff();
    }

    void InitialiseVariables() {

        context = getApplicationContext();
        db = new DatabaseHelper(context);

        job = new Job();
        shift = new Shift(startDate, endDate);

        jobsSpinner = (Spinner) findViewById(R.id.jobsSpinner);
        jobsSpinner.setOnItemSelectedListener(this);

        hoursLabel = (TextView) findViewById(R.id.totalHoursLabel);
        startDateBtn = (Button) findViewById(R.id.shiftStartDateBtn);
        endDateBtn = (Button) findViewById(R.id.shiftEndDateBtn);
        startTimeBtn = (Button) findViewById(R.id.shiftStartTimeBtn);
        endTimeBtn = (Button) findViewById(R.id.shiftEndTimeBtn);
        saveShiftBtn = (Button) findViewById(R.id.saveShiftBtn);
        shiftNotes = (EditText) findViewById(R.id.shiftNotes);

        jobs = (ArrayList) db.getAllJobs();
    }
}