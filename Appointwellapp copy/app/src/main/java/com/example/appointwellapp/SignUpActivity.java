package com.example.appointwellapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

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



        phoneNumberInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                NumberIsValid(phoneNumberInput);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailIsValid(emailInput);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        healthCardNumberInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                NumberIsValid(healthCardNumberInput);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordIsValid(passwordInput);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        firstNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nameIsValid(firstNameInput);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        lastNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nameIsValid(lastNameInput);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private boolean nameIsValid(EditText input){
        boolean valid = true;

            String firstName = String.valueOf(input.getText());
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


        return valid;
    }

    private boolean NumberIsValid(EditText input ){
        boolean valid= true;



            String phoneNum= String.valueOf(input.getText());
            if (phoneNum.length()!= 10){
                valid=false;
                if (phoneNum.length()==0){
                    input.setError("Field cannot be empty.");
                } else {
                    input.setError("Number should consist of 10 numbers.");
                }
            } else {
                for (int i=0; i<10; i++){
                    if (phoneNum.charAt(i)<48 || phoneNum.charAt(i)>57){
                        valid=false;
                        input.setError("Number can only consist of numerical characters.");
                        break;
                    }
                }
            }



        return valid;
    }

    private boolean emailIsValid(EditText input){
        boolean valid;
        String regex="^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
            String email= String.valueOf( input.getText());
            valid= (Pattern.compile(regex).matcher(email).matches());
            if (!valid){
                input.setError("Email address format is incorrect.");
            }

        return valid;
    }
    private boolean passwordIsValid(EditText input){
            boolean valid=true;
            char c;
            boolean specialChar=false;
            boolean uppercase=false;
            boolean lowercase=false;
            boolean num=false;

            String password= String.valueOf(input.getText());
            if (password.length()<9){
                valid=false;
                input.setError("Password consist of at least 8 characters.");
            } else {
                for (int i=0; i<password.length();i++){
                    c=password.charAt(i);
                    if (c>= 65 && c<=90){
                        uppercase=true;
                    } else {
                        if (c>=97 && c<=122){
                            lowercase=true;
                        } else {
                            if (c>=48 && c<=57){
                                num=true;
                            } else {
                                if ((c>=33 && c<=47)|| (c>=58 && c<= 64)|| (c>=91 && c<=96)|| (c>=123 && c<=126)){
                                    specialChar=true;
                                } else {
                                    valid = false;
                                    input.setError("Password can only consist of numbers, uppercase or lowercase characters and symbols (~`!@#$%^&*()_-+={[}]|\\:;\"'<,>.?/)");
                                    break;
                                }
                            }
                        }
                    }

                }
                if (!(uppercase && lowercase && specialChar && num)){
                    valid=false;
                    String errorMessage="";
                    if (!uppercase){
                        errorMessage=errorMessage+"Password should contain an uppercase alphabet. ";
                    }
                    if (!lowercase){
                        errorMessage=errorMessage+"Password should contain a lowercase alphabet. ";
                    }
                    if (!num){
                        errorMessage=errorMessage+"Password should contain a number. ";
                    }
                    if (!specialChar){
                        errorMessage=errorMessage+"Password should contain a symbol.";
                    }
                    input.setError(errorMessage);
                }
            }




        return valid;
    }



}