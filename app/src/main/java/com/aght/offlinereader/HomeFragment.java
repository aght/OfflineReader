package com.aght.offlinereader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView savedPagesView;

    public HomeFragment() {}

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        savedPagesView = rootView.findViewById(R.id.saved_pages_view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(rootView.getContext());
        savedPagesView.setLayoutManager(layoutManager);
        savedPagesView.setItemAnimator(new DefaultItemAnimator());

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(savedPagesView.getContext(),
                ((LinearLayoutManager) layoutManager).getOrientation());
        savedPagesView.addItemDecoration(dividerItemDecoration);

        List<SavedPage> savedPages = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            savedPages.add(new SavedPage("Title: " + String.valueOf(i), "URL"));
        }

        SavedPageAdapter adapter = new SavedPageAdapter(savedPages);

        savedPagesView.setAdapter(adapter);

        return rootView;
    }

}
