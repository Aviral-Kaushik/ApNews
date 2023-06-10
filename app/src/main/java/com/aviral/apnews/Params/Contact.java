package com.aviral.apnews.Params;

public class Contact {

    private final String name;
    private final String email;
    private final String message;
    private final String rating;
    private final String type;

    public Contact(String name, String email, String message, String rating, String type) {
        this.name = name;
        this.email = email;
        this.message = message;
        this.rating = rating;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMessage() {
        return message;
    }

    public String getRating() {
        return rating;
    }

    public String getType() {
        return type;
    }
}
