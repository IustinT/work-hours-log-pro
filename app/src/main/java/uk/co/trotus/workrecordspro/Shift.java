package uk.co.trotus.workrecordspro;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;

/**
 * Created by Iustin on 05/08/2015.
 */
public class Shift {
    private int ID, job, minutesInWork, breakMinutes;
    private DateTime startDate, endDate;
    private double wages, wagesPerHour;
    private Boolean isHolidayPay;
    private String notes;

    public Shift() {
    }

    public Shift(DateTime startDate, DateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Shift(DateTime startDate, DateTime endDate, int breakMinutes) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.breakMinutes = breakMinutes;
    }

    public Shift(int ID, DateTime startDate, DateTime endDate) {
        this.ID = ID;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Shift(int ID, int job, DateTime startDate, DateTime endDate) {
        this.ID = ID;
        this.job = job;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Shift(int ID, int job, DateTime startDate, DateTime endDate, Boolean isHolidayPay, String notes) {
        this.ID = ID;
        this.job = job;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isHolidayPay = isHolidayPay;
        this.notes = notes;
    }

    // getting ID
    public int getID() {
        return this.ID;
    }

    // setting id
    public void setID(int ID) {
        this.ID = ID;
    }

    // getting the job
    public int getJob() {
        return this.job;
    }

    // setting the job
    public void setJob(int job) {
        this.job = job;
    }

    public DateTime getStartDate() {
        return this.startDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    public DateTime getEndDate() {
        return this.endDate;
    }

    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }

    public int getMinutesInWork() {
        minutesInWork = CalculateDatePeriod(startDate, endDate).getMinutes();
        return this.minutesInWork;
    }

    public int getBreakMinutes() {
        return this.breakMinutes;
    }

    public void setBreakMinutes(int breakMinutes) {
        this.breakMinutes = breakMinutes;
    }

    public double getWages() {
        return this.wages;
    }

    public void setWages(double wages) {
        this.wages = wages;
    }

    public double getWagesPerHour() {
        return this.wagesPerHour;
    }

    public void setWagesPerHour(double wagesPerHour) {
        this.wagesPerHour = wagesPerHour;
    }

    public boolean getHolidayPay() {
        return this.isHolidayPay;
    }

    public void setHolidayPay(boolean isHolidayPay) {
        this.isHolidayPay = isHolidayPay;
    }

    private Period CalculateDatePeriod(DateTime startDate, DateTime endDate) {

        if ((startDate.equals(endDate))
                || startDate.isBefore(endDate))

            return new Period(startDate, endDate.plusSeconds(36), PeriodType.minutes());

        return new  Period(startDate, endDate, PeriodType.minutes());
    }
}