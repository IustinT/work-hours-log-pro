package uk.co.trotus.workrecordspro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.joda.time.DateTime;

public class MainActivity extends BaseActivity {
    static DateTime punchInDate = new DateTime().withSecondOfMinute(0).withMillisOfSecond(0);
    static DateTime punchOutDate = new DateTime(punchInDate);

    static Button punchButton;
    static Button punchInDateBtn;
    static Button punchInHourBtn;
    static Button cancelPunchBtn;
    static boolean punchedIn;

    DatabaseHelper db;

    public void PunchButton_OnClick(View v) {
        if (punchedIn)
            PunchOut();
        else PunchIn();
    }

    //region Punch In Out
    void PunchIn() {
        punchInDate = new DateTime().withSecondOfMinute(0).withMillisOfSecond(0);
        punchedIn = true;
        ToggleButtonsStateAndText();
    }

    void PunchOut() {
        punchOutDate = new DateTime().withSecondOfMinute(0).withMillisOfSecond(0);
        punchedIn = false;
        ToggleButtonsStateAndText();


        Intent intent = new Intent(this, NewShift.class);
        intent.putExtra("makeNewShiftFromPunch", true);
        intent.putExtra("punchInDate", DateTimeToString(punchInDate));
        intent.putExtra("punchOutDate", DateTimeToString(punchOutDate));
        startActivity(intent);

        //ToggleButtonsStateAndText();
    }
    //endregion Punch In Out

    void ToggleButtonsStateAndText() {
        if (punchedIn) {
            SetButtonsVisibility(View.VISIBLE);
            punchButton.setText("Punch Out");

            punchInDateBtn.setText(MakeTextForDateButtons(punchInDate));
            punchInHourBtn.setText(MakeTextForHourButtons(punchInDate));
        } else {
            SetButtonsVisibility(View.GONE);
            punchButton.setText("Punch In");
        }
    }

    void SetButtonsVisibility(int visibility) {
        if (punchInDateBtn.getVisibility() != visibility)
            punchInDateBtn.setVisibility(visibility);

        if (punchInHourBtn.getVisibility() != visibility)
            punchInHourBtn.setVisibility(visibility);

        if (cancelPunchBtn.getVisibility() != visibility)
            cancelPunchBtn.setVisibility(visibility);
    }

    public void CancelPunch(View v) {
        punchedIn = false;
        ToggleButtonsStateAndText();
    }

    //region Select time
    public void PunchSelectTime(View v) {
        int id = v.getId();
        if (id == R.id.punchInHourBtn)
            showTimePickerDialog("punchin", punchInDate);
        else ; // TODO: 08/08/2015 Analytics
    }

    public void CatchReturnedHour(int hour, int minute) {
        String tag = timePickerDialog.getTag();

        if (tag.equals("punchin")) {
            punchInDate = punchInDate.withTime(hour, minute, 0, 0);
            punchInHourBtn.setText(MakeTextForHourButtons(punchInDate));
        }
    }
    //endregion select time

    //region Select Date
    public void PunchSelectDate(View v) {
        int id = v.getId();
        if (id == (R.id.punchInDateBtn))
            showDatePickerDialog("punchin", punchInDate);
        else ;  //// TODO: 08/08/2015 Analytics
    }

    public void CatchReturnedDate(int year, int month, int day) {
        String tag = datePickerDialog.getTag();

        if (tag.equals("punchin")) {
            punchInDate = punchInDate.withDate(year, month + 1, day);
            punchInDateBtn.setText(MakeTextForDateButtons(punchInDate));
        }

    }
    //endregion select Date

    void InitialiseVariables() {
        punchButton = (Button) findViewById(R.id.punchBtn);
        punchInDateBtn = (Button) findViewById(R.id.punchInDateBtn);
        punchInHourBtn = (Button) findViewById(R.id.punchInHourBtn);
        cancelPunchBtn = (Button) findViewById(R.id.cancelPunchBtn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitialiseVariables();
        ToggleButtonsStateAndText();

        db = new DatabaseHelper(getApplicationContext());
    }

}