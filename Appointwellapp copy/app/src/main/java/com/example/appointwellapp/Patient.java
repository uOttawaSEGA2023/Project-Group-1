package com.example.appointwellapp;

public class Patient {
    private String email, firstName, lastName, password,address, areaCode;
    private int healthCardNumber, phoneNumber;

    public Patient(String email, String firstName, String lastName, String password, String address, String areaCode, int healthCardNumber, int phoneNumber) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.address = address;
        this.areaCode = areaCode;
        this.healthCardNumber = healthCardNumber;
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

