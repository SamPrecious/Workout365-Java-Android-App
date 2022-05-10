package com.example.workout365;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.text.InputType;
import android.widget.EditText;

import androidx.annotation.NonNull;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        SharedPreferences sp = getPreferenceScreen().getSharedPreferences();
        EditTextPreference repsPref = (EditTextPreference) findPreference("reps");
        EditTextPreference setsPref = (EditTextPreference) findPreference("sets");
        repsPref.setSummary(sp.getString("reps",""));
        setsPref.setSummary(sp.getString("sets",""));

        repsPref.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);  //forces us to input numbers to avoid bugs
        setsPref.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        //Automatically refresh the setting field changed on the page, to display the update
        Preference cityPref = findPreference(key);
        // Set summary to be the user-description for the selected value
        cityPref.setSummary(sharedPreferences.getString(key, ""));

    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
