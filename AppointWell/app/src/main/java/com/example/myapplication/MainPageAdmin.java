package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainPageAdmin extends AppCompatActivity {
    Button pendingBtn;
    Button rejectedBtn;
    DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://new-database-b712b-default-rtdb.firebaseio.com/").child("Users");

    LinearLayout requestList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage_admin);

        requestList=findViewById(R.id.requestsList);
        pendingBtn = findViewById(R.id.pending);
        rejectedBtn = findViewById(R.id.rejected);

        userDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot child: snapshot.getChildren()){
                        UserAccount userAccount = child.getValue(Patient.class);
                        if (userAccount!=null){
                            if (userAccount.getStatus().equals("Pending")){
                                addRequest(userAccount.getFirstName(), userAccount.getLastName(), userAccount.type);

                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        rejectedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pendingBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.toggle)));
                rejectedBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            }
        });

        pendingBtn.setOnClickListener(new View.OnClickListener() {      // changes color on click
            @Override
            public void onClick(View v) {
                pendingBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                rejectedBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.toggle)));
            }
        });

    }
    public void addRequest(String firstName, String lastname, String type){
        View requestView= getLayoutInflater().inflate(R.layout.registration_request,null,false);
        TextView name = (TextView) requestView.findViewById(R.id.name);
        TextView accType=(TextView) requestView.findViewById(R.id.type);
        accType.setText(type);
        name.setText(firstName+" "+lastname);
        requestList.addView(requestView);

    }
}