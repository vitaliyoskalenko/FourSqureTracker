package com.voskalenko.foursquretracker;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.googlecode.androidannotations.annotations.EApplication;
import com.voskalenko.foursquretracker.service.DetectCheckInSevice_;

import java.lang.ref.WeakReference;

@EApplication
public class ApplicationEx extends Application {

    private LocationBroadcastReceiver locationBroadcastReceiver;
    private static ApplicationEx instance;

    public static ApplicationEx getInstance() {
        return instance;
    }

    public LocationBroadcastReceiver getLocationBroadcastReceiver() {
        if (locationBroadcastReceiver == null) {
            locationBroadcastReceiver = new LocationBroadcastReceiver(this);
        }

        return locationBroadcastReceiver;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.instance = this;
        FourSqureTrackerHelper.registerBrodcastReceiver(this, getLocationBroadcastReceiver(), LocationManagerEx.ACTION_LOCATION_CHANGED);
    }

    private static class LocationBroadcastReceiver extends BroadcastReceiver {
        private final WeakReference<Context> contextWeakReference;

        private Context getContext() {
            return contextWeakReference != null ? contextWeakReference.get() : null;
        }

        public LocationBroadcastReceiver(final Context context) {
            if (context != null) {
                contextWeakReference = new WeakReference<Context>(context);
            } else {
                contextWeakReference = null;
            }
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                final String action = intent.getAction();
                if (TextUtils.equals(action, LocationManagerEx.ACTION_LOCATION_CHANGED)) {
                    final Intent detectIntent = new Intent(getContext(), DetectCheckInSevice_.class);
                    detectIntent.putExtra(LocationManagerEx.LATITUDE, intent.getDoubleExtra(LocationManagerEx.LATITUDE, 0));
                    detectIntent.putExtra(LocationManagerEx.LONGITUDE, intent.getDoubleExtra(LocationManagerEx.LONGITUDE, 0));
                    getContext().startService(detectIntent);
                }
            }
        }
    }
}