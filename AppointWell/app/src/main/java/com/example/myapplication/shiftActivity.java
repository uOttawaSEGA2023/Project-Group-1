
package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CalendarView;

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
    Shift shift = new Shift(); //Create an instance of the Shift class
    CalendarView calendarView;
    //Assume hours worked between 8.00-17.00: 24h format
    String[] hours = {"08:00","08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30","13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00"};
    AutoCompleteTextView autoCompleteTextView1;
    AutoCompleteTextView autoCompleteTextView2;
    TextInputLayout dropdown1; // To fill the text box with the selected time
    TextInputLayout dropdown2;
    ArrayAdapter<String> adapterItems1; //Contains only relevant time
    ArrayAdapter<String> adapterItems2;
    Button create;
    int year, month, day;
    FirebaseAuth mAuth;
    DatabaseReference database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://new-database-b712b-default-rtdb.firebaseio.com/");
    DatabaseReference userDatabase = database.child("Users").child("Approved Users");

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
                year= Year;
                month= Month+1;
                day= Day;
                Toast.makeText(shiftActivity.this, "You select " + day + "/" + month + "/" + year, Toast.LENGTH_SHORT).show();
            }
        });
        // Ask the user to select a start time and end time
        autoCompleteTextView1 = findViewById(R.id.autoCompleteTextView1);
        dropdown1 = findViewById(R.id.dropdown1);
        adapterItems1 = new ArrayAdapter<String>(this, R.layout.listhours, hours);
        autoCompleteTextView2 = findViewById(R.id.autoCompleteTextView2);
        dropdown2 = findViewById(R.id.dropdown2);
        adapterItems2= new ArrayAdapter<String>(this, R.layout.listhours, hours);
        autoCompleteTextView1.setAdapter(adapterItems1);
        autoCompleteTextView2.setAdapter(adapterItems2);

        create = findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LocalTime sTime = null;
                LocalTime eTime = null;
                String startTime = autoCompleteTextView1.getText().toString();
                String endTime = autoCompleteTextView2.getText().toString();
                LocalDate currentDate = shift.getCurrentDate();
                LocalDate selectedDate = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    selectedDate = LocalDate.of(year, month, day);
                }
                // Check if the selected date has passed
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (selectedDate.isBefore(currentDate)) {
                        Toast.makeText(shiftActivity.this, "The day has already passed", Toast.LENGTH_SHORT).show();
                    }
                }
                //Check if startTime and endTime are valid
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (isValidTimeFormat(startTime)) {
                        autoCompleteTextView1.setText(startTime);
                        sTime = LocalTime.parse(startTime, DateTimeFormatter.ofPattern("HH:mm"));
                        shift.setStartTime(sTime);
                    } else {
                        // Invalid time format, display a message to the user
                        Toast.makeText(shiftActivity.this, "Invalid StartTime format (HH:MM) worked hours between 08:00-17:00", Toast.LENGTH_SHORT).show();
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (isValidTimeFormat(endTime)) {
                        autoCompleteTextView2.setText(endTime);
                        eTime = LocalTime.parse(endTime, DateTimeFormatter.ofPattern("HH:mm"));
                    } else {
                        // Invalid time format, display a message to the user
                        Toast.makeText(shiftActivity.this, "Invalid EndTime format (HH:MM) worked hours between 08:00-17:00", Toast.LENGTH_SHORT).show();
                    }
                    if (sTime != null && eTime != null && eTime.isBefore(sTime)) {
                        Toast.makeText(shiftActivity.this, "EndTime can't be before StartTime", Toast.LENGTH_SHORT).show();
                    } else
                        shift.setEndTime(eTime);
                }

               // Check if user is signed in (non-null) as doctor.
               FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
               if (currentUser != null) {
                    String uID = currentUser.getUid();
                    userDatabase.child(uID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.hasChild("shifts")) {
                                // If it doesn't exist, create a "shift" node.
                                LocalDate selectedDate = shift.getSelectedDate();
                                LocalTime startTime = shift.getStartTime();
                                LocalTime endTime = shift.getEndTime();

                                //create the Shift object that will be stored in the database
                                Shift shifts = new Shift(selectedDate, startTime, endTime);
                                userDatabase.child(uID).setValue(shifts);
                            } else {
                                    // Check for conflicts
                                    LocalDate date = shift.getSelectedDate();
                                    LocalTime startTime = shift.getStartTime();
                                    LocalTime endTime = shift.getEndTime();

                                    DatabaseReference shiftsRef = userDatabase.child(uID).child("shifts");

                                    shiftsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot shiftsDataSnapshot) {
                                            boolean hasConflict = false;

                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                for (DataSnapshot shiftSnapshot : shiftsDataSnapshot.getChildren()) {
                                                    String shiftDate = shiftSnapshot.child("date").getValue(String.class);
                                                    String shiftStartTime = shiftSnapshot.child("startTime").getValue(String.class);
                                                    String shiftEndTime = shiftSnapshot.child("endTime").getValue(String.class);

                                                    if (shiftDate != null && shiftStartTime != null && shiftEndTime != null) {
                                                        try {
                                                            LocalDate parsedDate = LocalDate.parse(shiftDate);
                                                            LocalTime parsedStartTime = LocalTime.parse(shiftStartTime);
                                                            LocalTime parsedEndTime = LocalTime.parse(shiftEndTime);

                                                            if (isConflict(date, startTime, endTime, parsedDate, parsedStartTime, parsedEndTime)) {
                                                                hasConflict = true;
                                                                break;
                                                            }
                                                        } catch (DateTimeParseException e) {
                                                            // Handle the parsing error.
                                                            Toast.makeText(shiftActivity.this, "Error parsing date or time", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                }

                                                if (hasConflict) {
                                                    Toast.makeText(shiftActivity.this, "Shift conflicts", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    // Add the new shift since there are no conflicts.
                                                    shiftsRef.push().setValue(shift);
                                                    Toast.makeText(shiftActivity.this, "Shift successfully created", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            // Handle any errors that may occur during the database operation.
                                        }
                                    });
                                }
                            }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle any errors that may occur during the database operation.
                        }
                    });
               }
            }
        });
    }

    //Method to validate the time format
    private boolean isValidTimeFormat(String time) {
        // Use regular expression to check for valid HH:mm format
        String timeRegex = "^(08:00|08:30|09:00|09:30|10:00|10:30|11:00|11:30|12:00|12:30|13:00|13:30|14:00|14:30|15:00|15:30|16:00|16:30|17:00)$";
        return time.matches(timeRegex);
    }
    private boolean isConflict(LocalDate date, LocalTime startTime, LocalTime endTime, LocalDate parseDate, LocalTime parseStartTime, LocalTime parseEndTime) {
        // Check for date conflict
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && date.isEqual(parseDate)) {
            // If the dates are the same, check for time conflict
            if (startTime.isBefore(parseEndTime) && endTime.isAfter(parseStartTime)) {
                // There is a conflict
                return true;
            } else if (startTime.equals(parseStartTime) || endTime.equals(parseEndTime)) {
                // There is a conflict
                return true;
            }
        }
        return false;
    }

}
