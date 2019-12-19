package com.tejani.pehlaj.chairlift.network;

import android.content.Context;

import com.tejani.pehlaj.chairlift.components.Loader;
import com.tejani.pehlaj.chairlift.interfaces.WebServiceCallBack;

import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 8/18/2016.
 */
public class WebClientCallBack<T> implements Callback<T> {

    private Context context;
    private WebServiceCallBack callBack;
    private boolean showLoader;
    private Loader loader;

    public WebClientCallBack(Context context, WebServiceCallBack callBack, boolean showLoader){
        this.context = context;
        this.callBack = callBack;
        this.showLoader = showLoader;

        initiateCall();
    }

    public void initiateCall(){

        if(showLoader) {
            loader = new Loader();
            loader.showLoader(context, "", "");
        }
    }


    @Override
    public void onResponse(retrofit2.Call call, Response response) {

        try {
            if (response == null) {
                callBack.onFailure("");
                return;
            }

            if (response.code() == 200) {
                callBack.onSuccess(response.body());
            } else {
                callBack.onFailure(response.message());
            }

            if (showLoader) {
                if (loader != null) {
                    loader.hideLoader();
                }
            }
        } catch (Throwable th) {
            callBack.onFailure(th.getMessage());
            th.printStackTrace();
        }

    }

    @Override
    public void onFailure(retrofit2.Call call, Throwable t) {

        try {
            callBack.onFailure(t.getMessage());
            if (showLoader) {
                if (loader != null) {
                    loader.hideLoader();
                }
            }
        } catch (Throwable th) {
            callBack.onFailure(th.getMessage());
            t.printStackTrace();
        }

    }
}
