package com.pta.phonenumberlogin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
public class SignUp extends AppCompatActivity {


    TextInputEditText signupName, signup_phoneNumber, signupEmail, signupPassword;
    TextView loginRedirectText;

    Button signupButton;
    FirebaseDatabase database;
    DatabaseReference reference;

    ProgressBar progressBar;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        signupName = findViewById(R.id.signup_name);
        signupEmail = findViewById(R.id.signup_email);
        signup_phoneNumber = findViewById(R.id.signup_phoneNumber);
        signupPassword = findViewById(R.id.signup_password);
        loginRedirectText = findViewById(R.id.loginRedirectText);
        signupButton = findViewById(R.id.signup_button);



        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);

                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");

                String name = signupName.getText().toString();
                String email = signupEmail.getText().toString();
                String phoneNumber = signup_phoneNumber.getText().toString();
                String password = signupPassword.getText().toString();
                //=====================================================================

                //checking the validity of the email
                if(email.isEmpty())
                {
                    signupEmail.setError("Enter an email address");
                    signupEmail.requestFocus();
                    return;
                }

                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    signupEmail.setError("Enter a valid email address");
                    signupEmail.requestFocus();
                    return;
                }

                //checking the validity of the password
                if(password.isEmpty())
                {
                    signupPassword.setError("Enter a password");
                    signupPassword.requestFocus();
                    return;
                }

                if (password.length()<6){
                    signupPassword.setError("Minimum length of password should be 6");
                    signupPassword.requestFocus();
                    return;
                }
                // firebase ======================



                //======================================================================

                HelperClass helperClass = new HelperClass(name, email, phoneNumber, password);
                reference.child(phoneNumber).setValue(helperClass);
                Toast.makeText(SignUp.this, "You have signup successfully!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                Intent intent = new Intent(SignUp.this, SignIn.class);
                startActivity(intent);
                finish();
            }
        });
//=======================================================================
        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this, SignIn.class);
                startActivity(intent);
            }
        });

    }
}