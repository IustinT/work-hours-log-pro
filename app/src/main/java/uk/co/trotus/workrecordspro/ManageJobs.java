package uk.co.trotus.workrecordspro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ManageJobs extends BaseActivity implements AdapterView.OnItemClickListener {
    static Button newJobBtn;
    static ListView jobsList;
    static DatabaseHelper db;

    ListAdapter jobsListAdapter;
    ArrayList<Job> jobs;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        jobIDToBeLoaded = jobs.get(position).getID();
        loadASpecificJob=true;
        startActivity(new Intent(this, EditJob.class));
    }

    void DisplayJobsList(ListView listView, List<Job> listOfJobs) {
        int counter = 0;
        String[] jobNameAndPay = new String[listOfJobs.size()];

        for (Job element : listOfJobs) {
            jobNameAndPay[counter] = element.getName();
            counter++;
        }

        jobsListAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, jobNameAndPay);
        listView.setAdapter(jobsListAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_jobs);
        InitializeVariables();
        DisplayJobsList(jobsList, jobs);
    }

    void InitializeVariables() {
        context = getApplicationContext();
        newJobBtn = (Button) findViewById(R.id.addNewJobBtn);
        jobsList = (ListView) findViewById(R.id.jobsListView);

        jobsList.setOnItemClickListener(this);
        db = new DatabaseHelper(context);
        jobs = (ArrayList) db.getAllJobs();
    }
}