package com.voskalenko.foursquretracker.database;

import android.text.TextUtils;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.api.Scope;
import com.voskalenko.foursquretracker.Logger;
import com.voskalenko.foursquretracker.model.CheckInsHistory;
import com.voskalenko.foursquretracker.model.Venue;

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

    public List<Venue> getVenues() {
        List<Venue> venueList = null;
        try {
            venueList = getDbHelper().getVenuesDao().queryForAll();
        } catch (SQLException e) {
            Logger.e(TAG + ": Failed to get twitter home lines: ", e);
        }
        return venueList;
    }

    public List<CheckInsHistory> getCheckInsHistory(String condition) {
        List<CheckInsHistory> checkInsList = null;
        try {
            if (TextUtils.isEmpty(condition)) {
            checkInsList = getDbHelper().getCheckInsHistoryDao().queryForAll();
            } else {
                checkInsList = getDbHelper().getCheckInsHistoryDao()
                        .queryBuilder().where().like("place", "%" + condition + "%").query();
            }
        } catch (SQLException e) {
            Logger.e(TAG + ": Failed to get check-in's history", e);
        }
        return checkInsList;
    }

    public void addChickInToHistory(CheckInsHistory checkInsHistory) {
        try {
            getDbHelper().getCheckInsHistoryDao().create(checkInsHistory);
        } catch (SQLException e) {
            Logger.e(TAG + "Failed to add to check-in's history", e);
        }
    }


    public void addOrUpdVenues(List<Venue> venues) {
        try {
            for (Venue venue : venues) {
                getDbHelper().getVenuesDao().createOrUpdate(venue);
            }
        } catch (SQLException e) {
            Logger.e(TAG + ": Failed to add twitter home lines: ", e);
        }
    }

    public void uncheckProposed() {
        try {
            getDbHelper().getVenuesDao().updateRaw("UPDATE `" + Venue.TABLE_NAME + "` SET proposed = 0;");
        } catch (SQLException e) {
            Logger.e(TAG + ": Failde to uncheck proposed field", e);
        }
    }

    public void setMuted(Venue venue) {
        try {
            getDbHelper().getVenuesDao().update(venue);
        } catch (SQLException e) {
            Logger.e(TAG + ": Failed to mute venue", e);
        }
    }

    public void cleanCheckInsHistory() {
        try {
            getDbHelper().getCheckInsHistoryDao().deleteBuilder().delete();
        } catch (SQLException e) {
            Logger.e(TAG + ": Failed to clean check-ns history", e);
        }
    }
}
