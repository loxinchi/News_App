package com.example.android.newsapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.newsapp.News;
import com.example.android.newsapp.R;

import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private static final String LOG_TAG = NewsAdapter.class.getSimpleName();
    private static final String CONTENTS_RESTRICTION = " ... ";
    private Context context;
    private ArrayList<News> newsModelArrayList;

    // Constructor
    /**
     * Create a new {@link NewsAdapter} object.
     *
     * @param context                   is the current context (i.e. Activity) that the adapter is being created in.
     * @param newsModelArrayList        is the list of {@link News} to be displayed.
     */
    public NewsAdapter(Context context, ArrayList<News> newsModelArrayList) {
        this.context = context;
        this.newsModelArrayList = newsModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_card, parent, false);
//        return new ViewHolder(view);
        return new ViewHolder(
                LayoutInflater.from(context)
                .inflate(R.layout.news_card, parent, false)
        );
    }

    // Set data to textView and imageView of card layout
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Find the current news that was clicked on
        News currentNews = newsModelArrayList.get(position);
        // Set text and image to the corresponded textView and imageView
        holder.headlineTextView.setText(currentNews.getHeadline());
        holder.sectionTextView.setText(currentNews.getSection());
        holder.contentTextView.setText(currentNews.getContents());
        holder.authorTextView.setText(currentNews.getAuthor());
        holder.timeTextView.setText(getTimeDifference(formatDate(currentNews.getTimeInMilliseconds())));
        holder.thumbnailImageView.setImageDrawable(LoadImageFromWebOperations(currentNews.getThumbnailUrl()));

        // Set OnClickListener to browse by the news article URL
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri newsUri = Uri.parse(currentNews.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                if (websiteIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(websiteIntent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number
        // of card items in recycler view.
        return newsModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView headlineTextView;
        private TextView sectionTextView;
        private TextView contentTextView;
        private TextView authorTextView;
        private TextView timeTextView;
        private ImageView thumbnailImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            headlineTextView = itemView.findViewById(R.id.headline_text_view);
            sectionTextView = itemView.findViewById(R.id.section_text_view);
            contentTextView = itemView.findViewById(R.id.contents_text_view);
            authorTextView = itemView.findViewById(R.id.author_text_view);
            timeTextView = itemView.findViewById(R.id.time_text_view);
            thumbnailImageView = itemView.findViewById(R.id.card_image_view);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }

    /**
     * Convert date and time in UTC (firstPublicationDate) into Local time
     *
     * @param dateStringUTC is the first Publication Date of the news (i.e. 2021-01-18T06:20:21Z)
     * @return the formatted date string in Local time(i.e 18 JAN, 2021  6:20 AM)
     */
    private String formatDate(String dateStringUTC) {
        // Parse the dateString into a Date object
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss'Z'");
        Date dateObj = null;
        try {
            dateObj = simpleDateFormat.parse(dateStringUTC);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat df = new SimpleDateFormat("d MMM, yyyy  h:mm a", Locale.ENGLISH);
        String formattedDateUTC = df.format(dateObj);
        // Convert UTC into Local time
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = df.parse(formattedDateUTC);
            df.setTimeZone(TimeZone.getDefault());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return df.format(date);
    }

    /**
     * Get the formatted first Publication Date string in milliseconds
     * @param formattedDate string of the formatted first Publication Date
     * @return milliseconds of the formatted first Publication Date
     */
    private static long getDateInMillis(String formattedDate) {
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("d MMM, yyyy  h:mm a");
        long dateMilliseconds;
        Date dateObj;
        try {
            dateObj = simpleDateFormat.parse(formattedDate);
            dateMilliseconds = dateObj.getTime();
            return dateMilliseconds;
        } catch (ParseException e) {
            Log.e(LOG_TAG, "Problem parsing date error: "+  e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Get the time difference between the current date and first Publication Date Time
     * @param formattedDate string of the formatted first Publication Date Time
     * @return time difference (i.e "1 day ago")
     */
    private CharSequence getTimeDifference(String formattedDate) {
        long currentTime = System.currentTimeMillis();
        long publicationTime = getDateInMillis(formattedDate);
        return DateUtils.getRelativeTimeSpanString(publicationTime, currentTime,
                DateUtils.SECOND_IN_MILLIS);
    }

    /**
     * Get thumbnail image from news thumbnail URL
     * @param url string url of the news thumbnail
     * @return Drawable image
     */
    private static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream inputStream = (InputStream) new URL(url).getContent();
            Drawable drawableImage = Drawable.createFromStream(inputStream, "thumbnailImage");
            return drawableImage;
        } catch (Exception e) {
            Log.e(LOG_TAG, "Problem download drawable error: " + e.getMessage());
            return null;
        }
    }

    /**
     *  Clear all data (a list of {@link News} objects)
     */
    public void clearAll() {
        newsModelArrayList.clear();
        notifyDataSetChanged();
    }

    /**
     * Add  a list of {@link News}
     * @param newsList is the list of news, which is the data source of the adapter
     */
    public void addAll(List<News> newsList) {
        newsModelArrayList.clear();
        newsModelArrayList.addAll(newsList);
        notifyDataSetChanged();
    }
}
