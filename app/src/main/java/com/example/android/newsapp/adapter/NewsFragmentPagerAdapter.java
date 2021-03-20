package com.example.android.newsapp.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.android.newsapp.Fragment.BusinessFragment;
import com.example.android.newsapp.Fragment.SportFragment;
import com.example.android.newsapp.Fragment.TechnologyFragment;
import com.example.android.newsapp.Fragment.WorldFragment;
import com.example.android.newsapp.R;

/**
 * Provides the appropriate {@link Fragment} for a view pager.
 */
public class NewsFragmentPagerAdapter extends FragmentPagerAdapter {

    /**
     * Context of the app
     */
    private Context mContext;

    /**
     * Create a new {@link NewsFragmentPagerAdapter} object.
     *
     * @param context is the context of the app
     * @param fm      is the fragment manager that will keep each fragment's state in the adapter
     *                across swipes.
     */
    public NewsFragmentPagerAdapter(Context context, @NonNull FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @NonNull
    public Fragment getItem(int position) {
        if (position == 0) {
            return new WorldFragment();
        } else if (position == 1) {
            return new BusinessFragment();
        } else if (position == 2) {
            return new TechnologyFragment();
        } else {
            return new SportFragment();
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getString(R.string.world_tab_title);
        } else if (position == 1) {
            return mContext.getString(R.string.business_tab_title);
        } else if (position == 2) {
            return mContext.getString(R.string.technology_tab_title);
        } else {
            return mContext.getString(R.string.sport_tab_title);
        }
    }
}
