package com.voskalenko.foursquretracker;

public class Constants {
    public static final String CLIENT_ID = "RKX3KJE5GUBTYGYPFCHEPRIM5VQAD3PREURVVZ0F1ZWS3JTO";
    public static final String CLIENT_SECRET = "QGY1OI21CP1FERHQIXVKNV1PJQLUJ4AH5XVMNJXZRCJQTCUM";
    
	public static final  String ROOT_URL = "https://foursquare.com";
    public static final String CALLBACK_URL = "http://www.FourSqureTracker.com/redirect_uri";
	public static final String AUTH_URL = "/oauth2/authorize?response_type=code";
	public static final String TOKEN_URL = "/oauth2/access_token?grant_type=authorization_code";	
	public static final String API_URL = "https://api.foursquare.com";
	
    public static final int DETECT_PERIOD = 5000;
    public static final int DETECT_RADIUS = 5;
}
