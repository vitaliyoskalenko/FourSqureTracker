package com.voskalenko.foursquretracker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.j256.ormlite.field.DatabaseField;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName("user")
public class User {

    @DatabaseField(id = true)
    @JsonProperty("id")
    private long id;

    @DatabaseField
    @JsonProperty("firstName")
    private String firstName;

    @DatabaseField
    @JsonProperty("lastName")
    private String lastName;

    @DatabaseField
    @JsonProperty("homeCity")
    private String city;

    @DatabaseField
    @JsonProperty("photo")
    private String photoUrl;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}

