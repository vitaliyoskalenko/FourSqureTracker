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

    @SystemService
    AlarmManager alarmManager;
    @Bean
    AccountManager accountManager;

    private AccountManager getAccountManager() {
        return accountManager;
    }

    @Override
    public void onReceive(Context ctx, Intent intent) {
        scheduleDetect(ctx);
    }

    public void scheduleDetect(Context context) {
        boolean startFlag = getAccountManager().getIsDetectSvcRunning();

        Intent detectIntent = new Intent(context, RunService_.class);
        PendingIntent pIntent = PendingIntent.getService(context, 0, detectIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (startFlag) {
            context.startService(detectIntent);
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime() + accountManager.getDetectTime(), getAccountManager().getDetectTime(), pIntent);
        } else {
            alarmManager.cancel(pIntent);
        }
    }
}

