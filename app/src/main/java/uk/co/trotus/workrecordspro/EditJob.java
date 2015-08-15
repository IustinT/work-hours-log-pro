package uk.co.trotus.workrecordspro;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class EditJob extends BaseActivity {

    static Job job = new Job();
    static EditText jobNameEditText;
    static EditText hourlyPayEditText;
    static EditText notesEditText;
    static CheckBox jobEnabledCheckBox;
    // Database Helper
    static DatabaseHelper db;

    public void SaveJobButton_OnClick(View v) {
        SaveJob(job);
    }

    void LoadAJob(int jobID) {
        Job job = db.getJob(jobID);
        PopulateJob(job);
    }
    void PopulateJob(Job job){
        jobNameEditText.setText(job.getName());
        jobEnabledCheckBox.setChecked(job.getEnable());
        hourlyPayEditText.setText(Double.toString(job.getWagesPerHour()));
        notesEditText.setText(job.getNotes());
    }

    void SaveJob(Job job) {

        String jobName = jobNameEditText.getText().toString();
        String notes = notesEditText.getText().toString();
        Double hourlyPay;

        if (!jobName.isEmpty())
            job.setName(jobName);
        else {
            ShowToast("The Name field is empty!", context);
            return;
        }

        job.setEnabled(jobEnabledCheckBox.isChecked());

        if (!hourlyPayEditText.getText().toString().isEmpty())
            hourlyPay = Double.parseDouble(hourlyPayEditText.getText().toString());
        else {
            ShowToast("The Hourly Pay field is empty!", context);
            return;
        }
        job.setWagesPerHour(hourlyPay);

        if (!notes.isEmpty())
            job.setNotes(notes);

        db = new DatabaseHelper(getApplicationContext());
        db.createJob(job);

    }

    void InitializeVariables() {
        context = getApplicationContext();
        jobNameEditText = (EditText) findViewById(R.id.jobNameEditText);
        hourlyPayEditText = (EditText) findViewById(R.id.hourlyPayEditText);
        notesEditText = (EditText) findViewById(R.id.notesEditText);
        jobEnabledCheckBox = (CheckBox) findViewById(R.id.jobEnabledCheckBox);
        db= new DatabaseHelper(getApplicationContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_job);
        InitializeVariables();

        if (loadASpecificJob && jobIDToBeLoaded > 0) {
            loadASpecificJob=false;
            LoadAJob(jobIDToBeLoaded);
            jobIDToBeLoaded=0;
        }
    }
}
