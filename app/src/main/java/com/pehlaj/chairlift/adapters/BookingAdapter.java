package com.pehlaj.chairlift.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pehlaj.chairlift.constants.Constants;
import com.pehlaj.chairlift.entities.Booking;
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

    public BookingAdapter(Context context, List<Booking> list) {

        this.context = context;
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? Constants.VIEW_TYPE_HEADER : Constants.VIEW_TYPE_DEFAULT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Constants.VIEW_TYPE_HEADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_booking_header, parent, false);
            return new HeaderViewHolder(view);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        if (viewHolder instanceof BookingViewHolder) {
            BookingViewHolder holder = (BookingViewHolder) viewHolder;
            Booking commission = list.get(position - 1);

            holder.txtName.setText(String.valueOf(commission.getBusId()));
            holder.txtAmount.setText(String.valueOf(commission.getRiderId()));
            holder.txtCategory.setText(commission.getStatus());
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
