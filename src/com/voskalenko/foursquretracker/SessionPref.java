package com.voskalenko.foursquretracker;

import com.googlecode.androidannotations.annotations.sharedpreferences.DefaultString;
import com.googlecode.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref(value=SharedPref.Scope.APPLICATION_DEFAULT)
public interface SessionPref {

    long dateCreation();
    long venuesUpdateDate();
    @DefaultString("")
    String accessToken();
    @DefaultString("" + Constants.DEFAULT_DETECT_TIME)
    String detectTime();
    @DefaultString("" + Constants.DEFAULT_DETECT_RADIUS)
    String detectRadius();
    boolean disableDetectInCurrRadius();
    String lastLocation();
    boolean autoCheckIn();
    @DefaultString("" + Constants.DEFAULT_AUTO_CHECKIN_RADIUS)
    String autoCheckInRadius();
    String scheduleTerm();
    boolean isDetectSvcRunning();
}
