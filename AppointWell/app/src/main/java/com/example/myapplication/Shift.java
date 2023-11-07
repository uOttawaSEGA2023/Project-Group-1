package com.example.myapplication;

import android.os.Build;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

public class Shift {
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate selectedDate;

    public Shift() {
    }
    public Shift(LocalDate selectedDate, LocalTime startTime, LocalTime endTime) {
        this.selectedDate=selectedDate;
        this.startTime=startTime;
        this.endTime=endTime;
    }
    public void setStartTime(LocalTime startTime) {
            this.startTime = startTime;
    }
    public LocalTime getStartTime() {
        return startTime;
    }
    public void setEndTime(LocalTime endTime) {
            this.endTime= endTime ;
    }
    public LocalTime getEndTime() {
        return endTime;
    }

    public void setSelectedDate(LocalDate selectedDate) {
        this.selectedDate =  selectedDate;
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }
}



