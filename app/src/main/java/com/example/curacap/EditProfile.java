package com.example.curacap;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

public class EditProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true); // Show the back button
            actionBar.setDisplayShowHomeEnabled(true); // Show the app icon in the ActionBar
            actionBar.setTitle("Edit Profile"); // Set the title for the ActionBar
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed(); // Handle the back button click
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}