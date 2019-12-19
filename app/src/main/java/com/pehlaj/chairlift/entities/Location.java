package com.pehlaj.chairlift.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.JsonObject;
import com.pehlaj.chairlift.constants.Constants;
import com.pehlaj.chairlift.constants.KeyConstants;

/**
 * @author Pehlaj
 * @since 6/4/2017.
 */

public class Location implements Parcelable {

    private double lat;
    private double lng;

    public Location() {
    }

    public Location(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(KeyConstants.KEY_LAT, lat);
        jsonObject.addProperty(KeyConstants.KEY_LNG, lng);
        return jsonObject;
    }

    public String getLatLng() {
        return String.format(Constants.FORMAT_SCommaS, lat, lng);
    }

    @NonNull
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nLAT: ");
        sb.append(lat);
        sb.append(", \nLNG: ");
        sb.append(lng);
        return sb.toString();
    }

    protected Location(Parcel in) {
        lat = in.readDouble();
        lng = in.readDouble();
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
        dest.writeDouble(lat);
        dest.writeDouble(lng);
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}