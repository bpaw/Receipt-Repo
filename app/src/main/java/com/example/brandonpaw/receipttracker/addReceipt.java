package com.example.brandonpaw.receipttracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class addReceipt extends AppCompatActivity  implements View.OnClickListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    // UI components for the input EditTexts
    EditText inputReceipt;
    EditText inputTip;
    EditText inputTax;
    EditText inputTotal;
    EditText inputFolders;
    EditText inputDescription;
    DatePicker inputDate;

    // Button to mark adding receipts
    Button add;

    ArrayList<String> suggestions;

    byte[] imageBytes;
    Uri photoPath;

    // Firebase Authentication object used to get the user to write a new post to
    private FirebaseAuth firebaseAuth;
    private FirebaseUser fireyUser;
    private DatabaseReference databaseReference;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_receipt);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize the Firebase member(s)
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        // Initialize the UI input components in this file
        inputReceipt = (EditText) findViewById(R.id.add_receipt);
        inputTip = (EditText) findViewById(R.id.add_tip);
        inputTax = (EditText) findViewById(R.id.add_tax);
        inputTotal = (EditText) findViewById(R.id.add_total);
        inputFolders = (MultiAutoCompleteTextView) findViewById(R.id.add_folders);
        inputDescription = (EditText) findViewById(R.id.add_description);
        inputDate = (DatePicker) findViewById(R.id.add_date);
        add = (Button) findViewById(R.id.button_continue);

        add.setOnClickListener(this);

        // Initialize the suggestions field

        // Add a ValueEventListener to get the User's name
//        ValueEventListener postListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                // Get the User's information
//                User user = dataSnapshot.child("Users").child(firebaseAuth.getCurrentUser().getUid()).getValue(User.class);
//
////                suggestions = user.folders;
//
//                // Set the suggestions for our MultiAutoCompleteTeView for the folders information
//                MultiAutoCompleteTextView folderSuggestions = (MultiAutoCompleteTextView) findViewById(R.id.add_folders);
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.suggestion, suggestions);
//                folderSuggestions.setAdapter(adapter);
//                folderSuggestions.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        };
//        databaseReference.addValueEventListener(postListener);
    }

    public boolean requiredFieldsFilled() {

        String receiptVal = inputReceipt.getText().toString().trim();
        String totalVal = inputTotal.getText().toString().trim();

        return (!TextUtils.isEmpty(receiptVal) && !TextUtils.isEmpty(totalVal));
    }

    public String[] getValues() {

        String[] values = new String[5];

        values[0] = inputReceipt.getText().toString().trim();
        values[1] = inputTip.getText().toString().trim();
        values[2] = inputTax.getText().toString().trim();
        values[3] = inputTotal.getText().toString().trim();
        values[4] = inputFolders.getText().toString().trim();

        // Parse the text that have integral types
        values[1] = parseText(values[1]);
        values[2] = parseText(values[2]);
        values[3] = parseText(values[3]);

        // Return the final product
        return values;
    }

    public String parseText(String input) {

        try {
            String retVal = String.valueOf(Double.parseDouble(input));
            return retVal;
        } catch (NumberFormatException e) {
            String retVal = "0";
            return retVal;
        }
    }

    public ArrayList<String> parseFolders(String folders) {

        ArrayList<String> individualFolders = new ArrayList<>();
        int lastIndex = 0;


        while(folders.indexOf(",") != -1) {

            int index = folders.indexOf(",");
            String currFolder = folders.substring(lastIndex, index);
            individualFolders.add(currFolder);
            folders = folders.substring(index, folders.length());
        }

        return individualFolders;
    }

    private String dateFormatter(DatePicker date) {
        return (date.getYear() + "-" + (date.getMonth() + 1) + "-" + date.getDayOfMonth());
    }

    public void uploadReceipt() throws JSONException {

        Log.e("BPAW", "Calling uploadReceipt");

        // Error check the fields
//        if (!requiredFieldsFilled()) {
//            Toast.makeText(this, "Fill out all required fields...", Toast.LENGTH_SHORT).show();
//            return;
//        }


        // Upload the view_receipt object
        fireyUser = firebaseAuth.getCurrentUser();

        // Upload the image of the view_receipt if the user took a picture of it
        if (imageBytes != null) {

            String[] input = getValues();

            // Construct a Receipt object and write it to the Firebase Databse
            double tip = Double.parseDouble(input[1]);
            double tax = Double.parseDouble(input[2]);
            double total = Double.parseDouble(input[3]);
            String description = inputDescription.getText().toString().trim();

            Receipt receipt = new Receipt(input[0], tip, tax, total, input[4], dateFormatter(inputDate), description);
            receipt.photo = imageBytes;
            UtilREST util = new UtilREST(this);
            try {
                util.createReceipt(new Long(1), receipt);
            }
            catch (JSONException e) {
                Log.e("BPAW", e.getMessage());
            }

            finish();
        }
        else {

            // Get the user input to construct a Receipt object
            String[] input = getValues();

            // Construct a Receipt object and write it to the Firebase Databse
            double tip = Double.parseDouble(input[1]);
            double tax = Double.parseDouble(input[2]);
            double total = Double.parseDouble(input[3]);
            String description = inputDescription.getText().toString().trim();

//            Receipt receipt = new Receipt(input[0], tip, tax, total, input[4], dateFormatter(inputDate));
            Receipt receipt = new Receipt(input[0], tip, tax, total, input[4], dateFormatter(inputDate), description);
            UtilREST util = new UtilREST(this);
            util.createReceipt(new Long(1), receipt);
        }
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch(id) {
            case R.id.button_continue:

                String prompt = "Would you like to upload a picture of your view_receipt?";
                new AlertDialog.Builder(this)
                        .setTitle("Upload a picture?")
                        .setMessage(prompt)
                        .setPositiveButton("Add photo", new DialogInterface.OnClickListener()
                        {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dispatchTakePictureIntent();
                            }
                        })
                        .setNegativeButton("Skip photo", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    uploadReceipt();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).show();
                break;
        }
    }

    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //mImageView.setImageBitmap(imageBitmap);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            imageBytes = baos.toByteArray();

            try {
                uploadReceipt();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
