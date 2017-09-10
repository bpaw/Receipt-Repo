package com.example.brandonpaw.receipttracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // User identification fields
    private Button signUpButton;
    private Button loginButton;
    private EditText usernameEditText;
    private EditText passwordEditText;

    // Firebase necessary fields
    private FirebaseAuth firebaseAuth;

    // Members used for UX
    private ProgressDialog progressDialog;

    public static Account user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the Firebase member(s)
        firebaseAuth = FirebaseAuth.getInstance();

        // Check if the user is logged in already
        if (firebaseAuth.getCurrentUser() != null) {
            // Get information on user from the server
            UtilREST util = new UtilREST(this);
            if (firebaseAuth.getCurrentUser().getEmail() != null)
                Toast.makeText(this, firebaseAuth.getCurrentUser().getEmail(), Toast.LENGTH_LONG).show();
            util.getAccount("bp1").addOnSuccessListener(new OnSuccessListener<JSONObject>() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    JSONArray accounts = null;
                    try {
                        accounts = jsonObject.getJSONArray("accounts");
                        PersistentDataSingleton.persistentData.user = new Account(accounts.getJSONObject(0));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            // Start the homepage activity
            finish();
            Intent homepageIntent = new Intent(this, HomepageActivity.class);
            startActivity(homepageIntent);
        }

        // initialize the user identification fields
        signUpButton = (Button) findViewById(R.id.signup_button);
        loginButton = (Button) findViewById(R.id.login_button);
        usernameEditText = (EditText) findViewById(R.id.username_text);
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
            String username = usernameEditText.getText().toString().trim();
            final String password = passwordEditText.getText().toString().trim();

            // Both the email and password must have been inputted (non-empty strings)
            if (TextUtils.isEmpty(username)) {

                return;
            }
            if (TextUtils.isEmpty(password)) {

                return;
            }

            // Alert the user that the registration process has begun
            progressDialog.setMessage("Logging in, please wait...");
            progressDialog.show();

            UtilREST util = new UtilREST(this);
            util.getAccount(username).addOnSuccessListener(new OnSuccessListener<JSONObject>() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    try {
                        JSONArray accounts = jsonObject.getJSONArray("accounts");
                        Log.e("BPAW", "The JSONObect in the response array is : " + accounts.getJSONObject(0).toString());
                        user = new Account(accounts.getJSONObject(0));

                        if (user == null) {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "There seems to have been an error trying to log you in", Toast.LENGTH_LONG).show();
                            return;
                        }

                        // Sign in the user using the email and password
                        String email = user.email;
                        final String emailUpdate = email;

                        firebaseAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {

                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        progressDialog.dismiss();

                                        if (task.isSuccessful()) {
                                            firebaseAuth.getCurrentUser().updateEmail(emailUpdate);
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
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void login() {
        // Get the email and password
        String email = usernameEditText.getText().toString().trim();
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