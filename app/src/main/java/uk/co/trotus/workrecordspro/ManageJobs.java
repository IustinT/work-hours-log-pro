package uk.co.trotus.workrecordspro;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class ManageJobs extends BaseActivity {
    Button newJobBtn;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_jobs);
        InitializeVariables();
    }

    void InitializeVariables() {
        newJobBtn = (Button) findViewById(R.id.addNewJobBtn);
    }
}