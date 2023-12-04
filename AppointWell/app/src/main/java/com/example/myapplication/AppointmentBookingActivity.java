package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class AppointmentBookingActivity extends AppCompatActivity {
    DatabaseReference timeSlotDB = FirebaseDatabase.getInstance().getReference().child("Available Time Slots");
    DatabaseReference approvedDB = FirebaseDatabase.getInstance().getReference().child("Users").child("Approved Users");
    private ArrayList<TimeSlot> timeSlots = new ArrayList<>();
    private RecyclerView recyclerView;
    private SearchView searchView;
    private SearchAppointmentAdapter adapter;
    private TextView searchForTV;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_booking);
        recyclerView = findViewById(R.id.recyclerview);
        searchView = findViewById(R.id.searchView);
        searchForTV = findViewById(R.id.searchForText);
        back = findViewById(R.id.backtomainpage);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AppointmentBookingActivity.this, MainPagePatient.class);
                startActivity(intent);
                finish();
            }
        });



        timeSlotDB.keepSynced(true);
        approvedDB.keepSynced(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchForTV.setVisibility(View.GONE);
                timeSlots.clear();
                findTimeSlots(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });



    }

    public void findTimeSlots(String specialty) {
        adapter = new SearchAppointmentAdapter(this, timeSlots, specialty, FirebaseAuth.getInstance().getCurrentUser().getUid());
        recyclerView.setAdapter(adapter);


        timeSlotDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                timeSlots.clear();
                if(snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        if (child.exists()) {
                            TimeSlot timeSlot = child.getValue(TimeSlot.class);
                            if(timeSlot != null) {
                                approvedDB.child(timeSlot.getDoctorID()).child("specialties").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            for (DataSnapshot child : snapshot.getChildren()) {
                                                String sp = child.getValue(String.class);
                                                if (sp.equals(specialty)) {
                                                    boolean alreadyAdded=false;
                                                    for (int i=0;i<timeSlots.size();i++){
                                                        if(timeSlot.getDoctorID()==timeSlots.get(i).getDoctorID() && timeSlot.getStartTime()==timeSlots.get(i).getStartTime() && timeSlot.getDate()==timeSlots.get(i).getDate()){
                                                            alreadyAdded=true;
                                                            break;
                                                        }
                                                    }
                                                    if (!alreadyAdded) {
                                                        timeSlots.add(timeSlot);
                                                        adapter.notifyDataSetChanged();
                                                    }
                                                    break;
                                                }
                                            }

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}