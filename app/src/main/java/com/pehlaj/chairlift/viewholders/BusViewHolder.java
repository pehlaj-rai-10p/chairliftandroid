package com.pehlaj.chairlift.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pehlaj.chairlift.R;
import com.pehlaj.chairlift.entities.Bus;

/**
 * Created by 18th-Dec-2019.
 */
public class BusViewHolder extends RecyclerView.ViewHolder {

    private TextView txtTitle, txtDesc, txtTime, txtStatus, txtSeats;

    public View view;
    public View bookRide;

    public BusViewHolder(View view) {
        super(view);
        this.view = view;
        initUI();
    }

    private void initUI() {

        bookRide = view.findViewById(R.id.btnBook);
        txtDesc = view.findViewById(R.id.txtDesc);
        txtTime = view.findViewById(R.id.txtTime);
        txtTitle = view.findViewById(R.id.txtTitle);
        txtSeats = view.findViewById(R.id.txtSeats);
        txtStatus = view.findViewById(R.id.txtStatus);
    }

    public void setData(Bus bus) {

        txtDesc.setText(bus.getRegistrationNumber());
        txtTime.setText(bus.getChasisNumber());
        txtTitle.setText(bus.getDriverName());
        txtStatus.setText(bus.getStatus());
        txtSeats.setText(String.format("Seats remaining: %s", bus.getAvailableSeats()));
    }

}
