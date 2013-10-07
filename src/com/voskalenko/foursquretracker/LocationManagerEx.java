/*
 * @(#)LocationManagerEx.java  1.0 2013/09/20
 *
 * Copyright (C) 2013 Vitaly Oskalenko, oskalenkoVit@ukr.net
 * All rights for the program belong to the postindustria company
 * and are its intellectual property
 */

package com.voskalenko.foursquretracker;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.SystemService;

/**
 * Obtain the coordinates of the location service (GPS)
 *
 * @author Vitaly Oskalenko
 * @version 1.0 20 Sep 2013
 */

@EBean
public class LocationManagerEx implements LocationListener {


    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    private static final String TAG = LocationManagerEx.class.getSimpleName();

    private Location location;

    @SystemService
    LocationManager locationManager;

    @AfterInject
    public void init() {
        try {
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (isGPSEnabled)
                getLocation(LocationManager.GPS_PROVIDER);
            else if (location == null && isNetworkEnabled) {
                getLocation(LocationManager.NETWORK_PROVIDER);
            } else Logger.i(TAG + ": Misfortune of getting location");
        } catch (Exception e) {
            Logger.e(TAG + ": Get location error", e);
        }
    }

    public void getLocation(String provider) throws Exception {
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

        if (locationManager != null) {
            location = locationManager
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
    }

    public Location getLocation() {
        return location != null ? location : new Location("");
    }

    @Override
    public void onLocationChanged(Location location) {
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
}
