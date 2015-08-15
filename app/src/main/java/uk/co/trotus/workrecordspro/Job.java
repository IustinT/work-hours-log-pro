package uk.co.trotus.workrecordspro;

/**
 * Created by Iustin on 05/08/2015.
 */
public class Job {
    int ID;
    Boolean enabled;
    String name, notes;
    double wagesPerHour, totalWages;

    public Job(){
    }

    public Job(int ID){
        this.ID=ID;
    }
    public Job(int ID, String name){
        this.ID=ID;
        this.name=name;

    }
    public Job(int ID, String name, double wagesPerHour){
        this.ID=ID;
        this.name=name;
        this.wagesPerHour = wagesPerHour;
    }
    public Job(int ID, String name, double wagesPerHour, String notes){
        this.ID=ID;
        this.name=name;
        this.wagesPerHour = wagesPerHour;
        this.notes = notes;
    }
    public Job(String name, double wagesPerHour, String notes){
        this.ID=ID;
        this.name=name;
        this.wagesPerHour = wagesPerHour;
        this.notes = notes;
    }

    // getting ID
    public int getID() {
        return this.ID;
    }

    // setting id
    public void setID(int ID) {
        if(ID>0)
        this.ID = ID;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean getEnable () {
        return this.enabled;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public double getWagesPerHour() {
        return this.wagesPerHour;
    }

    public void setWagesPerHour(double wagesPerHour) {
        this.wagesPerHour = wagesPerHour;
    }
    public double getTotalWages() {
        return this.totalWages;
    }

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}
