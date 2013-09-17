package com.voskalenko.foursquretracker.task;

import android.content.Context;
import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.annotations.rest.RestService;
import com.googlecode.androidannotations.api.Scope;
import com.voskalenko.foursquretracker.Constants;
import com.voskalenko.foursquretracker.model.Token;
import com.voskalenko.foursquretracker.callback.*;
import com.voskalenko.foursquretracker.model.CheckIn;
import com.voskalenko.foursquretracker.model.CheckInList;
import com.voskalenko.foursquretracker.model.User;
import com.voskalenko.foursquretracker.model.Venue;
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
            CheckInList checkInLst = getService().getAllCheckIn(token, version).getCheckInList();
            callback.onSuccess(checkInLst);
        } catch (Exception e) {
            callback.onFail("Failed to get all checkIns", e);
        }
    }

    @Background
    public void likeCheckInTask(String checkInId, LikeCheckInCallback callback) {
        try {
            getService().setRootUrl(Constants.API_URL);
            CheckIn checkIn = getService().likeCheckIn(checkInId).getBody();
            callback.onSuccess(checkIn);
        } catch (RestClientException e) {
            callback.onFail("Failed to like check in", e);
        }
    }

    @Background
    public  void getUserTask(UserCallback callback) {
        try {
            getService().setRootUrl(Constants.API_URL);
            User userProfile = getService().getUserProfile(version).getBody();
            callback.onSuccess(userProfile);
        } catch (RestClientException e) {
            callback.onFail("Failed to get user's profile", e);
        }
    }

    @Background
    public void getNearestVenuesTask(String token, double longitude, double latitude, GetNearestVenuesCallback callback) {
        try {
            getService().setRootUrl(Constants.API_URL);
            List<Venue> venueList = getService().getNearestVenues(token, longitude, latitude, Constants.DETECT_RADIUS, version).getVenues();
            callback.onSuccess(venueList);
        } catch (RestClientException e) {
            callback.onFail("Failed to get nearest venues", e);
        }
    }
}
