/*
 * @(#)AppController.java  1.0 2013/09/11
 *
 * Copyright (C) 2013 Vitaly Oskalenko, oskalenkoVit@ukr.net 
 * All rights for the program belong to the postindustria company 
 * and are its intellectual property
 */


package com.voskalenko.foursquretracker;

import android.app.Activity;
import android.app.ActivityManager;
import android.text.TextUtils;
import com.googlecode.androidannotations.annotations.*;
import com.voskalenko.foursquretracker.callback.*;
import com.voskalenko.foursquretracker.db.DBManager;
import com.voskalenko.foursquretracker.model.*;
import com.voskalenko.foursquretracker.svc.DetectCheckInSvc;
import com.voskalenko.foursquretracker.task.ApiClient;
import com.voskalenko.foursquretracker.ui.VerifyDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@EBean
public class FourSqureTrackerApp {

    @RootContext
    Activity ctx;

    @Bean
    ApiClient apiClient;

    @SystemService
    ActivityManager activityMng;

    private static final String VERIFY_DIALOG = "verify_dialog";
    private VerifyDialog verifyDialog;
    private String verifyUrl;
    private Session session;
    private DBManager dbManager;

    @AfterInject
    void init() {
        session = Session.getInstance();
        dbManager = DBManager.init(ctx);

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
                Calendar currDate = Calendar.getInstance();
                currDate.setTime(new Date(System.currentTimeMillis()));
                session.setDateCreation(currDate);
                session.setAccessToken(ctx, token);
                getUser();
                getAllCheckIn(token);
            }

            @Override
            public void onFail(String error, Exception e) {
                Logger.e(error, e);
            }
        };
        apiClient.getAccessToken(verifyCode, callback);
    }

    public void getAllCheckIn(String token) {
        GetAllCheckInCallback callback = new GetAllCheckInCallback() {
            @Override
            public void onSuccess(CheckInList checkInList) {
                session.setCheckInList(getStub());
                dbManager.addOrUpdCheckIns(getStub().getCheckins());
            }

            @Override
            public void onFail(String error, Exception e) {
                Logger.e(error, e);
            }
        };

        apiClient.getAllCheckInTask(token, callback);
    }

    public void likeCheckIn(String checkInId) {
        LikeCheckInCallback callback = new LikeCheckInCallback() {
            @Override
            public void onSuccess(CheckIn checkIn) {

            }

            @Override
            public void onFail(String error, Exception e) {
                Logger.e(error, e);
            }

        };

        apiClient.likeCheckInTask(checkInId, callback);
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

        apiClient.getUserTask(callback);
    }

    public void matchVenuesWithCheckIns(String token, double longitude, double latitude) {
        GetNearestVenuesCallback callback = new GetNearestVenuesCallback() {
            @Override
            public void onSuccess(List<Venue> venues) {
                if (venues != null) {
                    List<Venue> nearestVenues = new ArrayList<Venue>();
                    for (CheckIn checkIn : session.getCheckInList().getCheckins()) {
                        if (venues.contains(checkIn.getVenue()))
                            nearestVenues.add(checkIn.getVenue());
                    }
                }
            }

            @Override
            public void onFail(String error, Exception e) {
                Logger.e(error, e);
            }
        };

        apiClient.getNearestVenuesTask(token, longitude, latitude, callback);
    }

    public void verify() {
        verifyDialog.show(ctx.getFragmentManager(), VERIFY_DIALOG);
    }

    public boolean sessionIsActual() {
        Date currDate = new Date(System.currentTimeMillis());
        return !session.getDateCreation().before(currDate);
    }

    public void restoreSessionFromDB() {
        CheckInList checkInList = new CheckInList();
        checkInList.setCheckins(dbManager.getCheckIns());
        Session.getInstance().setCheckInList(checkInList);
    }

    public boolean isDetectSvcRunning() {
        for (ActivityManager.RunningServiceInfo service : activityMng.getRunningServices(Integer.MAX_VALUE)) {
            if (DetectCheckInSvc.class.getName().equals(service.service.getClassName()))
                return true;
        }
        return false;
    }

    public CheckInList getStub() {
        List<CheckIn> lst = new ArrayList<CheckIn>();
        Location location = new Location();
        location.setCity("Kherson");
        location.setCountry("Ukraine");
        location.setLatitude(40.7);
        location.setLongitude(-74);
        Venue venue = new Venue();
        venue.setId("503de4dce4b0857b003af5f7");
        venue.setName("monkeyHut");
        venue.setLocation(location);
        CheckIn checkIn = new CheckIn();
        checkIn.setId("52342faf11d29b9f56bec3db");
        checkIn.setCreatedAt(1379151791);
        checkIn.setVenue(venue);
        lst.add(checkIn);
        CheckInList lst2 = new CheckInList();
        lst2.setCheckins(lst);
        return lst2;
    }
}
