package com.example.appointwellapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    RadioButton radioButtonPatient;
    RadioButton radioButtonDoctor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        radioButtonPatient = findViewById(R.id.patientbtn);
        radioButtonDoctor = findViewById(R.id.doctorbtn);
        TextView healthcardno = findViewById(R.id.healthcardnumbertxt);
        EditText specialtiesinput = findViewById(R.id.specialtiesinput);
        TextView specialtiestext = findViewById(R.id.specialtiestxt);
        LinearLayout fieldlayout = findViewById(R.id.fieldslayout);

        radioButtonPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                healthcardno.setText("Health Card Number");
                specialtiestext.setVisibility(View.GONE);
                specialtiesinput.setVisibility(View.GONE);
                specialtiesinput.setEnabled(false);
            }
        });

        radioButtonDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                healthcardno.setText("Employee Number");
                specialtiestext.setVisibility(View.VISIBLE);
                specialtiesinput.setVisibility(View.VISIBLE);
                specialtiesinput.setEnabled(true);

            }
        });
    }


}