package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
                                addRequest(userAccount.getFirstName(), userAccount.getLastName(), userAccount.type,child.getKey());

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
    public void addRequest(String firstName,  String lastname, String type, String uID){
        View requestView= getLayoutInflater().inflate(R.layout.registration_request,null,false);
        requestView.setTag(uID);
        TextView name = (TextView) requestView.findViewById(R.id.name);
        TextView accType=(TextView) requestView.findViewById(R.id.type);
        accType.setText(type);
        name.setText(firstName+" "+lastname);
        requestView.setOnClickListener(requestOnClick);
        requestList.addView(requestView);

    }

    View.OnClickListener requestOnClick= new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            showSlide();

            String uID = v.getTag().toString();

            View slideView = getLayoutInflater().inflate(R.layout.bottom_dialogue, null, false);


            TextView name = (TextView) slideView.findViewById(R.id.slideInName);
            TextView address = (TextView) slideView.findViewById(R.id.slideInAddressText);
            TextView healthCard = (TextView) slideView.findViewById(R.id.slideInHealthCardNumberText);
            TextView email = (TextView) slideView.findViewById(R.id.slideInEmailText);
            TextView phoneNum = (TextView) slideView.findViewById(R.id.slideInPhoneNumberText);
            TextView type = (TextView) slideView.findViewById(R.id.slideInType);
            TextView specialties = (TextView) slideView.findViewById(R.id.slideInSpecialtiesText);

            Log.d("tag Junho", uID);



            userDatabase.child(uID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        UserAccount account = snapshot.getValue(UserAccount.class);
                        if (account!=null){
                            if (account.getType().equals("Patient")){

                                Patient patient=snapshot.getValue(Patient.class);

                                String nameMsg = patient.getFirstName()+" "+patient.getLastName();
                                Log.d("tag Junho", nameMsg);
                                name.setText("kk");


                                String addressMsg = "Address: "+patient.getAddress();
                                address.setText(addressMsg);

                                Log.d("tag Junho", addressMsg);

                                String healthCardMsg = String.valueOf("Health Card Number: "+patient.getHealthCardNumber());
                                healthCard.setText(healthCardMsg);

                                String emailMsg = "Email: "+patient.getEmail();
                                email.setText(emailMsg);

                                String phoneNumMsg = "Phone Number: "+String.valueOf(patient.getPhoneNumber());
                                phoneNum.setText(phoneNumMsg);

                                String typeMsg = patient.getType();
                                type.setText(typeMsg);

                                specialties.setText("");

                            } else {

                                Doctor doctor=snapshot.getValue(Doctor.class);
                                name.setText(doctor.getFirstName()+" "+doctor.getLastName());
                                address.setText("Address: "+doctor.getAddress());
                                healthCard.setText("Employee Number: "+String.valueOf(doctor.getEmployeeNumber()));
                                email.setText("Email: "+doctor.getEmail());
                                phoneNum.setText("Phone Number: "+String.valueOf(doctor.getPhoneNumber()));
                                type.setText(doctor.getType());
                                String specialtiesMsg=doctor.getSpecialties().get(0);
                                for (int i=1; i<doctor.getSpecialties().size();i++){
                                    specialtiesMsg=specialtiesMsg+", "+doctor.getSpecialties().get(i);
                                }
                                specialties.setText("Specialties: "+specialtiesMsg);
                            }
                        }
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    };

    private void showSlide(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_dialogue);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

}
