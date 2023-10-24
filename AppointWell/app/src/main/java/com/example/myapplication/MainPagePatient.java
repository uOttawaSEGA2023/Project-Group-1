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

public class MainPagePatient extends AppCompatActivity {
    private TextView welcomeMessageTextView;
    private TextView userTypeTextView;
    private ImageButton logOutBtn;

    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage_logoff);

        // Initialize the TextView
        welcomeMessageTextView = findViewById(R.id.welcomeMessageTextView);



        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Users").child(userID);


        // Attach a ValueEventListener to retrieve the user's data
        String userEmail= FirebaseAuth.getInstance().getCurrentUser().getEmail();


        if (!(userEmail.equals("admin@gmail.com"))){
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        Patient patient = snapshot.getValue(Patient.class);
                        if(patient!=null){
                            String welcomeMessage = patient.getFirstName() + " " + patient.getLastName();
                            type = patient.getType();
                            welcomeMessageTextView.setText(welcomeMessage);
                            userTypeTextView.setText("You are logged in as a " + type);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }




        userTypeTextView = findViewById(R.id.logintext);


        if (userEmail.equals("admin@gmail.com")){
            userTypeTextView.setText("You are logged in as an administrator");
        }
        logOutBtn = findViewById(R.id.logout);

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainPagePatient.this, Login.class);
                startActivity(intent);
            }
        });
    }
}










