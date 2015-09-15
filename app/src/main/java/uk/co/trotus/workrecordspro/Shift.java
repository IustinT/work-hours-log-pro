package uk.co.trotus.workrecordspro;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;

public class Shift {
    private int ID, minutesInWork, breakMinutes, overtime, overtime2 = 0;
    private DateTime startDate, endDate;
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

    public Shift(int ID, DateTime startDate, DateTime endDate, Boolean isHolidayPay, String notes) {
        this.ID = ID;
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

    public boolean getHolidayPay() {
        return this.isHolidayPay;
    }

    public void setHolidayPay(boolean isHolidayPay) {
        this.isHolidayPay = isHolidayPay;
    }

    private Period CalculateDatePeriod(DateTime startDate, DateTime endDate) {

        if (startDate.isBefore(endDate.plusSeconds(1)))
            return new Period(startDate, endDate.plusSeconds(37), PeriodType.minutes());

        return new Period(startDate, endDate, PeriodType.minutes());
    }
}