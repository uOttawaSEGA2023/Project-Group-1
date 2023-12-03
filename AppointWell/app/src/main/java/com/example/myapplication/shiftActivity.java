
package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CalendarView;

import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class shiftActivity extends AppCompatActivity {
    List<Shift> doctorShiftsList;
    ArrayList<TimeSlot> availableTimeSlots;
    CalendarView calendarView;
    Spinner spinner1;
    Spinner spinner2;
    ArrayAdapter<CharSequence> adapter;
    Button create;
    int year, month, day;
    String startTime, endTime;
    String dateString;
    LocalTime currentTime;
    LinearLayout shiftList;
    ImageButton backto;

    String docName;
    LocalDate currentDate;
    String currentdate;

    DatabaseReference database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://new-database-b712b-default-rtdb.firebaseio.com/");
    DatabaseReference userDatabase = database.child("Users").child("Approved Users");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addshift);
        shiftList = findViewById(R.id.shiftslist);
        //set up the calendar
        calendarView = findViewById(R.id.calendarview);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int Year, int Month, int Day) {
                year = Year;
                month = Month + 1;
                day = Day;
                dateString = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month, day);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    currentDate = LocalDate.now();
                }
                currentdate = currentDate.toString();
                Toast.makeText(shiftActivity.this, "You select " + dateString, Toast.LENGTH_SHORT).show();
            }
        });
        backto = findViewById(R.id.backto);
        backto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(shiftActivity.this, upcomingShiftActivity.class);
                startActivity(intent);
            }
        });
        //set the startTime and endTime
        adapter = ArrayAdapter.createFromResource(this, R.array.hours, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1 = findViewById(R.id.spinner1);
        spinner1.setAdapter(adapter);
        spinner2 = findViewById(R.id.spinner2);
        spinner2.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //store the startTime in variable startTime
                startTime = spinner1.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //store the endTime in variable endTime
                endTime = spinner2.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        create = findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (dateString.compareTo(currentdate) < 0) {
                    Toast.makeText(shiftActivity.this, "The day has already passed- select another date", Toast.LENGTH_SHORT).show();
                    return;//avoid the app terminating
                } else if (dateString.compareTo(currentdate) == 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        currentTime = LocalTime.now();
                        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                        String currentTimeStr = currentTime.format(dateTimeFormatter);
                        if (endTime.compareTo(currentTimeStr) < 0 || startTime.compareTo(currentTimeStr) < 0) {
                            Toast.makeText(shiftActivity.this, "The current time is " + currentTimeStr + " the day has already passed- select another date", Toast.LENGTH_SHORT).show();
                            return;
                        }else{
                            Toast.makeText(shiftActivity.this, "Cannot create shift for the same date", Toast.LENGTH_SHORT).show();
                        }
                    }
                }//check if endTime is before startTime
                if (endTime.compareTo(startTime) < 0) {
                    Toast.makeText(shiftActivity.this, "EndTime can't be before StartTime", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (startTime.compareTo(endTime)==0) {
                    Toast.makeText(shiftActivity.this, "EndTime can't be equal to StartTime", Toast.LENGTH_SHORT).show();
                    return;
                }
                // get userId
               FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
               String uID = currentUser.getUid();

                //TRYING TO FIX
                //GETTING SHIFT LIST FROM DOCTOR
                userDatabase.child(uID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Doctor doctor = snapshot.getValue(Doctor.class);
                            if(doctor!=null){
                                doctorShiftsList =  doctor.getShifts();
                                if (doctorShiftsList==null){
                                    doctorShiftsList=new ArrayList<Shift>();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



               DatabaseReference shiftsRef = userDatabase.child(uID).child("shifts");

                shiftsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if (!dataSnapshot.exists()) {
//                            // "shifts" node doesn't exist, create it
//                            shiftsRef.setValue(""); // This will create the "shifts" node
//                        }
                        // Check for conflicts
                        shiftsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot shiftsDataSnapshot) {
                                boolean hasConflict = false;
                                for (DataSnapshot shiftSnapshot : shiftsDataSnapshot.getChildren()) {
                                    Shift existingShift = shiftSnapshot.getValue(Shift.class);
                                    if (existingShift != null) {
                                        String shiftDate = existingShift.getSelectedDate();
                                        String shiftStartTime = existingShift.getStartTime();
                                        String shiftEndTime = existingShift.getEndTime();
                                        hasConflict = isConflict(dateString, startTime, endTime, shiftDate, shiftStartTime, shiftEndTime);
                                        if (hasConflict) {
                                            break;
                                        }
                                    }
                                }
                                if (!hasConflict) {
                                    Shift newShift = new Shift(dateString, startTime, endTime);
                                    doctorShiftsList.add(newShift);
                                    shiftsRef.setValue(doctorShiftsList);
                                    //ADDED FOR DELIV 4
                                    userDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()){
                                                Doctor doctor=snapshot.getValue(Doctor.class);
                                                if (doctor!=null){
                                                    docName=doctor.getFirstName()+" "+ doctor.getLastName();
                                                    Log.d("NAME1", docName);
                                                    addTimeSlots(dateString,startTime,endTime,docName,FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                }
                                            }
                                        } 
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    Toast.makeText(shiftActivity.this, "Shift successfully created", Toast.LENGTH_SHORT).show();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            startActivity(new Intent(getApplicationContext(), upcomingShiftActivity.class));
                                        }
                                    }, 500); // Delayed navigation after showing the toast for 1 second
                                } else {
                                    Toast.makeText(shiftActivity.this, "Shift conflicts", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Handle any errors that may occur during the database operation.
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle any errors that may occur during the database operation.
                    }

                });
            }
        });

    }

    private void addTimeSlots(String dateString, String startTime, String endTime,String docName, String uid) {
        float start;
        float end;
        DatabaseReference tDB = database.child("Available Time Slots");
        Log.d("UID", uid);

        String timeSlotStart;
        String timeSlotEnd;
        start=Float.parseFloat(startTime.substring(0,2));
        if (Integer.parseInt(startTime.substring(3,5))==30){
            start+=0.5;
        }
        end=Float.parseFloat(endTime.substring(0,2));
        if (Integer.parseInt(endTime.substring(3,5))==30){
            end+=0.5;
        }
        List<TimeSlot> timeSlots = new ArrayList<>();
        for (float i=start;i<end;i+=0.5){
            if (i-Math.floor(i)!=0){
                timeSlotStart=String.valueOf((int)Math.floor(i))+":30";
                timeSlotEnd=String.valueOf((int)(i+0.5))+":00";
            } else {
                timeSlotStart=String.valueOf((int)i)+":00";
                timeSlotEnd=String.valueOf((int)i)+":30";
            }
            if (timeSlotStart.length()!=5){
                timeSlotStart="0"+timeSlotStart;
            }
            if (timeSlotEnd.length()!=5){
                timeSlotEnd="0"+timeSlotEnd;
            }

            TimeSlot t = new TimeSlot(timeSlotStart,timeSlotEnd,dateString,docName,uid);
            tDB.child(dateString+"-"+timeSlotStart+"-"+uid).setValue(t);
            timeSlots.add(t);
            //update the doctors available time slots
//            userDatabase.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if (snapshot.exists()){
//
//                        Doctor doctor= snapshot.getValue(Doctor.class);
//                        Log.d("abc", t.getDate());
//                        doctor.addAvailableTimeSlot(uid,t);
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
            // Update the doctor's available time slots
            userDatabase.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Doctor doctor = snapshot.getValue(Doctor.class);
                        for (TimeSlot timeSlot : timeSlots) {
                            doctor.addAvailableTimeSlot(uid, timeSlot);
                        }
                        // Update the doctor object in the database
                        userDatabase.child(uid).setValue(doctor);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }

    private boolean isConflict(String dateString, String startTime, String endTime, String existingDate, String existingStartTime, String existingEndTime) {
        // Check for date conflict
        if (dateString.compareTo(existingDate)==0) {
            // If the dates are the same, check for time conflict
            if (startTime.compareTo(existingEndTime)<0&& endTime.compareTo(existingStartTime)>0) {
                // There is a conflict
                return true;
            } else if (startTime.equals(existingStartTime) || endTime.equals(existingEndTime)) {
                // There is a conflict
                return true;
            }
        }
        return false;
    }



}
