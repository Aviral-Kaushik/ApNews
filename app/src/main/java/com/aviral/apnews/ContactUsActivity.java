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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aviral.apnews.Params.Contact;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ContactUsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private FirebaseAuth mAuth;
    private EditText name, email, message;
    private RatingBar ratingBar;
    private DatabaseReference mDatabase;
    private TextView firstHeaderText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        SharedPreferences darkModePreferences = getSharedPreferences("UserSettings", MODE_PRIVATE);
        boolean isDarkModeEnabled = darkModePreferences.getBoolean("isNightModeEnabled", true);

        if (isDarkModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        firstHeaderText = findViewById(R.id.first_header_text);
        name = findViewById(R.id.contact_us_name);
        email = findViewById(R.id.contact_us_email);
        message = findViewById(R.id.textArea_contact_us);
        ratingBar = findViewById(R.id.ratingBarContactUs);
        Button contactUsSubmit = findViewById(R.id.contact_us_submit);
        
        SharedPreferences userDetailsPreferences = getSharedPreferences("userDetails", MODE_PRIVATE);
        String namePreferences = userDetailsPreferences.getString("firstname", null);
        String emailPreferences = userDetailsPreferences.getString("email", null);

        name.setText(namePreferences);
        email.setText(emailPreferences);

        ratingBar.setVisibility(View.INVISIBLE);

        Intent getIntent = getIntent();
        String source = getIntent.getStringExtra("source");

        if (source.equals("report")) {
            firstHeaderText.setText(R.string.report);
            ratingBar.setVisibility(View.INVISIBLE);
        }

        if (source.equals("rate")) {
            firstHeaderText.setText(R.string.rate_header);
            ratingBar.setVisibility(View.VISIBLE);
        }

        contactUsSubmit.setOnClickListener(view -> sendToFirebase());


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

        ImageView sidemenu = findViewById(R.id.side_menu);
        sidemenu.setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));

        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void sendToFirebase() {

        Contact contact = new Contact(String.valueOf(name.getText()),
                String.valueOf(email.getText()),
                String.valueOf(message.getText()),
                String.valueOf(ratingBar.getNumStars()),
                String.valueOf(firstHeaderText.getText()));

        mDatabase.child("contact").child(String.valueOf(name.getText())).setValue(contact);
        
        name.setText("");
        email.setText("");
        message.setText("");

        Toast.makeText(this, "Thank you for your response, Your response has been send to admin!", Toast.LENGTH_SHORT).show();

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
                startActivity(new Intent(ContactUsActivity.this, MainActivity.class));
                break;
            case R.id.user_profile:
                Intent intent = new Intent(ContactUsActivity.this, UserProfileActivity.class);
                startActivity(intent);
                drawerLayout.setSelected(true);
                break;
            case R.id.settings_side_menu:
                Intent intentForSetting = new Intent(ContactUsActivity.this, SettingsActivity.class);
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
                Intent shaneNewsSettingsIntent = new Intent(ContactUsActivity.this, ShareNewsSettingsActivity.class);
                startActivity(shaneNewsSettingsIntent);
                drawerLayout.setSelected(true);
                break;
            case R.id.contact_us:
                drawerLayout.setSelected(true);
                break;
            case R.id.report:
                Intent reportIntent = new Intent(ContactUsActivity.this, ContactUsActivity.class);
                reportIntent.putExtra("source", "report");
                startActivity(reportIntent);
                drawerLayout.setSelected(true);
                break;
            case R.id.rate_us:
                Intent rateUsIntent = new Intent(ContactUsActivity.this, ContactUsActivity.class);
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