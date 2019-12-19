package com.pehlaj.chairlift.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pehlaj.chairlift.R;
import com.pehlaj.chairlift.entities.Booking;
import com.pehlaj.chairlift.interfaces.ItemCallback;
import com.pehlaj.chairlift.viewholders.BookingViewHolder;

import java.util.List;

/**
 * @author Pehlaj
 * @since 19th-Dec-2019.
 */
public class BookingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final List<Booking> list;

    private int lastPosition = -1;

    private final ItemCallback itemCallback;

    public BookingAdapter(Context context, List<Booking> list, ItemCallback itemCallback) {

        this.list = list;
        this.context = context;
        this.itemCallback = itemCallback;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {

        BookingViewHolder holder = (BookingViewHolder) viewHolder;
        final Booking booking = list.get(position);

        holder.txtStatus.setText(booking.getStatus());
        holder.txtPickup.setText(booking.getPickupLatLng());
        holder.txtDropOff.setText(booking.getDropoffLatLng());
        holder.txtBookingTime.setText(booking.getBookingTime());
        holder.txtDropoffTime.setText(booking.getDropOffTime());
        holder.txtTrackingNum.setText(booking.getTrackingNumber());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (itemCallback == null) {
                    return;
                }

                itemCallback.onItemClick(booking);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        return list.size();
    }
}
