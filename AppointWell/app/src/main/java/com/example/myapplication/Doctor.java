package com.example.myapplication;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Doctor extends UserAccount {
    private long employeeNumber;
    private DatabaseReference approvedDB = FirebaseDatabase.getInstance().getReference().child("Users").child("Approved Users");

    private List<String> specialties;

    private float rating;
    private int numRating;
    private List<Shift> shifts;
    private boolean autoAcceptsRequests;
    private ArrayList<AppointmentRequest> appointmentRequests;


    private ArrayList<TimeSlot> availableTimeSlots;

    public ArrayList<TimeSlot> getAvailableTimeSlots() {
        return availableTimeSlots;
    }

    public void setAvailableTimeSlots(ArrayList<TimeSlot> availableTimeSlots) {
        this.availableTimeSlots = availableTimeSlots;
    }

    public Doctor() {
    }

    public Doctor(String email, String password, String firstName, String lastName, String address, long employeeNumber, long phoneNumber, List<String> specialties) {
        super(email, password, "Doctor", "Pending", firstName, lastName, address, phoneNumber);
        this.employeeNumber = employeeNumber;
        this.specialties = specialties;
        shifts = new ArrayList<Shift>();
        appointmentRequests = new ArrayList<AppointmentRequest>();
        availableTimeSlots=new ArrayList<TimeSlot>();
        rating=0;
        numRating=0;
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

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getNumRating() {
        return numRating;
    }

    public void setNumRating(int numRating) {
        this.numRating = numRating;
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

    public void addAvailableTimeSlot(String doctorUID,TimeSlot ts){
        if (availableTimeSlots==null){
            availableTimeSlots=new ArrayList<TimeSlot>();
        }
        availableTimeSlots.add(ts);
        approvedDB.child(doctorUID).child("availableTimeSlots").setValue(availableTimeSlots);

    }

    public void removeAvailableTimeSlot(String doctorUID,TimeSlot ts){
        availableTimeSlots.remove(ts);
        approvedDB.child(doctorUID).child("availableTimeSlots").setValue(availableTimeSlots);
    }

    public float avgRating(){
        if (numRating==0){
            return 0;
        }
        return rating/numRating;
    }

    public void incrementNumRating(){
        numRating++;
    }
    public void increaseTotalRating(float r){
        rating+=r;
    }


}