/*
 * @(#)ScheduleReceiver.java  1.0 2013/09/11
 *
 * Copyright (C) 2013 Vitaly Oskalenko, oskalenkoVit@ukr.net 
 * All rights for the program belong to the postindustria company 
 * and are its intellectual property
 */

package com.voskalenko.foursquretracker.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EReceiver;
import com.googlecode.androidannotations.annotations.SystemService;
import com.voskalenko.foursquretracker.AccountManager;

/**
 * Receiver runs service in some period
 *
 * @author Vitaly Oskalenko
 * @version 1.0 11 Sep 2013
 */

@EReceiver
public class ScheduleReceiver extends BroadcastReceiver {

    public static final String STOP_SCHEDULE = "stop_schedule";

    @SystemService
    static AlarmManager alarmManager;
    @Bean
    AccountManager accountManager;

    private AccountManager getAccountManager() {
        return accountManager;
    }

    @Override
    public void onReceive(Context ctx, Intent intent) {
        boolean stopSchedule = intent.getBooleanExtra(STOP_SCHEDULE, false);
        scheduleDetect(ctx, stopSchedule);
    }

    public void scheduleDetect(Context ctx, boolean stopSchedule) {
        Intent intent = new Intent(ctx, DetectCheckInSevice_.class);
        PendingIntent pIntent = PendingIntent.getService(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (stopSchedule) {
            alarmManager.cancel(pIntent);
        } else {
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime() + 10000/*accountManager.getDetectTime()*/, 10000/* getAccountManager().getDetectTime()*/, pIntent);
        }
    }
}

