package com.pehlaj.chairlift.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pehlaj.chairlift.R;

/**
 * Created by 19th Dec, 2019.
 */
public class BookingViewHolder extends RecyclerView.ViewHolder {

    public final TextView txtTrackingNum, txtStatus, txtPickup, txtDropOff, txtBookingTime, txtDropoffTime;

    public View view;

    public BookingViewHolder(View view) {
        super(view);
        this.view = view;

        txtStatus = view.findViewById(R.id.txtStatus);
        txtPickup = view.findViewById(R.id.txtPickup);
        txtDropOff = view.findViewById(R.id.txtDropoff);
        txtBookingTime = view.findViewById(R.id.txtBookingTime);
        txtDropoffTime = view.findViewById(R.id.txtDropoffTime);
        txtTrackingNum = view.findViewById(R.id.txtTrackingNum);
    }
}
