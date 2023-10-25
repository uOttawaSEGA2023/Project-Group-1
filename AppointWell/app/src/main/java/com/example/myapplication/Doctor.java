package com.example.myapplication;

import java.util.List;

public class Doctor extends UserAccount{
    private long employeeNumber;
    private List<String> specialties;

    public Doctor(){}
    public Doctor(String email, String password, String firstName, String lastName, String address, long employeeNumber, long phoneNumber, List<String> specialties) {
        super(email, password, "Doctor", "Pending", firstName, lastName, address, phoneNumber);
        this.employeeNumber = employeeNumber;
        this.specialties = specialties;
    }

    public long getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(long employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public List<String> getSpecialties() {
        return specialties;
    }

    public void setSpecialties(List<String> specialties) {
        this.specialties = specialties;
    }


}