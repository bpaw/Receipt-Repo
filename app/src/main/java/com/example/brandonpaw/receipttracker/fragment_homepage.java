package com.example.brandonpaw.receipttracker;


import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_homepage extends Fragment {

    // UI elements
    TextView greeting;

    // Firebase necessary fields
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    public fragment_homepage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);

        // Initialize the UI elements
        greeting = (TextView) view.findViewById(R.id.text_view_greeting);

        // Initialize the Firebase members
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());

        // Add a ValueEventListener to get the User's name
//        ValueEventListener postListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                // Get the User's information
//                User user= dataSnapshot.getValue(User.class);
//
//                // Parse the User's information to get the User's name
//                if (user != null) {
//                    String name = user.userName;
//                    if (name == null) {
//                        name = "";
//                    } else {
//                        int spaceIndex = name.indexOf(" ");
//                        if (spaceIndex != -1)
//                            name = name.substring(0, spaceIndex);
//                    }
//
//                    String currGreeting = "Hello, ";
//                    greeting.setText((currGreeting + " " + name));
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        };
//        databaseReference.addValueEventListener(postListener);

        return view;
    }

}
