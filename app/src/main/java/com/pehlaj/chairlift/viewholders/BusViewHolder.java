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

    private TextView txtDriverName, txtRegistrationNum, txtChasisNum, txtBusStatus, txtSeats;

    public View view;
    public View bookRide;

    public BusViewHolder(View view) {
        super(view);
        this.view = view;
        initUI();
    }

    private void initUI() {

        bookRide = view.findViewById(R.id.btnBook);
        txtSeats = view.findViewById(R.id.txtSeats);
        txtBusStatus = view.findViewById(R.id.txtBusStatus);
        txtChasisNum = view.findViewById(R.id.txtChasisNum);
        txtDriverName = view.findViewById(R.id.txtDriverName);
        txtRegistrationNum = view.findViewById(R.id.txtRegistrationNum);
    }

    public void setData(Bus bus) {

        txtSeats.setText(bus.getSeatsAvailability());
        txtBusStatus.setText(bus.getStatus());
        txtChasisNum.setText(bus.getChasisNumber());
        txtDriverName.setText(bus.getDriverName());
        txtRegistrationNum.setText(bus.getRegistrationNumber());
    }

}
