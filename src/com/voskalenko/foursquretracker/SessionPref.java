package com.voskalenko.foursquretracker;

import com.googlecode.androidannotations.annotations.sharedpreferences.DefaultInt;
import com.googlecode.androidannotations.annotations.sharedpreferences.DefaultString;
import com.googlecode.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref
public interface SessionPref {

    long dateCreation();
    long venuesUpdateDate();
    @DefaultString("")
    String accessToken();
    @DefaultInt(Constants.DEFAULT_DETECT_TIME)
    int detectTime();
    @DefaultInt(Constants.DEFAULT_DETECT_RADIUS)
    int detectRadius();
    boolean disableDetectInCurrRadius();
    String lastLocation();
}
