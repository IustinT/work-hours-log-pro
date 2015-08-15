package uk.co.trotus.workrecordspro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Iustin on 13/08/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    //region Define Variables
    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "WorkHoursPro";

    // Table Names
    private static final String TABLE_JOBS = "Jobs";
    private static final String TABLE_SHIFTS = "Shift";
    private static final String TABLE_JOBS_SHIFTS = "Jobs_Shifts";

    // Common column names
    private static final String KEY_ID = "ID";
    private static final String KEY_CREATED_AT = "created_at";

    // JOBS Table - column names
    private static final String KEY_NAME = "Name";
    private static final String KEY_ENABLED = "Enabled";

    // SHIFTS Table - column names
    private static final String KEY_START_DATE = "Start_date";
    private static final String KEY_END_DATE = "End_date";

    // JOBS_SHIFTS Table - column names
    private static final String KEY_JOB_ID = "Job_id";
    private static final String KEY_SHIFT_ID = "Shift_id";
    //endregion


    //region Table Create Statements
    // JOBS table create statement
    private static final String CREATE_TABLE_JOBS = "CREATE TABLE "
            + TABLE_JOBS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME
            + " TEXT," + KEY_ENABLED + " BOOLEAN NOT NULL CHECK (" + KEY_ENABLED + " IN (0,1))," + KEY_CREATED_AT
            + " DATETIME" + ")";

    // SHIFTS table create statement
    private static final String CREATE_TABLE_SHIFTS = "CREATE TABLE " + TABLE_SHIFTS
            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_START_DATE + " INTEGER,"
            + KEY_END_DATE + " INTEGER" + ")";

    // JOBS_SHIFTS table create statement
    //// TODO: COMPOUND KEY
    private static final String CREATE_TABLE_JOBS_SHIFTS = "CREATE TABLE "
            + TABLE_JOBS_SHIFTS + "("
            + KEY_JOB_ID + " INTEGER NOT NULL,"
            + KEY_SHIFT_ID + " INTEGER NOT NULL,"
            + "FOREIGN KEY (" + KEY_JOB_ID + ") REFERENCES "
            + TABLE_JOBS + "(" + KEY_ID + "), "
            + "FOREIGN KEY (" + KEY_SHIFT_ID + ") REFERENCES "
            + TABLE_SHIFTS + "(" + KEY_ID + ")"
            + "  )";
//endregion

    /*
 * Creating a JOB
 */
    public void createJob(Job job) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, job.getName());
        values.put(KEY_ENABLED, job.getEnable());

        // insert row
        db.insert(TABLE_JOBS, null, values);

    }

    /*
 * getting all JOBS
 * */
    public List<Job> getAllJobs() {
        List<Job> jobs = new ArrayList<Job>();
        String selectQuery = "SELECT  * FROM " + TABLE_JOBS;
        boolean enabled;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Job job = new Job();
                job.setID(cursor.getInt((cursor.getColumnIndex(KEY_ID))));
                job.setName((cursor.getString(cursor.getColumnIndex(KEY_NAME))));

                enabled = IntToBoolean(cursor.getInt(cursor.getColumnIndex(KEY_ENABLED)));
                job.setEnabled(enabled);

                // adding to jobs list
                jobs.add(job);
                Log.e(LOG, "Job from DB " + job.getID() + " " + job.getName() + " " + job.getEnable());

            } while (cursor.moveToNext());
            Log.e(LOG, jobs.toString());
        }
//closeDB();
        return jobs;
    }

    /*
 * get single JOB
 */
    public Job getJob(long job_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        boolean enabled;
        String selectQuery = "SELECT  * FROM " + TABLE_JOBS + " WHERE "
                + KEY_ID + " = " + job_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Job job = new Job();
        job.setID(c.getInt(c.getColumnIndex(KEY_ID)));
        job.setName((c.getString(c.getColumnIndex(KEY_NAME))));
        enabled = IntToBoolean(c.getInt(c.getColumnIndex(KEY_ENABLED)));
        job.setEnabled(enabled);

        Log.e(LOG, "Job id " + job.getID() + " name " + job.getName() + " enabled " + job.getEnable());

        return job;
    }

    Boolean IntToBoolean(int integer) {

        if (integer == 1)
            return (true);
        else if (integer == 0)
            return (false);
        //// TODO: 14/08/2015 Analytics wrong data in database
        return false;
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_JOBS);
        db.execSQL(CREATE_TABLE_SHIFTS);
        db.execSQL(CREATE_TABLE_JOBS_SHIFTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOBS_SHIFTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOBS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHIFTS);

        // create new tables
        onCreate(db);
    }
}