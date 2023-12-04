package com.example.myapplication;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Doctor extends UserAccount {
    private long employeeNumber;
    private DatabaseReference approvedDB = FirebaseDatabase.getInstance().getReference().child("Users").child("Approved Users");

    private List<String> specialties;
    private List<Shift> shifts;
    private boolean autoAcceptsRequests;
    private ArrayList<AppointmentRequest> appointmentRequests;

    public Doctor() {
    }

    public Doctor(String email, String password, String firstName, String lastName, String address, long employeeNumber, long phoneNumber, List<String> specialties) {
        super(email, password, "Doctor", "Pending", firstName, lastName, address, phoneNumber);
        this.employeeNumber = employeeNumber;
        this.specialties = specialties;
        shifts = new ArrayList<Shift>();
        appointmentRequests = new ArrayList<AppointmentRequest>();
    }

    public List<Shift> getShifts() {
        return shifts;
    }

    public void setShifts(List<Shift> shifts) {
        this.shifts = shifts;
    }

    public void deleteShift(Shift shift) {
        shifts.remove(shift);
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

    public ArrayList<AppointmentRequest> getAppointmentRequests() {
        return appointmentRequests;
    }

    public void setAppointmentRequests(ArrayList<AppointmentRequest> appointmentRequests) {
        this.appointmentRequests = appointmentRequests;
    }

    public boolean isAutoAcceptsRequests() {
        return autoAcceptsRequests;
    }

    public void setAutoAcceptsRequests(boolean autoAcceptsRequests) {
        this.autoAcceptsRequests = autoAcceptsRequests;
    }

    public void addUpcomingAppointment(String doctorUID, AppointmentRequest appointmentRequest) {
//        //remove this!!!!
        if (appointmentRequests==null){
            appointmentRequests=new ArrayList<AppointmentRequest>();
        }
//        //up to here!!!
        appointmentRequests.add(appointmentRequest);
        approvedDB.child(doctorUID).child("appointmentRequests").setValue(appointmentRequests);

    }

    public void removeUpcomingAppointment(String doctorUID, AppointmentRequest appointmentRequest) {
        for (AppointmentRequest request : appointmentRequests) {
            if (request.getDate().equals(appointmentRequest.getDate()) && request.getStartTime().equals(appointmentRequest.getStartTime()) &&
                    request.getPatientUID().equals(appointmentRequest.getPatientUID()) && request.getDoctorUID().equals(appointmentRequest.getDoctorUID())){
                appointmentRequests.remove(request);
                break;
        }
    }
        approvedDB.child(doctorUID).child("appointmentRequests").setValue(appointmentRequests);

    }

    public void approveAppointment(String doctorUID, AppointmentRequest appointmentRequest){
        for (AppointmentRequest request: appointmentRequests){
            if (request.getDate().equals(appointmentRequest.getDate()) && request.getStartTime().equals(appointmentRequest.getStartTime()) &&  request.getPatientUID().equals(appointmentRequest.getPatientUID()) &&
                    request.getDoctorUID().equals(appointmentRequest.getDoctorUID())){
//                AppointmentRequest temp = request;
//                appointmentRequests.remove(request);
//                temp.setStatus("Approved");
//                appointmentRequests.add(temp);
                request.setStatus("Approved");
                break;

            }
        }
        approvedDB.child(doctorUID).child("appointmentRequests").setValue(appointmentRequests);
    }

    public void completeAppointment(String doctorUID, AppointmentRequest appointmentRequest){
        for (AppointmentRequest request: appointmentRequests){
            if (request.getDate().equals(appointmentRequest.getDate()) && request.getStartTime().equals(appointmentRequest.getStartTime()) &&  request.getPatientUID().equals(appointmentRequest.getPatientUID()) &&
                    request.getDoctorUID().equals(appointmentRequest.getDoctorUID())){
//                AppointmentRequest temp = request;
//                appointmentRequests.remove(request);
//                temp.setStatus("Approved");
//                appointmentRequests.add(temp);
                request.setStatus("Completed");
                break;

            }
        }
        approvedDB.child(doctorUID).child("appointmentRequests").setValue(appointmentRequests);
    }


}