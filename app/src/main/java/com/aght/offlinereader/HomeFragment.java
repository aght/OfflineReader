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
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class HomeFragment extends Fragment {

    private RecyclerView savedPagesView;

    private SharedPreferences preferences;
    private static final String LAST_URL_COPIED_KEY = "last_url_copied";

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

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(rootView.getContext());
        savedPagesView.setLayoutManager(layoutManager);
        savedPagesView.setItemAnimator(new DefaultItemAnimator());
        savedPagesView.setAdapter(new SavedPageAdapter(generateTestData()));
        savedPagesView.addOnItemTouchListener(new RecyclerViewTouchListener(getActivity(), savedPagesView, new RecyclerViewTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                startActivity(new Intent(getActivity(), WebViewActivity.class));
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
                preferences.edit().putString(LAST_URL_COPIED_KEY, url).commit();
                promptToAdd(url);
            }
        }

        super.onResume();
    }

    private void promptToAdd(String url) {
        CoordinatorLayout layout = getActivity().findViewById(R.id.main_container);

        String trimmedUrl = url.substring(url.indexOf("//") + 2, url.length());

        Snackbar snackbar = Snackbar
                .make(layout, "Add copied URL to your list?\n" + trimmedUrl, 3000)
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

    private List<SavedPage> generateTestData() {
        List<SavedPage> savedPages = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            savedPages.add(new SavedPage("Title: " + String.valueOf(i), "URL"));
        }

        return savedPages;
    }


}
