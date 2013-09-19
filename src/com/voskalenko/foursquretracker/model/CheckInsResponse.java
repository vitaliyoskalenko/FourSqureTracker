package com.voskalenko.foursquretracker.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckInsResponse {

    @JsonProperty("response")
    private CheckInList response;

    public CheckInList getResponse() {
        return response;
    }

    public void setResponse(CheckInList response) {
        this.response = response;
    }


}
