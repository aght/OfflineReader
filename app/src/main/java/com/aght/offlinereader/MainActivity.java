package com.aght.offlinereader;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import org.apache.commons.io.IOUtils;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";

    // Load linked library
    static {
        System.loadLibrary("ad-block-lib");
    }

    private native boolean initAdBlocker(@NonNull byte[] filterBytes);

    private native boolean shouldBlockUrl(@NonNull String domain, @NonNull String url);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TEST ONLY
        testAdBlocker();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_view_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(getNavigationListener());

        loadFragment(HomeFragment.newInstance());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener getNavigationListener() {
        return new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        loadFragment(HomeFragment.newInstance());
                        return true;
                    case R.id.navigation_favourites:
                        loadFragment(FavouritesFragment.newInstance());
                        return true;
                    case R.id.navigation_search:
                        return true;
                    case R.id.navigation_archive:
                        return true;
                    case R.id.navigation_account:
                        return true;
                }

                return false;
            }
        };
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void testAdBlocker() {
        try {

            if (initAdBlocker(IOUtils.toByteArray(getAssets().open("filter.dat")))) {
                String domain = "https://stackoverflow.com/questions/10972577/c-cmake-add-non-built-files";
                String url = "https://secure.quantserve.com/quant.js";
                Log.e(TAG, String.valueOf(shouldBlockUrl(domain, url)));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
