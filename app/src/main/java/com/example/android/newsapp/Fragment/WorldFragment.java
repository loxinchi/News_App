package com.example.android.newsapp.Fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.loader.app.LoaderManager.LoaderCallbacks;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.newsapp.MyRecyclerView;
import com.example.android.newsapp.News;
import com.example.android.newsapp.NewsLoader;
import com.example.android.newsapp.R;
import com.example.android.newsapp.adapter.NewsAdapter;

import java.util.ArrayList;
import java.util.List;

public class WorldFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<List<News>> {

    private static final String LOG_TAG = WorldFragment.class.getName();

    /**
     * URI for earthquake data from the The Guardian dataset
     */
    private static final String GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/search";

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;

    /**
     * Constant value for the News loader ID.
     */
    private static final int NEWS_LOADER_ID = 1;

    /**
     * Adapter for the list of news
     */
    private NewsAdapter mAdapter;

    public WorldFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.recycle, container, false);

        // Create a list of guides
        mAdapter = new NewsAdapter(getActivity(), new ArrayList<News>());
        MyRecyclerView recyclerView = (MyRecyclerView) rootView.findViewById(R.id.recycle_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(layoutManager);

        //Set to empty view if no data
        mEmptyStateTextView = rootView.findViewById(R.id.empty_view);
        recyclerView.setEmptyView(mEmptyStateTextView);

        if(isConnected()){
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            // Hide loading indicator because no_internet_connection
//            View loadingIndicator = findViewById(R.id.loading_indicator);
//            loadingIndicator.setVisibility(View.GONE);

            // Set empty state text to display "No earthquakes found."
//            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

        return rootView;
    }

    @NonNull
    @Override
    public Loader<List<News>> onCreateLoader(int id, @Nullable Bundle args) {
        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value. For example, the `format=json`
        uriBuilder.appendQueryParameter("section", "world");
        uriBuilder.appendQueryParameter("format", "json");
        uriBuilder.appendQueryParameter("from-date", "2021-01-01");
        uriBuilder.appendQueryParameter("show-fields", "headline,trailText,byline,firstPublicationDate,thumbnail");
        uriBuilder.appendQueryParameter("orderby", "relevance");
        uriBuilder.appendQueryParameter("api-key", "test");

        // Return the completed uri
        return new NewsLoader(getActivity(), uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<News>> loader, List<News> newsInfo) {
        // Hide loading indicator because the data has been loaded
        getView().findViewById(R.id.progress_bar).setVisibility(View.GONE);
        // Clear the adapter of previous clear data
        mAdapter.clearAll();
        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (newsInfo != null && !newsInfo.isEmpty()) {
//            mAdapter.addAll(newsInfo);
        }
    }

    @Override
    public void onLoaderReset (@NonNull Loader<List<News>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clearAll();
    }

    /**
     *  Check for network connectivity.
     */
    private boolean isConnected() {
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
