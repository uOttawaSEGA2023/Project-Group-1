package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainPageAdmin extends AppCompatActivity {
    Dialog dialog;
    Button pendingBtn;
    Button rejectedBtn;
    ImageButton logOutBtn;
    DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://new-database-b712b-default-rtdb.firebaseio.com/").child("Users");

    LinearLayout requestList;
    String nameMsg, emailMsg, addressMsg, phoneNumMsg, typeMsg, specialtiesMsg, healthCardMsg;
    Administrator admin = new Administrator();

    boolean pendingSelected = true;
    @Override
    protected void onStart() {
        super.onStart();
        pendingBtn = findViewById(R.id.pending);
        pendingBtn.callOnClick();

        pendingBtn.setOnClickListener(new View.OnClickListener() {      // changes color on click
            @Override
            public void onClick(View v) {
                pendingSelected = true;
                pendingBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                rejectedBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.toggle)));

                refreshList("Pending Users");

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage_admin);

        logOutBtn = findViewById(R.id.logOutAdmin);
        requestList=findViewById(R.id.requestsList);
        pendingBtn = findViewById(R.id.pending);
        rejectedBtn = findViewById(R.id.rejected);

        rejectedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pendingSelected = false;
                pendingBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.toggle)));
                rejectedBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));

                refreshList("Rejected Users");

            }
        });

        pendingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pendingSelected = true;
                pendingBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                rejectedBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.toggle)));

                refreshList("Pending Users");
            }
        });

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();

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
            View slideView = getLayoutInflater().inflate(R.layout.bottom_dialogue, null, false);

            TextView name = (TextView) slideView.findViewById(R.id.slideInName);
            TextView address = (TextView) slideView.findViewById(R.id.slideInAddressText);
            TextView healthCard = (TextView) slideView.findViewById(R.id.slideInHealthCardNumberText);
            TextView email = (TextView) slideView.findViewById(R.id.slideInEmailText);
            TextView phoneNum = (TextView) slideView.findViewById(R.id.slideInPhoneNumberText);
            TextView type = (TextView) slideView.findViewById(R.id.slideInType);
            TextView specialties = (TextView) slideView.findViewById(R.id.slideInSpecialtiesText);

            Button approveBtn = (Button) slideView.findViewById(R.id.approveButton);
            Button rejectBtn = (Button) slideView.findViewById(R.id.rejectButton);



            String uID = v.getTag().toString();

            approveBtn.setTag(uID);
            rejectBtn.setTag(uID);

            approveBtn.setOnClickListener(approve);
            rejectBtn.setOnClickListener(reject);
            String databasePath = "Pending Users";
            if(!pendingSelected){
                databasePath = "Rejected Users";
            }
            userDatabase.child(databasePath).child(uID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        UserAccount userAccount = snapshot.getValue(UserAccount.class);

                        if (userAccount!=null){
                            if (userAccount.getType().equals("Patient")){

                                Patient patient=snapshot.getValue(Patient.class);

                                nameMsg = patient.getFirstName()+" "+patient.getLastName();

                                addressMsg = "Address: "+patient.getAddress();

                                healthCardMsg = "Health Card Number: " + patient.getHealthCardNumber();

                                emailMsg = "Email: "+patient.getEmail();

                                phoneNumMsg = "Phone Number: " + patient.getPhoneNumber();

                                typeMsg = patient.getType();

                                specialtiesMsg = "";


                            } else {

                                Doctor doctor=snapshot.getValue(Doctor.class);

                                nameMsg = doctor.getFirstName()+" "+doctor.getLastName();

                                addressMsg = "Address: "+doctor.getAddress();

                                healthCardMsg = "Employee Number: " + doctor.getEmployeeNumber();

                                emailMsg = "Email: "+doctor.getEmail();

                                phoneNumMsg = "Phone Number: " + doctor.getPhoneNumber();

                                typeMsg = doctor.getType();

                                specialtiesMsg = "Specialties: " + doctor.getSpecialties().get(0);
                                for (int i=1; i<doctor.getSpecialties().size();i++){
                                    specialtiesMsg=specialtiesMsg+", "+doctor.getSpecialties().get(i);
                                }


                            }
                            name.setText(nameMsg);
                            address.setText(addressMsg);
                            healthCard.setText(healthCardMsg);
                            email.setText(emailMsg);
                            phoneNum.setText(phoneNumMsg);
                            type.setText(typeMsg);
                            specialties.setText(specialtiesMsg);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            showSlide(slideView);
        }
    };



    private void showSlide(View slideView){
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(slideView);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    View.OnClickListener approve = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String uID = v.getTag().toString();
            String databasePath = "Pending Users";
            if(!pendingSelected){
                databasePath = "Rejected Users";
            }
            String finalDatabasePath = databasePath;
            userDatabase.child(databasePath).child(uID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        UserAccount userAccount = snapshot.getValue(UserAccount.class);
                        admin.approveRegistrant(userAccount, uID);
                        dialog.dismiss();
                        refreshList(finalDatabasePath);


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            
        }
    };

    private void refreshList(String databasePath) {
        requestList.removeAllViews();
        userDatabase.child(databasePath).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot child: snapshot.getChildren()){
                        UserAccount userAccount = child.getValue(Patient.class);
                        if (userAccount!=null){
                            addRequest(userAccount.getFirstName(), userAccount.getLastName(), userAccount.type,child.getKey());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    View.OnClickListener reject = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String uID = v.getTag().toString();
            userDatabase.child("Pending Users").child(uID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        UserAccount userAccount = snapshot.getValue(UserAccount.class);
                        admin.rejectRegistrant(userAccount, uID);
                        dialog.dismiss();
                        refreshList("Pending Users");

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    };
}
