package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class upcomingShiftActivity extends AppCompatActivity {
    ImageButton addbtn, back;
    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_shift);
        addbtn = findViewById(R.id.plus);
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
}