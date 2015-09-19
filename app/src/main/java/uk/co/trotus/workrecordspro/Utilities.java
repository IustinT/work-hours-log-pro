package uk.co.trotus.workrecordspro;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Iustin on 04/08/2015.
 */
public class Utilities {
    public void CreateDefaultJob(Context context){
        DatabaseHelper db=new DatabaseHelper(context);
        Job job=new Job(0);
        job.setName("Default job");
        job.setEnabled(true);
        job.payRate.setHourlyRate(0);

        long jobID= db.createJob(job);
        long payRateID = db.createPayRate(job.payRate);
        db.createJob_PayRate(jobID, payRateID, 1000);

    }
}
