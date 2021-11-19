package com.example.allnews;

public class News {
    String title;
    String author;
    String url;
    String imageURL;

    public News(String title, String author, String url, String urlToImage) {
        this.title=title;
        this.author=author;
        this.url=url;
        this.imageURL= urlToImage;
    }
}
