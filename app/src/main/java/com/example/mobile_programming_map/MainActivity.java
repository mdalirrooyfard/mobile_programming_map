package com.example.mobile_programming_map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mapbox.mapboxsdk.Mapbox;

import java.util.ArrayList;
import java.util.concurrent.ThreadPoolExecutor;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;


public class MainActivity extends AppCompatActivity {
    ThreadPoolExecutor executor;
    private Boolean isFirstPageLoading = true;

    private BottomNavigationView bottomNavigation;
    public MyMapFragment mapFragment;
    private BookmarkFragment bookmarkFragment;
    private SettingsFragment settingFragment;
    int NightMode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    DbHelper mydb;
    ArrayList db_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 1);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
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
                                mapFragment.setStart_point(null);
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
        ArrayList db_data = mydb.getAllContacts();
        if (!checkConnection()){
            Toast.makeText(getApplicationContext(), "Network is not connected!",
                    Toast.LENGTH_SHORT).show();
        }

    }

    public MyMapFragment getMapFragment() {
        return mapFragment;
    }

    public BottomNavigationView getBottomNavigation() {
        return bottomNavigation;
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