package com.example.smartbin.tabbedapplication;

/**
 * Created by Sandhya Chunduri on 10/17/2015.
 */

import android.os.Bundle;
import android.preference.PreferenceActivity;


public class UserSettingsActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);

    }
}