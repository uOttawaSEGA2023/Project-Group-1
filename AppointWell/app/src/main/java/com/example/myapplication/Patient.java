package com.example.myapplication;

public class Patient extends UserAccount{
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
}

