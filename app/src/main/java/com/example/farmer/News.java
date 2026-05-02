package com.example.farmer;

public class News {
    public String id;
    public String title;
    public String description;
    public String url;
    public long timestamp;

    public News() {}

    public News(String id, String title, String description, String url, long timestamp) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.url = url;
        this.timestamp = timestamp;
    }
}

