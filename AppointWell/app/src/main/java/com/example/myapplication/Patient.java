package com.example.myapplication;

public class Patient extends UserAccount{
    private int healthCardNumber;
    public Patient(){}

    public Patient(String email, String firstName, String lastName, String password, String address, int healthCardNumber, int phoneNumber) {
        super(email, password, "Patient", "Pending", firstName, lastName, address, phoneNumber);
        this.healthCardNumber = healthCardNumber;
    }

    public int getHealthCardNumber() {
        return healthCardNumber;
    }

    public void setHealthCardNumber(int healthCardNumber) {
        this.healthCardNumber = healthCardNumber;
    }
}

