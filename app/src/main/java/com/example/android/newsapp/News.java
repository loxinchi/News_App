package com.example.android.newsapp;

/**
 * {@link News} represents the News information that introduced in this app.
 *  It contains resource IDs for news headline, news section, author, time, Image URL, Website URL.
 */
public class News {

    /** String resource ID for news headline */
    private String mHeadline;

    /** String resource ID for news section */
    private String mSection;

    /** String resource ID for news contents */
//    private String mContents;

    /** String resource ID for news author */
    private String mAuthor;

    /** Time of the news article */
    private String mTimeInMilliseconds;

    /** Website URL of the news article */
    private String mUrl;

    /** Thumbnail image URL of the news */
    private String mThumbnailUrl;

    /**
     * Create a new Word object.
     *
     * @param headline is the string resource Id for the news headline
     * @param section is the string resource Id for the news section
//     * @param contents is the string resource Id for the news contents
     * @param author is the string resource Id for the news author
     * @param timeInMilliseconds is the resource ID for the date
     * @param url is the website URL to news article
     * @param thumbnailUrl is the URL of the news thumbnail image
     * */
    public News (String headline, String section, String author, String timeInMilliseconds, String url, String thumbnailUrl) {
        mHeadline = headline;
        mSection = section;
//        mContents = contents;
        mAuthor = author;
        mTimeInMilliseconds = timeInMilliseconds;
        mUrl = url;
        mThumbnailUrl = thumbnailUrl;
    }

    /**
     * Get the string resource ID for news headline
     */
    public String getHeadline() {
        return mHeadline;
    }

    /**
     * Get the string resource ID for news section
     */
    public String getSection() {
        return mSection;
    }

    /**
     * Get the string resource ID for news contents
     */
//    public String getContents() {
//        return mContents;
//    }

    /**
     * Get the string resource ID for news author
     */
    public String getAuthor() {
        return mAuthor;
    }

    /**
     * Returns the time of the news public timestamp.
     */
    public String getTimeInMilliseconds() {
        return mTimeInMilliseconds;
    }

    /**
     * Returns the website URL to the news article
     */
    public String getUrl() {
        return mUrl;
    }

    /**
     * Returns the website URL to the news article
     */
    public String getThumbnailUrl() {
        return mThumbnailUrl;
    }
}


