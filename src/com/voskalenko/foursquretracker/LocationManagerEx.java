/*
 * @(#)LocationManagerEx.java  1.0 2013/09/20
 *
 * Copyright (C) 2013 Vitaly Oskalenko, oskalenkoVit@ukr.net
 * All rights for the program belong to the postindustria company
 * and are its intellectual property
 */

package com.voskalenko.foursquretracker;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.annotations.SystemService;

/**
 * Obtain the coordinates of the location service (GPS)
 *
 * @author Vitaly Oskalenko
 * @version 1.0 20 Sep 2013
 */

@EBean
public class LocationManagerEx implements LocationListener {

    public final static String ACTION_LOCATION_CHANGED = "action.location.changed";
    public final static String LATITUDE = "latitude";
    public final static String LONGITUDE = "longitude";

    private static final String TAG = LocationManagerEx.class.getSimpleName();
    private static final int NOTIFICATION_ID = 2;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    private static final long MIN_TIME_BW_UPDATES = 0;
    private static final long CONNECT_TIMEOUT_IN_MS = 60000;

    public void setContext(Context context) {
        this.context = context;
    }

    @SystemService
    LocationManager locationManager;
    @RootContext
    Context context;
    @Bean
    AccountManager accountManager;

    public LocationManagerEx() {
    }

    public AccountManager getAccountManager() {
        return accountManager;
    }

    public LocationManager getLocationManager() {
        return locationManager;
    }

    public Context getContext() {
        return context;
    }


    enum Providers {
        ALL_PROVIDER("ALL_PROVIDER"),
        GPS(LocationManager.GPS_PROVIDER),
        NETWORK(LocationManager.NETWORK_PROVIDER);

        private String name;

        private Providers(String s) {
            name = s;
        }

        public String getName() {
            return name;
        }
    }

    public void turnOn(String failProvider) {
        try {
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            int providerIndex = getAccountManager().getPreferableProvider();

            if (providerIndex != Providers.ALL_PROVIDER.ordinal()) {
                String provider = Providers.values()[providerIndex].getName();
                if (provider.equals(LocationManager.GPS_PROVIDER) && !isGPSEnabled)
                    toggleGPS();
                enableProvider(provider);

            } else {
                if (!isGPSEnabled) {
                    toggleGPS();
                }
                if (isGPSEnabled && !failProvider.equals(LocationManager.GPS_PROVIDER)) {
                    enableProvider(LocationManager.GPS_PROVIDER);
                } else if (isNetworkEnabled) {
                    enableProvider(LocationManager.NETWORK_PROVIDER);
                } else Logger.i(TAG + ": Misfortune of getting location");
            }
        } catch (Exception e) {
            Logger.e(TAG + ": Get location error", e);
        }
    }

    public void enableProvider(String provider) throws Exception {
        getLocationManager().requestLocationUpdates(provider, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this, Looper.getMainLooper());

        final Handler removeHandler = new Handler(Looper.getMainLooper());
        removeHandler.postDelayed(new Runnable() {
            public void run() {
                turnOff();
            }
        }, CONNECT_TIMEOUT_IN_MS);
    }

    public void turnOff() {
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            sendResponseIntent(location, location.getProvider());
        }
        turnOff();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }

    private void sendResponseIntent(Location location, String provider) {
        if (location != null) {
            Intent intent = new Intent(ACTION_LOCATION_CHANGED);
            intent.putExtra(LATITUDE, location.getLatitude());
            intent.putExtra(LONGITUDE, location.getLongitude());
            FourSqureTrackerHelper.sendIntent(context, intent);
        } else {
            turnOn(provider);
        }
    }

    private void toggleGPS() {

        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        FourSqureTrackerHelper.sendNotification(context, intent, defaultSound, R.drawable.ic_notification, R.string.foursqure_needs_gps,
                R.string.enable_gps, R.string.checkin_notif_text, NOTIFICATION_ID);
    }
}
