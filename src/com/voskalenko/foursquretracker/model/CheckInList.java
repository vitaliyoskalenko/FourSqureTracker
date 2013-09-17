package com.voskalenko.foursquretracker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckInList  implements Serializable {

    @JsonProperty("count")
    private int count;
    @JsonProperty("items")
    private List<CheckIn> checkins;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<CheckIn> getCheckins() {
        return checkins;
    }

    public void setCheckins(List<CheckIn> checkins) {
        this.checkins = checkins;
    }
}
