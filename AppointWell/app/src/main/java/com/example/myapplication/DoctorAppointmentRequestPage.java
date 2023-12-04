package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DoctorAppointmentRequestPage extends AppCompatActivity {

    MyAdapter myAdapter;
    ArrayList<AppointmentRequest> list;
    Button pending, approved, completed;
    Switch switchAutoApprove;
    boolean pendingSelected = true;
    String autoApprove;
    FirebaseAuth mAuth;
    TextView autoApproveText;
    ImageButton logoutDoctor;


    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_appointment);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        pending = findViewById(R.id.pending);
        approved = findViewById(R.id.approved);
        list = new ArrayList<>();
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new MyAdapter(this,list);
        recyclerView.setAdapter(myAdapter);
        switchAutoApprove = findViewById(R.id.switchAutoApprove);
        autoApprove = "false";
        completed = findViewById(R.id.completed);
        mAuth = FirebaseAuth.getInstance();
        String uID = mAuth.getCurrentUser().getUid();
        autoApproveText = findViewById(R.id.autoApproveTextid);
        logoutDoctor = findViewById(R.id.logOutDoctor);

        logoutDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainPageDoctor.class);
                startActivity(intent);
                finish();
            }
        });


        String databaseUrl = "https://new-database-b712b-default-rtdb.firebaseio.com/";

        // Reference to the "Appointments" node
        DatabaseReference appointmentsRef = FirebaseDatabase.getInstance(databaseUrl).getReference("Users").child("Approved Users").child(uID).child("appointmentRequests");

        DatabaseReference db =  FirebaseDatabase.getInstance(databaseUrl).getReference("Users").child("Approved Users").child(uID).child("AutoApprove");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                autoApprove = String.valueOf(snapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        if (pendingSelected == true){
            switchAutoApprove.setVisibility(View.VISIBLE);
            autoApproveText.setVisibility(View.VISIBLE);
            switchAutoApprove.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (compoundButton.isChecked()){
                        autoApprove = "true";
                        db.setValue("true");

                        appointmentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    if (dataSnapshot.child("status").exists() && dataSnapshot.child("status").getValue() != null) {
                                        if (dataSnapshot.child("status").getValue().equals("Pending")) {
                                            dataSnapshot.child("status").getRef().setValue("Approved");
                                        }
                                    }
                                }
                                // Notify the adapter after updating the statuses
                                list.clear();
                                myAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }
                    else{
                        autoApprove = "false";
                        db.setValue("false");
                    }
                }
            });
        }




        // Add a ValueEventListener to retrieve data
        appointmentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appointmentSnapshot : dataSnapshot.getChildren()) {
                    // Convert each child of "Appointments" to an AppointmentRequest object
//                    AppointmentRequest appointment = appointmentSnapshot.getValue(AppointmentRequest.class);
                    String date = String.valueOf(appointmentSnapshot.child("date").getValue());
                    String endTime = String.valueOf(appointmentSnapshot.child("endTime").getValue());
                    String startTime = String.valueOf(appointmentSnapshot.child("startTime").getValue());
                    String patientName = String.valueOf(appointmentSnapshot.child("patientName").getValue());
                    String status = String.valueOf(appointmentSnapshot.child("status").getValue());
                    String patientUID = String.valueOf(appointmentSnapshot.child("patientUID").getValue());
                    String countID = String.valueOf(appointmentSnapshot.child("countID").getValue());
                    AppointmentRequest request = new AppointmentRequest(patientName,patientUID,status,startTime,endTime,date, FirebaseAuth.getInstance().getCurrentUser().getUid());
                    request.setCountID(Integer.valueOf(countID));
                    if (status.equals("Pending")){
                        list.add(request);
                    }
                }
                myAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("Error reading data: " + databaseError.getMessage());
            }

        });



        pending.setOnClickListener(new View.OnClickListener() {      // changes color on click
            @Override
            public void onClick(View v) {
                switchAutoApprove.setVisibility(View.VISIBLE);
                autoApproveText.setVisibility(View.VISIBLE);
                pendingSelected = true;
                pending.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                approved.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.toggle)));
                completed.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.toggle)));
               refreshList("Pending", uID);
            }
        });

        approved.setOnClickListener(new View.OnClickListener() {
            // changes color on click
            @Override
            public void onClick(View v) {
                switchAutoApprove.setVisibility(View.INVISIBLE);
                autoApproveText.setVisibility(View.INVISIBLE);
                pendingSelected = false;
                pending.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.toggle)));
                approved.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                completed.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.toggle)));
               refreshList("Approved", uID);
            }
        });

        completed.setOnClickListener(new View.OnClickListener() {      // changes color on click
            @Override
            public void onClick(View v) {
                switchAutoApprove.setVisibility(View.INVISIBLE);
                autoApproveText.setVisibility(View.INVISIBLE);
                pendingSelected = false;
                pending.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.toggle)));
                approved.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.toggle)));
                completed.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                refreshList("Completed", uID);
            }
        });





    }

    private void refreshList(String currentStatus, String uID){
        list.clear();
        // Firebase Realtime Database URL
        String databaseUrl = "https://new-database-b712b-default-rtdb.firebaseio.com/";

        // Reference to the "Appointments" node
        DatabaseReference appointmentsRef = FirebaseDatabase.getInstance(databaseUrl).getReference("Users").child("Approved Users").child(uID).child("appointmentRequests");


        // Add a ValueEventListener to retrieve data
        appointmentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appointmentSnapshot : dataSnapshot.getChildren()) {
                    // Convert each child of "Appointments" to an AppointmentRequest object
//                    AppointmentRequest appointment = appointmentSnapshot.getValue(AppointmentRequest.class);
                    String date = String.valueOf(appointmentSnapshot.child("date").getValue());
                    String endTime = String.valueOf(appointmentSnapshot.child("endTime").getValue());
                    String startTime = String.valueOf(appointmentSnapshot.child("startTime").getValue());
                    String patientName = String.valueOf(appointmentSnapshot.child("patientName").getValue());
                    String status = String.valueOf(appointmentSnapshot.child("status").getValue());
                    String patientUID = String.valueOf(appointmentSnapshot.child("patientUID").getValue());
                    String countID = String.valueOf(appointmentSnapshot.child("countID").getValue());
                    AppointmentRequest request = new AppointmentRequest(patientName,patientUID,status,startTime,endTime,date,FirebaseAuth.getInstance().getCurrentUser().getUid());
                    request.setCountID(Integer.valueOf(countID));
                    if (status.equals(currentStatus)){
                        list.add(request);
                    }
                }
                myAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("Error reading data: " + databaseError.getMessage());
            }

        });

    }

} 