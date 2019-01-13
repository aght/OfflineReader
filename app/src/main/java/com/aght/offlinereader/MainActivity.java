package com.aght.offlinereader;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_view_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(getNavigationListener());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener getNavigationListener() {
        return new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        return true;
                    case R.id.navigation_favourites:
                        return true;
                    case R.id.navigation_search:
                        return true;
                    case R.id.navigation_archive:
                        return true;
                }
                return false;
            }
        };
    }
}
