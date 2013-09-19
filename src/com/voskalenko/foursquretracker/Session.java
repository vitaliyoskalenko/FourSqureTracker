package com.voskalenko.foursquretracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import com.voskalenko.foursquretracker.model.CheckIns;
import com.voskalenko.foursquretracker.model.User;

import java.util.Calendar;
import java.util.Date;

public class Session {

	private static final String ACCESS_TOKEN = "access_token";
    private static final String DATE_TOKEN = "date_token";
	
	private static final Session instance = new Session();

    private static User userProfile;
    private static CheckIns checkInList;

	private Session() {
	}

	public static Session getInstance() {
		return instance;
	}

    public Calendar getDateCreation(Context ctx) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        Calendar dateToken = Calendar.getInstance();
        dateToken.setTime(new Date(pref.getLong(DATE_TOKEN, 0)));
        return dateToken;
    }

    public void setDateCreation(Context ctx, Date dateToken) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        Editor ed = pref.edit();
        ed.putLong(DATE_TOKEN, dateToken.getTime());
        ed.commit();
    }

	public void setAccessToken(Context ctx, String accessToken) {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
		Editor ed = pref.edit();
		ed.putString(ACCESS_TOKEN, accessToken);
		ed.commit();

	}

	public String getAccessToken(Context ctx) {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
		return pref.getString(ACCESS_TOKEN, null);
	}

    public User getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(User userProfile) {
        this.userProfile = userProfile;
    }

    public CheckIns getCheckInList() {
        return checkInList;
    }

    public void setCheckInList(CheckIns checkInList) {
        this.checkInList = checkInList;
    }

}
