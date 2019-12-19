package com.tejani.pehlaj.chairlift.entities;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * @author Pehlaj
 * @since 6/4/2017.
 */

public class Rider implements Parcelable {

    protected int id;

    private int credits;

    @SerializedName("numFreeRides")
    private int freeRides;

    private String firstName;
    private String lastName;
    private String userName;

    private String passwordHash;

    private String emailAddress;

    private String mobileNumber;
    private String profilePicUrl;
    private String status;

    private String error;

    private Rider(Parcel in) {
        id = in.readInt();
        credits = in.readInt();
        freeRides = in.readInt();
        firstName = in.readString();
        lastName = in.readString();
        userName = in.readString();
        passwordHash = in.readString();
        emailAddress = in.readString();
        mobileNumber = in.readString();
        profilePicUrl = in.readString();
        status = in.readString();
        error = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(credits);
        dest.writeInt(freeRides);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(userName);
        dest.writeString(passwordHash);
        dest.writeString(emailAddress);
        dest.writeString(mobileNumber);
        dest.writeString(profilePicUrl);
        dest.writeString(status);
        dest.writeString(error);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Rider> CREATOR = new Creator<Rider>() {
        @Override
        public Rider createFromParcel(Parcel in) {
            return new Rider(in);
        }

        @Override
        public Rider[] newArray(int size) {
            return new Rider[size];
        }
    };

    @NonNull
    public String toString() {
        return "ID: " + id +
                ", \tStatus: " +
                status +
                ", \tFN: " +
                firstName +
                ", \tLN: " +
                lastName +
                ", \tUN: " +
                userName +
                ", \tMOBILE: " +
                mobileNumber +
                ", \tEMAIL: " +
                emailAddress +
                ", \tPIC: " +
                profilePicUrl;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public String getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

}