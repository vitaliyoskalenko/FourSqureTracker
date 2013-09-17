package com.voskalenko.foursquretracker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckInsResp {

    @JsonProperty("checkins")
    private CheckInList checkInList;

    public CheckInList getCheckInList() {
        return checkInList;
    }

    public void setCheckInList(CheckInList checkInList) {
        this.checkInList = checkInList;
    }
}
