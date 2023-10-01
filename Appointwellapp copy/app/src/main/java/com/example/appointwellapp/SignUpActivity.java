package com.example.appointwellapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    RadioButton radioButtonPatient;
    RadioButton radioButtonDoctor;
    EditText emailInput;
    EditText firstNameInput;
    EditText lastNameInput;
    EditText passwordInput;
    EditText addressInput;
    EditText areaCodeInput;
    EditText healthCardNumberInput;
    EditText phoneNumberInput;
    TextView healthcardno;
    EditText specialtiesinput;
    TextView specialtiestext;
    boolean allFieldsValid;
    FirebaseDatabase db;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        radioButtonPatient = findViewById(R.id.patientbtn);
        radioButtonDoctor = findViewById(R.id.doctorbtn);
        healthcardno = findViewById(R.id.healthcardnumbertxt);
        specialtiestext = findViewById(R.id.specialtiestxt);
        emailInput = findViewById(R.id.emailinput);
        firstNameInput = findViewById(R.id.firstnameinput);
        lastNameInput = findViewById(R.id.lastnameinput);
        passwordInput = findViewById(R.id.passwordinput);
        addressInput = findViewById(R.id.addressinput);
        areaCodeInput = findViewById(R.id.areacodeinput);
        phoneNumberInput = findViewById(R.id.phonenumberinput);
        healthCardNumberInput = findViewById(R.id.healthcardinput);
        specialtiesinput = findViewById(R.id.specialtiesinput);

        radioButtonPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                healthcardno.setText("Health Card Number");
                specialtiestext.setVisibility(View.GONE);
                specialtiesinput.setVisibility(View.GONE);
                specialtiesinput.setEnabled(false);
                emailInput.setText("");
                firstNameInput.setText("");
                lastNameInput.setText("");
                passwordInput.setText("");
                addressInput.setText("");
                areaCodeInput.setText("");
                phoneNumberInput.setText("");
                healthCardNumberInput.setText("");
                specialtiesinput.setText("");

            }
        });

        radioButtonDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                healthcardno.setText("Employee Number");
                specialtiestext.setVisibility(View.VISIBLE);
                specialtiesinput.setVisibility(View.VISIBLE);
                specialtiesinput.setEnabled(true);
                emailInput.setText("");
                firstNameInput.setText("");
                lastNameInput.setText("");
                passwordInput.setText("");
                addressInput.setText("");
                areaCodeInput.setText("");
                phoneNumberInput.setText("");
                healthCardNumberInput.setText("");
                specialtiesinput.setText("");
            }
        });

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
            public void onFocusChange(View v, boolean hasFocus) {
                nameIsValid(v, firstNameInput);
            }
        });

        lastNameInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                nameIsValid(v, lastNameInput);
            }
        });
    }
    private void nameIsValid(View v, EditText input){
        if (!(v.hasFocus())) {
            String firstName = String.valueOf(input.getText());
            boolean valid = true;
            if (firstName.matches("")) {
                input.setError("Field cannot be empty.");
                valid = false;
            } else if (!(firstName.charAt(0) >= 'A' && firstName.charAt(0) <= 'Z')) {
                input.setError("Name has to start with an Uppercase alphabet");
                valid = false;
            } else {
                for (int i = 0; i < firstName.length(); i++) {
                    if (!((firstName.charAt(i) >= 'A' && firstName.charAt(i) <= 'Z') || (firstName.charAt(i) >= 'a' && firstName.charAt(i) <= 'z') || (firstName.charAt(i) == ' ') || (firstName.charAt(i) == '-') || (firstName.charAt(i) == '\''))) {
                        input.setError("Name has to contain alphabets, dash(-), single quote(') or space( ) only.");
                        valid = false;
                    }
                }
            }
            if (valid) {
                input.setError(null);
            }
        }
    }




}