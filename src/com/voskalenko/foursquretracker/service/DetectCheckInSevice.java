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
import android.location.Location;
import android.location.LocationManager;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EService;
import com.googlecode.androidannotations.annotations.SystemService;
import com.voskalenko.foursquretracker.LocationManagerEx;
import com.voskalenko.foursquretracker.FourSqureTrackerApp;
import com.voskalenko.foursquretracker.Logger;
import com.voskalenko.foursquretracker.Session;
import com.voskalenko.foursquretracker.callback.DetectCheckInSvcCallback;
import com.voskalenko.foursquretracker.model.CheckIns;

/**
 * Service runs with BroadcastReceiver in some period and looking for appropriate check-in
 *
 * @author Vitaly Oskalenko
 * @version 1.0 11 Sep 2013
 */
@EService
public class DetectCheckInSevice extends IntentService {

    @Bean
    LocationManagerEx currLocation;
    @Bean
    FourSqureTrackerApp trackerApp;

    private static final String TAG = DetectCheckInSevice.class.getSimpleName();
    private final Session session;

    @SystemService
    LocationManager locationManager;

    public DetectCheckInSevice() {
        super("DetectCheckInSvc_");
        session = Session.getInstance();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Logger.i(TAG + ": Service is running");
        CheckIns checkInList;
        if (trackerApp.sessionIsActual()) {
            trackerApp.restoreSessionFromDB();
        } else if (trackerApp.hasAccessToken()) {
            trackerApp.getAllCheckIn(callback);
        } else Logger.i("You need to pass authentication in Activity");

    }

    DetectCheckInSvcCallback callback = new DetectCheckInSvcCallback() {
        @Override
        public void onSuccess() {
            Location location = currLocation.getLocation();
            if (location != null)
                trackerApp.matchVenuesWithCheckIns(46.631440669811525,32.613043785095215);
/*
                trackerApp.matchVenuesWithCheckIns(location.getLongitude(), location.getLatitude());
*/
        }

        @Override
        public void onFail(String error, Exception e) {
        }
    };

}
