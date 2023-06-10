package com.aviral.apnews.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aviral.apnews.FullNewsActivity;
import com.aviral.apnews.Params.News;
import com.aviral.apnews.R;
import com.squareup.picasso.Picasso;


import java.util.List;


public class NewsViewAdapter extends RecyclerView.Adapter<NewsViewAdapter.ViewHolder> {

    private final List<News> newsList;
    private final Context context;

    public NewsViewAdapter(Context context, List<News> newsList) {
        this.newsList = newsList;
        this.context = context;
    }


    @NonNull
    @Override
    public NewsViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        News news = newsList.get(position);

        holder.heading.setText(news.getTitle());
        holder.date.setText(news.getPublishedAt());
        Picasso.get()
                .load(news.getImageUrl())
                .resize(100, 150)
                .centerCrop()
                .error(R.drawable.ic_sample_news_background)
                .into(holder.newsImage);


    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView heading;
        private final TextView date;
        private final ImageView newsImage;

        public ViewHolder(View itemView) {
            super(itemView);

            heading = itemView.findViewById(R.id.heading);
            date = itemView.findViewById(R.id.date);
            newsImage = itemView.findViewById(R.id.news_image);

            itemView.setOnClickListener(view -> {
                int position = this.getAdapterPosition();
                News news = newsList.get(position);

                String heading = news.getTitle();
                String description = news.getDescription();
                String imageUrl = news.getImageUrl();
                String websiteUrl = news.getUrl();
                String author = news.getAuthor();
                String source = news.getSource();

                Intent intent = new Intent(context, FullNewsActivity.class);
                intent.putExtra("heading", heading);
                intent.putExtra("description", description);
                intent.putExtra("imageUrl", imageUrl);
                intent.putExtra("websiteUrl", websiteUrl);
                intent.putExtra("author", author);
                intent.putExtra("source", source);
                context.startActivity(intent);
            });
        }


        @Override
        public void onClick(View view) {
            int position = this.getAdapterPosition();
            News news = newsList.get(position);

            String heading = news.getTitle();
            String description = news.getDescription();
            String imageUrl = news.getImageUrl();
            String websiteUrl = news.getUrl();

            Intent intent = new Intent(context, FullNewsActivity.class);
            intent.putExtra("heading", heading);
            intent.putExtra("description", description);
            intent.putExtra("imageUrl", imageUrl);
            intent.putExtra("websiteUrl", websiteUrl);
            context.startActivity(intent);
        }
    }
}
