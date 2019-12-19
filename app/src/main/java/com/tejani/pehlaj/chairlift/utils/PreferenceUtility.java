package com.tejani.pehlaj.chairlift.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * This class serves as an intermediate layer between {@link SharedPreferences}
 * and your code. Contains various methods to set/get persistent values in
 * Shared Preferences.
 *
 * 
 */
public class PreferenceUtility {

	/**
	 * Set an integer value for a key in {@link SharedPreferences}.
	 * 
	 * @param context A valid context
	 * @param key Shared preferences key
	 * @param value Integer value to be saved
	 */
	public static void setInteger (Context context, String key, int value) {

		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences (context).edit ();
		editor.putInt (key, value);
		editor.apply();
		editor.commit ();
	}

	/**
	 * Set a boolean value for a key in {@link SharedPreferences}.
	 * 
	 * @param context A valid context
	 * @param key Shared preferences key
	 * @param value Boolean value to be saved
	 */
	public static void setBoolean (Context context, String key, boolean value) {

		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences (context).edit ();
		editor.putBoolean (key, value);
		editor.apply();
		editor.commit ();
	}

	/**
	 * Set a string value for a key in {@link SharedPreferences}.
	 * 
	 * @param context A valid context
	 * @param key Shared preferences key
	 * @param value String value to be saved
	 */
	public static void setString (Context context, String key, String value) {

		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences (context).edit ();
		editor.putString (key, value);
		editor.apply();
		editor.commit ();
	}

	/**
	 * Set a float value for a key in {@link SharedPreferences}.
	 * 
	 * @param context A valid context
	 * @param key Shared preferences key
	 * @param value Float value to be saved
	 */
	public static void setFloat (Context context, String key, float value) {

		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences (context).edit ();
		editor.putFloat (key, value);
		editor.apply();
		editor.commit ();
	}

	/**
	 * Set a long value for a key in {@link SharedPreferences}.
	 * 
	 * @param context A valid context
	 * @param key Shared preferences key
	 * @param value Long value to be saved
	 */
	public static void setLong (Context context, String key, long value) {

		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences (context).edit ();
		editor.putLong (key, value);
		editor.apply();
		editor.commit ();
	}

	/**
	 * Get an integer value for a key from {@link SharedPreferences}.
	 * 
	 * @param context A valid context
	 * @param key Shared preferences key, whose value is to be retrieved
	 * @param defaultValue Default value
	 * @return The integer value against the key. If key is not set,
	 *         defaultValue will be returned
	 */
	public static int getInteger (Context context, String key, int defaultValue) {

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences (context);
		return prefs.getInt (key, defaultValue);
	}

	/**
	 * Get a string value for a key from {@link SharedPreferences}.
	 * 
	 * @param context A valid context
	 * @param key Shared preferences key, whose value is to be retrieved
	 * @param defaultValue Default value
	 * @return The string value against the key. If key is not set, defaultValue
	 *         will be returned
	 */
	public static String getString (Context context, String key, String defaultValue) {

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences (context);
		return prefs.getString (key, defaultValue);
	}

	/**
	 * Get a boolean value for a key from {@link SharedPreferences}.
	 * 
	 * @param context A valid context
	 * @param key Shared preferences key, whose value is to be retrieved
	 * @param defaultValue Default value
	 * @return The boolean value against the key. If key is not set,
	 *         defaultValue will be returned
	 */
	public static boolean getBoolean (Context context, String key, boolean defaultValue) {

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences (context);
		return prefs.getBoolean (key, defaultValue);
	}

	/**
	 * Get a float value for a key from {@link SharedPreferences}.
	 * 
	 * @param context A valid context
	 * @param key Shared preferences key, whose value is to be retrieved
	 * @param defaultValue Default value
	 * @return The float value against the key. If key is not set, defaultValue
	 *         will be returned
	 */
	public static float getFloat (Context context, String key, float defaultValue) {

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences (context);
		return prefs.getFloat (key, defaultValue);
	}

	/**
	 * Get a long value for a key from {@link SharedPreferences}.
	 * 
	 * @param context A valid context
	 * @param key Shared preferences key, whose value is to be retrieved
	 * @param defaultValue Default value
	 * @return The longu value against the key. If key is not set, defaultValue
	 *         will be returned
	 */
	public static long getLong (Context context, String key, long defaultValue) {

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences (context);
		return prefs.getLong (key, defaultValue);
	}

    public static void saveStatusArray(Context context,String key, ArrayList<Integer> array) {

        JSONArray jArray = new JSONArray(array);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences (context);
        SharedPreferences.Editor editor= prefs.edit();
        editor.remove(key);
        editor.putString(key, jArray.toString());
		editor.apply();
		editor.commit();
    }
    public static void saveSortByPriceArray(Context context,String key, ArrayList<Integer> array) {

        JSONArray jArray = new JSONArray(array);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences (context);
        SharedPreferences.Editor editor= prefs.edit();
        editor.remove(key);
        editor.putString(key, jArray.toString());
		editor.apply();
		editor.commit();
    }
    public static ArrayList<Integer> getSortByPriceArray(Context context,String key) {
        ArrayList<Integer> array = new ArrayList<Integer>();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences (context);
        String jArrayString = prefs.getString(key, "NOPREFSAVED");
        if (jArrayString.matches("NOPREFSAVED")) return getDefaultArray();
        else {
            try {
                JSONArray jArray = new JSONArray(jArrayString);
                for (int i = 0; i < jArray.length(); i++) {
                    array.add(Integer.parseInt(jArray.getString(i)));
                }
                return array;
            } catch (JSONException e) {
                return getDefaultArray();
            }
        }
    }
    public static ArrayList<Integer> getStatusArray(Context context,String key) {
        ArrayList<Integer> array = new ArrayList<Integer>();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences (context);
        String jArrayString = prefs.getString(key, "NOPREFSAVED");
        if (jArrayString.matches("NOPREFSAVED")) return getDefaultArray();
        else {
            try {
                JSONArray jArray = new JSONArray(jArrayString);
                for (int i = 0; i < jArray.length(); i++) {
                    array.add(Integer.parseInt(jArray.getString(i)));
                }
                return array;
            } catch (JSONException e) {
                return getDefaultArray();
            }
        }
    }
    public static ArrayList<Integer> getFiltersByPartLevel(Context context,String key) {
        ArrayList<Integer> array = new ArrayList<Integer>();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences (context);
        String jArrayString = prefs.getString(key, "SAVED");
        if (jArrayString.matches("SAVED")) return getDefaultArray();
        else {
            try {
                JSONArray jArray = new JSONArray(jArrayString);
                for (int i = 0; i < jArray.length(); i++) {
                    array.add(Integer.parseInt(jArray.getString(i)));
                }
                return array;
            } catch (JSONException e) {
                return getDefaultArray();
            }
        }
    }
    public static void saveFiltersByPartLevel(Context context,String key, ArrayList<Integer> array) {

        JSONArray jArray = new JSONArray(array);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences (context);
        SharedPreferences.Editor editor= prefs.edit();
        editor.remove(key);
        editor.putString(key, jArray.toString());
		editor.apply();
		editor.commit();
    }
    // Get a default array in the event that there is no array
    // saved or a JSONException occurred
    private static ArrayList<Integer> getDefaultArray() {
         ArrayList<Integer> array = new ArrayList<Integer>();
        return array;
    }

	/**
	 * Set a string value for a key in {@link SharedPreferences}.
	 *
	 * @param context A valid context
	 * @param key Shared preferences key
	 */
	public static void removeString (Context context, String key) {

		PreferenceManager.getDefaultSharedPreferences (context)
				.edit()
				.remove(key)
				.apply();
	}
}
