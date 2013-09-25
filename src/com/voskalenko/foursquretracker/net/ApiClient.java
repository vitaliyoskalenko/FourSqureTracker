package com.voskalenko.foursquretracker.net;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.googlecode.androidannotations.annotations.*;
import com.googlecode.androidannotations.annotations.rest.RestService;
import com.googlecode.androidannotations.api.Scope;
import com.voskalenko.foursquretracker.*;
import com.voskalenko.foursquretracker.callback.AddCheckInCallback;
import com.voskalenko.foursquretracker.callback.AllVenueCallback;
import com.voskalenko.foursquretracker.callback.TokenCallback;
import com.voskalenko.foursquretracker.callback.VerifyDialogCallback;
import com.voskalenko.foursquretracker.database.DBManager;
import com.voskalenko.foursquretracker.dialog.VerifyDialog;
import com.voskalenko.foursquretracker.dialog.VerifyDialog_;
import com.voskalenko.foursquretracker.model.CheckIn;
import com.voskalenko.foursquretracker.model.CheckIns;
import com.voskalenko.foursquretracker.model.Token;
import com.voskalenko.foursquretracker.model.Venue;
import com.voskalenko.foursquretracker.service.NetworkService;
import com.voskalenko.foursquretracker.ui.ProposedVenuesActivity_;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@EBean(scope = Scope.Singleton)
public class ApiClient {

    @RestService
    NetworkService service;
    @RootContext
    Context ctx;
    @Bean
    DBManager dbManager;
    @Bean
    AccountManager accountManager;
    @SystemService
    NotificationManager notificationMng;

    private static final int NOTIFICATION_ID = 1;
    private static final String VERIFY_DIALOG = "verify_dialog";

    private VerifyDialog verifyDialog;
    private String verifyUrl;
    private String token;


    public NetworkService getService() {
        return service;
    }

    private String version;


    @AfterInject
    void init() {
        token = accountManager.getAccessToken();

        verifyUrl = Constants.ROOT_URL + Constants.AUTH_URL + "&client_id=" + Constants.CLIENT_ID + "&redirect_uri=" + Constants.CALLBACK_URL;
        VerifyDialogCallback verifyDialogCallback = new VerifyDialogCallback() {

            @Override
            public void onSuccess(String verifyCode) {

                TokenCallback callback = new TokenCallback() {

                    @Override
                    public void onSuccess(String token) {
                        accountManager.setDateCreation(System.currentTimeMillis());
                        accountManager.setAccessToken(token);
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




    public void getSuitableVenues(double latitude, double longitude) {
        boolean isProposedExist = false;
        List<Venue> venueList = accountManager.getVenueList();
        int detectRadius = accountManager.getDetectRadius();
        for (Venue venue : venueList) {
            float distance = FourSqureTrackerHelper.distanceBetween(latitude, longitude,
                    venue.getLocation().getLatitude(), venue.getLocation().getLongitude());
            if (distance <= detectRadius && venue.getMuted() != Venue.FLAG_MUTED) {
                isProposedExist = true;
                venue.setProposed(Venue.PROPOSED_FLAG);
            }
        }
        if (isProposedExist) {
            dbManager.addOrUpdVenues(venueList);
            showProposedVenues();
        }
    }

    private void showProposedVenues() {
        Intent notificationIntent = new Intent(ctx, ProposedVenuesActivity_.class);
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
        notificationMng.cancel(NOTIFICATION_ID);
        notificationMng.notify(NOTIFICATION_ID, builder.build());
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
            accountManager.setVenuesUpdateDate(System.currentTimeMillis());
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
            callback.onSuccess(checkIn);
        } catch (RestClientException e) {
            callback.onFail("Failed to like check in", e);
        }
    }


}
