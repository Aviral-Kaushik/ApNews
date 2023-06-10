package com.aviral.apnews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.material.navigation.NavigationView;

public class UserProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private EditText firstNameText, lastNameText, ageText, usernameText, emailText;
    private ProgressBar progressBarUserProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        progressBarUserProfile = findViewById(R.id.progressBarUserProfile);
        progressBarUserProfile.setVisibility(View.VISIBLE);

        SharedPreferences darkModePreferences = getSharedPreferences("UserSettings", MODE_PRIVATE);
        boolean isDarkModeEnabled = darkModePreferences.getBoolean("isNightModeEnabled", true);

        if (isDarkModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        firstNameText = findViewById(R.id.first_name_text);
        lastNameText = findViewById(R.id.last_name_text);
        ageText = findViewById(R.id.age_text);
        usernameText = findViewById(R.id.username_text);
        emailText = findViewById(R.id.email_text);
        final Button updateUserDetails = findViewById(R.id.update_user_details);

        SharedPreferences userDetails = getSharedPreferences("userDetails", MODE_PRIVATE);
        String firstname = userDetails.getString("firstname", null);
        String lastname = userDetails.getString("lastname", null);
        String age = userDetails.getString("age", null);
        String username = userDetails.getString("username", null);
        String email = userDetails.getString("email", null);

        if (firstname != null) {
            firstNameText.setText(firstname);
        }
        if (lastname != null) {
            lastNameText.setText(lastname);
        }
        if ( age != null) {
            ageText.setText(age);
        }
        if (username != null) {
            usernameText.setText(username);
        }
        if (email != null) {
            emailText.setText(email);
        }

        progressBarUserProfile.setVisibility(View.INVISIBLE);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        ImageView sidemenu = findViewById(R.id.side_menu);
        sidemenu.setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));

        updateUserDetails.setOnClickListener(view -> {
            progressBarUserProfile.setVisibility(View.VISIBLE);
            SharedPreferences userDetailsPreferences = getSharedPreferences("userDetails", MODE_PRIVATE);
            SharedPreferences.Editor userDetailsPreferencesEditor = userDetailsPreferences.edit();
            userDetailsPreferencesEditor.putString("firstname", String.valueOf(firstNameText.getText()));
            userDetailsPreferencesEditor.putString("lastname", String.valueOf(lastNameText.getText()));
            userDetailsPreferencesEditor.putString("age", String.valueOf(ageText.getText()));
            userDetailsPreferencesEditor.putString("username", String.valueOf(usernameText.getText()));
            userDetailsPreferencesEditor.putString("email", String.valueOf(emailText.getText()));
            userDetailsPreferencesEditor.apply();
            progressBarUserProfile.setVisibility(View.INVISIBLE);
        });

        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.backToMain:
                startActivity(new Intent(UserProfileActivity.this, MainActivity.class));
                break;
            case R.id.user_profile:
                drawerLayout.setSelected(true);
                break;
            case R.id.settings_side_menu:
                Intent intentForSetting = new Intent(UserProfileActivity.this, SettingsActivity.class);
                startActivity(intentForSetting);
                drawerLayout.setSelected(true);
                break;
            case R.id.share_application:
                String shareString = "Download ApNews Now! \n"
                        + "For getting the information about latest news in real-time \n"
                        + "This application sis developed by Aviral kaushik";
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, shareString);
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
                drawerLayout.setSelected(true);
                break;
            case R.id.share_news_settings:
                Intent shaneNewsSettingsIntent = new Intent(UserProfileActivity.this, ShareNewsSettingsActivity.class);
                startActivity(shaneNewsSettingsIntent);
                drawerLayout.setSelected(true);
                break;
            case R.id.contact_us:
                Intent contactUsIntent = new Intent(UserProfileActivity.this, ContactUsActivity.class);
                contactUsIntent.putExtra("source", "contact");
                startActivity(contactUsIntent);
                drawerLayout.setSelected(true);
                break;
            case R.id.report:
                Intent reportIntent = new Intent(UserProfileActivity.this, ContactUsActivity.class);
                reportIntent.putExtra("source", "report");
                startActivity(reportIntent);
                drawerLayout.setSelected(true);
                break;
            case R.id.rate_us:
                Intent rateUsIntent = new Intent(UserProfileActivity.this, ContactUsActivity.class);
                rateUsIntent.putExtra("source", "rate");
                startActivity(rateUsIntent);
                drawerLayout.setSelected(true);
                break;
            case R.id.website:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://aviralkaushik.epizy.com/"));
                startActivity(browserIntent);
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}