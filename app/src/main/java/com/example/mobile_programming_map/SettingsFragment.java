package com.example.mobile_programming_map;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

public class SettingsFragment extends PreferenceFragmentCompat {
    MainActivity activity;


    public  SettingsFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;



    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        Preference dark_mode = findPreference("dark_or_light");

        assert dark_mode != null;
        dark_mode.setDefaultValue(false);
        dark_mode.setOnPreferenceChangeListener((preference, newValue) -> {
            change_theme((Boolean) newValue);
            activity.finish();
            Intent refresh = new Intent(activity, MainActivity.class);

            startActivity(refresh);
            activity.overridePendingTransition(0,0);;

            return true;
        });
        Preference delete_data = findPreference("Delete data");

        delete_data.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                activity.mydb.deleteAllData();

                return true;
            }
        });
    }

    public void change_theme(boolean dark_light){
        if (dark_light){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

}