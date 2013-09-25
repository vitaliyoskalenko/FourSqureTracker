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
    public static final int DEFAULT_DETECT_TIME = 7 * 1000;
    public static final int DEFAULT_DETECT_RADIUS = 100;
    public static final int TOKEN_EXPIRED_TERM =  ONE_DAY_MILSECONDS;
    public static final int UPDATE_VENUES_TERM =  ONE_DAY_MILSECONDS / 2;
}


