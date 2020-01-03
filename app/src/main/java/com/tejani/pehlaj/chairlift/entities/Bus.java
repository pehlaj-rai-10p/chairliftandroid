package com.tejani.pehlaj.chairlift.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;

/**
 * @author Pehlaj
 * @since 6/4/2017.
 */

public class Bus implements Parcelable {

    protected int id;

    private String year;
    private String capacity;
    private String availableSeats;

    private String registrationNumber;

    private String chasisNumber;

    private String driverName;
    private String make;
    private String model;
    private String status;

    private String error;

    private JsonArray route;

    public Bus() {
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ");
        sb.append(id);
        sb.append(", \tStatus: ");
        sb.append(status);
        sb.append(", \tRegistration #: ");
        sb.append(registrationNumber);
        sb.append(", \tChasis #: ");
        sb.append(chasisNumber);
        sb.append(", \tMake = ");
        sb.append(make);
        sb.append(", \tModel: ");
        sb.append(model);
        sb.append(", \tYear: ");
        sb.append(year);
        sb.append(", \tCapacity: ");
        sb.append(capacity);
        sb.append(", \tAvailable Seats #: ");
        sb.append(availableSeats);
        return sb.toString();
    }

    protected Bus(Parcel in) {
        id = in.readInt();
        year = in.readString();
        capacity = in.readString();
        availableSeats = in.readString();
        registrationNumber = in.readString();
        chasisNumber = in.readString();
        driverName = in.readString();
        make = in.readString();
        model = in.readString();
        status = in.readString();
        error = in.readString();
    }

    public static final Creator<Bus> CREATOR = new Creator<Bus>() {
        @Override
        public Bus createFromParcel(Parcel in) {
            return new Bus(in);
        }

        @Override
        public Bus[] newArray(int size) {
            return new Bus[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getYear() {
        return year;
    }

    public String getCapacity() {
        return capacity;
    }

    public String getSeatsAvailability() {
        return String.format("Seats remaining: %s", availableSeats);
    }

    public String getAvailableSeats() {
        return availableSeats;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getChasisNumber() {
        return chasisNumber;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public String getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public JsonArray getRoute() {
        return route;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(year);
        dest.writeString(capacity);
        dest.writeString(availableSeats);
        dest.writeString(registrationNumber);
        dest.writeString(chasisNumber);
        dest.writeString(driverName);
        dest.writeString(make);
        dest.writeString(model);
        dest.writeString(status);
        dest.writeString(error);
    }
}