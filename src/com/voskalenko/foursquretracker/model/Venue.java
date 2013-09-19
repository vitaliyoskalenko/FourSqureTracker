package com.voskalenko.foursquretracker.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Venue implements Serializable, Parcelable{

    @DatabaseField(id = true)
    @JsonProperty("id")
    private String id;

    @DatabaseField
    @JsonProperty("name")
    private String name;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    @JsonProperty("location")
    private Location location;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
    @Override
    public boolean equals(Object obj){
        if(this == obj) return true;
        if(obj == null) return false;
        if(getClass() != obj.getClass()) return false;
        Venue other = (Venue) obj;
        if(id == null){
            if(other.id != null) return false;
        } else if(!id.equals(other.id)) return false;
        return true;
    }

    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeParcelable(location, i);
    }

    public static final Parcelable.Creator<Venue> CREATOR = new Parcelable.Creator<Venue>() {

        @Override
        public Venue createFromParcel(Parcel in) {
            Venue  venue= new Venue();
            venue.setId(in.readString());
            venue.setName(in.readString());
            venue.setLocation((Location) in.readParcelable(Location.class.getClassLoader()));
            return venue;
        }

        @Override
        public Venue[] newArray(int size) {
            return new Venue[size];
        }

    };

    @Override
    public String toString() {
        return "id: " + getId() + ", name: " + getName();
    }
}
