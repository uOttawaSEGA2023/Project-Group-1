package com.example.myapplication;

import java.util.List;

public class Doctor extends Account{
    private String firstName, lastName, address;
    private int employeeNumber, phoneNumber;
    private List<String> specialties;

    public Doctor(String email, String password, String firstName, String lastName, String address, int employeeNumber, int phoneNumber, List<String> specialties) {
        super(email, password, "Doctor");
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.employeeNumber = employeeNumber;
        this.phoneNumber = phoneNumber;
        this.specialties = specialties;
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

    public int getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(int employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<String> getSpecialties() {
        return specialties;
    }

    public void setSpecialties(List<String> specialties) {
        this.specialties = specialties;
    }


}