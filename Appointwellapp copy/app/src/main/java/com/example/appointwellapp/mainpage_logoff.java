package com.example.appointwellapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import android.content.SharedPreferences;

public class mainpage_logoff extends AppCompatActivity {
    private TextView welcomeMessageTextView;
    private TextView userTypeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage_logoff);

        // Initialize the TextView
        welcomeMessageTextView = findViewById(R.id.welcomeMessageTextView);

        // Get the authenticated user's UID
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Get a reference to the user's data in the Firebase Realtime Database
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

        // Attach a ValueEventListener to retrieve the user's data
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Retrieve the user object
                    User user = dataSnapshot.getValue(User.class);

                    // Check if user is not null
                    if (user != null) {
                        // Set the welcome message with the user's first name and last name
                        String welcomeMessage = user.getFirstName() + " " + user.getLastName();
                        welcomeMessageTextView.setText(welcomeMessage);
                    }
                }
            }

            @Override
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









