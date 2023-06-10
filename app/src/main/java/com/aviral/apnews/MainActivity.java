package com.aviral.apnews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.aviral.apnews.Adapter.NewsViewAdapter;
import com.aviral.apnews.Params.News;
import com.aviral.apnews.Utils.Spacing;
import com.google.android.material.navigation.NavigationView;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.util.ArrayList;

import static com.aviral.apnews.Params.News.API_KEY;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView newsRecyclerView;
    private NewsViewAdapter newsViewAdapter;
    private ArrayList<News> newsArrayList;
    private News news;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Hell Code it written by me

        newsRecyclerView = findViewById(R.id.news_recycler_view);
        newsRecyclerView.setHasFixedSize(true);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Spacing itemSpacing = new Spacing(10);
        newsRecyclerView.addItemDecoration(new DividerItemDecoration(newsRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        newsRecyclerView.addItemDecoration(itemSpacing);

        boolean connected;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        //we are connected to a network
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

        if (connected) {

            SharedPreferences darkModePreferences = getSharedPreferences("UserSettings", MODE_PRIVATE);
            boolean isDarkModeEnabled = darkModePreferences.getBoolean("isNightModeEnabled", true);

            if (isDarkModeEnabled) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }

            ProgressBar loadingBar = findViewById(R.id.loadingBar);
            loadingBar.setVisibility(View.VISIBLE);

            newsArrayList = new ArrayList<>();

                NewsApiClient newsApiClient = new NewsApiClient(API_KEY);

                    // /v2/everything
                newsApiClient.getTopHeadlines(
                        new TopHeadlinesRequest.Builder()
                                .language("en")
                                .pageSize(100)
                                .build(),
                        new NewsApiClient.ArticlesResponseCallback() {
                            @Override
                            public void onSuccess(ArticleResponse response) {
                                for (int i = 0; i <= 55; i++) {

                                    String title = response.getArticles().get(i).getTitle();
                                    String urlToImage = response.getArticles().get(i).getUrlToImage();
                                    String description = response.getArticles().get(i).getDescription();
                                    String url = response.getArticles().get(i).getUrl();
                                    String author = response.getArticles().get(i).getAuthor();
                                    String publishedAt = response.getArticles().get(i).getPublishedAt();
                                    String source = String.valueOf(response.getArticles().get(i).getSource());

                                    news = new News(title, urlToImage, description, url, author, publishedAt, source);
                                    newsArrayList.add(news);
                                    Log.d("AviralKaushik", response.getArticles().get(0).getTitle());
                                }
                                newsViewAdapter = new NewsViewAdapter(MainActivity.this, newsArrayList);
                                newsRecyclerView.setAdapter(newsViewAdapter);
                            }

                            @Override
                            public void onFailure(Throwable throwable) {
                                System.out.println(throwable.getMessage());
                                Log.d("Aviral", throwable.getMessage());
                            }
                        }
                        );

                newsViewAdapter = new NewsViewAdapter(MainActivity.this, newsArrayList);
                newsRecyclerView.setAdapter(newsViewAdapter);
                loadingBar.setVisibility(View.GONE);

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

                final ImageView searchNews = findViewById(R.id.search_news_main);
                searchNews.setOnClickListener(view -> {
                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    startActivity(intent);
                });

                final ImageView sidemenu = findViewById(R.id.side_menu);
                sidemenu.setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));

                navigationView.bringToFront();
                navigationView.setNavigationItemSelectedListener(this);

        } else {
            startActivity(new Intent(MainActivity.this, NoInternetActivity.class));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        newsViewAdapter = new NewsViewAdapter(MainActivity.this, newsArrayList);
        newsRecyclerView.setAdapter(newsViewAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        newsViewAdapter = new NewsViewAdapter(MainActivity.this, newsArrayList);
        newsRecyclerView.setAdapter(newsViewAdapter);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        newsViewAdapter = new NewsViewAdapter(MainActivity.this, newsArrayList);
        newsRecyclerView.setAdapter(newsViewAdapter);
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
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.user_profile:
                Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                startActivity(intent);
                drawerLayout.setSelected(true);
                break;
            case R.id.settings_side_menu:
                Intent intentForSetting = new Intent(MainActivity.this, SettingsActivity.class);
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
                Intent shaneNewsSettingsIntent = new Intent(MainActivity.this, ShareNewsSettingsActivity.class);
                startActivity(shaneNewsSettingsIntent);
                drawerLayout.setSelected(true);
                break;
            case R.id.contact_us:
                Intent contactUs = new Intent(MainActivity.this, ContactUsActivity.class);
                contactUs.putExtra("source", "contact");
                startActivity(contactUs);
                drawerLayout.setSelected(true);
                break;
            case R.id.report:
                Intent reportIntent = new Intent(MainActivity.this, ContactUsActivity.class);
                reportIntent.putExtra("source", "report");
                startActivity(reportIntent);
                drawerLayout.setSelected(true);
                break;
            case R.id.rate_us:
                Intent rateUsIntent = new Intent(MainActivity.this, ContactUsActivity.class);
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