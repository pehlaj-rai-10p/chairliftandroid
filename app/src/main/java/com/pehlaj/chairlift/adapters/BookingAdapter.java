package com.pehlaj.chairlift.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pehlaj.chairlift.constants.Constants;
import com.pehlaj.chairlift.entities.Booking;
import com.pehlaj.chairlift.interfaces.ItemCallback;
import com.pehlaj.chairlift.viewholders.HeaderViewHolder;
import com.pehlaj.chairlift.viewholders.BookingViewHolder;
import com.pehlaj.chairlift.R;

import java.util.List;

/**
 * @author Pehlaj
 * @since 19th-June-2017.
 */
public class BookingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private final Context                 context;
	private final List<Booking> list;

	private int                           lastPosition = -1;

	private final ItemCallback itemCallback;

    public BookingAdapter(Context context, List<Booking> list, ItemCallback itemCallback) {

        this.list = list;
        this.context = context;
        this.itemCallback = itemCallback;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? Constants.VIEW_TYPE_HEADER : Constants.VIEW_TYPE_DEFAULT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == Constants.VIEW_TYPE_HEADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_booking_header, parent, false);
            return new HeaderViewHolder(view);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {

        if (viewHolder instanceof BookingViewHolder) {
            BookingViewHolder holder = (BookingViewHolder) viewHolder;
            final Booking booking = list.get(position - 1);

            holder.txtName.setText(String.valueOf(booking.getBusId()));
            holder.txtAmount.setText(String.valueOf(booking.getRiderId()));
            holder.txtCategory.setText(booking.getStatus());

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

    }

    @Override
    public int getItemCount() {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        return list.size() + 1;
    }
}
