package com.pehlaj.chairlift.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.JsonObject;
import com.pehlaj.chairlift.constants.KeyConstants;

/**
 * @author Pehlaj
 * @since 6/4/2017.
 */

public class Location implements Parcelable {

    private String lat;
    private String lng;

    public Location() {
    }

    public Location(String lat, String lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(KeyConstants.KEY_LAT, lat);
        jsonObject.addProperty(KeyConstants.KEY_LNG, lng);
        return jsonObject;
    }

    @NonNull
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nLAT: ");
        sb.append(lat);
        sb.append(", \tLNG: ");
        sb.append(lng);
        return sb.toString();
    }

    protected Location(Parcel in) {
        lat = in.readString();
        lng = in.readString();
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(lat);
        dest.writeString(lng);
    }
}