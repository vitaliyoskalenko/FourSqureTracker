
/*
 * @(#)Constants.java  1.0 2013/09/11
 *
 * Copyright (C) 2013 Vitaly Oskalenko, oskalenkoVit@ukr.net
 * All rights for the program belong to the postindustria company
 * and are its intellectual property
 */

package com.voskalenko.foursquretracker;

public class Constants {
    public static final String CLIENT_ID = "RKX3KJE5GUBTYGYPFCHEPRIM5VQAD3PREURVVZ0F1ZWS3JTO";
    public static final String CLIENT_SECRET = "QGY1OI21CP1FERHQIXVKNV1PJQLUJ4AH5XVMNJXZRCJQTCUM";
    
	public static final  String ROOT_URL = "https://foursquare.com";
    public static final String CALLBACK_URL = "http://www.FourSqureTracker.com/redirect_uri";
	public static final String AUTH_URL = "/oauth2/authorize?response_type=code";
	public static final String TOKEN_URL = "/oauth2/access_token?grant_type=authorization_code";	
	public static final String API_URL = "https://api.foursquare.com";

    private static final int ONE_DAY_MILSECONDS = 24 * 60 * 60 * 1000;
    public static final int DEFAULT_DETECT_TIME = 10 * 60 * 1000;
    public static final int DEFAULT_DETECT_RADIUS = 100;
    public static final int DEFAULT_AUTO_CHECKIN_RADIUS = 50;
    public static final int TOKEN_EXPIRED_TERM =  ONE_DAY_MILSECONDS;
    public static final int OBTAIN_FRESH_VENUES_TERM =  ONE_DAY_MILSECONDS;
    public static final int SCHEDULE_TERM = 3;
    public static final int PREFERABLE_PROVIDER = 0;
}


