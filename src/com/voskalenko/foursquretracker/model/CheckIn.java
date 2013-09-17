package com.voskalenko.foursquretracker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckIn  {

    @DatabaseField(id = true)
    @JsonProperty("id")
    private String id;

    @DatabaseField
    @JsonProperty("createdAt")
    private long createdAt;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    @JsonProperty("venue")
    private Venue venue;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }


}
