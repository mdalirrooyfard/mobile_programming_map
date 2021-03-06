package com.example.mobile_programming_map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

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
            Intent refresh = new Intent(activity, MainActivity.class);
            activity.finish();
            startActivity(refresh);
            activity.overridePendingTransition(0,0);;

            return true;
        });
        Preference delete_data = findPreference("Delete data");

        delete_data.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog
                        .Builder(getContext());
                builder.setMessage("Do you want to delete your data?");
                builder.setCancelable(true);


                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which)
                                    {
                                        activity.mydb.deleteAllData();
                                    }
                                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which)
                                    {
                                        dialog.cancel();
                                    }
                                });

                // Create the Alert dialog
                AlertDialog alertDialog = builder.create();

                // Show the Alert Dialog box
                alertDialog.show();
                return true;
            }
        });
    }

    public void change_theme(boolean dark_light){
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;
        sharedPreferences = activity.getPreferences(MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean("dark_or_light", dark_light);
        editor.apply();
    }

}