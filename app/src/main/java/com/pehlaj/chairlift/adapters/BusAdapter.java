package com.pehlaj.chairlift.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.pehlaj.chairlift.R;
import com.pehlaj.chairlift.entities.Bus;
import com.pehlaj.chairlift.interfaces.BusCallback;
import com.pehlaj.chairlift.viewholders.BusViewHolder;

import java.util.List;

/**
 * @author Pehlaj
 * @since 18th-Dec-2019.
 */
public class BusAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final List<Bus> list;

    private int lastPosition = -1;

    private final BusCallback itemCallback;

    public BusAdapter(Context context, List<Bus> list, BusCallback itemCallback) {

        this.list = list;
        this.context = context;
        this.itemCallback = itemCallback;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bus_list, parent, false);
        return new BusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {

        BusViewHolder holder = (BusViewHolder) viewHolder;
        final Bus bus = list.get(position);

        holder.setData(bus);

        setAnimation(holder.view, position);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (itemCallback == null) {
                    return;
                }

                itemCallback.onItemClick(bus);
            }
        });

        holder.bookRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (itemCallback == null) {
                    return;
                }

                itemCallback.onBookRide(bus);
            }
        });
    }

    private void setAnimation(View viewToAnimate, int position) {
        Animation animation = AnimationUtils.loadAnimation(context, position > lastPosition ? R.anim.slide_in_left : R.anim.slide_up);
        viewToAnimate.startAnimation(animation);
        lastPosition = position;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : (list.size());
    }
}
