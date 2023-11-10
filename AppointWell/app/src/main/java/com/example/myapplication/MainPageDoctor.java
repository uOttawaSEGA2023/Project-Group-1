package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainPageDoctor extends AppCompatActivity {
    private TextView welcomeMessageTextView;
    private TextView userTypeTextView;
    private View shiftbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage_doctor);

        // Initialize the TextView
        welcomeMessageTextView = findViewById(R.id.welcomeMessageTextView);
        userTypeTextView = findViewById(R.id.logintext);

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference accountRef = FirebaseDatabase.getInstance().getReference("Users").child("Approved Users").child(userID);


        // Attach a ValueEventListener to retrieve the user's data
        accountRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    UserAccount userAccount = snapshot.getValue(UserAccount.class);
                    if(userAccount!=null){
                        String welcomeMessage = userAccount.getFirstName() + " " + userAccount.getLastName();
                        welcomeMessageTextView.setText(welcomeMessage);
                        userTypeTextView.setText("You are logged in as a Doctor");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        shiftbtn = findViewById(R.id.shiftBtn);

        shiftbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPageDoctor.this, upcomingShiftActivity.class);
                startActivity(intent);
            }
        });
    }
//    // Method to display user data
//    private void displayUserData() {
//        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        DatabaseReference accountRef = FirebaseDatabase.getInstance().getReference("Users").child("Approved Users").child(userID);
//
//        accountRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    UserAccount userAccount = snapshot.getValue(UserAccount.class);
//                    if (userAccount != null) {
//                        String welcomeMessage = userAccount.getFirstName() + " " + userAccount.getLastName();
//                        welcomeMessageTextView.setText(welcomeMessage);
//                        userTypeTextView.setText("You are logged in as a Doctor");
//                    }
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Handle errors
//            }
//        });
//    }

}