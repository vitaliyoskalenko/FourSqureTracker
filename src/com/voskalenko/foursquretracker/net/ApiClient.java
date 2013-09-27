/*
 * @(#)ApiClient.java  1.0 2013/09/11
 *
 * Copyright (C) 2013 Vitaly Oskalenko, oskalenkoVit@ukr.net
 * All rights for the program belong to the postindustria company
 * and are its intellectual property
 */

package com.voskalenko.foursquretracker.net;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import com.googlecode.androidannotations.annotations.*;
import com.googlecode.androidannotations.annotations.rest.RestService;
import com.googlecode.androidannotations.api.Scope;
import com.voskalenko.foursquretracker.*;
import com.voskalenko.foursquretracker.callback.AddCheckInCallback;
import com.voskalenko.foursquretracker.callback.AllVenueCallback;
import com.voskalenko.foursquretracker.callback.TokenCallback;
import com.voskalenko.foursquretracker.callback.VerifyDialogCallback;
import com.voskalenko.foursquretracker.database.DatabaseManager;
import com.voskalenko.foursquretracker.dialog.VerifyDialog;
import com.voskalenko.foursquretracker.dialog.VerifyDialog_;
import com.voskalenko.foursquretracker.model.*;
import com.voskalenko.foursquretracker.service.NetworkService;
import com.voskalenko.foursquretracker.ui.ProposedVenuesActivity_;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

import java.text.SimpleDateFormat;
import java.util.*;

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
    @SystemService
    NotificationManager notificationMng;

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

    @AfterInject
    void init() {
        token = getAccountManager().getAccessToken();

        final String verifyUrl = Constants.ROOT_URL + Constants.AUTH_URL + "&client_id=" + Constants.CLIENT_ID + "&redirect_uri=" + Constants.CALLBACK_URL;
        final VerifyDialogCallback verifyDialogCallback = new VerifyDialogCallback() {

            @Override
            public void onSuccess(String verifyCode) {

                TokenCallback callback = new TokenCallback() {

                    @Override
                    public void onSuccess(String token) {
                        getAccountManager().setDateCreation(System.currentTimeMillis());
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
            getDbManager().addOrUpdVenues(venueList);
        }

        return isProposedExist;
    }

    public void CheckInProposedVenue(double latitude, double longitude) {
        if (getProposedVenues(latitude, longitude)) {
            final AddCheckInCallback callback = new AddCheckInCallback() {
                @Override
                public void onSuccess(CheckIn checkIn) {
                    getAccountManager().setDisableDetectInCurrRadius(true);
                }

                @Override
                public void onFail(String error, Exception e) {
                    Logger.e(error, e);
                }
            };

            Venue venue = new Venue();
            List<Venue> venueList = getAccountManager().getVenueList();
            venue = Collections.min(venueList);
            if (venue.getDistance() <= 200/*getAccountManager().getAutoCheckInRadius()*/)
                addCheckIn(venue.getId(), callback);
        }
    }

    public void showProposedVenues(double latitude, double longitude) {
        if (getProposedVenues(latitude, longitude)) {

            Intent notificationIntent = new Intent(context, ProposedVenuesActivity_.class);
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_notification)
                     .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_notification))
                    .setTicker(context.getString(R.string.foursqure_suitable_venues))
                    .setWhen(System.currentTimeMillis())
                    .setContentTitle(context.getString(R.string.checkin_notif_title))
                    .setLights(Color.GREEN, 1, 2)
                    .setAutoCancel(true)
                    .setSound(defaultSound)
                    .setContentText(context.getString(R.string.checkin_notif_text));
            notificationMng.cancel(NOTIFICATION_ID);
            notificationMng.notify(NOTIFICATION_ID, builder.build());
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
