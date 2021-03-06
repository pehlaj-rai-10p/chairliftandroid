package com.tejani.pehlaj.chairlift.interfaces;

import com.tejani.pehlaj.chairlift.entities.Bus;

/**
 * @author Pehlaj Rai
 * @since 20th-June-2017.
 */

public interface BusCallback {

    void onItemClick(Bus bus);

    void onBookRide(Bus bus);
}
