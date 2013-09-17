package com.voskalenko.foursquretracker.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.voskalenko.foursquretracker.Logger;
import com.voskalenko.foursquretracker.model.CheckIn;
import com.voskalenko.foursquretracker.model.User;

public class DBHelper  extends OrmLiteSqliteOpenHelper{

    private static final String DATABASE_NAME = "foursquretracker.sqlite";
    private static final String TAG = DBHelper.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;
    private Dao<CheckIn, Integer> checkInDao = null;
    private Dao<User, Integer> userDao = null;

    public DBHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource conn) {
        try {
            TableUtils.createTable(conn, CheckIn.class);
            TableUtils.createTable(conn, User.class);
        } catch (SQLException e) {
            Logger.e(TAG + ": Can't create database", e);
            throw new RuntimeException(e);
        } catch (java.sql.SQLException e) {
            Logger.e(TAG, e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource conn, int oldVersion, int newVersion) {
    }

    public Dao<CheckIn, Integer> getCheckInDao() {
        if (checkInDao == null) {
            try {
                checkInDao = getDao(CheckIn.class);
            }catch (java.sql.SQLException e) {
                Logger.e(e);
            }
        }
        return checkInDao;
    }

    public Dao<User, Integer> getUserDao() {
        if (userDao == null) {
            try {
                userDao = getDao(User.class);
            }catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return userDao;
    }

}
