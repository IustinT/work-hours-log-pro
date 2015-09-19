package uk.co.trotus.workrecordspro;

/**
 * Created by Iustin on 19/09/2015.
 */
public class Job_PayRate {
    long jobID, payRateID;
    long startDate;

    public Job_PayRate() {
    }

    public Job_PayRate(long jobID, long payRateID, long startDate) {
        this.jobID = jobID;
        this.payRateID = payRateID;
        this.startDate = startDate;
    }
    public void setJobID(long jobID){
        this.jobID=jobID;
    }
    public long getJobID(){
        return this.jobID;
    }

    public void setPayRateID(long payRateID){
        this.payRateID=payRateID;
    }
    public long getPayRateID(){
        return this.payRateID;
    }

    public void setStartDate(long startDate){
        this.startDate=startDate;
    }
    public long getStartDate(){
        return this.startDate;
    }
}