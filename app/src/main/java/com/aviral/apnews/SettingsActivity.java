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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.hbb20.CountryCodePicker;

public class SettingsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Switch enableDarkModeSwitch;
    private CheckBox enableNotificationCheckBox;
    private CountryCodePicker countyCodePickerSettings;
    private ProgressBar progressBarSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        progressBarSettings = findViewById(R.id.progressBarSettings);
        progressBarSettings.setVisibility(View.VISIBLE);

        countyCodePickerSettings = findViewById(R.id.county_code_picker_settings);
        enableNotificationCheckBox = findViewById(R.id.enable_notification_check);
        enableDarkModeSwitch = findViewById(R.id.dark_mode_switch);

        SharedPreferences getSettingsPreferences = getSharedPreferences("UserSettings", MODE_PRIVATE);
        String userCountry = getSettingsPreferences.getString("userCountry", "91");
        boolean isNightModeEnabled = getSettingsPreferences.getBoolean("isNightModeEnabled", true);
        boolean isNotificationsEnabled = getSettingsPreferences.getBoolean("isNotificationsEnabled", false);

        countyCodePickerSettings.setCountryForNameCode(userCountry);
        if (isNightModeEnabled) {
            enableDarkModeSwitch.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            enableDarkModeSwitch.setChecked(false);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        if (isNotificationsEnabled) {
            enableNotificationCheckBox.setChecked(true);
        }

        progressBarSettings.setVisibility(View.INVISIBLE);

        enableDarkModeSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> {

            progressBarSettings.setVisibility(View.VISIBLE);

            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                SharedPreferences saveUserSettingsPreferences = getSharedPreferences("UserSettings", MODE_PRIVATE);
                SharedPreferences.Editor saveUserSettingsPreferencesEditor = saveUserSettingsPreferences.edit();
                saveUserSettingsPreferencesEditor.putBoolean("isNightModeEnabled", enableDarkModeSwitch.isChecked());
                saveUserSettingsPreferencesEditor.apply();

            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                SharedPreferences disableNightModePreferences = getSharedPreferences("UserSettings", MODE_PRIVATE);
                SharedPreferences.Editor disableNightModePreferencesEditor = disableNightModePreferences.edit();
                disableNightModePreferencesEditor.putBoolean("isNightModeEnabled", false);
                disableNightModePreferencesEditor.apply();
                enableDarkModeSwitch.setChecked(false);
            }

            progressBarSettings.setVisibility(View.INVISIBLE);
        });

        ImageView sidemenu = findViewById(R.id.side_menu);
        sidemenu.setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));

        drawerLayout = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);
        final Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);

        final Button saveSettings = findViewById(R.id.saveSettings);
        saveSettings.setOnClickListener(view -> {
            progressBarSettings.setVisibility(View.VISIBLE);

            SharedPreferences saveUserSettingsPreferences = getSharedPreferences("UserSettings", MODE_PRIVATE);
            SharedPreferences.Editor saveUserSettingsPreferencesEditor = saveUserSettingsPreferences.edit();
            saveUserSettingsPreferencesEditor.putString("userCountry", countyCodePickerSettings.getSelectedCountryCode());
            saveUserSettingsPreferencesEditor.putBoolean("isNightModeEnabled", enableDarkModeSwitch.isChecked());
            saveUserSettingsPreferencesEditor.putBoolean("isNotificationsEnabled", enableNotificationCheckBox.isChecked());
            saveUserSettingsPreferencesEditor.apply();

            Toast.makeText(this, "Settings Saved Successfully", Toast.LENGTH_SHORT).show();

            progressBarSettings.setVisibility(View.INVISIBLE);
        });
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
                startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                break;
            case R.id.user_profile:
                Intent intent = new Intent(SettingsActivity.this, UserProfileActivity.class);
                startActivity(intent);
                drawerLayout.setSelected(true);
                break;
            case R.id.settings_side_menu:
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
                Intent shaneNewsSettingsIntent = new Intent(SettingsActivity.this, ShareNewsSettingsActivity.class);
                startActivity(shaneNewsSettingsIntent);
                drawerLayout.setSelected(true);
                break;
            case R.id.contact_us:
                Intent contactUsIntent = new Intent(SettingsActivity.this, ContactUsActivity.class);
                contactUsIntent.putExtra("source", "contact");
                startActivity(contactUsIntent);
                drawerLayout.setSelected(true);
                break;
            case R.id.report:
                Intent reportIntent = new Intent(SettingsActivity.this, ContactUsActivity.class);
                reportIntent.putExtra("source", "report");
                startActivity(reportIntent);
                drawerLayout.setSelected(true);
                break;
            case R.id.rate_us:
                Intent rateUsIntent = new Intent(SettingsActivity.this, ContactUsActivity.class);
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