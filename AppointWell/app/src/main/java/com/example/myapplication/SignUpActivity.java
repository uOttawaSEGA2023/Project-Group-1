package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
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
    TextView healthCardNo;
    EditText specialtiesInput;
    TextView specialtiesText;
    Button signUpBtn;
    boolean allFieldsValid;
    DatabaseReference database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://new-database-b712b-default-rtdb.firebaseio.com/"); //create a realtime database on firebase called "users"-is the key for reference
    DatabaseReference userdatabase = database.child("Users");
    FirebaseAuth auth;
    private RadioGroup userTypeRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        radioButtonPatient = findViewById(R.id.patientbtn);
        radioButtonDoctor = findViewById(R.id.doctorbtn);
        healthCardNo = findViewById(R.id.healthcardnumbertxt);
        specialtiesText = findViewById(R.id.specialtiestxt);
        emailInput = findViewById(R.id.emailinput);
        firstNameInput = findViewById(R.id.firstnameinput);
        lastNameInput = findViewById(R.id.lastnameinput);
        passwordInput = findViewById(R.id.passwordinput);
        addressInput = findViewById(R.id.addressinput);
        areaCodeInput = findViewById(R.id.areacodeinput);
        phoneNumberInput = findViewById(R.id.phonenumberinput);
        healthCardNumberInput = findViewById(R.id.healthcardinput);
        specialtiesInput = findViewById(R.id.specialtiesinput);
        signUpBtn = findViewById(R.id.signup);
        auth = FirebaseAuth.getInstance();

        radioButtonPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                healthCardNo.setText("Health Card Number");
                specialtiesText.setVisibility(View.GONE);
                specialtiesInput.setVisibility(View.GONE);
                specialtiesInput.setEnabled(false);
                emailInput.setText("");
                firstNameInput.setText("");
                lastNameInput.setText("");
                passwordInput.setText("");
                addressInput.setText("");
                phoneNumberInput.setText("");
                healthCardNumberInput.setText("");
                specialtiesInput.setText("");
                emailInput.setError(null);
                firstNameInput.setError(null);
                lastNameInput.setError(null);
                passwordInput.setError(null);
                addressInput.setError(null);
                phoneNumberInput.setError(null);
                healthCardNumberInput.setError(null);
                //specialtiesInput.setError(null);

            }
        });
        userTypeRadioGroup=findViewById(R.id.userTypeRadioGroup);
        radioButtonDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                healthCardNo.setText("Employee Number");
                specialtiesText.setVisibility(View.VISIBLE);
                specialtiesInput.setVisibility(View.VISIBLE);
                specialtiesInput.setEnabled(true);
                emailInput.setText("");
                firstNameInput.setText("");
                lastNameInput.setText("");
                passwordInput.setText("");
                addressInput.setText("");
                phoneNumberInput.setText("");
                healthCardNumberInput.setText("");
                specialtiesInput.setText("");
                emailInput.setError(null);
                firstNameInput.setError(null);
                lastNameInput.setError(null);
                passwordInput.setError(null);
                addressInput.setError(null);
                phoneNumberInput.setError(null);
                healthCardNumberInput.setError(null);
                //specialtiesInput.setError(null);
            }
        });
        userTypeRadioGroup = findViewById(R.id.userTypeRadioGroup);

        phoneNumberInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                numberIsValid(phoneNumberInput);
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
                numberIsValid(healthCardNumberInput);
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

        addressInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                addressIsValid(addressInput);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        specialtiesInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                specialtiesIsValid(specialtiesInput);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nameIsValid(firstNameInput) && nameIsValid(lastNameInput) && emailIsValid(emailInput) && passwordIsValid(passwordInput) && numberIsValid(phoneNumberInput) && numberIsValid(healthCardNumberInput)) {
                    if (radioButtonDoctor.isChecked() || radioButtonPatient.isChecked()) {

                        String email = emailInput.getText().toString().trim();
                        String password = passwordInput.getText().toString().trim();

                        int selectedRadioGroupButtonId=userTypeRadioGroup.getCheckedRadioButtonId();
                        String userType="";

                        if (selectedRadioGroupButtonId==R.id.patientbtn){
                            userType="Patient";
                        } else {
                            userType="Doctor";
                        }

                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs",MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("userType", userType);
                        editor.apply();
                        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseDatabase database = FirebaseDatabase.getInstance(); //create a realtime database on firebase called "users"-is the key for reference
                                    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();


                                    String firstName = firstNameInput.getText().toString().trim(); //get the first and last name after the user entering it
                                    String lastName = lastNameInput.getText().toString().trim();
                                    String address = addressInput.getText().toString().trim();
                                    int healthcardNumber = Integer.parseInt(healthCardNumberInput.getText().toString());
                                    int phoneNumber = Integer.parseInt(phoneNumberInput.getText().toString());


                                    if (radioButtonDoctor.isChecked()){
                                        List<String> specialties = textToList(specialtiesInput);

                                        // create the Doctor object that will be stored in the database
                                        Doctor doctorRegistrator = new Doctor(email, password, firstName, lastName, address, healthcardNumber, phoneNumber, specialties);

                                        userdatabase.child(userID).setValue(doctorRegistrator);

                                        // Swap Activity to main page
                                        Intent intent = new Intent(SignUpActivity.this, mainpagePatient.class);

                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // create the Patient object that will be stored in the database
                                        Patient patientRegistrator = new Patient (email, firstName, lastName, password, address,healthcardNumber,phoneNumber);

                                        // we use username as unique identifier
                                        //I ADDED .PUSH IT MIGHTBE WRONG CHECK
                                        userdatabase.child(userID).setValue(patientRegistrator);

                                        // Swap Activity to main page
                                        Intent intent = new Intent(SignUpActivity.this, mainpagePatient.class);
                                        startActivity(intent);
                                    }

                                    // Finish the current activity to prevent the user from going back.
                                    finish();
                                } else {
                                    Toast.makeText(SignUpActivity.this, "Registration Unsuccessful", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } else {
                    Toast.makeText(SignUpActivity.this, "Field(s) invalid. Unable to register.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean nameIsValid(EditText input) {
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

    private boolean numberIsValid(EditText input) {
        boolean valid = true;


        String phoneNum = String.valueOf(input.getText());
        if (phoneNum.length() != 10) {
            valid = false;
            if (phoneNum.length() == 0) {
                input.setError("Field cannot be empty.");
            } else {
                input.setError("Number should consist of 10 numbers.");
            }
        } else {
            for (int i = 0; i < 10; i++) {
                if (phoneNum.charAt(i) < 48 || phoneNum.charAt(i) > 57) {
                    valid = false;
                    input.setError("Number can only consist of numerical characters.");
                    break;
                }
            }
        }


        return valid;
    }

    private boolean emailIsValid(EditText input) {
        boolean valid;
        String regex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        String email = String.valueOf(input.getText());
        valid = (Pattern.compile(regex).matcher(email).matches());
        if (!valid) {
            input.setError("Email address format is incorrect.");
        }

        return valid;
    }

    private boolean passwordIsValid(EditText input) {
        boolean valid = true;
        char c;
        boolean specialChar = false;
        boolean uppercase = false;
        boolean lowercase = false;
        boolean num = false;

        String password = String.valueOf(input.getText());
        if (password.length() < 9) {
            if (password.trim().length() == 0) {
                input.setError("Field cannot be empty.");
                return false;
            }
            valid = false;
            input.setError("Password consist of at least 8 characters.");
        } else {
            for (int i = 0; i < password.length(); i++) {
                c = password.charAt(i);
                if (c >= 65 && c <= 90) {
                    uppercase = true;
                } else {
                    if (c >= 97 && c <= 122) {
                        lowercase = true;
                    } else {
                        if (c >= 48 && c <= 57) {
                            num = true;
                        } else {
                            if ((c >= 33 && c <= 47) || (c >= 58 && c <= 64) || (c >= 91 && c <= 96) || (c >= 123 && c <= 126)) {
                                specialChar = true;
                            } else {
                                valid = false;
                                input.setError("Password can only consist of numbers, uppercase or lowercase characters and symbols (~`!@#$%^&*()_-+={[}]|\\:;\"'<,>.?/)");
                                break;
                            }
                        }
                    }
                }

            }
            if (!(uppercase && lowercase && specialChar && num)) {
                valid = false;
                String errorMessage = "";
                if (!uppercase) {
                    errorMessage = errorMessage + "Password should contain an uppercase alphabet. ";
                }
                if (!lowercase) {
                    errorMessage = errorMessage + "Password should contain a lowercase alphabet. ";
                }
                if (!num) {
                    errorMessage = errorMessage + "Password should contain a number. ";
                }
                if (!specialChar) {
                    errorMessage = errorMessage + "Password should contain a symbol.";
                }
                input.setError(errorMessage);
            }
        }


        return valid;
    }

    private boolean addressIsValid(EditText input) {
        if (input.getText().toString().trim().length() == 0) {
            input.setError("Field cannot be empty.");
            return false;
        }
        return true;
    }

    private boolean specialtiesIsValid(EditText input) {
        if (input.getText().toString().trim().length() == 0) {
            input.setError("Field cannot be empty.");
            return false;
        }
        return true;
    }

    private List<String> textToList(EditText input){  // passes specialty text as parameter and returns a list of the specialties
        String special = specialtiesInput.getText().toString().trim();
        String specialty = "" ;
        List<String> specialtiesList = new ArrayList<>();

        for (int i = 0 ; i < special.length() ; i ++ ) {
            if (!(String.valueOf(special.charAt(i)).equals(","))) {
                specialty = specialty + special.charAt(i);
            } else {
                specialtiesList.add(specialty.trim());
                specialty = "";
            }
        }
        specialtiesList.add(specialty.trim());
        return specialtiesList;
    }

    }



