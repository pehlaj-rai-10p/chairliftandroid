package com.tejani.pehlaj.chairlift.interfaces;

/**
 * @author Pehlaj
 * @since 6/05/2017.
 */
public interface WebServiceCallBack {

    void onSuccess(Object response);

    void onFailure(String errorMessage);

}
