package com.tejani.pehlaj.chairlift.interfaces;

import com.tejani.pehlaj.chairlift.entities.Booking;

/**
 * @author Pehlaj Rai
 * @since 20th-June-2017.
 */

public interface BookingCallback {

    void onItemClick(Booking booking);

    void onTrackRide(Booking booking);
}
