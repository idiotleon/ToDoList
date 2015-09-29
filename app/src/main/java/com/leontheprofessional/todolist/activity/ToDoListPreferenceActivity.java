package com.leontheprofessional.todolist.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.leontheprofessional.todolist.R;

import java.util.List;

public class ToDoListPreferenceActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.todolist_preference);
    }

    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.todolist_preference_headers, target);
    }
}
