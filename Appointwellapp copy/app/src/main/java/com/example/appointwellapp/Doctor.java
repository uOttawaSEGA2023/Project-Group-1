package com.example.appointwellapp;

public class Doctor extends Account{
    private String firstName, lastName, address, areaCode;
    private int employeeNumber, phoneNumber;
    private List<String> specialties;

    public Doctor(String email, String firstName, String lastName, String password, String address, String areaCode, List<String> specialties, int employeeNumber, int phoneNumber) {
        super(email, password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.areaCode = areaCode;
        this.specialties = specialties;
        this.employeeNumber = employeeNumber;
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