package com.pehlaj.chairlift.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.pehlaj.chairlift.R;
import com.pehlaj.chairlift.fragments.BookingFragment;

/**
 * @author Pehlaj
 * @since 17-Jun-17.
 */
public class HomeViewPagerAdapter extends FragmentPagerAdapter {

	private final Context    context;

	private static final int PAGE_COUNT = 3;

    public HomeViewPagerAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);

        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {

        String type = context.getString(R.string.current);
        switch (position) {
            case 1:
                type = context.getString(R.string.future);
                break;
            case 2:
                type = context.getString(R.string.completed);
                break;
        }

        return BookingFragment.newInstance(type);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 1:
                return context.getString(R.string.future);
            case 2:
                return context.getString(R.string.completed);
        }

        return context.getString(R.string.current);
    }

}
