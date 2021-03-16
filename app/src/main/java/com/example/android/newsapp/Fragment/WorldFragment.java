package com.example.android.newsapp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.newsapp.News;
import com.example.android.newsapp.R;
import com.example.android.newsapp.adapter.NewsAdapter;

import java.util.ArrayList;

public class WorldFragment extends Fragment {

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
        final ArrayList<News> news = new ArrayList<News>();
        news.add(new News(R.string.hot_item_1, R.string.hot_item_region_1, R.drawable.lack_annecy, R.string.hot_item_tel_1, R.string.hot_item_add_1, R.string.hot_item_web_1));

        NewsAdapter adapter = new NewsAdapter(getActivity(), news);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycle_list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return rootView;
    }
}
