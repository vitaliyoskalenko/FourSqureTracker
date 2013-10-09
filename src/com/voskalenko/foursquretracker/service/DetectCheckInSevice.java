/*
 * @(#)DetectCheckInSvc.java  1.0 2013/09/11
 *
 * Copyright (C) 2013 Vitaly Oskalenko, oskalenkoVit@ukr.net 
 * All rights for the program belong to the postindustria company 
 * and are its intellectual property
 */

package com.voskalenko.foursquretracker.service;

import android.app.IntentService;
import android.content.Intent;
import android.location.LocationManager;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EService;
import com.googlecode.androidannotations.annotations.SystemService;
import com.voskalenko.foursquretracker.AccountManager;
import com.voskalenko.foursquretracker.FourSqureTrackerHelper;
import com.voskalenko.foursquretracker.LocationManagerEx;
import com.voskalenko.foursquretracker.Logger;
import com.voskalenko.foursquretracker.callback.AllVenueCallback;
import com.voskalenko.foursquretracker.database.DatabaseManager;
import com.voskalenko.foursquretracker.model.LocationEx;
import com.voskalenko.foursquretracker.model.Venue;
import com.voskalenko.foursquretracker.net.ApiClient;

import java.util.Calendar;
import java.util.List;

/**
 * Service runs with BroadcastReceiver in some period and looking for appropriate check-in
 *
 * @author Vitaly Oskalenko
 * @version 1.0 11 Sep 2013
 */

@EService
public class DetectCheckInSevice extends IntentService {

    @Bean
    ApiClient apiClient;
    @Bean
    DatabaseManager dbManager;
    @Bean
    AccountManager accountManager;
    @SystemService
    LocationManager locationManager;

    private static final String TAG = DetectCheckInSevice.class.getSimpleName();

    private double latitude;
    private double longitude;

    public DetectCheckInSevice() {
        super("DetectCheckInSvc_");
    }

    private DatabaseManager getDbManager() {
        return dbManager;
    }

    private ApiClient getApiClient() {
        return apiClient;
    }

    private AccountManager getAccountManager() {
        return accountManager;
    }

//        Getting all the venues where the user has been
        private final AllVenueCallback callback = new AllVenueCallback() {

            @Override
            public void onSuccess(List<Venue> venues) {
                if (venues.size() > 0) {
                    getAccountManager().setVenueList(venues);
                    getDbManager().addOrUpdVenues(venues);
                    doCheckIn();
                }
            }

            @Override
            public void onFail(String error, Exception e) {
                Logger.e(error, e);
            }
        };

    @Override
    protected void onHandleIntent(Intent intent) {
//        Specify which days of the service will be activated
        Calendar calendar = Calendar.getInstance();
        boolean[] scheduleDays = getAccountManager().getScheduleTerm();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (scheduleDays[day - 1]) {
            return;
        }

//        Obtain the coordinates of the location service (GPS)
        if (!intent.hasExtra(LocationManagerEx.LATITUDE)) {
            return;
        }

        latitude = intent.getDoubleExtra(LocationManagerEx.LATITUDE, 0);
        longitude = intent.getDoubleExtra(LocationManagerEx.LONGITUDE, 0);
        getAccountManager().setLastLocation(latitude, longitude);

//        Block the launch until we leave from a given radius
        if (getAccountManager().getDisableDetectInCurrRadius()) {
            LocationEx lastLocation = getAccountManager().getLastLocation();

            int detectRadius = getAccountManager().getDetectRadius();
            float distance = FourSqureTrackerHelper.distanceBetween(lastLocation.getLatitude(), lastLocation.getLongitude(),
                    latitude, longitude);
            if (distance > detectRadius)
                getAccountManager().setDisableDetectInCurrRadius(false);
            else return;
        }

//        Set the latest location for the future comparison
        getAccountManager().setLastLocation(latitude, longitude);
        getDbManager().uncheckProposed();

        if (getAccountManager().isSessionActual() && getAccountManager().isVenuesActual()) {
            getAccountManager().restoreSessionFromDB();
            doCheckIn();
        } else if (getAccountManager().hasAccessToken()) {
            getApiClient().getAllVenues(callback);
        } else Logger.i("You need to pass authentication in Activity");
    }

//    Proposed make check-in or run in automatically mode
    private void doCheckIn() {
        if (getAccountManager().getVenueList() != null &&
                getAccountManager().getVenueList().size() != 0) {

            if (getAccountManager().getAutoCheckIn()) {
                getApiClient().CheckInProposedVenue(latitude, longitude);
            } else {
                getApiClient().showProposedVenues(latitude, longitude);
            }
        }
    }
}
