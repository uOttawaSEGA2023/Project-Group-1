package com.example.myapplication;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Administrator extends Account{
    DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

    public Administrator(){
        super("admin@gmail.com","Adminpassword@2023", "Administrator", "Approved");
    }

    public void rejectRegistrant(UserAccount userAccount, String uID){
        userDatabase.child("Pending Users").child(uID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(userAccount.getType().equals("Patient")){
                        Patient patient  = snapshot.getValue(Patient.class);
                        patient.setStatus("Rejected");
                        userDatabase.child("Rejected Users").child(uID).setValue(patient);
                    }
                    else{
                        Doctor doctor = snapshot.getValue(Doctor.class);
                        doctor.setStatus("Rejected");
                        userDatabase.child("Rejected Users").child(uID).setValue(doctor);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        userDatabase.child("Pending Users").child(uID).removeValue();
    }

    public void approveRegistrant(UserAccount userAccount, String uID) {
        String statusBefore = userAccount.getStatus();
        userAccount.setStatus("Approved");
        String databasePath = "Pending Users";

        if (!(statusBefore.equals("Pending"))) {
            databasePath = "Rejected Users";
        }

        userDatabase.child(databasePath).child(uID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(userAccount.getType().equals("Patient")){
                        Patient patient = snapshot.getValue(Patient.class);
                        patient.setStatus("Approved");
                        userDatabase.child("Approved Users").child(uID).setValue(patient);
                    }
                    else{
                        Doctor doctor = snapshot.getValue(Doctor.class);
                        doctor.setStatus("Approved");
                        userDatabase.child("Approved Users").child(uID).setValue(doctor);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        userDatabase.child(databasePath).child(uID).removeValue();
    }
}
