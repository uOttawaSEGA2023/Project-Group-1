package com.example.myapplication;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Patient extends UserAccount{
    private DatabaseReference approvedDB = FirebaseDatabase.getInstance().getReference().child("Users").child("Approved Users");
    private List<AppointmentRequest> upcomingAppointments = new ArrayList<>();
    private long healthCardNumber;
    public Patient(){}

    public Patient(String email, String firstName, String lastName, String password, String address, long healthCardNumber, long phoneNumber) {
        super(email, password, "Patient", "Pending", firstName, lastName, address, phoneNumber);
        this.healthCardNumber = healthCardNumber;
    }

    public long getHealthCardNumber() {
        return healthCardNumber;
    }

    public void setHealthCardNumber(long healthCardNumber) {
        this.healthCardNumber = healthCardNumber;
    }

    public List<AppointmentRequest> getUpcomingAppointments() {
        return upcomingAppointments;
    }

    public void setUpcomingAppointments(List<AppointmentRequest> upcomingAppointments) {
        this.upcomingAppointments = upcomingAppointments;
    }

    public void addUpcomingAppointment(String patientUID, AppointmentRequest appointmentRequest){
        upcomingAppointments.add(appointmentRequest);
        approvedDB.child(patientUID).child("upcomingAppointments").setValue(upcomingAppointments);
    }
}

