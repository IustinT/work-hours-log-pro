package uk.co.trotus.workrecordspro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

public class EditJob extends BaseActivity implements AdapterView.OnItemSelectedListener {

    //region declare variables
    static Job job = new Job();
    static EditText jobNameEditText;
    static EditText newPayRateAmount;
    static EditText notesEditText;
    static CheckBox jobEnabledCheckBox;
    static LinearLayout SelectNewPayRateLayout;
    static Button SelectJobPayRateStartDateBtn;

    static Spinner payRatesSpinner;
    static SpinnerAdapter payRatesSpinnerAdapter;
    static ArrayList<PayRate> payRates;

    static ListView payRatesHistoryList;
    static ListAdapter jobPayRatesHistoryListAdapter;
    static ArrayList<Job_PayRate> job_PayRatesHistory;

    static DateTime job_PayRate_StartDate = new DateTime().withMillisOfDay(0);
    // Database Helper
    static DatabaseHelper db;

    //endregion


    void LoadAJob(int jobID) {
        this.job = db.getJob(jobID);
        int jobPayid = db.getJob_PayRateId(job.getID());
        job.payRate = db.getPayRate(jobPayid);
        DisplayJobFields(job, payRates);
        findViewById(R.id.jobDeleteBtn).setVisibility(View.VISIBLE);

        job_PayRatesHistory = db.getJob_PayRateHistory(job.getID());
        AddPayRatesHistoryToList(job_PayRatesHistory, payRates);

    }

    void DisplayJobFields(Job job, ArrayList<PayRate> payRates) {
        jobNameEditText.setText(job.getName());
        jobEnabledCheckBox.setChecked(job.getEnable());

        notesEditText.setText(job.getNotes());
        for (PayRate payRate : payRates) {
            if (payRate.getId() == job.payRate.getId())
                payRatesSpinner.setSelection(payRates.indexOf(payRate), true);
        }

    }

    public void SaveJob(View v) {
        SaveJob(job, job_PayRate_StartDate);
    }

    void SaveJob(Job job, DateTime job_PayRate_StartDate) {
        long newPayRateId = 0;
        long payRateStart=job_PayRate_StartDate.getMillis();

        if (ValidateUserInputForJob() == false)
            return;

        String jobName = jobNameEditText.getText().toString();
        String notes = notesEditText.getText().toString();
        Double newHourlyPay;

        job.setName(jobName);

        job.setEnabled(jobEnabledCheckBox.isChecked());

        job.setNotes(notes);

        if (SelectNewPayRateLayout.getVisibility() == View.VISIBLE) {
            newHourlyPay = Double.parseDouble(newPayRateAmount.getText().toString());

            job.payRate = new PayRate();
            job.payRate.setHourlyRate(newHourlyPay);
            newPayRateId = db.createPayRate(job.payRate);
        }

        if (job.getID() > 0) {
            db.updateJob(job);
            ShowToast("The Job has been Updated!", getApplicationContext());
        } else {
            job.setID((int) db.createJob(job));
            ShowToast("The Job has been saved!", getApplicationContext());
        }

        if (newPayRateId > 0)
            db.createJob_PayRate(job.getID(), newPayRateId, payRateStart);
        else if (job.payRate != null)
            db.createJob_PayRate(job.getID(), job.payRate.getId(), payRateStart);

        GoBackToManageJobs();
    }

    boolean ValidateUserInputForJob() {

        if (jobNameEditText.getText().toString().isEmpty()) {
            ShowToast("The Name field is empty!", context);
            return false;
        }
        if (SelectNewPayRateLayout.getVisibility() == View.VISIBLE)
            if (findViewById(R.id.newPayRateAmountLayout).getVisibility() == View.VISIBLE)
                if (newPayRateAmount.getText().toString().isEmpty()) {
                    ShowToast("The Pay Rate field is empty!", context);
                    return false;
                }

        return true;
    }

    public void DeleteJob(View v) {
        if (job.getID() > 0)
            db.deleteJOB(job.getID());
        // TODO: 15/08/2015 analytics job ID < 1

        GoBackToManageJobs();
    }

    void AddPayRatesToSpinner(ArrayList<PayRate> listOfPayRates) {
        int counter = 0;
        String[] payRatesNames = new String[listOfPayRates.size() + 1];

        for (PayRate payRate : listOfPayRates) {
            payRatesNames[counter] = Currency.getInstance(Locale.getDefault()) + " " + payRate.getHourlyRate();

            counter++;
        }

        payRatesNames[counter] = "Register a New Pay Rate";

        payRatesSpinnerAdapter = new ArrayAdapter<>
                (getApplicationContext(),
                        R.layout.spinner_item,
                        payRatesNames);
        payRatesSpinner.setAdapter(payRatesSpinnerAdapter);

    }

    void AddPayRatesHistoryToList(ArrayList<Job_PayRate> payRatesHistory, ArrayList<PayRate> payRates) {
        int counter = 0;
        String[] payRatesNames = new String[payRatesHistory.size()];

        for (Job_PayRate job_PayRate : payRatesHistory) {
            for (PayRate payRate : payRates) {
                if (payRate.getId() == job_PayRate.getPayRateID()) {

                    payRatesNames[counter] = Currency.getInstance(Locale.getDefault())
                            + " " + payRate.getHourlyRate()
                            + "  " + MakeSimpleTextForDateButtons(new DateTime(job_PayRate.getStartDate()));
                    break;
                }
            }
            counter++;
        }
        jobPayRatesHistoryListAdapter = new ArrayAdapter<>
                (getApplicationContext(),
                        android.R.layout.simple_list_item_1,
                        payRatesNames);
        payRatesHistoryList.setAdapter(jobPayRatesHistoryListAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

        if (adapterView.getId() == R.id.payRatesSpinner) {
            if ((position + 1) == adapterView.getCount()) {
                //ReadNewPay();
                DisplayNewPayAmountLayout();
            } else {
                HideNewPayAmountLayout();
                if (payRates.size() > 0) {
                    job.payRate = new PayRate();
                    job.payRate = payRates.get(position);
                }
            }
        } else
            ShowToast("Click on the list", getApplicationContext());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        ShowToast("Nothing selected", getApplicationContext());
    }

    //region Display / Hide Layouts for Adding new Pay Rates
    private void ReadPayRateForNewJobs() {
        ReadNewPay();
        findViewById(R.id.CancelNewPayBtn).setVisibility(View.GONE);
    }

    void ReadNewPay() {
        SelectNewPayRateLayout.setVisibility(View.VISIBLE);
        findViewById(R.id.PayRateStartLayout).setVisibility(View.VISIBLE);

        findViewById(R.id.ReadNewPayBtn).setVisibility(View.GONE);
        findViewById(R.id.CancelNewPayBtn).setVisibility(View.VISIBLE);
    }

    public void ReadNewPay(View v) {
        ReadNewPay();
    }


    public void HideNewPaySelectLayout(View v) {
        findViewById(R.id.CancelNewPayBtn).setVisibility(View.GONE);
        HideNewPayAmountLayout();
        SelectNewPayRateLayout.setVisibility(View.GONE);
        findViewById(R.id.PayRateStartLayout).setVisibility(View.GONE);

        findViewById(R.id.ReadNewPayBtn).setVisibility(View.VISIBLE);
    }

    private void HideNewPayAmountLayout() {
        newPayRateAmount.setText("");
        findViewById(R.id.newPayRateAmountLayout).setVisibility(View.GONE);
    }

    private void DisplayNewPayAmountLayout() {
        if (SelectNewPayRateLayout.getVisibility() == View.VISIBLE)
            if (findViewById(R.id.newPayRateAmountLayout).getVisibility() != View.VISIBLE) {
                newPayRateAmount.setText("");
                findViewById(R.id.newPayRateAmountLayout).setVisibility(View.VISIBLE);
            }
    }
    //endregion

    public void Select_Job_PayRate_StartDate(View v) {

        if (v == (SelectJobPayRateStartDateBtn))
            showDatePickerDialog("job_PayRate_StartDate", job_PayRate_StartDate);
    }

    public void CatchReturnedDate(int year, int month, int day) {
        String tag = datePickerDialog.getTag();

        if (tag.equals("job_PayRate_StartDate")) {
            job_PayRate_StartDate=job_PayRate_StartDate.withDate(year, month + 1, day);
        }
        UpdateTextOnButtonsAndLabels();
    }

    private void UpdateTextOnButtonsAndLabels() {
        SelectJobPayRateStartDateBtn.setText(MakeTextForDateButtons(job_PayRate_StartDate));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_job);
        InitializeVariables();
        AddPayRatesToSpinner(payRates);
        UpdateTextOnButtonsAndLabels();

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();

            if (extras != null) {
                if (extras.getBoolean("loadASpecificJob") && (extras.getInt("jobIDToBeLoaded") > 0)) {
                    job.setID(extras.getInt("jobIDToBeLoaded"));
                    LoadAJob(job.getID());
                }
            }else if (job.getID() > 0)
                job = new Job();
        }

        if (job.getID() == 0)
            ReadPayRateForNewJobs();
    }

    void InitializeVariables() {
        context = getApplicationContext();
        jobNameEditText = (EditText) findViewById(R.id.jobNameEditText);
        notesEditText = (EditText) findViewById(R.id.notesEditText);
        jobEnabledCheckBox = (CheckBox) findViewById(R.id.jobEnabledCheckBox);
        SelectNewPayRateLayout = (LinearLayout) findViewById(R.id.SelectNewPayRateLayout);
        newPayRateAmount = (EditText) findViewById(R.id.newPayRateAmount);
        SelectJobPayRateStartDateBtn = (Button) findViewById(R.id.SelectJobPayRateStartDateBtn);

        payRatesSpinner = (Spinner) findViewById(R.id.payRatesSpinner);
        payRatesSpinner.setOnItemSelectedListener(this);

        payRatesHistoryList = (ListView) findViewById(R.id.jobPayRatesHistoryList);
        payRatesHistoryList.setOnItemSelectedListener(this);

        db = new DatabaseHelper(getApplicationContext());

        payRates = db.getAllPayRates(false);

        // TODO: 31/08/2015 analitycs no payrates available, error
    }

    void GoBackToManageJobs() {
        Intent intent = new Intent(this, ManageJobs.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}