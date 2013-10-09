package com.voskalenko.foursquretracker.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EService;
import com.voskalenko.foursquretracker.ApplicationEx_;
import com.voskalenko.foursquretracker.LocationManagerEx;
import com.voskalenko.foursquretracker.LocationManagerEx_;


@EService
public class RunService extends IntentService {

    @Bean
    LocationManagerEx locationManagerEx;

    public LocationManagerEx getLocationManagerEx() {
        return locationManagerEx;
    }

    public RunService() {
        super("RunService_");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (getLocationManagerEx() instanceof LocationManagerEx_) {
            ((LocationManagerEx_) getLocationManagerEx()).rebind(ApplicationEx_.getInstance().getApplicationContext());
        }
        getLocationManagerEx().turnOn("");
        Log.e("turnOn","turnOn");
    }
}
