package com.aviral.apnews.Params;

public class News {

    public static final String API_KEY = "5c03efe7f5754c55bd0bb8180053aaff";

    private final String Title;
    private final String ImageUrl;
    private final String Description;
    private final String Url;
    private final String Author;
    private final String PublishedAt;
    private final String Source;

    public News(String title, String imageUrl, String description, String url, String author, String publishedAt, String source) {
        this.Title = title;
        this.ImageUrl =imageUrl;
        this.Description = description;
        this.Url = url;
        this.Author = author;
        this.PublishedAt = publishedAt;
        this.Source = source;
    }

    public String getTitle() {
        return Title;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public String getDescription() {
        return Description;
    }

    public String getUrl() {
        return Url;
    }

    public String getAuthor() {
        return Author;
    }

    public String getPublishedAt() {
        return PublishedAt;
    }

    public String getSource() {
        return Source;
    }
}
