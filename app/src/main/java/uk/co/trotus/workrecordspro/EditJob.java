package uk.co.trotus.workrecordspro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class EditJob extends BaseActivity {

    static Job job = new Job();
    static EditText jobNameEditText;
    static EditText hourlyPayEditText;
    static EditText notesEditText;
    static CheckBox jobEnabledCheckBox;
    static Button saveJobBtn;
    static Button deleteJobButton;
    // Database Helper
    static DatabaseHelper db;

    public void SaveJobButton_OnClick(View v) {
        SaveJob(job);
    }

    void LoadAJob(int jobID) {
        this.job = db.getJob(jobID);
        DisplayJobFields(job);
        deleteJobButton.setVisibility(View.VISIBLE);
    }

    void DisplayJobFields(Job job) {
        jobNameEditText.setText(job.getName());
        jobEnabledCheckBox.setChecked(job.getEnable());
        hourlyPayEditText.setText(Double.toString(job.getWagesPerHour()));
        notesEditText.setText(job.getNotes());
    }

    void SaveJob(Job job) {

        if (ValidateUserInputForJob() == false)
            return;

        String jobName = jobNameEditText.getText().toString();
        String notes = notesEditText.getText().toString();
        Double hourlyPay;

        job.setName(jobName);

        job.setEnabled(jobEnabledCheckBox.isChecked());
        hourlyPay = Double.parseDouble(hourlyPayEditText.getText().toString());

        job.setWagesPerHour(hourlyPay);

        if (!notes.isEmpty())
            job.setNotes(notes);

        if (job.getID() > 0) {
            db.updateJob(job);
            ShowToast("The Job has been Updated!", getApplicationContext());
        } else {
            db.createJob(job);
            ShowToast("The Job has been saved!", getApplicationContext());
        }
        GoBackToManageJobs();
    }

    boolean ValidateUserInputForJob() {

        if (jobNameEditText.getText().toString().isEmpty()) {
            ShowToast("The Name field is empty!", context);
            return false;
        }
        if (hourlyPayEditText.getText().toString().isEmpty()) {
            ShowToast("The Hourly Pay field is empty!", context);
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

    void GoBackToManageJobs() {
        Intent intent = new Intent(this, ManageJobs.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    void InitializeVariables() {
        context = getApplicationContext();
        jobNameEditText = (EditText) findViewById(R.id.jobNameEditText);
        hourlyPayEditText = (EditText) findViewById(R.id.hourlyPayEditText);
        notesEditText = (EditText) findViewById(R.id.notesEditText);
        jobEnabledCheckBox = (CheckBox) findViewById(R.id.jobEnabledCheckBox);
        saveJobBtn = (Button) findViewById(R.id.saveJobBtn);
        deleteJobButton = (Button) findViewById(R.id.jobDeleteBtn);

        db = new DatabaseHelper(getApplicationContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_job);
        InitializeVariables();

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                if (extras.getBoolean("loadASpecificJob"))
                    if (extras.getInt("jobIDToBeLoaded") > 0)
                        LoadAJob(extras.getInt("jobIDToBeLoaded"));
                ;
            } else if (job.getID() > 0)
                job = new Job();
        }
    }
}