package com.voskalenko.foursquretracker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckInList {

    @JsonProperty("checkins")
    private CheckIns checkInList;

    public CheckIns getCheckInList() {
        return checkInList;
    }

    public void setCheckInList(CheckIns checkInList) {
        this.checkInList = checkInList;
    }
}
