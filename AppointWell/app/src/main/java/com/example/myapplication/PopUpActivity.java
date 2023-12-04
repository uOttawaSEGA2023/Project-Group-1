package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.RatingBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class PopUpActivity extends AppCompatActivity {

    RatingBar ratingBar;
    DatabaseReference appUsers = FirebaseDatabase.getInstance().getReference().child("Users").child("Approved Users");

    //String uId = PastAppointmentsPatient.getCurUserUid();
    //AppointmentRequest curApt = PastAppointmentsPatient.getPastAppointment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up);

        appUsers.keepSynced(true);

        initializeViews();
        setRatingBarListener();
        setWindowSizeAndPosition();
    }

    protected void initializeViews() {
        ratingBar = findViewById(R.id.ratingBar);

    }

    protected void setRatingBarListener() {
        ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            updateRatingInFirebase(rating);
            finish();
        });
    }
    protected void updateRatingInFirebase(float rating) {
          String doctorUID = getIntent().getStringExtra("doctorUID");
          appUsers.child(doctorUID).addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot snapshot) {
                  if (snapshot.exists()){
                      Doctor doctor =snapshot.getValue(Doctor.class);
                      if (doctor!=null){
                          doctor.setRating(rating);
                          doctor.incrementNumRating();
                          appUsers.child(doctorUID).setValue(doctor);
                      }
                  }
              }

              @Override
              public void onCancelled(@NonNull DatabaseError error) {

              }
          });
//
//        appUsers.child(uId).child("upcomingAppointments").child(appointmentID).child("rating").setValue(rating);
//        curApt.setRating(rating);


    }


    protected void setWindowSizeAndPosition() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = (int) (dm.widthPixels * 0.8);
        int height = (int) (dm.heightPixels * 0.2);

        getWindow().setLayout(width, height);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);
    }
}