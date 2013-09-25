package com.voskalenko.foursquretracker.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.voskalenko.foursquretracker.Logger;

import java.io.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Venue implements Serializable, Parcelable {

    public static final String TABLE_NAME = "venue";
    public static final String FIELD_ID = "_id";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_PROPOSED = "proposed";
    public static final String FIELD_LOCATION = "location";
    public static final String FIELD_MUTED = "muted";
    public static final int PROPOSED_FLAG = 1;
    public static final int FLAG_MUTED = 1;

    private static final Object TAG = Venue.class.getSimpleName();

    @DatabaseField(id = true, columnName = FIELD_ID)
    @JsonProperty("id")
    private String id;

    @DatabaseField
    @JsonProperty(FIELD_NAME)
    private String name;

    @DatabaseField
    private int proposed;
    @DatabaseField
    private int muted;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    @JsonProperty("location")
    private LocationEx location;

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

    public static String getFieldProposed() {
        return FIELD_PROPOSED;
    }

    public LocationEx getLocation() {
        return location;
    }

    public void setLocation(LocationEx location) {
        this.location = location;
    }

    public int getProposed() {
        return proposed;
    }

    public void setProposed(int proposed) {
        this.proposed = proposed;
    }

    public int getMuted() {
        return muted;
    }

    public void setMuted(int muted) {
        this.muted = muted;
    }

    public static Venue fromCursor(Cursor cursor) {
        Venue venue = null;
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                venue = new Venue();

                int idIndex = cursor.getColumnIndex(FIELD_ID);
                if (idIndex >= 0) {
                    venue.setId(cursor.getString(idIndex));
                }

                int nameIndex = cursor.getColumnIndex(FIELD_NAME);
                if (nameIndex >= 0) {
                    venue.setName(cursor.getString(nameIndex));
                }

                int locationIndex = cursor.getColumnIndex(FIELD_LOCATION);
                if (locationIndex >= 0) {

                    try {
                        InputStream inputStream = new ByteArrayInputStream(cursor.getBlob(locationIndex));
                        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                        LocationEx location = (LocationEx) objectInputStream.readObject();
                        venue.setLocation(location);

                        objectInputStream.close();
                        inputStream.close();
                    } catch (IOException e) {
                        Logger.e(TAG + ": Converting byte[] to Location Exception", e);
                    } catch (ClassNotFoundException e) {
                        Logger.e(TAG + ": Converting byte[] to Location Exception", e);
                    }

                }
            }
        }

        return venue;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Venue other = (Venue) obj;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        return true;
    }

    @Override
    public int hashCode() {
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
            Venue venue = new Venue();
            venue.setId(in.readString());
            venue.setName(in.readString());
            venue.setLocation((LocationEx) in.readParcelable(LocationEx.class.getClassLoader()));
            return venue;
        }

        @Override
        public Venue[] newArray(int size) {
            return new Venue[size];
        }

    };

    @Override
    public String toString() {
        return "id: " + getId() + "\n name: " + getName();
    }
}
