package uk.co.trotus.workrecordspro;

/**
 * Created by Iustin on 25/08/2015.
 */
public class PayRate {
    private int id;
    private double hourlyRate;
    private boolean isProcent;

    public PayRate(){
    }
    public PayRate(int id){
        this.id=id;

        //// TODO: 25/08/2015 read hourlyRate from database
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public double getHourlyRate() {
        return this.hourlyRate;
    }

    public void setIsProcent(boolean isProcent) {
        this.isProcent = isProcent;
    }

    public boolean getIsProcent() {
        return this.isProcent;
    }


}