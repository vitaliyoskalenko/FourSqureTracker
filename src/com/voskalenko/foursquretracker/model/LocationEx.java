package com.voskalenko.foursquretracker.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationEx implements Serializable, Parcelable {

    public static final String FIELD_LATITUDE = "latitude";
    public static final String FIELD_LONGITUDE = "longitude";

    @JsonProperty("lat")
    private double latitude;
    @JsonProperty("lng")
    private double longitude;
    @JsonProperty("city")
    private String city;
    @JsonProperty("country")
    private String country;
    @JsonProperty("distance")
    private long distance;


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeString(city);
        parcel.writeString(country);
        parcel.writeLong(distance);
    }
    public static final Parcelable.Creator<LocationEx> CREATOR = new Parcelable.Creator<LocationEx>() {

        @Override
        public LocationEx createFromParcel(Parcel in) {
            LocationEx location= new LocationEx();
            location.setLatitude(in.readDouble());
            location.setLongitude(in.readDouble());
            location.setCity(in.readString());
            location.setCountry(in.readString());
            location.setDistance(in.readLong());

            return location;
        }

        @Override
        public LocationEx[] newArray(int size) {
            return new LocationEx[size];
        }
    };

    @Override
    public String toString() {
        return getCountry() + "," + getCity() + "\n distance: " + getDistance();
    }
}

