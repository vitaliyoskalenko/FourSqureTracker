package com.voskalenko.foursquretracker.model;


import com.fasterxml.jackson.annotation.JsonProperty;

public class CheckInPostBody {

    @JsonProperty("venueId")
    private String venueId;
    //private String eventId;
    @JsonProperty("shout")
    private String shout;

    public String getVenueId() {
        return venueId;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
    }

  /*  public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }*/

    public String getShout() {
        return shout;
    }

    public void setShout(String shout) {
        this.shout = shout;
    }
}