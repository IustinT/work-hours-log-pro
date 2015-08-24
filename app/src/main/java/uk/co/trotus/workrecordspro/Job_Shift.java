package uk.co.trotus.workrecordspro;

/**
 * Created by Iustin on 23/08/2015.
 */
public class Job_Shift {
    private int ID, jobID, shiftID =0;

    public Job_Shift(){    }
    public Job_Shift(int jobID, int shiftID){ setJobID(jobID); setShiftID(shiftID);   }
    public Job_Shift(int jobID, int shiftID, int ID){ setJobID(jobID); setShiftID(shiftID);  setID(ID); }

    public void setJobID(int jobID){
        this.jobID = jobID;
    }

    public int getJobID(){
        return this.jobID;
    }

    public void setShiftID(int shiftID){
        this.shiftID = shiftID;
    }

    public int getShiftID(){
        return this.shiftID;
    }

    public void setID(int ID){
        this.ID = ID;
    }

    public int getID(){
        return this.ID;
    }
}
