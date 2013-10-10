/*
 * @(#)ApiClient.java  1.0 2013/09/11
 *
 * Copyright (C) 2013 Vitaly Oskalenko, oskalenkoVit@ukr.net
 * All rights for the program belong to the postindustria company
 * and are its intellectual property
 */

package com.voskalenko.foursquretracker.net;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import com.googlecode.androidannotations.annotations.*;
import com.googlecode.androidannotations.annotations.rest.RestService;
import com.googlecode.androidannotations.api.Scope;
import com.voskalenko.foursquretracker.*;
import com.voskalenko.foursquretracker.activity.ProposedVenuesActivity_;
import com.voskalenko.foursquretracker.callback.AddCheckInCallback;
import com.voskalenko.foursquretracker.callback.AllVenueCallback;
import com.voskalenko.foursquretracker.callback.TokenCallback;
import com.voskalenko.foursquretracker.callback.VerifyDialogCallback;
import com.voskalenko.foursquretracker.database.DatabaseManager;
import com.voskalenko.foursquretracker.dialog.VerifyDialog;
import com.voskalenko.foursquretracker.dialog.VerifyDialog_;
import com.voskalenko.foursquretracker.model.*;
import com.voskalenko.foursquretracker.service.NetworkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * It's client for getting and sending of queries through Network Service
 *
 * @author Vitaly Oskalenko
 * @version 1.0 11 Sep 2013
 */

@EBean(scope = Scope.Singleton)
public class ApiClient {

    @RestService
    NetworkService service;
    @RootContext
    Context context;
    @Bean
    DatabaseManager dbManager;
    @Bean
    AccountManager accountManager;

    private static final String TAG = ApiClient.class.getSimpleName();
    private static final int NOTIFICATION_ID = 1;
    private static final String VERIFY_DIALOG = "verify_dialog";

    private VerifyDialog verifyDialog;
    private String token;
    private String version;

    private NetworkService getService() {
        return service;
    }

    private DatabaseManager getDbManager() {
        return dbManager;
    }

    private AccountManager getAccountManager() {
        return accountManager;
    }

    //    it's verify dialog callback that triggered when user has typed auth data
    private final VerifyDialogCallback verifyDialogCallback = new VerifyDialogCallback() {

        @Override
        public void onSuccess(String verifyCode) {

            TokenCallback callback = new TokenCallback() {

                @Override
                public void onSuccess(String token) {
                    getAccountManager().setTokenCreationDate(System.currentTimeMillis());
                    getAccountManager().setAccessToken(token);
                }

                @Override
                public void onFail(String error, Exception e) {
                    Logger.e(error, e);
                }
            };

            getAccessToken(verifyCode, callback);
        }

        @Override
        public void onFail(String error, Exception e) {

        }
    };

    //    it's triggered when user has made check in
    private final AddCheckInCallback callback = new AddCheckInCallback() {
        @Override
        public void onSuccess(CheckIn checkIn) {
            getAccountManager().setDisableDetectInCurrRadius(true);
        }

        @Override
        public void onFail(String error, Exception e) {
            Logger.e(error, e);
        }
    };

    @AfterInject
    void init() {
        token = getAccountManager().getAccessToken();

        final String verifyUrl = Constants.ROOT_URL + Constants.AUTH_URL + "&client_id=" + Constants.CLIENT_ID + "&redirect_uri=" + Constants.CALLBACK_URL;

        verifyDialog = VerifyDialog_.builder()
                .verifyUrl(verifyUrl)
                .callback(verifyDialogCallback)
                .build();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyymmdd");
        version = formatter.format(new Date(System.currentTimeMillis()));
    }


    private boolean getProposedVenues(double latitude, double longitude) {
        boolean isProposedExist = false;
        List<Venue> venueList = getAccountManager().getVenueList();
        int detectRadius = getAccountManager().getDetectRadius();

        for (Venue venue : venueList) {
            float distance = FourSqureTrackerHelper.distanceBetween(latitude, longitude,
                    venue.getLocation().getLatitude(), venue.getLocation().getLongitude());
            if (distance <= detectRadius && venue.getMuted() != Venue.FLAG_MUTED) {
                isProposedExist = true;
                venue.setProposed(Venue.PROPOSED_FLAG);
                venue.setDistance(distance);
            }
        }

        if (isProposedExist) {
            getDbManager().updateVenues(venueList);
        }

        return isProposedExist;
    }

    public void CheckInProposedVenue(double latitude, double longitude) {
        if (getProposedVenues(latitude, longitude)) {
            List<Venue> venueList = getAccountManager().getVenueList();
            List<Venue> venueListTemp = new ArrayList<Venue>();
            for (Venue venue : venueList) {
                if (venue.getProposed() == 1) {
                    venueListTemp.add(venue);
                }
            }
            Venue venue = Collections.min(venueListTemp);

            if (venue.getDistance() <= getAccountManager().getAutoCheckInRadius())
                addCheckIn(venue.getId(), callback);
        }
    }

    public void showProposedVenues(double latitude, double longitude) {
        if (getProposedVenues(latitude, longitude)) {

            Intent notificationIntent = new Intent(context, ProposedVenuesActivity_.class);
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            FourSqureTrackerHelper.sendNotification(context, notificationIntent, defaultSound, R.drawable.ic_notification, R.string.foursqure_suitable_venues,
                    R.string.checkin_notif_title, R.string.checkin_notif_text, NOTIFICATION_ID);
        }
    }

    public void verify(Activity activity) {
        verifyDialog.show(activity.getFragmentManager(), VERIFY_DIALOG);
    }

    @Background
    public void getAccessToken(String verifyCode, TokenCallback callback) {
        try {
            getService().setRootUrl(Constants.ROOT_URL + Constants.TOKEN_URL);
            ResponseEntity<Token> token = service.getAccessToken(Constants.CLIENT_ID,
                    Constants.CLIENT_SECRET, Constants.CALLBACK_URL, verifyCode);
            callback.onSuccess(token.getBody().getAccess_token());
        } catch (Exception e) {
            callback.onFail("Failed to get access token", e);
        }
    }

    @Background
    public void getAllVenues(AllVenueCallback callback) {
        try {
            getService().setRootUrl(Constants.API_URL);
            CheckIns checkInLst = getService().getAllCheckIn(token, version).getResponse().getCheckInList();
            List<Venue> venueList = new ArrayList<Venue>();
            for (CheckIn checkIn : checkInLst.getCheckins()) {
                venueList.add(checkIn.getVenue());
            }
            getAccountManager().setVenuesUpdateDate(System.currentTimeMillis());
            callback.onSuccess(venueList);
        } catch (Exception e) {
            callback.onFail("Failed to get all checkIns", e);
        }
    }

    @Background
    public void addCheckIn(String venueId, AddCheckInCallback callback) {
        try {
            getService().setRootUrl(Constants.API_URL);
            CheckIn checkIn = getService().addCheckIn(token, venueId, "", version).getResponse().getCheckIn();

            CheckInsHistory checkInsHistory = new CheckInsHistory();
            checkInsHistory.setAutomatically(getAccountManager().getAutoCheckIn());
            checkInsHistory.setId(System.currentTimeMillis());
            checkInsHistory.setPlace(checkIn.getVenue().getName());
            checkInsHistory.setCheckInDate(System.currentTimeMillis());
            checkInsHistory.setVenue(checkIn.getVenue());
            getDbManager().addChickInToHistory(checkInsHistory);

            Logger.i(TAG + ": Check-in was performed");
            callback.onSuccess(checkIn);
        } catch (RestClientException e) {
            callback.onFail("Failed to like check in", e);
        }
    }
}
