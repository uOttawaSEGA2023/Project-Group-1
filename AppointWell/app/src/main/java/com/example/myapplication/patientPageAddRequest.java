package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class patientPageAddRequest extends AppCompatActivity {

    Button addRequest;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_page_add_request);
        DatabaseReference database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://new-database-b712b-default-rtdb.firebaseio.com/");
      reference = database.child("Users").child("Approved Users").child("uzoS3miOxgWZ0H4RJ3vBarQeJ7G2").child("Appointment");



       addRequest = findViewById(R.id.addRequest);


       reference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.exists()) {
                   ArrayList<AppointmentRequest> requestsList = (ArrayList<AppointmentRequest>) snapshot.getValue();
                   int size = requestsList.size();
                   addRequest.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           AppointmentRequest request = new AppointmentRequest("John Doe ", "123", "Pending", "9:00", "10:00", "11-11-2023");
                           request.setCountID(size);
                           requestsList.add(request);
                           reference.getRef().setValue(requestsList);
                       }
                   });
               }
               else{
                   addRequest.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           ArrayList<AppointmentRequest> list = new ArrayList<>();
                           AppointmentRequest request = new AppointmentRequest("Aniket Luchmun", "123", "Approved", "8:00", "9:00", "11-11-2023");
                           request.setCountID(0);
                           list.add(request);
                           reference.getRef().setValue(list);
                       }
                   });

               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });




    }
}