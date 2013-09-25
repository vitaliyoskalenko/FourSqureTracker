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
import com.voskalenko.foursquretracker.database.DBManager;
import com.voskalenko.foursquretracker.model.LocationEx;
import com.voskalenko.foursquretracker.model.Venue;
import com.voskalenko.foursquretracker.net.ApiClient;

import java.util.List;

/**
 * Service runs with BroadcastReceiver in some period and looking for appropriate check-in
 *
 * @author Vitaly Oskalenko
 * @version 1.0 11 Sep 2013
 */
@EService
public class DetectCheckInSevice extends IntentService {

    //@Bean
    LocationManagerEx currLocation;
    @Bean
    ApiClient apiClient;
    @Bean
    DBManager dbManager;
    @Bean
    AccountManager accountManager;
    @SystemService
    LocationManager locationManager;

    private static final String TAG = DetectCheckInSevice.class.getSimpleName();

    public DetectCheckInSevice() {
        super("DetectCheckInSvc_");
    }

    public DBManager getDbManager() {
        return dbManager;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Logger.i(TAG + ": Service is running");

       /* Location currentLocation = currLocation.getLocation();
        if (currentLocation != null) {
          accountManager.setLastLocation(currentLocation.getLatitude(),
                  currentLocation.getLongitude());
        } else return;*/

        if (accountManager.getDisableDetectInCurrRadius()) {
            LocationEx lastLocation = accountManager.getLastLocation();

            int detectRadius = accountManager.getDetectRadius();
            float distance = FourSqureTrackerHelper.distanceBetween(lastLocation.getLatitude(), lastLocation.getLongitude(),
                    40.631440669811525, 32.613043785095215);
            if (distance > detectRadius)
                accountManager.setDisableDetectInCurrRadius(false);
            else return;
        }

        AllVenueCallback callback = new AllVenueCallback() {

            @Override
            public void onSuccess(List<Venue> venues) {
                accountManager.setVenueList(venues);
                getDbManager().addOrUpdVenues(venues);
            }

            @Override
            public void onFail(String error, Exception e) {
                Logger.e(error, e);
            }
        };

        accountManager.setLastLocation(46.631440669811500, 32.613043785095200);
        getDbManager().uncheckProposed();

        if (accountManager.isSessionActual() && accountManager.isVenuesActual()) {
            accountManager.restoreSessionFromDB();
        } else if (accountManager.hasAccessToken()) {
            getApiClient().getAllVenues(callback);
        } else Logger.i("You need to pass authentication in Activity");

        if (accountManager.getVenueList() != null &&
                accountManager.getVenueList().size() != 0 ) {
            getApiClient().getSuitableVenues(46.631440669811525, 32.613043785095215);
        }
    }
}
