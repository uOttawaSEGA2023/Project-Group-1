package com.example.appointwellapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import android.content.SharedPreferences;

public class mainpageDoctor extends AppCompatActivity {
    private TextView welcomeMessageTextView;
    private TextView userTypeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage_logoff);

        // Initialize the TextView
        welcomeMessageTextView = findViewById(R.id.welcomeMessageTextView);



        String username = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://appointwell-app-default-rtdb.firebaseio.com/");


        // Attach a ValueEventListener to retrieve the user's data
        database.child("Users").child("Doctors").child(username).addListenerForSingleValueEvent(new ValueEventListener(){
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                if (dataSnapshot.exists()) {
                    Doctor doctor = dataSnapshot.getValue(Doctor.class);

                    if (doctor != null) {
                        // Set the welcome message with the user's first name and last name
                        String welcomeMessage = doctor.getFirstName() + " " + doctor.getLastName();
                        welcomeMessageTextView.setText(welcomeMessage);
                    }
                }
            }

            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors here
            }

        });


        userTypeTextView = findViewById(R.id.logintext);

        // Retrieve the selected user type from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userType = sharedPreferences.getString("userType", "");

        // Display the user type in the TextView
        userTypeTextView.setText("You are logged in as a " + userType);
    }
}
