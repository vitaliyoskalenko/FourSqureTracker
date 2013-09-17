package com.voskalenko.foursquretracker;

import android.util.Log;

/**Class is used to log all warnings and information messages 
* @version 1.0 11 Sep 2013
* @author  Vitaly Oskalenko
*/

public class Logger {

	private static final String TAG = "Visit card";
	private static final boolean isActive = true;
	
	public static void i(String msg){
		if(!isActive)
			return;
		Log.i(TAG, msg);
	}
	
	public static void d(String msg){
		if(!isActive)
			return;
		Log.d(TAG, msg);
	}
	
	public static void e(String msg, Exception e){
		if(!isActive)
			return;
		Log.e(TAG, msg, e);
	}
	
	public static void e(Exception e){
		if(!isActive)
			return;
		Log.e(TAG,e.getMessage());
	}
}
