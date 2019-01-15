package com.aght.offlinereader;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}
