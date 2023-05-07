package com.example.newsapp;

public class Article {

    /**
     * Magnitude of the earthquake
     */
    private String mWebTitle;
    private String mDate;
    private String mUrl;
    private String mSection;
    private String mAuthor;


    public Article(String webTitle, String date, String url, String section, String autor) {
        mWebTitle = webTitle;
        mDate = date;
        mUrl = url;
        mSection = section;
        mAuthor = autor;
    }

    /**
     * Returns the Title of the Article.
     */
    public String getWebTitle() {
        return mWebTitle;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getDate() {
        return mDate;
    }

    public String getSection() {
        return mSection;
    }

}