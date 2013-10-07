/*
 * @(#)SessionEx.java  1.0 2013/09/20
 *
 * Copyright (C) 2013 Vitaly Oskalenko, oskalenkoVit@ukr.net
 * All rights for the program belong to the postindustria company
 * and are its intellectual property
 */

package com.voskalenko.foursquretracker;

import com.googlecode.androidannotations.annotations.sharedpreferences.DefaultString;
import com.googlecode.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * Interface contains all preference fields and used in AccountManager
 *
 * @author Vitaly Oskalenko
 * @version 1.0 20 Sep 2013
 */

@SharedPref(value=SharedPref.Scope.APPLICATION_DEFAULT)
public interface SessionEx {

    long tokenCreationDate();
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
