package com.example.myapplication;

import android.os.Build;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

public class Shift {
    private String startTime;
    private String endTime;
    private String selectedDate;

    public Shift() {
    }
    public Shift(String selectedDate, String startTime, String endTime) {
        this.selectedDate=selectedDate;
        this.startTime=startTime;
        this.endTime=endTime;
    }
    public void setStartTime(String startTime) {
            this.startTime = startTime;
    }
    public String getStartTime() {
        return startTime;
    }
    public void setEndTime(String endTime) {
            this.endTime= endTime ;
    }
    public String getEndTime() {
        return endTime;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate =  selectedDate;
    }

    public String getSelectedDate() {
        return selectedDate;
    }
}



