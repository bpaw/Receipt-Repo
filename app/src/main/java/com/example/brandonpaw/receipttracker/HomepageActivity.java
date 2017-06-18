package com.example.brandonpaw.receipttracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

public class HomepageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Firebase necessary fields
    private FirebaseAuth firebaseAuth;

    // Variables used to create a Floating Action Button Driver
    int FAB = 0;
    int HOME = 1;
    int RECEIPTS = 2;
    int FOLDERS = 3;
    int STATS = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize the Firebase member(s)
        firebaseAuth = FirebaseAuth.getInstance();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageBitmap(textAsBitmap("+", 18, Color.WHITE));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (FAB == HOME || FAB == RECEIPTS) {
                    // Start a new Activity to add a receipt
                    Intent addReceiptActivity = new Intent(getApplicationContext(), addReceipt.class);
                    startActivity(addReceiptActivity);
                }
                if (FAB == FOLDERS) {

                    // Start a new Activity to add a folder
                    Toast.makeText(getApplicationContext(), "IMPLEMENT addFolders.java YO", Toast.LENGTH_SHORT).show();
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        displaySelectedScreen(R.id.nav_home);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.homepage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Conduct appropriate operation based on the ID given
        switch(id) {

            case R.id.action_settings:
                break;
            case R.id.action_sign_out:

                // Confirm with the user that they want to log out
                String confirmation = "Are you sure you want to sign out?";
                final String signOutY = "Signing out...";
                new AlertDialog.Builder(this)
                    .setTitle("Sign Out")
                    .setMessage(confirmation)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                    {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // Tell User that the app is signing user out
                            Toast.makeText(getApplicationContext(), signOutY, Toast.LENGTH_SHORT)
                            .show();

                            // Sign the user out, close current activity, start MainActivity
                            firebaseAuth.signOut();
                            finish();
                            Intent loginActivity = new Intent(getApplicationContext(),
                                    MainActivity.class);
                            startActivity(loginActivity);
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        // Call a helper method used as a driver for navigation item clicks
        displaySelectedScreen(item.getItemId());

        return true;
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object and String object
        Fragment fragment = null;
        String title = getTitle().toString().trim();

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_home:
                fragment = new fragment_homepage();
                title = "Home";
                FAB = HOME;
                break;
            case R.id.nav_receipts:
                fragment = new fragment_receipt();
                title = "Receipts";
                FAB = RECEIPTS;
                break;
            case R.id.nav_folders:
                fragment = new fragment_folders();
                title = "Folders";
                FAB = FOLDERS;
                break;
            case R.id.nav_stats:
                fragment = new fragment_statistics();
                title = "Statistics";
                FAB = STATS;
                break;
        }

        // Replacing the current layout with the specified page's fragment
        if (fragment != null) {

            // Publish the fragment
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();

            // Set the title
            setTitle(title);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public static Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint(ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.0f); // round
        int height = (int) (baseline + paint.descent() + 0.0f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }
}
