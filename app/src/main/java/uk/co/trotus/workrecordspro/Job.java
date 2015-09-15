package uk.co.trotus.workrecordspro;

import android.content.Context;

/**
 * Created by Iustin on 05/08/2015.
 */
public class Job {
    private int ID = 0;
    private Boolean enabled;
    private String name, notes;
    private double totalWages;
    public PayRate payRate;
    public Overtime Overtime = new Overtime(), Overtime2 = new Overtime();
    public Job() {
    }

    public Job(int ID) {
        this.ID = ID;
    }


    // getting ID
    public int getID() {
        return this.ID;
    }

    // setting id
    public void setID(int ID) {
        if (ID > 0)
            this.ID = ID;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean getEnable() {
        return this.enabled;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}