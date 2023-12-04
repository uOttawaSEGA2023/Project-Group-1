package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;

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

public class PatientUpcomingAppointments extends AppCompatActivity {

    MyAdapter2 myAdapter;
    ArrayList<AppointmentRequest> list;
    FirebaseAuth mAuth;
    ImageButton logoutPatient;


    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_upcoming_appointments);
        RecyclerView recyclerView = findViewById(R.id.recyclerview2);
        list = new ArrayList<>();
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new MyAdapter2(this,list);
        recyclerView.setAdapter(myAdapter);
        mAuth = FirebaseAuth.getInstance();
        String uID = mAuth.getCurrentUser().getUid();
        logoutPatient = findViewById(R.id.logOutPatient);

        logoutPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainPagePatient.class);
                startActivity(intent);
                finish();
            }
        });


        String databaseUrl = "https://new-database-b712b-default-rtdb.firebaseio.com/";

        // Reference to the "Appointments" node
        DatabaseReference appointmentsRef = FirebaseDatabase.getInstance(databaseUrl).getReference("Users").child("Approved Users").child(uID).child("upcomingAppointments");

        DatabaseReference db =  FirebaseDatabase.getInstance(databaseUrl).getReference("Users").child("Approved Users").child(uID).child("AutoApprove");


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
                    String doctorUID = String.valueOf(appointmentSnapshot.child("doctorUID").getValue());
                    String countID = String.valueOf(appointmentSnapshot.child("countID").getValue());
                    AppointmentRequest request = new AppointmentRequest(patientName,patientUID,status,startTime,endTime,date, doctorUID);
                    request.setCountID(Integer.valueOf(countID));
                        list.add(request);

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