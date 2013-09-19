package com.voskalenko.foursquretracker.net;

import android.content.Context;
import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.annotations.rest.RestService;
import com.googlecode.androidannotations.api.Scope;
import com.voskalenko.foursquretracker.Constants;
import com.voskalenko.foursquretracker.callback.*;
import com.voskalenko.foursquretracker.model.*;
import com.voskalenko.foursquretracker.service.NetworkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@EBean(scope = Scope.Singleton)
public class ApiClient {

    @RootContext
    Context ctx;
    @RestService
    protected NetworkService service;

    public NetworkService getService() {
        return service;
    }

    private String version;

    @AfterInject
    void init() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyymmdd");
        version = formatter.format(new Date(System.currentTimeMillis()));
    }

    @Background
    public void getAccessToken(String verifyCode, GetTokenCallback callback) {
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
    public void getAllCheckInTask(String token, GetAllCheckInCallback callback) {
        try {
            getService().setRootUrl(Constants.API_URL);
            CheckIns checkInLst = getService().getAllCheckIn(token, version).getResponse().getCheckInList();
            callback.onSuccess(checkInLst);
        } catch (Exception e) {
            callback.onFail("Failed to get all checkIns", e);
        }
    }

    @Background
    public void addCheckInTask(String token, CheckInPostBody postBody, AddCheckInCallback callback) {
        try {
            getService().setRootUrl(Constants.API_URL);
            CheckIn checkIn = getService().addCheckIn(token, postBody.getVenueId(), postBody.getShout(), version).getResponse().getCheckIn();
            callback.onSuccess(checkIn);
        } catch (RestClientException e) {
            callback.onFail("Failed to like check in", e);
        }
    }

    @Background
    public void getUserTask(String token, UserCallback callback) {
        try {
            getService().setRootUrl(Constants.API_URL);
            User userProfile = getService().getUserProfile(token, version).getResponse().getUser();
            callback.onSuccess(userProfile);
        } catch (RestClientException e) {
            callback.onFail("Failed to get user's profile", e);
        }
    }

    @Background
    public void getNearestVenuesTask(String token, double latitude, double longitude, GetNearestVenuesCallback callback) {
        try {
            getService().setRootUrl(Constants.API_URL);
            List<Venue> venueList = getService().getNearestVenues(token, latitude, longitude,
                    Constants.DETECT_RADIUS, version).getResponse().getVenues();
            callback.onSuccess(venueList);
        } catch (RestClientException e) {
            callback.onFail("Failed to get nearest venues", e);
        }
    }
}
