package com.example.myapplication;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    // Fields
    EditText editTextEmail, editTextPassword;
    Button buttonLogin;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView signUp;
    DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://new-database-b712b-default-rtdb.firebaseio.com/").child("Users");

    // Firebase
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            String uID = currentUser.getUid();
            userDatabase.child(uID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        Account account = snapshot.getValue(Account.class);
                        if(account!=null && account.getStatus().equals("Approved")) {
                                if (account.getType().equals("Patient")) {
                                    Intent intent = new Intent(Login.this, MainPagePatient.class);
                                    Toast.makeText(Login.this, "Logged in as a Patient", Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
                                } else if (account.getType().equals("Doctor")) {
                                    Intent intent = new Intent(Login.this, MainPageDoctor.class);
                                    Toast.makeText(Login.this, "Logged in as a Doctor", Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(Login.this, MainPageAdmin.class);
                                    Toast.makeText(Login.this, "Logged in as a Admin", Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progressBar);
        signUp = findViewById(R.id.registerNow);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email, password;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());

                // Check if fields == empty
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(Login.this,"Please enter Email",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(Login.this,"Please enter Password",Toast.LENGTH_SHORT).show();
                    return;
                }

                // Firebase Authenticator - Sign in
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    //gets userID of current user
                                    String userID = mAuth.getCurrentUser().getUid();


                                    DatabaseReference approvedRef = userDatabase.child("Approved Users").child(userID);

                                    approvedRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(snapshot.exists()){
                                                Account account = snapshot.getValue(Account.class);
                                                if(account!=null){
                                                        switch (account.type) {
                                                            case "Patient":
                                                                Intent intentP = new Intent(getApplicationContext(), MainPagePatient.class);
                                                                Toast.makeText(Login.this, "Logged in as a Patient", Toast.LENGTH_SHORT).show();
                                                                startActivity(intentP);
                                                                finish();
                                                                break;
                                                            case "Doctor":
                                                                Intent intentD = new Intent(getApplicationContext(), MainPageDoctor.class);
                                                                Toast.makeText(Login.this, "Logged in as a Doctor", Toast.LENGTH_SHORT).show();
                                                                startActivity(intentD);
                                                                finish();
                                                                break;
                                                            case "Administrator":
                                                                Intent intentA = new Intent(getApplicationContext(), MainPageAdmin.class);
                                                                Toast.makeText(Login.this, "Logged in as a Admin", Toast.LENGTH_SHORT).show();
                                                                startActivity(intentA);
                                                                finish();
                                                                break;
                                                                default:
                                                                break;
                                                    }
                                                }
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                    DatabaseReference pendingRef = userDatabase.child("Pending Users").child(userID);

                                    pendingRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(snapshot.exists()){
                                                Toast.makeText(getApplicationContext(), "Your registration has not yet been processed. Try again later.", Toast.LENGTH_LONG).show();
                                                mAuth.signOut();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });


                                    DatabaseReference rejectedRef = userDatabase.child("Rejected Users").child(userID);

                                    rejectedRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(snapshot.exists()){
                                                Toast.makeText(getApplicationContext(), "Your registration has been rejected.", Toast.LENGTH_LONG).show();
                                                mAuth.signOut();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                } else {

                                    Toast.makeText(Login.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                }
        });

    }
}