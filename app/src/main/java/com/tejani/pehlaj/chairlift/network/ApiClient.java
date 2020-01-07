package com.tejani.pehlaj.chairlift.network;

/**
 * Created by 8/17/2016.
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tejani.pehlaj.chairlift.constants.EnvironmentConstants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String GSON_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static Retrofit retrofit = null;

    private ApiClient() {
    }

    public static synchronized void resetClient() {
        retrofit = null;
    }

    public static synchronized Retrofit getClient() {

        Gson gson = new GsonBuilder()
                .setDateFormat(GSON_DATE_FORMAT)
                .setLenient()
                .create();

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(EnvironmentConstants.API_BASE_URL_AWS)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build();
        }

        return retrofit;
    }
}