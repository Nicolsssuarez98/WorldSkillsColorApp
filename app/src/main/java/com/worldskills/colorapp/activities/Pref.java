package com.worldskills.colorapp.activities;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.worldskills.colorapp.R;

public class Pref extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       addPreferencesFromResource(R.xml.preferencias);
    }
}
