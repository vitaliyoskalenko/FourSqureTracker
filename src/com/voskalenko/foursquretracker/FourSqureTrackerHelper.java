/*
 * @(#)FourSqureTrackerHelper.java  1.0 2013/09/11
 *
 * Copyright (C) 2013 Vitaly Oskalenko, oskalenkoVit@ukr.net 
 * All rights for the program belong to the postindustria company 
 * and are its intellectual property
 */

package com.voskalenko.foursquretracker;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.SystemService;
import com.voskalenko.foursquretracker.service.DetectCheckInSevice_;

/**
 * Class provides additional functionality
 *
 * @author Vitaly Oskalenko
 * @version 1.0 11 Sep 2013
 */

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

    //To get distance between two location
    public static float distanceBetween(double currentLatitude, double currentLongitude, double venueLatitude, double venueLongitude) {
        Location currentLocation = new Location("");
        currentLocation.setLatitude(currentLatitude);
        currentLocation.setLongitude(currentLongitude);

        Location venueLocation = new Location("");
        venueLocation.setLatitude(venueLatitude);
        venueLocation.setLongitude(venueLongitude);

        return currentLocation.distanceTo(venueLocation);
    }

    public static void registerBrodcastReceiver(final Context context, final BroadcastReceiver receiver, String... args) {
        try {
            if (context != null && args != null && args.length > 0) {
                IntentFilter filter = new IntentFilter();

                for (String arg : args) {
                    filter.addAction(arg);
                }

                LocalBroadcastManager.getInstance(context).registerReceiver(receiver, filter);
            }
        } catch (Exception e) {
            Logger.e(e);
        }
    }

    public static void sendIntent(final Context context, final Intent intent) {
        try {
            if (context != null) {
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        } catch (Exception e) {

        }
    }

    public static void sendNotification(Context context, Intent intent, Uri defaultSound, int smallIcon, int ticker, int title, int contentText, int notificationId) {
        NotificationManager notificationMng = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,  PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentIntent(pendingIntent)
                .setSmallIcon(smallIcon)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), smallIcon))
                .setTicker(context.getString(ticker))
                .setWhen(System.currentTimeMillis())
                .setContentTitle(context.getString(title))
                .setLights(Color.GREEN, 1, 2)
                .setSound(defaultSound)
                .setAutoCancel(true)
                .setContentText(context.getString(contentText));
        notificationMng.cancel(notificationId);
        notificationMng.notify(notificationId, builder.build());
    }
}
