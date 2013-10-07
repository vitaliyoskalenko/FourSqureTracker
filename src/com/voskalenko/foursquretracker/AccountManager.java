/*
 * @(#)AccountManager.java  1.0 2013/09/21
 *
 * Copyright (C) 2013 Vitaly Oskalenko, oskalenkoVit@ukr.net
 * All rights for the program belong to the postindustria company
 * and are its intellectual property
 */

package com.voskalenko.foursquretracker;

import android.text.TextUtils;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;
import com.googlecode.androidannotations.api.Scope;
import com.voskalenko.foursquretracker.database.DatabaseManager;
import com.voskalenko.foursquretracker.model.LocationEx;
import com.voskalenko.foursquretracker.model.Venue;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

/**
 * Class provides getSession() information. Work with SessionEx interface
 *
 * @author Vitaly Oskalenko
 * @version 1.0 21 Sep 2013
 */

@EBean(scope = Scope.Singleton)
public class AccountManager {

    @Bean
    DatabaseManager dbManager;
    @Pref
    SessionEx_ session;

    private static final String TAG = AccountManager.class.getSimpleName();

    private List<Venue> venueList;

    private DatabaseManager getDbManager() {
        return dbManager;
    }

    private SessionEx_ getSession() {
        return session;
    }

    public Calendar getTokenCreationDate() {
        Calendar creationDate = Calendar.getInstance();
        creationDate.setTimeInMillis(getSession().tokenCreationDate().get());
        return creationDate;
    }

    public void setTokenCreationDate(long creationDate) {
        getSession().tokenCreationDate().put(creationDate);
    }

    public void setAccessToken(String accessToken) {
        getSession().accessToken().put(accessToken);
    }

    public String getAccessToken() {
        return getSession().accessToken().get();
    }


    public List<Venue> getVenueList() {
        return venueList;
    }

    public void setVenueList(List<Venue> venueList) {
        this.venueList = venueList;
    }

    public int getDetectTime() {
        return Integer.parseInt(getSession().detectTime().get());
    }

    public int getDetectRadius() {
        return Integer.parseInt(getSession().detectRadius().get());
    }

    public void setDisableDetectInCurrRadius(boolean detect) {
        getSession().disableDetectInCurrRadius().put(detect);
    }

    public boolean getDisableDetectInCurrRadius() {
        return getSession().disableDetectInCurrRadius().get();
    }

    private Calendar getVenuesUpdateDate() {
        Calendar updateDate = Calendar.getInstance();
        updateDate.setTimeInMillis(getSession().venuesUpdateDate().get());
        return updateDate;
    }

    public void setVenuesUpdateDate(long updateDate) {
        getSession().venuesUpdateDate().put(updateDate);
    }

    public void setLastLocation(double latitude, double longitude) {
        JSONObject json = new JSONObject();
        try {
            json.put(LocationEx.FIELD_LATITUDE, latitude);
            json.put(LocationEx.FIELD_LONGITUDE, longitude);
            getSession().lastLocation().put(json.toString());
        } catch (JSONException e) {
            Logger.e(TAG + ": setLastLocation JSON error", e);
        }
    }

    public LocationEx getLastLocation() {
        LocationEx location = new LocationEx();
        String content = getSession().lastLocation().get();
        if (!TextUtils.isEmpty(content)) {
            try {
                JSONObject json = new JSONObject(content);
                location.setLatitude(json.getDouble(LocationEx.FIELD_LATITUDE));
                location.setLongitude(json.getDouble(LocationEx.FIELD_LONGITUDE));
            } catch (JSONException e) {
                Logger.e(TAG + ": getLastLocation JSON error", e);
            }
        }
        return location;
    }

    public boolean getAutoCheckIn() {
        return getSession().autoCheckIn().get();
    }

    public int getAutoCheckInRadius() {
        return Integer.parseInt(getSession().autoCheckInRadius().get());
    }

    public boolean hasAccessToken() {
        return !TextUtils.isEmpty(getAccessToken());
    }


    public boolean isSessionActual() {
        return !dateExpired(getTokenCreationDate(), Constants.TOKEN_EXPIRED_TERM);
    }

    public boolean isVenuesActual() {
        return !dateExpired(getVenuesUpdateDate(), Constants.UPDATE_VENUES_TERM);
    }

    public void setIsDetectSvcRunning(boolean isRunning) {
        getSession().isDetectSvcRunning().put(isRunning);
    }

    public boolean getIsDetectSvcRunning() {
        return getSession().isDetectSvcRunning().get();
    }

    private boolean dateExpired(Calendar date, int term) {
        double dateDiff = (System.currentTimeMillis() - date.getTimeInMillis()) / term;
        return dateDiff > 1;
    }

    public void restoreSessionFromDB() {
        setVenueList(getDbManager().getVenues());
    }

    public boolean[] getScheduleDays() {
        boolean[] scheduleDays = new boolean[7];
        switch (Integer.parseInt(getSession().scheduleTerm().get())) {
            case 0:
                scheduleDays = new boolean[]{false, true, true, true, true, true, false};
                break;
            case 1:
                scheduleDays = new boolean[]{true, false, false, false, false, false, true};
                break;
            case 2:
                scheduleDays = new boolean[]{true, true, true, true, true, true, true};
                break;
        }
        return scheduleDays;
    }
}
