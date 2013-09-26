package com.voskalenko.foursquretracker.model;

import com.j256.ormlite.field.DatabaseField;

public class CheckInsHistory {

    public static final String FIELD_ID = "_id";

    @DatabaseField(id = true, columnName = FIELD_ID)
    private long id;
    @DatabaseField
    String place;
    @DatabaseField
    private long checkInDate;
    @DatabaseField
    boolean automatically;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Venue venue;

    public static String getFieldId() {
        return FIELD_ID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public long getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(long checkInDate) {
        this.checkInDate = checkInDate;
    }

    public boolean isAutomatically() {
        return automatically;
    }

    public void setAutomatically(boolean automatically) {
        this.automatically = automatically;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }
}
