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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class NewShift extends BaseActivity implements AdapterView.OnItemSelectedListener {

    //region Declare Variables

    static TextView hoursLabel;
    static TextView totalWagesLabel;

    static EditText shiftNotes;
    static Spinner jobsSpinner;

    static Button startDateBtn;
    static Button endDateBtn;
    static Button saveShiftBtn;

    static Button startTimeBtn;
    static Button endTimeBtn;
    static DatabaseHelper db;

    static Job job;
    static Shift shift;

    static SpinnerAdapter jobsSpinnerAdapter;
    static ArrayList<Job> jobs;
    //endregion

    void Stuff() {
    }

    void CalculateWages(Shift shift, Job job) {
        int minutesInWork = shift.getMinutesInWork();
        int overtime = 0, overtime2 = 0;
        double totalShiftWages;

        if (job.Overtime.minutesBeforeOvertime>0 && job.Overtime.minutesBeforeOvertime < minutesInWork) {
            if (job.Overtime2.minutesBeforeOvertime>0 && job.Overtime2.minutesBeforeOvertime < minutesInWork)
                overtime2 = minutesInWork - job.Overtime2.minutesBeforeOvertime;

            overtime = (minutesInWork - job.Overtime.minutesBeforeOvertime) - overtime2;
            minutesInWork -= (overtime + overtime2);
        }

        totalShiftWages = (overtime2/(double)60 * job.Overtime2.getHourlyRate())
                        + (overtime/(double)60 * job.Overtime.getHourlyRate())
                        + (minutesInWork/(double)60 * job.payRate.getHourlyRate());

        UpdateWagesLabel(totalShiftWages);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        job = (jobs.get(position));
        //job.
        CalculateWages(shift,job);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parentView) {
    }

    public void SaveShift(View v) {
        SaveShift(shift);
    }

    void SaveShift(Shift shift) {

        int shiftId = db.createShift(shift);
        db.createJob_Shift(shiftId, job.getID());

        CalculateWages(shift, job);

        ShowToast("Shift id = " + shiftId, getApplicationContext());

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

        UpdateHoursLabel(shift.getMinutesInWork());
        CalculateWages(shift,job);
    }

    void UpdateHoursLabel(int minutes) {
        // TODO: 23/08/2015 make this method in 2 differit methods
        int hours;
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

            text = 0 + " m";
            errorText = context.getResources().getString(R.string.warning_shift_start_date_is_after_end_date);
            ShowToast(errorText, context);

        } else if (minutes == 0) {
            text = minutes + " m";
            EnableSaveButton(false);
        } // TODO: 05/08/2015 unpredicted situation

        hoursLabel.setText(text);
    }

    void UpdateWagesLabel(double wages){
        totalWagesLabel.setText(
                Currency.getInstance(Locale.getDefault())
                        +" "+ String.format("%.2f", wages)
        );
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
            showDatePickerDialog("StartDate", shift.getStartDate());
        else if (id == (R.id.shiftEndDateBtn))
            showDatePickerDialog("EndDate", shift.getEndDate());
        // TODO: 08/08/2015 Analytics error
    }

    public void CatchReturnedDate(int year, int month, int day) {
        String tag = datePickerDialog.getTag();

        if (tag.equals("StartDate")) {
            shift.setStartDate(shift.getStartDate().withDate(year, month + 1, day));
        } else if (tag.equals("EndDate")) {
            shift.setEndDate(shift.getEndDate().withDate(year, month + 1, day));
        }

        UpdateTextOnButtonsAndLabels();
    }
    //endregion Select Date


    //region Select Time
    public void ShiftSelectTime(View v) {
        int id = v.getId();

        if (id == (R.id.shiftStartTimeBtn))
            showTimePickerDialog("StartTime", shift.getStartDate());

        else if (id == (R.id.shiftEndTimeBtn))
            showTimePickerDialog("EndTime", shift.getEndDate());
    }

    public void CatchReturnedHour(int hour, int minute) {
        String tag = timePickerDialog.getTag();

        if (tag.equals("StartTime")) {
            shift.setStartDate(shift.getStartDate().withTime(hour, minute, 0, 0));
        } else if (tag.equals("EndTime")) {
            shift.setEndDate(shift.getEndDate().withTime(hour, minute, 0, 0));
        }
        UpdateTextOnButtonsAndLabels();
    }
    //endregion


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_shift);

        DateTime startDate = new DateTime().withSecondOfMinute(0).withMillisOfSecond(0);
        DateTime endDate = new DateTime(startDate);

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
            }
        }

        InitialiseVariables(startDate, endDate);
        UpdateTextOnButtonsAndLabels();

        UpdateHoursLabel(shift.getMinutesInWork());

        AddJobsToSpinner(jobs);
        Stuff();
    }

    void InitialiseVariables(DateTime startDate, DateTime endDate) {

        context = getApplicationContext();
        db = new DatabaseHelper(context);

        job = new Job();
        shift = new Shift(startDate, endDate);

        jobsSpinner = (Spinner) findViewById(R.id.jobsSpinner);
        jobsSpinner.setOnItemSelectedListener(this);

        hoursLabel = (TextView) findViewById(R.id.totalHoursLabel);
        totalWagesLabel = (TextView) findViewById(R.id.totalWagesLabel);

        startDateBtn = (Button) findViewById(R.id.shiftStartDateBtn);
        endDateBtn = (Button) findViewById(R.id.shiftEndDateBtn);
        startTimeBtn = (Button) findViewById(R.id.shiftStartTimeBtn);
        endTimeBtn = (Button) findViewById(R.id.shiftEndTimeBtn);
        saveShiftBtn = (Button) findViewById(R.id.saveShiftBtn);
        shiftNotes = (EditText) findViewById(R.id.shiftNotes);

        jobs = db.getAllJobs();

    }
}