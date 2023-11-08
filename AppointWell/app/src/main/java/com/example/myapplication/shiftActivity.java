
package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CalendarView;

import android.widget.Spinner;
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

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class shiftActivity extends AppCompatActivity {
    CalendarView calendarView;
    Spinner spinner1;
    Spinner spinner2;
    ArrayAdapter<CharSequence> adapter;
    Button create;
    int year, month, day;
    String startTime, endTime;
    String dateString;

    DatabaseReference database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://new-database-b712b-default-rtdb.firebaseio.com/");
    DatabaseReference userDatabase = database.child("Users").child("Approved Users").child("XvxJMNsAE1NNJGXsxZCE6xVz2dL2");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addshift);
        //set up the calendar
        calendarView = findViewById(R.id.calendarview);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int Year, int Month, int Day) {
                year = Year;
                month = Month + 1;
                day = Day;
                Toast.makeText(shiftActivity.this, "You select " + day + "/" + month + "/" + year, Toast.LENGTH_SHORT).show();
            }
        });
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
                startTime= spinner1.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //store the endTime in variable endTime
                endTime= spinner2.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        create = findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LocalDate currentDate = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    currentDate = LocalDate.now();
                }
                LocalDate selectedDate = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    selectedDate = LocalDate.of(year, month, day);
                }
                // Check if the selected date has passed
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (selectedDate.isBefore(currentDate)) {
                        Toast.makeText(shiftActivity.this, "The day has already passed", Toast.LENGTH_SHORT).show();
                    }else{
                      dateString = selectedDate.toString();
                    }
                }//check if endTime is before startTime
                if (endTime.compareTo(startTime)<0){
                    Toast.makeText(shiftActivity.this, "EndTime can't be before StartTime", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}


//                //Check if startTime and endTime are valid
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    if (isValidTimeFormat(startTime)) {
//                        autoCompleteTextView1.setText(startTime);
//                        sTime = LocalTime.parse(startTime, DateTimeFormatter.ofPattern("HH:mm"));
//                        shift.setStartTime(sTime);
//                    } else {
//                        // Invalid time format, display a message to the user
//                        Toast.makeText(shiftActivity.this, "Invalid StartTime format (HH:MM) worked hours between 08:00-17:00", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    if (isValidTimeFormat(endTime)) {
//                        autoCompleteTextView2.setText(endTime);
//                        eTime = LocalTime.parse(endTime, DateTimeFormatter.ofPattern("HH:mm"));
//                    } else {
//                        // Invalid time format, display a message to the user
//                        Toast.makeText(shiftActivity.this, "Invalid EndTime format (HH:MM) worked hours between 08:00-17:00", Toast.LENGTH_SHORT).show();
//                    }
//                    if (sTime != null && eTime != null && eTime.isBefore(sTime)) {
//                        Toast.makeText(shiftActivity.this, "EndTime can't be before StartTime", Toast.LENGTH_SHORT).show();
//                    } else
//                        shift.setEndTime(eTime);
//                }

               // get userId
//               FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//               if (currentUser != null) {
//                    String uID = currentUser.getUid();
//                String uID = "XvxJMNsAE1NNJGXsxZCE6xVz2dL2";
//                    userDatabase.child(uID).addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            if (!dataSnapshot.hasChild("shifts")) {
//                                // If it doesn't exist, create a "shifts" node.
//                                LocalDate selectedDate = shift.getSelectedDate();
//                                LocalTime startTime = shift.getStartTime();
//                                LocalTime endTime = shift.getEndTime();
//                                // Create the Shift object that will be stored in the database
//                                Shift newShift = new Shift(selectedDate, startTime, endTime);
//                                userDatabase.child(uID).child("shifts").push().setValue(newShift);
//                            } else {
//                                // Check for conflicts
//                                LocalDate date = shift.getSelectedDate();
//                                LocalTime startTime = shift.getStartTime();
//                                LocalTime endTime = shift.getEndTime();
//                                DatabaseReference shiftsRef = userDatabase.child(uID).child("shifts");
//
//                                shiftsRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot shiftsDataSnapshot) {
//                                        boolean hasConflict = false;
//
//                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                            for (DataSnapshot shiftSnapshot : shiftsDataSnapshot.getChildren()) {
//                                                Shift existingShift = shiftSnapshot.getValue(Shift.class);
//
//                                                if (existingShift != null) {
//                                                    LocalDate shiftDate = existingShift.getSelectedDate();
//                                                    LocalTime shiftStartTime = existingShift.getStartTime();
//                                                    LocalTime shiftEndTime = existingShift.getEndTime();
//
//                                                    if (isConflict(date, startTime, endTime, shiftDate, shiftStartTime, shiftEndTime)) {
//                                                        hasConflict = true;
//                                                        break;
//                                                    }
//                                                }
//                                            }
//
//                                            if (hasConflict) {
//                                                Toast.makeText(shiftActivity.this, "Shift conflicts", Toast.LENGTH_SHORT).show();
//                                            } else {
//                                                // Add the new shift since there are no conflicts.
//                                                Shift newShift = new Shift(date, startTime, endTime);
//                                                shiftsRef.push().setValue(newShift);
//                                                Toast.makeText(shiftActivity.this, "Shift successfully created", Toast.LENGTH_SHORT).show();
//                                            }
//                                            }
//                                        }
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError databaseError) {
//                                            // Handle any errors that may occur during the database operation.
//                                        }
//                                    });
//                                }
//                            }
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//                            // Handle any errors that may occur during the database operation.
//                        }
//                    });
//               }
////            }
//        });
//    }
//
////    //Method to validate the time format
////    private boolean isValidTimeFormat(String time) {
////        // Use regular expression to check for valid HH:mm format
////        String timeRegex = "^(08:00|08:30|09:00|09:30|10:00|10:30|11:00|11:30|12:00|12:30|13:00|13:30|14:00|14:30|15:00|15:30|16:00|16:30|17:00)$";
////        return time.matches(timeRegex);
////    }
//    private boolean isConflict(LocalDate date, LocalTime startTime, LocalTime endTime, LocalDate existingDate, LocalTime existingStartTime, LocalTime existingEndTime) {
//        // Check for date conflict
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && date.isEqual(existingDate)) {
//            // If the dates are the same, check for time conflict
//            if (startTime.isBefore(existingEndTime) && endTime.isAfter(existingStartTime)) {
//                // There is a conflict
//                return true;
//            } else if (startTime.equals(existingStartTime) || endTime.equals(existingEndTime)) {
//                // There is a conflict
//                return true;
//            }
//        }
//        return false;
//    }
//
//}
