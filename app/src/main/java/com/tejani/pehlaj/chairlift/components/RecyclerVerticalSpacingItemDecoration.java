package com.tejani.pehlaj.chairlift.components;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * Created by 8/24/2016.
 */
public class RecyclerVerticalSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private final int mVerticalSpaceHeight;

    public RecyclerVerticalSpacingItemDecoration(int mVerticalSpaceHeight) {
        this.mVerticalSpaceHeight = mVerticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = mVerticalSpaceHeight;
    }
}
