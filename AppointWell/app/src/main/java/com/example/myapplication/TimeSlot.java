package com.example.myapplication;

public class TimeSlot {
    private String startTime;
    private String endTime;
    private String date;
    private String doctorName;

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    private String doctorID;

    public TimeSlot(){}

    public TimeSlot(String startTime, String endTime, String date, String doctorName,String doctorID) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.doctorID=doctorID;
        this.doctorName = doctorName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
}
