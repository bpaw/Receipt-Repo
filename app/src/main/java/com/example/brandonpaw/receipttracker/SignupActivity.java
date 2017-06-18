package com.example.brandonpaw.receipttracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    // User sign up specific fields
    EditText nameEditText;
    EditText emailEditText;
    EditText passwordEditText;
    EditText passwordConfirmedEditText;
    Button signUpButton;

    // Used for parsing Calendar.MONTH values
    String[] months = {"January", "February", "March", "April", "May", "June", "July", "August",
                       "September", "October", "November", "December", };

    // Firebase necessary fields
    private FirebaseAuth firebaseAuth;
    private FirebaseUser fireyUser;
    private DatabaseReference databaseReference;

    // Members used for UX
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize the Firebase member(s)
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Initialize User sign up members
        nameEditText = (EditText) findViewById(R.id.name_field);
        emailEditText = (EditText) findViewById(R.id.email_field);
        passwordEditText = (EditText) findViewById(R.id.password_field);
        passwordConfirmedEditText = (EditText) findViewById(R.id.confirm_password_field);
        signUpButton = (Button) findViewById(R.id.signup_button);

        // Initialize the UX members
        progressDialog = new ProgressDialog(this);

        signUpButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view == signUpButton) {

            String name = nameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String passwordC = passwordConfirmedEditText.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email)||
                    TextUtils.isEmpty(password) || TextUtils.isEmpty(passwordC)) {

                Toast.makeText(SignupActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT)
                        .show();
            }
            else {

                progressDialog.setMessage("Registering User...");
                progressDialog.show();

                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();


                        if (task.isSuccessful()) {
                            // Start the homepage activity
                            Toast.makeText(SignupActivity.this, "Register successful", Toast.LENGTH_SHORT).show();

                            finish();
                            Intent homepageIntent = new Intent(getApplicationContext(), HomepageActivity.class);
                            startActivity(homepageIntent);

                            String name = nameEditText.getText().toString().trim();
                            ArrayList<String> folders = new ArrayList<>();

                            // Get the current
                            Calendar c = Calendar.getInstance();
                            int month = c.get(Calendar.MONTH);
                            String year = String.valueOf(c.get(Calendar.YEAR));
                            String initialFolder = months[month] + " " + year;
                            User UserInformation = new User(name, folders);
                            folders.add(initialFolder);

                            fireyUser = firebaseAuth.getCurrentUser();
                            databaseReference.child("Users").child(fireyUser.getUid()).setValue(UserInformation);
                        }
                        else {
                            Toast.makeText(SignupActivity.this, "Could not register, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }
}