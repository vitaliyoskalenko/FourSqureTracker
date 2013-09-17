package com.voskalenko.foursquretracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import com.voskalenko.foursquretracker.model.CheckInList;
import com.voskalenko.foursquretracker.model.User;

import java.util.Calendar;

public class Session {

	private static final String ACCESS_TOKEN = "access_token";
	
	private static final Session instance = new Session();


    private static Calendar dateCreation;
    private static User userProfile;
    private static CheckInList checkInList;

	private Session() {
	}

	public static Session getInstance() {
		return instance;
	}

    public Calendar getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Calendar dateCreation) {
        Session.dateCreation = dateCreation;
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

    public CheckInList getCheckInList() {
        return checkInList;
    }

    public void setCheckInList(CheckInList checkInList) {
        this.checkInList = checkInList;
    }

}
