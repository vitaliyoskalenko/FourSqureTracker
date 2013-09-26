package com.voskalenko.foursquretracker.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import com.voskalenko.foursquretracker.R;

import java.util.List;

public class FourSqureTrackerPreference extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.preference_headers, target);
    }

    public static class SearchPreferencesFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.search_preference_screen);
        }
    }

    public static class AutoCheckInPreferencesFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.auto_checkin_preference_screen);
        }
    }

    public static class SchedulePreferencesFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.schedule_preference_screen);
        }
    }



    public static void start(Context context) {
        Intent intent = new Intent(context, FourSqureTrackerPreference.class);
        context.startActivity(intent);
    }
}
