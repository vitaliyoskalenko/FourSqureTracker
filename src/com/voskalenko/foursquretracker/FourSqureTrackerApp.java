/*
 * @(#)AppController.java  1.0 2013/09/11
 *
 * Copyright (C) 2013 Vitaly Oskalenko, oskalenkoVit@ukr.net 
 * All rights for the program belong to the postindustria company 
 * and are its intellectual property
 */


package com.voskalenko.foursquretracker;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.googlecode.androidannotations.annotations.*;
import com.voskalenko.foursquretracker.callback.*;
import com.voskalenko.foursquretracker.db.DBManager;
import com.voskalenko.foursquretracker.model.*;
import com.voskalenko.foursquretracker.service.DetectCheckInSvc_;
import com.voskalenko.foursquretracker.net.ApiClient;
import com.voskalenko.foursquretracker.ui.VenuesActivity_;
import com.voskalenko.foursquretracker.dialog.VerifyDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@EBean
public class FourSqureTrackerApp {

    @RootContext
    Activity activity;
    @RootContext
    Context ctx;
    @Bean
    ApiClient apiClient;
    @Bean
    DBManager dbManager;

    @SystemService
    ActivityManager activityMng;
    @SystemService
    NotificationManager notificationMng;

    private static final String VERIFY_DIALOG = "verify_dialog";

    private VerifyDialog verifyDialog;
    private String verifyUrl;
    private Session session;
    private String token;

    @AfterInject
    void init() {
        session = Session.getInstance();
        token = session.getAccessToken(ctx);
        //dbManager = DBManager.init(ctx);

        verifyUrl = Constants.ROOT_URL + Constants.AUTH_URL + "&client_id=" + Constants.CLIENT_ID + "&redirect_uri=" + Constants.CALLBACK_URL;
        VerifyDialog.VerifyDialogCallback verifyDialogCallback = new VerifyDialog.VerifyDialogCallback() {

            @Override
            public void onSuccess(String verifyCode) {
                getAccessToken(verifyCode);
            }

            @Override
            public void onFail(String error) {

            }
        };
        verifyDialog = VerifyDialog.newInstance(verifyUrl, verifyDialogCallback);
    }

    public boolean hasAccessToken() {
        return !TextUtils.isEmpty(Session.getInstance().getAccessToken(ctx));
    }

    public void getAccessToken(final String verifyCode) {
        GetTokenCallback callback = new GetTokenCallback() {

            @Override
            public void onSuccess(String token) {
                session.setDateCreation(ctx, new Date(System.currentTimeMillis()));
                session.setAccessToken(ctx, token);
                getUser();
                getAllCheckIn(null);
            }

            @Override
            public void onFail(String error, Exception e) {
                Logger.e(error, e);
            }
        };
        apiClient.getAccessToken(verifyCode, callback);
    }

    public void getAllCheckIn(DetectCheckInSvcCallback callbackSvc) {
        GetAllCheckInCallback callback = new GetAllCheckInCallback() {

            private DetectCheckInSvcCallback callback;

            @Override
            public void onSuccess(CheckIns checkInList) {
                session.setCheckInList(checkInList);
                dbManager.addOrUpdCheckIns(checkInList.getCheckins());
                if(callback != null)
                    callback.onSuccess();
            }

            @Override
            public void onFail(String error, Exception e) {
                Logger.e(error, e);
            }

            public GetAllCheckInCallback init(DetectCheckInSvcCallback callbackSvc) {
                callback = callbackSvc;
                return this;

            }
        }.init(callbackSvc);

        apiClient.getAllCheckInTask(token, callback);
    }

    public void addCheckIn(String venueId, String message, AddCheckInCallback userCallback) {
        AddCheckInCallback callback = new AddCheckInCallback() {

            private AddCheckInCallback userCallback;

            @Override
            public void onSuccess(CheckIn checkIn) {
                if (userCallback != null)
                    userCallback.onSuccess(checkIn);

            }

            @Override
            public void onFail(String error, Exception e) {
                Logger.e(error, e);
            }

            private AddCheckInCallback init(AddCheckInCallback userCallback) {
                this.userCallback = userCallback;
                return this;
            }

        }.init(userCallback);

        CheckInPostBody postBody = new CheckInPostBody();
        postBody.setVenueId(venueId);
        postBody.setShout(message);
        apiClient.addCheckInTask(token, postBody, callback);
    }

    public void getUser() {
        UserCallback callback = new UserCallback() {
            @Override
            public void onSuccess(User user) {
                session.setUserProfile(user);
                dbManager.addOrUpdUser(user);
            }

            @Override
            public void onFail(String error, Exception e) {
                Logger.e(error, e);
            }
        };

        apiClient.getUserTask(token, callback);
    }

    public void matchVenuesWithCheckIns(double latitude, double longitude) {
        GetNearestVenuesCallback callback = new GetNearestVenuesCallback() {
            @Override
            public void onSuccess(List<Venue> venues) {
                restoreSessionFromDB();
                if (venues != null) {
                    List<Venue> nearestVenues = new ArrayList<Venue>();
                    List<CheckIn> checkIns = session.getCheckInList().getCheckins();
                    if (checkIns != null)
                        for (CheckIn checkIn : checkIns)
                            if (venues.contains(checkIn.getVenue()))
                                nearestVenues.add(checkIn.getVenue());

                    if (nearestVenues != null)
                    showNearestVenues(nearestVenues);
                }
            }

            @Override
            public void onFail(String error, Exception e) {
                Logger.e(error, e);
            }
        };

        apiClient.getNearestVenuesTask(token, latitude, longitude, callback);
    }

    private void showNearestVenues(List<Venue> venues) {
        Intent notificationIntent = new Intent(ctx, VenuesActivity_.class);
        notificationIntent.putParcelableArrayListExtra("VENUES", (ArrayList) venues);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        notificationIntent.addFlags(Intent.FLAG_FROM_BACKGROUND);

        PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0, notificationIntent, 0);
        Notification.Builder builder = new Notification.Builder(ctx);
        builder.setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_action_checkin)
                .setTicker(ctx.getString(R.string.foursqure_suitable_venues))
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle(ctx.getString(R.string.checkin_notif_title))
                .setContentText(ctx.getString(R.string.checkin_notif_text));

        notificationMng.notify(1, builder.build());
    }

    public void verify() {
        verifyDialog.show(activity.getFragmentManager(), VERIFY_DIALOG);
    }

    public boolean sessionIsActual() {
        Calendar currDate = Calendar.getInstance();
        currDate.setTime(new Date(System.currentTimeMillis()));
        return session.getDateCreation(ctx) == null ? false : !session.getDateCreation(ctx).before(currDate);
    }

    public void restoreSessionFromDB() {
        CheckIns checkInList = new CheckIns();
        checkInList.setCheckins(dbManager.getCheckIns());
        Session.getInstance().setCheckInList(checkInList);
    }

    public boolean isDetectSvcRunning() {
        for (ActivityManager.RunningServiceInfo service : activityMng.getRunningServices(Integer.MAX_VALUE)) {
            if (DetectCheckInSvc_.class.getName().equals(service.service.getClassName()))
                return true;
        }
        return false;
    }
}
