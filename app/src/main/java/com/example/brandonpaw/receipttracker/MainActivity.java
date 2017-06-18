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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // User identification fields
    private Button signUpButton;
    private Button loginButton;
    private EditText emailEditText;
    private EditText passwordEditText;

    // Firebase necessary fields
    private FirebaseAuth firebaseAuth;

    // Members used for UX
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the Firebase member(s)
        firebaseAuth = FirebaseAuth.getInstance();

        // Check if the user is logged in already
        if (firebaseAuth.getCurrentUser() != null) {

            // Start the homepage activity
            finish();
            Intent homepageIntent = new Intent(this, HomepageActivity.class);
            startActivity(homepageIntent);
        }

        // initialize the user identification fields
        signUpButton = (Button) findViewById(R.id.signup_button);
        loginButton = (Button) findViewById(R.id.login_button);
        emailEditText = (EditText) findViewById(R.id.email_text);
        passwordEditText = (EditText) findViewById(R.id.password_text);

        // Initialize the UX members
        progressDialog = new ProgressDialog(this);

        // Attach click listeners to the buttons
        signUpButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        // Click listeners for the sign up and login buttons
        if (view == signUpButton) {

            Intent signUpIntent = new Intent(this, SignupActivity.class);
            startActivity(signUpIntent);
        }
        if (view == loginButton) {

            // Get the email and password
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            // Both the email and password must have been inputted (non-empty strings)
            if (TextUtils.isEmpty(email)) {

                return;
            }
            if (TextUtils.isEmpty(password)) {

                return;
            }

            // Alert the user that the registration process has begun
            progressDialog.setMessage("Logging in, please wait...");
            progressDialog.show();

            // Sign in the user using the email and password
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    progressDialog.dismiss();

                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                        Intent homepageIntent = new Intent(getApplicationContext(), HomepageActivity.class);
                        finish();
                        startActivity(homepageIntent);
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Could not login, please try again", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}