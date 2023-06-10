package com.aviral.apnews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.aviral.apnews.Adapter.NewsViewAdapter;
import com.aviral.apnews.Params.News;
import com.aviral.apnews.Utils.Spacing;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.util.ArrayList;

import static com.aviral.apnews.Params.News.API_KEY;

public class SearchActivity extends AppCompatActivity {

    private EditText searchNews;
    private LottieAnimationView searchNewsAnimation, notFoundAnimation;
    private RecyclerView searchNewsRecyclerView;
    private ArrayList<News> searchNewsArrayList;
    private News news;
    private NewsViewAdapter searchNewsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        SharedPreferences darkModePreferences = getSharedPreferences("UserSettings", MODE_PRIVATE);
        boolean isDarkModeEnabled = darkModePreferences.getBoolean("isNightModeEnabled", true);

        if (isDarkModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        searchNewsRecyclerView = findViewById(R.id.searchNewsRecyclerView);
        searchNewsRecyclerView.setVisibility(View.INVISIBLE);

        searchNewsRecyclerView.setHasFixedSize(true);
        searchNewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Spacing itemSpacing = new Spacing(10);
        searchNewsRecyclerView.addItemDecoration(new DividerItemDecoration(searchNewsRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        searchNewsRecyclerView.addItemDecoration(itemSpacing);

        searchNewsArrayList = new ArrayList<>();

        searchNews = findViewById(R.id.search_news);
        searchNews.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        searchNews.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                View view = this.getCurrentFocus();
                InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                NewsApiClient newsApiClient = new NewsApiClient(API_KEY);

                newsApiClient.getEverything(
                        new EverythingRequest.Builder()
                                .q(String.valueOf(searchNews.getText()))
                                .build(),
                        new NewsApiClient.ArticlesResponseCallback() {
                            @Override
                            public void onSuccess(ArticleResponse response) {

                                if(!response.getArticles().isEmpty()) {
                                    for (int i = 0 ; i < 19; i++) {
                                        String title = response.getArticles().get(i).getTitle();
                                        String urlToImage = response.getArticles().get(i).getUrlToImage();
                                        String description = response.getArticles().get(i).getDescription();
                                        String url = response.getArticles().get(i).getUrl();
                                        String author = response.getArticles().get(i).getAuthor();
                                        String publishedAt = response.getArticles().get(i).getPublishedAt();
                                        String source = String.valueOf(response.getArticles().get(i).getSource());

                                        news = new News(title, urlToImage, description, url, author, publishedAt, source);
                                        searchNewsArrayList.add(news);
                                    }

                                    searchNewsRecyclerView.setVisibility(View.VISIBLE);
                                    searchNewsAnimation.setVisibility(View.GONE);

                                    searchNewsAdapter = new NewsViewAdapter(SearchActivity.this, searchNewsArrayList);
                                    searchNewsRecyclerView.setAdapter(searchNewsAdapter);

                                } else {
                                    notFoundAnimation.setVisibility(View.VISIBLE);
                                    searchNewsRecyclerView.setVisibility(View.GONE);
                                }

                            }

                            @Override
                            public void onFailure(Throwable throwable) {
                                notFoundAnimation.setVisibility(View.VISIBLE);
                                searchNewsRecyclerView.setVisibility(View.GONE);
                            }
                        }
                );
                handled = true;
            }
            return handled;
        });

        searchNewsAnimation = findViewById(R.id.searchingAnimation);
        notFoundAnimation = findViewById(R.id.notFoundAnimation);
        notFoundAnimation.setVisibility(View.INVISIBLE);


        ImageView back = findViewById(R.id.backToMain);
        back.setOnClickListener(view -> {
            Intent intent = new Intent(SearchActivity.this, MainActivity.class);
            startActivity(intent);
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        searchNewsAdapter = new NewsViewAdapter(this, searchNewsArrayList);
        searchNewsRecyclerView.setAdapter(searchNewsAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchNewsAdapter = new NewsViewAdapter(this, searchNewsArrayList);
        searchNewsRecyclerView.setAdapter(searchNewsAdapter);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        searchNewsAdapter = new NewsViewAdapter(this, searchNewsArrayList);
        searchNewsRecyclerView.setAdapter(searchNewsAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        View view = this.getCurrentFocus();
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}