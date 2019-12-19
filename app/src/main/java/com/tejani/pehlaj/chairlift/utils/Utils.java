package com.tejani.pehlaj.chairlift.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.tejani.pehlaj.chairlift.constants.Constants;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import static android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN;

/**
 * @author Pehlaj
 * @since 6/4/2017.
 */

public final class Utils {

    public static final String USERNAME = "iDryd_LG_UZRNM";
    public static final String PASSWORD = "iDryd_LG_PSWRD";
    public static final String LOGIN_STATUS = "iDryd_LG_STATUS";

    public static boolean validateEmail(String email) {

        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static void showToast(Context context, String message) {

        if (context == null) {
            return;
        }

        message = TextUtils.isEmpty(message) ? Constants.UNKNOWN_ERROR:  message;
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void showLongToast(Context context, String message) {

        if (context == null || TextUtils.isEmpty(message)) {
            return;
        }

        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static double getDouble(String str) {

        try {

            return Double.parseDouble(str);
        } catch (Exception e) {
            return 0d;
        }
    }

    public static void allowServerCertificates() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(
                    context.getSocketFactory());
        } catch (Throwable e) { // should never happen

        }
    }

    public static int getInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return 0;
    }

    public static boolean hasInValidResponse(Object response) {
        boolean check = response == null || TextUtils.isEmpty(response.toString()) || response.toString().toLowerCase().equalsIgnoreCase("bad request");

        return check;
    }

    public static boolean hasSuccess(Object response) {

        boolean check = response == null || TextUtils.isEmpty(response.toString()) || response.toString().toLowerCase().equalsIgnoreCase("bad request");

        if (check) {
            return !check;
        }

        String[] data = response.toString().split("\\|");

        if (data != null && data.length > 0) {

            for (int i = 0; i < data.length; i++) {
                if (!TextUtils.isEmpty(data[i]) && data[i].equals("000")) {
                    return true;
                }
            }
        }

        return false;
    }

    public static String getResponseCode(Object response) {

        boolean check = response == null || TextUtils.isEmpty(response.toString()) || response.toString().toLowerCase().equalsIgnoreCase("bad request");

        if (check) {
            return "";
        }

        String[] data = response.toString().split("\\|");

        if (data != null && data.length >= 6) {

            return data[5];
        }

        return "";
    }

    public static String getBase6Authorization(String username, String password) {
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            return "";
        }

        String credentials = String.format("%s:%s", username.trim(), password.trim());
        String basicAuth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        return basicAuth;
    }

    public static String sha256(String stringToHash) {
        StringBuilder sb = new StringBuilder();
        final MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            byte[] result = digest.digest(stringToHash.getBytes("UTF-8"));
            // Another way to make HEX, my previous post was only the method like your solution

            for (byte b : result) // This is your byte[] result..
            {
                sb.append(String.format("%02x", b));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String messageDigest = sb.toString();

        Log.w("SHA", messageDigest);

        return messageDigest;
    }

    public static void hideKeyboard(Activity activity) {
        try {
            View view = activity.getCurrentFocus();
            if(view != null) {
                InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            Window window = activity.getWindow();
            if(window != null) {
                window.setSoftInputMode(SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            }
        } catch(Throwable ignored) {}
    }

    public static void hideKeyboard(Context context, View view) {
        try {
            if(view != null) {
                InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch(Throwable ignored) {}
    }

    public static float dp2px (Resources resources, float dp) {

        final float scale = resources.getDisplayMetrics ().density;
        return dp * scale + 0.5f;
    }

    public static float sp2px (Resources resources, float sp) {

        final float scale = resources.getDisplayMetrics ().scaledDensity;
        return sp * scale;
    }

    public static float pixelsToSp (Context context, float px) {

        float scaledDensity = context.getResources ().getDisplayMetrics ().scaledDensity;
        return px / scaledDensity;
    }

}
