/*
 * @(#)DatabaseHelper.java  1.0 2013/09/11
 *
 * Copyright (C) 2013 Vitaly Oskalenko, oskalenkoVit@ukr.net
 * All rights for the program belong to the postindustria company
 * and are its intellectual property
 */

package com.voskalenko.foursquretracker.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.api.Scope;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.voskalenko.foursquretracker.Logger;
import com.voskalenko.foursquretracker.model.CheckInsHistory;
import com.voskalenko.foursquretracker.model.Venue;

@EBean(scope = Scope.Singleton)
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "foursquretracker8.sqlite";
    private static final String TAG = DatabaseHelper.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;
    private Dao<Venue, Integer> venuesDao = null;
    private Dao<CheckInsHistory, Integer> checkInsHistoryDao = null;

    public DatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource conn) {
        try {
            TableUtils.createTable(conn, Venue.class);
            TableUtils.createTable(conn, CheckInsHistory.class);
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

    public Dao<Venue, Integer> getVenuesDao() {
        if (venuesDao == null) {
            try {
                venuesDao = getDao(Venue.class);
            } catch (java.sql.SQLException e) {
                Logger.e(e);
            }
        }
        return venuesDao;
    }

    public Dao<CheckInsHistory, Integer> getCheckInsHistoryDao() {
        if (checkInsHistoryDao == null) {
            try {
                checkInsHistoryDao = getDao(CheckInsHistory.class);
            } catch (java.sql.SQLException e) {
                Logger.e(e);
            }
        }
        return checkInsHistoryDao;
    }
}
