package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class upcomingShiftActivity extends AppCompatActivity {
    ImageButton addbtn, back;
    String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();

//    String uID = "XvxJMNsAE1NNJGXsxZCE6xVz2dL2";

    Context appContext;
    LinearLayout shiftList;
    DatabaseReference approvedUserDB = FirebaseDatabase.getInstance().getReferenceFromUrl("https://new-database-b712b-default-rtdb.firebaseio.com/").child("Users").child("Approved Users");

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_shift);
        appContext = getApplicationContext();
        addbtn = findViewById(R.id.plus);
        shiftList = findViewById(R.id.shiftslist);

        refreshList();
        addbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(upcomingShiftActivity.this, shiftActivity.class);
                startActivity(intent);
            }
        });
        back = findViewById(R.id.backtomainpage);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(upcomingShiftActivity.this, MainPageDoctor.class);
                startActivity(intent);
            }
        });

    }
    private void addShift(Shift shift, String shiftId) {
        View shiftView = getLayoutInflater().inflate(R.layout.newshift, null, false);
        TextView Date = shiftView.findViewById(R.id.Date);

        DateTimeFormatter inputFormatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        }
        DateTimeFormatter outputFormatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            outputFormatter = DateTimeFormatter.ofPattern("d MMM yyyy");
        }
        LocalDate date = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            date = LocalDate.parse(shift.getSelectedDate(), inputFormatter);
        }
        String formattedDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formattedDate = date.format(outputFormatter);
        }
        Date.setText(formattedDate);

        TextView time = shiftView.findViewById(R.id.time);
        time.setText(shift.getStartTime() + " - " + shift.getEndTime());

        ImageButton delete= (ImageButton) shiftView.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                approvedUserDB.child(uID).child("shifts").child(shiftId).removeValue();
                Toast.makeText(upcomingShiftActivity.this, "Shift successfully deleted" ,Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshList();
                    }
                }, 270);
            }
        });

        shiftList.addView(shiftView);
    }
    public void refreshList(){
        shiftList.removeAllViews();

        DatabaseReference shiftsRef = approvedUserDB.child(uID).child("shifts");

            // Order the shifts by the "selectedDate" child in ascending order
            shiftsRef.orderByChild("selectedDate").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                shiftList.removeAllViews();
                if (snapshot.exists()){
                    for (DataSnapshot child: snapshot.getChildren()){
                        Shift shift = child.getValue(Shift.class);
                        if (shift!=null){
                            addShift(shift, child.getKey());
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}