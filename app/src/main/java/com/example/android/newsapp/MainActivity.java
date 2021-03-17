package com.example.android.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;

import com.example.android.newsapp.adapter.NewsAdapter;
import com.example.android.newsapp.adapter.NewsFragmentPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();

    /** URI of The Gardian */
    private static final String USGS_REQUEST_URL = "https://content.guardianapis.com/search";

    /**
     * Constant value for the newsLoader ID.
     */
    private static final int EARTHQUAKE_LOADER_ID = 1;

    /** Adapter for the list of news */
    private NewsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        NewsFragmentPagerAdapter adapter = new NewsFragmentPagerAdapter(this, getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
    }

}