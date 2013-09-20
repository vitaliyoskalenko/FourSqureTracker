package com.voskalenko.foursquretracker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VenueResponse {

    @JsonProperty("response")
    private VenueList response;

    public VenueList getResponse() {
        return response;
    }

    public void setResponse(VenueList response) {
        this.response = response;
    }


}
