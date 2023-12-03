package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class upcomingShiftActivity extends AppCompatActivity {
    ImageButton addbtn, back;
    String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    ArrayList<Shift>  shiftDB=new ArrayList<Shift>();

//    String uID = "XvxJMNsAE1NNJGXsxZCE6xVz2dL2";

    LinearLayout shiftList;
    DatabaseReference approvedUserDB = FirebaseDatabase.getInstance().getReferenceFromUrl("https://new-database-b712b-default-rtdb.firebaseio.com/").child("Users").child("Approved Users");


    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_shift);
        addbtn = findViewById(R.id.plus);
        shiftList = findViewById(R.id.shiftslist);
        approvedUserDB.child(uID).child("shifts").keepSynced(true);
        refreshList();
        addbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(upcomingShiftActivity.this, shiftActivity.class);
                startActivity(intent);
            }
        });
        back = findViewById(R.id.backtomainpage);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(upcomingShiftActivity.this, MainPageDoctor.class);
                startActivity(intent);
            }
        });

    }
    private void addShift(Shift shift) {
        View shiftView = getLayoutInflater().inflate(R.layout.newshift, null, false);
        TextView Date = shiftView.findViewById(R.id.Date);

        DateTimeFormatter inputFormatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        }
        DateTimeFormatter outputFormatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            outputFormatter = DateTimeFormatter.ofPattern("d MMM yyyy");
        }
        LocalDate date = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            date = LocalDate.parse(shift.getSelectedDate(), inputFormatter);
        }
        String formattedDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formattedDate = date.format(outputFormatter);
        }

        Date.setText(formattedDate);

        TextView time = shiftView.findViewById(R.id.time);
        time.setText(shift.getStartTime() + " - " + shift.getEndTime());
        shiftList.addView(shiftView);


        ImageButton delete = (ImageButton) shiftView.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approvedUserDB.child(uID).child("shifts").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            shiftDB.clear(); // Clear the list before using it

                            for (DataSnapshot child : snapshot.getChildren()) {
                                Shift s = child.getValue(Shift.class);
                                if (!(s.getEndTime().equals(shift.getEndTime()) && s.getStartTime().equals(shift.getStartTime()) && s.getSelectedDate().equals(shift.getSelectedDate()))) {
                                    shiftDB.add(s);
                                } else {
                                    checkAppointment(shift);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error
                    }
                });
            }
        });
    }
    private void checkAppointment (Shift shift){
        ArrayList<AppointmentRequest> arr = new ArrayList<>();
        approvedUserDB.child(uID).child("appointmentRequests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    boolean cannotDeleteShift = false;

                    for (DataSnapshot child : snapshot.getChildren()) {
                        AppointmentRequest a = child.getValue(AppointmentRequest.class);
                        arr.add(a);
                    }

                    int i = 0;
                    while (!cannotDeleteShift && i < arr.size()) {
                        if (shift.getSelectedDate().equals(arr.get(i).getDate())) {
                            if (arr.get(i).getStartTime().compareTo(shift.getStartTime()) >= 0 || arr.get(i).getEndTime().compareTo(shift.getEndTime()) <= 0) {
                                // cannot delete shift
                                Toast.makeText(upcomingShiftActivity.this, "Cannot delete shifts", Toast.LENGTH_SHORT).show();
                                shiftDB.add(shift);
                                cannotDeleteShift = true;
                            }
                        }
                        i++;
                    }

                    // Update shifts node
                    if (!cannotDeleteShift) {
                        approvedUserDB.child(uID).child("shifts").setValue(shiftDB);
                        Toast.makeText(upcomingShiftActivity.this, "Shift successfully deleted", Toast.LENGTH_SHORT).show();
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            refreshList();
                        }
                    }, 500);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    public void refreshList() {

        DatabaseReference shiftsRef = approvedUserDB.child(uID).child("shifts");

        shiftsRef.orderByChild("selectedDate").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                shiftList.removeAllViews();
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        if (child != null) {
                            Shift shift = child.getValue(Shift.class);
                            addShift(shift);
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