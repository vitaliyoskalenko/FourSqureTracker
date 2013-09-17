/*
 * @(#)DetectCheckInSvc.java  1.0 2013/09/11
 *
 * Copyright (C) 2013 Vitaly Oskalenko, oskalenkoVit@ukr.net 
 * All rights for the program belong to the postindustria company 
 * and are its intellectual property
 */

package com.voskalenko.foursquretracker.svc;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EService;
import com.googlecode.androidannotations.annotations.SystemService;
import com.voskalenko.foursquretracker.FourSqureTrackerApp;
import com.voskalenko.foursquretracker.Logger;
import com.voskalenko.foursquretracker.Session;
import com.voskalenko.foursquretracker.model.CheckInList;
import com.voskalenko.foursquretracker.CurrLocation;

/**
 * Service runs with BroadcastReceiver in some period and looking for appropriate check-in
 *
 * @author Vitaly Oskalenko
 * @version 1.0 11 Sep 2013
 */
@EService
public class DetectCheckInSvc extends IntentService {

    @Bean
    CurrLocation currLocation;
    @Bean
    FourSqureTrackerApp trackerApp;

    private static final String TAG = DetectCheckInSvc.class.getSimpleName();

    private final Session session;
    private final Context ctx;

    @SystemService
    LocationManager locationManager;

    public DetectCheckInSvc() {
        super("DetectCheckInSvc_");
        ctx = getApplicationContext();
        session = Session.getInstance();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        CheckInList checkInList;
        if (trackerApp.sessionIsActual()) {
            trackerApp.restoreSessionFromDB();
        } else if (trackerApp.hasAccessToken())
            trackerApp.getAllCheckIn(session.getAccessToken(ctx));
        else Logger.i("You need to pass authentication in Activity");

        if((checkInList = session.getCheckInList()) != null) {
            Location location = currLocation.getLocation();
            if(location != null)
                trackerApp.matchVenuesWithCheckIns(session.getAccessToken(ctx), location.getLongitude(), location.getLatitude());

        }
    }

}
