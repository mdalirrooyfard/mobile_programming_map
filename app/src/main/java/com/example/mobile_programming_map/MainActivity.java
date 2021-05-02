package com.example.mobile_programming_map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;

import java.util.ArrayList;
import java.util.concurrent.ThreadPoolExecutor;


public class MainActivity extends AppCompatActivity {
    ThreadPoolExecutor executor;
    private Boolean isFirstPageLoading = true;

    private BottomNavigationView bottomNavigation;
    private MyMapFragment mapFragment;
    private BookmarkFragment bookmarkFragment;
    private SettingsFragment settingFragment;
    int NightMode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    DbHelper mydb;
    ArrayList db_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        sharedPreferences = getSharedPreferences("SharedPrefs", MODE_PRIVATE);
//        NightMode = sharedPreferences.getInt("dark_or_light", 1);
//        AppCompatDelegate.setDefaultNightMode(NightMode);
        super.onCreate(savedInstanceState);

        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_main);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.getMenu().getItem(1).setChecked(true);
        BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.map_item:
                                openFragment(mapFragment);
                                return true;
                            case R.id.bookmark:
                                openFragment(bookmarkFragment);
                                return true;
                            case R.id.settings:
                                openFragment(settingFragment);
                                return true;
                        }
                        return false;
                    }
                };
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        mapFragment = new MyMapFragment();
        openFragment(mapFragment);
        Log.i("here", "onCreate: HEREEE");
        bookmarkFragment = new BookmarkFragment();
        settingFragment = new SettingsFragment();

        mydb = new DbHelper(this);
        ArrayList db_data = mydb.getAllCotacts();

    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void startLoadingFirstPage() {
        this.isFirstPageLoading = true;
    }

    public void endLoadingFirstPage() {
        this.isFirstPageLoading = false;
    }

    public Boolean getIsFirstPageLoading() {
        return this.isFirstPageLoading;
    }

    public boolean checkConnection() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        NightMode = AppCompatDelegate.getDefaultNightMode();

        sharedPreferences = getSharedPreferences("SharedPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        editor.putInt("dark_or_light", NightMode);
        editor.apply();

    }
}