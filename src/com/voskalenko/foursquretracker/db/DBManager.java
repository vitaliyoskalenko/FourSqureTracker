package com.voskalenko.foursquretracker.db;

import android.content.Context;
import com.voskalenko.foursquretracker.Logger;
import com.voskalenko.foursquretracker.model.CheckIn;
import com.voskalenko.foursquretracker.model.User;

import java.sql.SQLException;
import java.util.List;

public class DBManager {

    private static final String TAG = DBManager.class.getSimpleName();
    private static DBManager instance;

    private DBHelper helper;

    public static DBManager init(Context ctx) {
        if (instance == null)
            instance = new DBManager(ctx);
        return instance;
    }

    public static DBManager getInstance() {
        return instance;
    }

    private DBManager(Context ctx) {
        helper = new DBHelper(ctx);
    }

    private DBHelper getHelper() {
        return helper;
    }

    public List<CheckIn> getCheckIns() {
        List<CheckIn> checkInsLst = null;
        try {
            checkInsLst = getHelper().getCheckInDao().queryForAll();
        } catch (SQLException e) {
            Logger.e(TAG + ": Failed to get twitter home lines: ", e);
        }
        return checkInsLst;
    }


    public User getUser() {
        User user = null;
        try {
            user = getHelper().getUserDao().queryForAll().get(0);
        } catch (SQLException e) {
            Logger.e(TAG + ": Failed to get user: ", e);
        }
        return user;
    }

    public void addOrUpdCheckIns(List<CheckIn> checkIns) {
        try {
            for(CheckIn obj : checkIns)
                getHelper().getCheckInDao().createOrUpdate(obj);
        } catch (SQLException e) {
            Logger.e(TAG + ": Failed to add twitter home lines: " , e);
        }
    }

    public void addOrUpdUser(User user) {
        try {
            getHelper().getUserDao().createOrUpdate(user);
        } catch (SQLException e) {
            Logger.e(TAG + "Failed to add user: ", e);
        }
    }

}
