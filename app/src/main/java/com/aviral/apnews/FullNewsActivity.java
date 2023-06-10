package com.aviral.apnews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class FullNewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_news);

        SharedPreferences darkModePreferences = getSharedPreferences("UserSettings", MODE_PRIVATE);
        boolean isDarkModeEnabled = darkModePreferences.getBoolean("isNightModeEnabled", true);

        if (isDarkModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        final TextView headingText = findViewById(R.id.dummy_heading_desc);
        final TextView descriptionText = findViewById(R.id.news_description_desc);
        final ImageView newsImageDescription = findViewById(R.id.news_image_desc);
        final Button readMore = findViewById(R.id.read_more);
        final ImageView backToMainDesc = findViewById(R.id.backToMainDesc);
        final TextView authorDesc = findViewById(R.id.author);
        final TextView sourceDesc = findViewById(R.id.source);
        final ImageView shareNews = findViewById(R.id.shareNewsDesc);
        final ImageView contactInfo = findViewById(R.id.contact_news);

        Intent intent = getIntent();
        String heading = intent.getStringExtra("heading");
        String description = intent.getStringExtra("description");
        String imageUrl = intent.getStringExtra("imageUrl");
        String websiteUrl = intent.getStringExtra("websiteUrl");
        String author = intent.getStringExtra("author");
        String source = intent.getStringExtra("source");

        headingText.setText(heading);
        descriptionText.setText(description);
        authorDesc.setText(author);
        sourceDesc.setText(source);

        Picasso.get().load(imageUrl).resize(400, 227).error(R.drawable.ic_sample_news_background).into(newsImageDescription);
        contactInfo.setOnClickListener(view -> {
            Intent intentToContact = new Intent(FullNewsActivity.this, ContactUsActivity.class);
            intentToContact.putExtra("source", "report");
            startActivity(intentToContact);
        });

        readMore.setOnClickListener(view -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl));
            startActivity(browserIntent);
        });

        backToMainDesc.setOnClickListener(view -> {
            Intent intentToMain = new Intent(FullNewsActivity.this, MainActivity.class);
            startActivity(intentToMain);
        });

        shareNews.setOnClickListener(view -> {

            String shareString = heading + "\n\n" +  description + "\n\n\n" + "News By"+ R.string.app_name + "Download now!";
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareString);
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        });

    }
}