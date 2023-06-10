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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;

public class ShareNewsSettingsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private EditText textArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_news_settings);

        SharedPreferences darkModePreferences = getSharedPreferences("UserSettings", MODE_PRIVATE);
        boolean isDarkModeEnabled = darkModePreferences.getBoolean("isNightModeEnabled", true);

        if (isDarkModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        final Button saveShareNewsSettings = findViewById(R.id.save_news_share_settings);

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

        textArea = findViewById(R.id.textArea_news_share_settings);

        String textAreaText = "This news is shared by ApNews! \n Download App Now for getting latest news information across the world";


        saveShareNewsSettings.setOnClickListener(view -> {
            textArea.getText();
            SharedPreferences shareNewsSettingsText =
                    getSharedPreferences("shareNewsSettingsTextArea", MODE_PRIVATE);

            final SharedPreferences.Editor editor = shareNewsSettingsText.edit();
            editor.putString("textAreaText", String.valueOf(textArea.getText()));
            editor.apply();

        });

        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences sharedPreferences = getSharedPreferences("shareNewsSettingsTextArea", MODE_PRIVATE);
        String textAreaTextByPreference = sharedPreferences.getString("textAreaText", textAreaText);

        textArea.setText(textAreaTextByPreference);
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
                startActivity(new Intent(ShareNewsSettingsActivity.this, MainActivity.class));
                break;
            case R.id.user_profile:
                Intent intent = new Intent(ShareNewsSettingsActivity.this, UserProfileActivity.class);
                startActivity(intent);
                drawerLayout.setSelected(true);
                break;
            case R.id.settings_side_menu:
                Intent intentForSetting = new Intent(ShareNewsSettingsActivity.this, SettingsActivity.class);
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
                Intent shaneNewsSettingsIntent = new Intent(ShareNewsSettingsActivity.this, ShareNewsSettingsActivity.class);
                startActivity(shaneNewsSettingsIntent);
                drawerLayout.setSelected(true);
                break;
            case R.id.contact_us:
                Intent contactUsIntent = new Intent(ShareNewsSettingsActivity.this, ContactUsActivity.class);
                contactUsIntent.putExtra("source", "contact");
                startActivity(contactUsIntent);
                drawerLayout.setSelected(true);
                break;
            case R.id.report:
                Intent reportIntent = new Intent(ShareNewsSettingsActivity.this, ContactUsActivity.class);
                reportIntent.putExtra("source", "report");
                startActivity(reportIntent);
                drawerLayout.setSelected(true);
                break;
            case R.id.rate_us:
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