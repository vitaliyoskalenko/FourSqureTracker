package com.voskalenko.foursquretracker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckInResp {

    @JsonProperty("response")
    private CheckInNode response;

    public CheckInNode getResponse() {
        return response;
    }

    public void setResponse(CheckInNode response) {
        this.response = response;
    }
}
