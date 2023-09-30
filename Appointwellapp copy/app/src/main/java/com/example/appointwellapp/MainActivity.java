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

        Patient newPatient;
        String email, firstName, lastName, password,address, areaCode;
        int healthCardNumber, phoneNumber;

        EditText emailInput = findViewById(R.id.emailinput);
        EditText firstNameInput = findViewById(R.id.firstnameinput);
        EditText lastNameInput = findViewById(R.id.lastnameinput);
        EditText passwordInput = findViewById(R.id.passwordinput);
        EditText addressInput = findViewById(R.id.addressinput);
        EditText areaCodeInput = findViewById(R.id.areacodeinput);
        EditText healthCardNumberInput = findViewById(R.id.healthcardinput);
        EditText phoneNumberInput = findViewById(R.id.phonenumberinput);

//        email = String.valueOf(emailInput.getText());
//        firstName = String.valueOf(firstNameInput.getText());
//        lastName = String.valueOf(lastNameInput.getText());
//        password = String.valueOf(passwordInput.getText());
//        address = String.valueOf(addressInput.getText());
//        areaCode = String.valueOf(areaCodeInput.getText());
//        healthCardNumber = Integer.parseInt(String.valueOf(healthCardNumberInput.getText()));
//        phoneNumber = Integer.parseInt(String.valueOf(phoneNumberInput.getText()));


        emailInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!(view.hasFocus())){
                    // call email validate function
                }
            }
        });

        firstNameInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!(view.hasFocus())){
                    String firstName = String.valueOf(firstNameInput.getText());
                    boolean valid = true;
                    if (firstName.matches("")){
                        firstNameInput.setError("Field cannot be empty.");
                        valid = false;
                    } else if (!(firstName.charAt(0)>='A' && firstName.charAt(0)<='Z')) {
                        firstNameInput.setError("Name has to start with an Uppercase alphabet");
                        valid = false;
                    } else{
                        for (int i = 0 ; i < firstName.length() ; i ++){
                            if (!((firstName.charAt(i)>='A' && firstName.charAt(i)<='Z')||(firstName.charAt(i)>='a' && firstName.charAt(i)<='z') || (firstName.charAt(i) == ' ') || (firstName.charAt(i) == '-') || (firstName.charAt(i) == '\''))){
                                firstNameInput.setError("Name has to contain alphabets, dash(-), single quote(') or space( ) only.");
                                valid = false;
                            }

                        }
                    }
                    if (valid) {
                        firstNameInput.setError(null);
                    }
                }
            }
        });

    }



}