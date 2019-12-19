package com.tejani.pehlaj.chairlift.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import static android.content.Context.MODE_PRIVATE;

/**
 * @author Pehlaj Rai
 * @since 01/27/2015.
 */

public class SharedPrefUtils {

    public static final long CACHE_TIME = 24 * 60 * 60 * 1000;

    public static final String MyPREFERENCES = "iDryd";

    public static final String EMPTY = "";
    public static SharedPreferences sharedPreferences;

    private SharedPreferences.Editor editor;

    public SharedPrefUtils(Context context) {
        sharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static void updateVal(Context context, final String key, float value) {
        sharedPreferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value).apply();
    }

    public static float getFloatValue(Context context, final String key) {
        sharedPreferences = getSharedPreferences(context);
        return sharedPreferences.getFloat(key, 0);
    }

    public static float getFloatValue(Context context, final String key, float defaultValue) {
        sharedPreferences = getSharedPreferences(context);
        return sharedPreferences.getFloat(key, defaultValue);
    }

    public static boolean getBooleanVal(Context context, final String KEY) {
        try {
            sharedPreferences = getSharedPreferences(context);
            if (sharedPreferences != null) {
                if (sharedPreferences.contains(KEY)) {
                    return sharedPreferences.getBoolean(KEY, false);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void updateVal(Context context, final String KEY, final boolean VAL) {
        try {
            sharedPreferences = getSharedPreferences(context);
            if (sharedPreferences != null) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (editor != null) {
                    editor.putBoolean(KEY, VAL);
                    editor.apply();
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void updateVal(Context context, final String KEY, final long VAL) {
        try {
            if (context != null) {
                sharedPreferences = getSharedPreferences(context);
                if (sharedPreferences != null) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    if (editor != null) {
                        editor.putLong(KEY, VAL);
                        editor.apply();
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void updateVal(Context context, final String KEY, final String VAL) {
        try {
            if (context != null) {
                sharedPreferences = getSharedPreferences(context);
                if (VAL != null) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    if (editor != null) {
                        editor.putString(KEY, VAL);
                        editor.apply();
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static String getStringVal(Context context, final String KEY) {
        String tmp = EMPTY;
        try {
            if (context != null) {
                sharedPreferences = getSharedPreferences(context);
                if (sharedPreferences != null) {
                    if (sharedPreferences.contains(KEY)) {
                        tmp = sharedPreferences.getString(KEY, EMPTY);
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return tmp;
    }

    public static SharedPreferences getSharedPreferences(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return context.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
        }
        return context.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
    }

    public static long getLongVal(Context context, final String KEY) {
        try {
            if (context != null) {
                sharedPreferences = getSharedPreferences(context);
                if (sharedPreferences != null) {
                    if (sharedPreferences.contains(KEY)) {
                        return sharedPreferences.getLong(KEY, 0);
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return 0;
    }

}
