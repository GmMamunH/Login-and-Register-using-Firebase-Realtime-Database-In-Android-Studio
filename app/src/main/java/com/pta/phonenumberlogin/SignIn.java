package com.pta.phonenumberlogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {

    EditText login_phoneNumber, loginPassword;
    Button loginButton;
    TextView signupRedirectText;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        progressBar = findViewById(R.id.progressBar);
        login_phoneNumber = findViewById(R.id.login_phoneNumber);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        signupRedirectText = findViewById(R.id.signupRedirectText);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validatePhoneNumber() || !validatePassword()) {
                } else {
                    checkUser();
                }
            }
        });

        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignIn.this, SignUp.class);
                startActivity(intent);
            }
        });
    } //oneCreate end

    public Boolean validatePhoneNumber() {
        String val = login_phoneNumber.getText().toString();
        if (val.isEmpty()) {
            login_phoneNumber.setError("Phone number cannot be empty");
            return false;
        } else {
            login_phoneNumber.setError(null);
            return true;
        }
    }
    public Boolean validatePassword(){
        String val = loginPassword.getText().toString();
        if (val.isEmpty()) {
            loginPassword.setError("Password cannot be empty");
            return false;
        } else {
            loginPassword.setError(null);
            return true;
        }
    }


    public void checkUser(){
        progressBar.setVisibility(View.VISIBLE);
        String userPhoneNumber = login_phoneNumber.getText().toString().trim();
        String userPassword = loginPassword.getText().toString().trim();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("phoneNumber").equalTo(userPhoneNumber);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    login_phoneNumber.setError(null);
                    String passwordFromDB = snapshot.child(userPhoneNumber).child("password").getValue(String.class);
                    if (passwordFromDB.equals(userPassword)) {
                        login_phoneNumber.setError(null);
                        String nameFromDB = snapshot.child(userPhoneNumber).child("name").getValue(String.class);
                        String emailFromDB = snapshot.child(userPhoneNumber).child("email").getValue(String.class);
                        String phoneNumberFromDB = snapshot.child(userPhoneNumber).child("phoneNumber").getValue(String.class);
                        progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(SignIn.this, MainActivity.class);

                        intent.putExtra("name", nameFromDB);
                        intent.putExtra("email", emailFromDB);
                        intent.putExtra("phoneNumber", phoneNumberFromDB);
                        intent.putExtra("password", passwordFromDB);
                        startActivity(intent);
                    } else {
                        loginPassword.setError("Invalid Credentials");
                        loginPassword.requestFocus();
                    }
                } else {
                    login_phoneNumber.setError("Phone number does not exist");
                    login_phoneNumber.requestFocus();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }


}