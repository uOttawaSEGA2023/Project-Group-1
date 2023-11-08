
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
                        Toast.makeText(shiftActivity.this, "The day has already passed- select another date", Toast.LENGTH_SHORT).show();
                        return;//avoid the app terminating
                    } else {
                        dateString = selectedDate.toString();
                    }
                }//check if endTime is before startTime
                if (endTime.compareTo(startTime) < 0) {
                    Toast.makeText(shiftActivity.this, "EndTime can't be before StartTime", Toast.LENGTH_SHORT).show();
                    return;
                }
                // get userId
//               FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//               if (currentUser != null) {
//               String uID = currentUser.getUid();
                String uID = "XvxJMNsAE1NNJGXsxZCE6xVz2dL2";
                userDatabase.child(uID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.hasChild("shifts")) {
                            // If it doesn't exist, create a "shifts" node.
                            // Create the Shift object that will be stored in the database
                            Shift newShift = new Shift(dateString, startTime, endTime);
                            userDatabase.child(uID).child("shifts").push().setValue(newShift);
                            Toast.makeText(shiftActivity.this, "Shift successfully created", Toast.LENGTH_SHORT).show();
                        } else {
                            // Check for conflicts
                            DatabaseReference shiftsRef = userDatabase.child(uID).child("shifts");
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
                                            // If a conflict is found, break out of the loop
                                            if (hasConflict) {
                                                break;
                                            }
                                        }
                                    }
                                    // Add the new shift if there are no conflicts
                                    if (!hasConflict) {
                                        Shift newShift = new Shift(dateString, startTime, endTime);
                                        shiftsRef.push().setValue(newShift);
                                        Toast.makeText(shiftActivity.this, "Shift successfully created", Toast.LENGTH_SHORT).show();
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
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle any errors that may occur during the database operation.
                    }
                });

//            }
            }
        });
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
