package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainPageAdmin extends AppCompatActivity {
    Button pendingBtn;
    Button rejectedBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage_admin);

        pendingBtn = findViewById(R.id.pending);
        rejectedBtn = findViewById(R.id.rejected);

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
}