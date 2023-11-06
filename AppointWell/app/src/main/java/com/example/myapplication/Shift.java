package com.example.myapplication;

import android.os.Build;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

public class Shift {
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate selectedDate;
    private LocalDate CurrentDate = null;
    public Shift() {
    }

    public Shift(LocalDate date, LocalTime startTime, LocalTime endTime) {

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

    public LocalDate getCurrentDate() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CurrentDate = LocalDate.now();
        }
        return CurrentDate;
    }
}



