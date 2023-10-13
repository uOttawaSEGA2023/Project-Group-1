package com.example.appointwellapp;

public class Patient extends Account{
    private String firstName, lastName,address, areaCode;
    private int healthCardNumber, phoneNumber;

    public Patient(String email, String firstName, String lastName, String password, String address, int healthCardNumber, int phoneNumber) {
        super(email, password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.healthCardNumber = healthCardNumber;
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public int getHealthCardNumber() {
        return healthCardNumber;
    }

    public void setHealthCardNumber(int healthCardNumber) {
        this.healthCardNumber = healthCardNumber;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


}

