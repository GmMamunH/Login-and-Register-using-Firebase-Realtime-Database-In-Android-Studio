package com.pta.phonenumberlogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {


    TextView profileName, profileEmail, profilePhoneNumber, profilePassword;
    TextView titleName, titlePhoneNumber;
    Button editProfile;
    ProgressBar progressBar;
    Button signOut;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressBar);
        signOut = findViewById(R.id.signOut);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Sign out success", Toast.LENGTH_SHORT).show();

                FirebaseAuth.getInstance().signOut();
                finish();
                progressBar.setVisibility(View.GONE);
                finish();
                Intent intent = new Intent(MainActivity.this, SignIn.class);
                startActivity(intent);

            }
        });

        profileName = findViewById(R.id.profileName);
        profileEmail = findViewById(R.id.profileEmail);
        profilePhoneNumber = findViewById(R.id.phoneNumber);
        profilePassword = findViewById(R.id.profilePassword);
        titleName = findViewById(R.id.titleName);
        titlePhoneNumber = findViewById(R.id.titlePhoneNumber);
        editProfile = findViewById(R.id.editButton);
        showAllUserData();
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passUserData();
            }
        });
    } //oneCreate end ======================================


    //============================================================
    public void showAllUserData() {
        Intent intent = getIntent();
        String nameUser = intent.getStringExtra("name");
        String emailUser = intent.getStringExtra("email");
        String userphoneNumber = intent.getStringExtra("phoneNumber");
        String passwordUser = intent.getStringExtra("password");
        titleName.setText(nameUser);
        titlePhoneNumber.setText(userphoneNumber);
        profileName.setText(nameUser);
        profileEmail.setText(emailUser);
        profilePhoneNumber.setText(userphoneNumber);
        profilePassword.setText(passwordUser);
    }

    public void passUserData() {
        String userPhoneNumber = profilePhoneNumber.getText().toString().trim();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("phoneNumber").equalTo(userPhoneNumber);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String nameFromDB = snapshot.child(userPhoneNumber).child("name").getValue(String.class);
                    String emailFromDB = snapshot.child(userPhoneNumber).child("email").getValue(String.class);
                    String phoneNumberFromDB = snapshot.child(userPhoneNumber).child("phoneNumber").getValue(String.class);
                    String passwordFromDB = snapshot.child(userPhoneNumber).child("password").getValue(String.class);
                    Intent intent = new Intent(MainActivity.this, EditProfileActivity.class);

                    intent.putExtra("name", nameFromDB);
                    intent.putExtra("email", emailFromDB);
                    intent.putExtra("phoneNumber", phoneNumberFromDB);
                    intent.putExtra("password", passwordFromDB);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }



}