package com.voskalenko.foursquretracker.db;

import android.content.Context;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.api.Scope;
import com.voskalenko.foursquretracker.Logger;
import com.voskalenko.foursquretracker.model.CheckIn;
import com.voskalenko.foursquretracker.model.User;

import java.sql.SQLException;
import java.util.List;

@EBean(scope = Scope.Singleton)
public class DBManager {

    @Bean
    DBHelper dbHelper;

    private static final String TAG = DBManager.class.getSimpleName();

    private DBHelper getDbHelper() {
        return dbHelper;
    }

    public List<CheckIn> getCheckIns() {
        List<CheckIn> checkInsLst = null;
        try {
            checkInsLst = getDbHelper().getCheckInDao().queryForAll();
        } catch (SQLException e) {
            Logger.e(TAG + ": Failed to get twitter home lines: ", e);
        }
        return checkInsLst;
    }


    public User getUser() {
        User user = null;
        try {
            user = getDbHelper().getUserDao().queryForAll().get(0);
        } catch (SQLException e) {
            Logger.e(TAG + ": Failed to get user: ", e);
        }
        return user;
    }

    public void addOrUpdCheckIns(List<CheckIn> checkIns) {
        try {
            for (CheckIn obj : checkIns)
                getDbHelper().getCheckInDao().createOrUpdate(obj);
        } catch (SQLException e) {
            Logger.e(TAG + ": Failed to add twitter home lines: ", e);
        }
    }

    public void addOrUpdUser(User user) {
        try {
            getDbHelper().getUserDao().createOrUpdate(user);
        } catch (SQLException e) {
            Logger.e(TAG + "Failed to add user: ", e);
        }
    }

}
