package com.voskalenko.foursquretracker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponse {

    @JsonProperty("response")
    private UserNode response;

    public UserNode getResponse() {
        return response;
    }

    public void setResponse(UserNode response) {
        this.response = response;
    }
}
