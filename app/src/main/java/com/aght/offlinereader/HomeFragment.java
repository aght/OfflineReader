package com.aght.offlinereader;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aght.offlinereader.database.WebPage;
import com.aght.offlinereader.database.WebPageDatabase;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView savedPagesView;

    private List<SavedPage> savedPages;
    private SharedPreferences preferences;
    private static final String LAST_URL_COPIED_KEY = "last_url_copied";
    private static final int SNACKBAR_DURATION = 3000;

    public HomeFragment() {
    }

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

        preferences = getActivity().getSharedPreferences(LAST_URL_COPIED_KEY, 0);

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        savedPagesView = rootView.findViewById(R.id.saved_pages_view);

        savedPages = getDatabaseEntries();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(rootView.getContext());
        savedPagesView.setLayoutManager(layoutManager);
        savedPagesView.setItemAnimator(new DefaultItemAnimator());
        savedPagesView.setAdapter(new SavedPageAdapter(savedPages));
        savedPagesView.addOnItemTouchListener(new RecyclerViewTouchListener(getActivity(), savedPagesView, new RecyclerViewTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent i = new Intent(getActivity(), WebViewActivity.class);
                i.putExtra("url", savedPages.get(position).getPageUrl());
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return rootView;
    }

    @Override
    public void onResume() {
        String url = getClipBoardContents().toString();

        if (Patterns.WEB_URL.matcher(url).matches()) {
            if (preferences.getString(LAST_URL_COPIED_KEY, null) == null ||
                    !preferences.getString(LAST_URL_COPIED_KEY, null).equals(url)) {
                preferences.edit().putString(LAST_URL_COPIED_KEY, url).apply();
                promptToAdd(url);
            }
        }

        super.onResume();
    }

    private void promptToAdd(String url) {
        CoordinatorLayout layout = getActivity().findViewById(R.id.main_container);

        String trimmedUrl = url.substring(url.indexOf("//") + 2, url.length());

        Snackbar snackbar = Snackbar
                .make(layout, "Add copied URL to list?\n" + trimmedUrl, SNACKBAR_DURATION)
                .setAction("Add", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(), "Clicked!", Toast.LENGTH_SHORT).show();
                    }
                });

        snackbar.show();
    }

    private CharSequence getClipBoardContents() {
        ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data = cm.getPrimaryClip();

        if (data != null) {
            return data.getItemAt(0).coerceToText(getActivity());
        }

        return null;
    }

    private List<SavedPage> getDatabaseEntries() {
        WebPageDatabase db = WebPageDatabase.getInstance();

        List<WebPage> tmp = db.access().getAllWebPages();

        List<SavedPage> savedPages = new ArrayList<>();

        for (WebPage page : tmp) {
            savedPages.add(new SavedPage(page.getWebPageTitle(), page.getWebPageUrl()));
        }

        return savedPages;
    }


}
