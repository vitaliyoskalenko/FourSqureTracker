/*
 * @(#)AppController.java  1.0 2013/09/11
 *
 * Copyright (C) 2013 Vitaly Oskalenko, oskalenkoVit@ukr.net 
 * All rights for the program belong to the postindustria company 
 * and are its intellectual property
 */


package com.voskalenko.foursquretracker;

import android.app.ActivityManager;
import android.location.Location;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.SystemService;
import com.voskalenko.foursquretracker.service.DetectCheckInSevice_;

@EBean
public class FourSqureTrackerHelper {
    @SystemService
    ActivityManager activityMng;

    public boolean isDetectSvcRunning() {
        for (ActivityManager.RunningServiceInfo service : activityMng.getRunningServices(Integer.MAX_VALUE)) {
            if (DetectCheckInSevice_.class.getName().equals(service.service.getClassName()))
                return true;
        }
        return false;
    }

    public static float distanceBetween (double currentLatitude, double currentLongitude, double venueLatitude, double venueLongitude) {
        Location currentLocation = new Location("");
        currentLocation.setLatitude(currentLatitude);
        currentLocation.setLongitude(currentLongitude);

        Location venueLocation = new Location("");
        venueLocation.setLatitude(venueLatitude);
        venueLocation.setLongitude(venueLongitude);

        return currentLocation.distanceTo(venueLocation);
    }
}
