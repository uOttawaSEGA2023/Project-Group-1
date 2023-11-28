package com.example.myapplication;

import com.google.firebase.database.PropertyName;

public class AppointmentRequest {

    private String patientUID, status, startTime, endTime,date, patientName, doctorUID;

    public int getCountID() {
        return countID;
    }

    public void setCountID(int countID) {
        this.countID = countID;
    }

    private int countID;
    public AppointmentRequest(){}

    public AppointmentRequest(String patientName, String patientUID, String status, String startTime, String endTime, String date, String doctorUID) {
        setPatientUID(patientUID);
        setStatus(status);
        setPatientName(patientName);
        setStartTime(startTime);
        setEndTime(endTime);
        setDate(date);
        setDoctorUID(doctorUID);
    }

    @PropertyName("patientName")
    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }
    @PropertyName("patientUID")
    public String getPatientUID() {
        return patientUID;
    }

    public void setPatientUID(String patientUID) {
        this.patientUID = patientUID;
    }

    @PropertyName("status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @PropertyName("startTime")
    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @PropertyName("endTime")
    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @PropertyName("date")
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDoctorUID() {
        return doctorUID;
    }

    public void setDoctorUID(String doctorUID) {
        this.doctorUID = doctorUID;
    }
}
