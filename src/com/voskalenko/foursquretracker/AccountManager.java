package com.voskalenko.foursquretracker;

import android.text.TextUtils;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;
import com.googlecode.androidannotations.api.Scope;
import com.voskalenko.foursquretracker.database.DBManager;
import com.voskalenko.foursquretracker.model.LocationEx;
import com.voskalenko.foursquretracker.model.Venue;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@EBean(scope = Scope.Singleton)
public class AccountManager {

    @Bean
    DBManager dbManager;
    @Pref
    SessionPref_ session;

    private static final String TAG = AccountManager.class.getSimpleName();

    private List<Venue> venueList;

    public DBManager getDbManager() {
        return dbManager;
    }

    public Calendar getDateCreation() {
        Calendar dateToken = Calendar.getInstance();
        dateToken.setTime(new Date(session.dateCreation().get()));
        return dateToken;
    }

    public void setDateCreation(long dateCreation) {
        session.dateCreation().put(dateCreation);
    }

    public void setAccessToken(String accessToken) {
        session.accessToken().put(accessToken);
    }

    public String getAccessToken() {
        return session.accessToken().get();
    }


    public List<Venue> getVenueList() {
        return venueList;
    }

    public void setVenueList(List<Venue> venueList) {
        this.venueList = venueList;
    }

    public int getDetectTime() {
        return session.detectTime().get();
    }

    public int getDetectRadius() {
        return session.detectRadius().get();
    }

    public void setDisableDetectInCurrRadius(boolean detect) {
        session.disableDetectInCurrRadius().put(detect);
    }

    public boolean getDisableDetectInCurrRadius() {
        return session.disableDetectInCurrRadius().get();
    }

    private Calendar getVenuesUpdateDate() {
        Calendar updateDate = Calendar.getInstance();
        updateDate.setTimeInMillis(session.venuesUpdateDate().get());
        return updateDate;
    }

    public void setVenuesUpdateDate(long updateDate) {
          session.venuesUpdateDate().put(updateDate);
    }

    public void setLastLocation(double latitude, double longitude) {
        JSONObject json = new JSONObject();
        try {
            json.put(LocationEx.FIELD_LATITUDE, latitude);
            json.put(LocationEx.FIELD_LONGITUDE, longitude);
            session.lastLocation().put(json.toString());
        } catch (JSONException e) {
            Logger.e(TAG + ": setLastLocation JSON error", e);
        }
    }

    public LocationEx getLastLocation() {
        LocationEx location = new LocationEx();
        String content = session.lastLocation().get();
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

    public boolean hasAccessToken() {
        return !TextUtils.isEmpty(getAccessToken());
    }


    public boolean isSessionActual() {
        return !dateExpired(getDateCreation(), Constants.TOKEN_EXPIRED_TERM);
    }

    public boolean isVenuesActual() {
        return !dateExpired(getVenuesUpdateDate(), Constants.UPDATE_VENUES_TERM);
    }


    private boolean dateExpired(Calendar date, int term) {
        double dateDiff = (System.currentTimeMillis() - date.getTimeInMillis()) / term;
        return dateDiff > 1;
    }

    public void restoreSessionFromDB() {
        setVenueList(getDbManager().getVenues());
    }
}
