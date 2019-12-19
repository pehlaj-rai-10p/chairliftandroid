package com.pehlaj.chairlift.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.pehlaj.chairlift.constants.Constants;

/**
 * @author Pehlaj
 * @since 6/4/2017.
 */

public class Booking extends BaseEntity implements Parcelable {

    protected int id;
    private int busId;
    private int riderId;

    private String status;
    private String pickupTime;
    private String arrivalTime;
    private String dropOffTime;
    private String bookingTime;

    private String trackingNumber;

    private Location pickupLocation;

    private Location dropoffLocation;

    public Booking() {
    }

    protected Booking(Parcel in) {
        id = in.readInt();
        busId = in.readInt();
        riderId = in.readInt();
        status = in.readString();
        pickupTime = in.readString();
        arrivalTime = in.readString();
        dropOffTime = in.readString();
        bookingTime = in.readString();
        trackingNumber = in.readString();
        pickupLocation = in.readParcelable(Location.class.getClassLoader());
        dropoffLocation = in.readParcelable(Location.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(busId);
        dest.writeInt(riderId);
        dest.writeString(status);
        dest.writeString(pickupTime);
        dest.writeString(arrivalTime);
        dest.writeString(dropOffTime);
        dest.writeString(bookingTime);
        dest.writeString(trackingNumber);
        dest.writeParcelable(pickupLocation, flags);
        dest.writeParcelable(dropoffLocation, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Booking> CREATOR = new Creator<Booking>() {
        @Override
        public Booking createFromParcel(Parcel in) {
            return new Booking(in);
        }

        @Override
        public Booking[] newArray(int size) {
            return new Booking[size];
        }
    };

    public int getId() {
        return id;
    }

    public int getBusId() {
        return busId;
    }

    public int getRiderId() {
        return riderId;
    }

    public String getStatus() {
        return status;
    }

    public String getPickupTime() {
        return pickupTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public String getDropOffTime() {
        return dropOffTime;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public Location getPickupLocation() {
        return pickupLocation;
    }

    public Location getDropoffLocation() {
        return dropoffLocation;
    }

    public String getPickupLatLng() {
        return pickupLocation == null ? Constants.NA : pickupLocation.getLatLng();
    }

    public String getDropoffLatLng() {
        return dropoffLocation == null ? Constants.NA : dropoffLocation.getLatLng();
    }
}
