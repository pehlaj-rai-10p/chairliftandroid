package com.pehlaj.chairlift.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pehlaj.chairlift.R;

/**
 * Created by 8/22/2016.
 */
public class BookingViewHolder extends RecyclerView.ViewHolder {

    public TextView txtName, txtAmount, txtCategory;

    public View view;

    public BookingViewHolder(View view) {
        super(view);
        this.view = view;
        initUI();
    }

    private void initUI() {

        txtName = view.findViewById(R.id.txtName);
        txtAmount = view.findViewById(R.id.txtAmount);
        txtCategory = view.findViewById(R.id.txtCategory);
    }
}
