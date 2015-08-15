package uk.co.trotus.workrecordspro;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.joda.time.DateTime;

public class NewShift extends BaseActivity {

    //region Declare Variables

    static TextView messages;
    static TextView hoursLabel;
    static EditText shiftNotes;

    static Button startDateBtn;
    static Button endDateBtn;
    static Button saveShiftBtn;

    static Button startTimeBtn;
    static Button endTimeBtn;

    static DateTime startDate = new DateTime().withSecondOfMinute(0).withMillisOfSecond(0);
    static DateTime endDate = new DateTime(startDate);

    static Job job;
    static Shift shift;
    //endregion

    void Stuff() {}

    //region Update the Text on buttons and labels
    void UpdateTextOnButtonsAndLabels() {
        startDateBtn.setText(MakeTextForDateButtons(shift.getStartDate()));
        endDateBtn.setText(MakeTextForDateButtons(shift.getEndDate()));

        startTimeBtn.setText(MakeTextForHourButtons(shift.getStartDate()));
        endTimeBtn.setText(MakeTextForHourButtons(shift.getEndDate()));

        if (shift.getStartDate().isBefore(shift.getEndDate().plusSeconds(1)))
            UpdateHoursLabel(shift.getMinutesInWork());
        else UpdateHoursLabel(-1);
    }

    void UpdateHoursLabel(int minutes) {

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
        } else ;// TODO: 05/08/2015 unpredicted situation

        hoursLabel.setText(text);
    }

    //endregion
    void EnableSaveButton(boolean newStage) {
        boolean stage = saveShiftBtn.isEnabled();

        if (stage != newStage)
            saveShiftBtn.setEnabled(newStage);
    }

    //region Select Date
    public void ShiftSelectDate(View v) {
        int id = v.getId();

        if (id == (R.id.shiftStartDateBtn))
            showDatePickerDialog("StartDate", startDate);
        else if (id == (R.id.shiftEndDateBtn))
            showDatePickerDialog("EndDate", endDate);
        else ;  //// TODO: 08/08/2015 Analytics
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
        InitialiseVariables();

        UpdateTextOnButtonsAndLabels();

        Stuff();
    }

    void InitialiseVariables() {

        if (makeNewShiftFromPunch) {
            makeNewShiftFromPunch = false;
            startDate = new DateTime(punchInDate);
            endDate = new DateTime(punchOutDate);
        } else {
            startDate = new DateTime().withSecondOfMinute(0).withMillisOfSecond(0);
            endDate = new DateTime(startDate);
        }
        job = new Job();
        shift = new Shift(startDate, endDate);
        shift.setJob(1); // TODO: 09/08/2015 add job
        shift.setWages(4);

        messages = (TextView) findViewById(R.id.messages);
        hoursLabel = (TextView) findViewById(R.id.totalHoursLabel);
        startDateBtn = (Button) findViewById(R.id.shiftStartDateBtn);
        endDateBtn = (Button) findViewById(R.id.shiftEndDateBtn);
        startTimeBtn = (Button) findViewById(R.id.shiftStartTimeBtn);
        endTimeBtn = (Button) findViewById(R.id.shiftEndTimeBtn);
        saveShiftBtn = (Button) findViewById(R.id.saveShiftBtn);
        shiftNotes = (EditText) findViewById(R.id.shiftNotes);
        context = getApplicationContext();

    }
}