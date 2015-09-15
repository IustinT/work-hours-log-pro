package uk.co.trotus.workrecordspro;

import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

public class EditJob extends BaseActivity implements AdapterView.OnItemSelectedListener {

    //region declare veriables
    static Job job = new Job();
    static EditText jobNameEditText;
    static EditText newPayRateAmount;
    static EditText notesEditText;
    static CheckBox jobEnabledCheckBox;
    static Button saveJobBtn;
    static Button deleteJobButton;
    static Spinner payRatesSpinner;
    static LinearLayout newPayRateLayout;
    static Switch newPayRatePercent;

    static SpinnerAdapter payRatesSpinnerAdapter;
    static ArrayList<PayRate> payRates;
    // Database Helper
    static DatabaseHelper db;

    static int orientation;
    static int orientationCounter;
    //endregion

    public void SaveJobButton_OnClick(View v) {
        SaveJob(job);
    }

    void LoadAJob(int jobID) {
        this.job = db.getJob(jobID);
        int jobPayid=db.getJob_PayRateId(job.getID());
        job.payRate=db.getPayRate(jobPayid);
        DisplayJobFields(job, payRates);
        deleteJobButton.setVisibility(View.VISIBLE);
    }

    void DisplayJobFields(Job job, ArrayList<PayRate> payRates) {
        jobNameEditText.setText(job.getName());
        jobEnabledCheckBox.setChecked(job.getEnable());

        notesEditText.setText(job.getNotes());
        for (PayRate payRate : payRates) {
            if (payRate.getId()==job.payRate.getId())
               payRatesSpinner.setSelection(payRates.indexOf(payRate), true);
        }

    }

    void SaveJob(Job job) {
        long newPayRateId=0;

        if (ValidateUserInputForJob() == false)
            return;

        String jobName = jobNameEditText.getText().toString();
        String notes = notesEditText.getText().toString();
        Double newHourlyPay;

        job.setName(jobName);

        job.setEnabled(jobEnabledCheckBox.isChecked());

        job.setNotes(notes);

        if (newPayRateLayout.getVisibility()==View.VISIBLE) {
            newHourlyPay = Double.parseDouble(newPayRateAmount.getText().toString());

            job.payRate = new PayRate();

            job.payRate.setAmount(newHourlyPay);
            newPayRateId = db.createPayRate(job.payRate);
        }

        if (job.getID() > 0) {
            db.updateJob(job);
            ShowToast("The Job has been Updated!", getApplicationContext());
        } else {
            job.setID((int) db.createJob(job));
            ShowToast("The Job has been saved!", getApplicationContext());
        }

        if (newPayRateId>0)
            db.createJob_PayRate(job.getID(),newPayRateId);
        else if (job.payRate!=null)
            db.createJob_PayRate(job.getID(), job.payRate.getId());

        GoBackToManageJobs();
    }

    boolean ValidateUserInputForJob() {

        if (jobNameEditText.getText().toString().isEmpty()) {
            ShowToast("The Name field is empty!", context);
            return false;
        }
        if (newPayRateLayout.getVisibility() == View.VISIBLE)
            if (newPayRateAmount.getText().toString().isEmpty()) {
                ShowToast("The Pay Rate field is empty!", context);
                return false;
            }

        return true;
    }

    public void DeleteJob(View v) {
        if (job.getID() > 0)
            db.deleteJOB(job.getID());
        else ; //// TODO: 15/08/2015 analytics job ID < 1

        GoBackToManageJobs();

    }

    //    public void ReadNewPay() {
//        Bundle arguments = new Bundle();
//        arguments.putBoolean("readPercent", false);
//
//        DialogFragment payDialogFragment = new PayDialogFragment();
//        payDialogFragment.setArguments(arguments);
//        payDialogFragment.show(getFragmentManager(), "payDialogFragment");
//    }

    public void ReadNewPay() {
        newPayRateLayout.setVisibility(View.VISIBLE);
    }

    void GoBackToManageJobs() {
        Intent intent = new Intent(this, ManageJobs.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_job);
        InitializeVariables();
        AddPayRatesToSpinner(payRates);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                if (extras.getBoolean("loadASpecificJob"))
                    if (extras.getInt("jobIDToBeLoaded") > 0)
                        LoadAJob(extras.getInt("jobIDToBeLoaded"));

            } else if (job.getID() > 0)
                job = new Job();
        }
    }

    void InitializeVariables() {
        context = getApplicationContext();
        if (orientation == 0)
            orientation = context.getResources().getConfiguration().orientation;
        jobNameEditText = (EditText) findViewById(R.id.jobNameEditText);
        notesEditText = (EditText) findViewById(R.id.notesEditText);
        jobEnabledCheckBox = (CheckBox) findViewById(R.id.jobEnabledCheckBox);
        saveJobBtn = (Button) findViewById(R.id.saveJobBtn);
        deleteJobButton = (Button) findViewById(R.id.jobDeleteBtn);
        newPayRateLayout = (LinearLayout) findViewById(R.id.newPayRateLayout);
        newPayRateAmount = (EditText) findViewById(R.id.newPayRateAmount);

        payRatesSpinner = (Spinner) findViewById(R.id.payRatesSpinner);
        payRatesSpinner.setOnItemSelectedListener(this);

        db = new DatabaseHelper(getApplicationContext());

        payRates = db.getAllPayRates(false);
        // TODO: 31/08/2015 analitycs no payrates available, error
    }

    void AddPayRatesToSpinner(ArrayList<PayRate> listOfPayRates) {
        int counter = 0;
        String[] payRatesNames = new String[listOfPayRates.size() + 1];

        for (PayRate payRate : listOfPayRates) {
            payRatesNames[counter] = Currency.getInstance(Locale.getDefault()) + " " + payRate.getAmount();

            counter++;
        }

        payRatesNames[counter] = "Add a New Pay Rate";

        payRatesSpinnerAdapter = new ArrayAdapter<>
                (getApplicationContext(),
                        R.layout.spinner_item,
                        payRatesNames);
        payRatesSpinner.setAdapter(payRatesSpinnerAdapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

//        if (orientation != getApplicationContext().getResources().getConfiguration().orientation) {
//            orientation = getApplicationContext().getResources().getConfiguration().orientation;
//            orientationCounter = 1;
//        }
//
//        if (orientationCounter > 0) {
//            orientationCounter -= 1;
//            return;
//        }
//
//        if (position  == 0)
//            return;

        if ((position + 1) == adapterView.getCount())
            ReadNewPay();
        else {
            HideNewPayFields();
            if (payRates.size() > 0) {
                job.payRate = new PayRate();
                job.payRate = payRates.get(position);
            }
        }
    }

    private void HideNewPayFields() {
        if (newPayRateLayout.getVisibility()==View.VISIBLE)
            newPayRateAmount.setText("");
        newPayRateLayout.setVisibility(View.GONE);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        ShowToast("Nothing selected", getApplicationContext());
    }
}