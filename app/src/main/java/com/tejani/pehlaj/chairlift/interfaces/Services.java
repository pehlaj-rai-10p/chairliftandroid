package com.tejani.pehlaj.chairlift.interfaces;

import com.google.gson.JsonObject;
import com.tejani.pehlaj.chairlift.constants.KeyConstants;
import com.tejani.pehlaj.chairlift.constants.ServiceConstants;
import com.tejani.pehlaj.chairlift.entities.ApiResponse;
import com.tejani.pehlaj.chairlift.entities.BaseEntity;
import com.tejani.pehlaj.chairlift.entities.BookingResponse;
import com.tejani.pehlaj.chairlift.entities.BusDetails;
import com.tejani.pehlaj.chairlift.entities.LoginResponse;
import com.tejani.pehlaj.chairlift.entities.BusResponse;
import com.tejani.pehlaj.chairlift.entities.RiderDetails;
import com.tejani.pehlaj.chairlift.entities.UserProfile;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author Pehlaj
 * @since 6/6/2017.
 */
public interface Services {

    @FormUrlEncoded
    @POST(ServiceConstants.SIGN_UP)
    Call<BaseEntity> register(@Field("devicetype") String type,
                              @Field("username") String username,
                              @Field("fullname") String fullName,
                              @Field("token") String token,
                              @Field("mobilenumber") String mobileNumber,
                              @Field("devicetoken") String gcmToken);

    @GET(ServiceConstants.LOGIN)
    Call<LoginResponse> login(@Header(KeyConstants.AUTHORIZATION) String base64Auth, @Header(KeyConstants.TOKEN) String gcmToken, @Header("devicetype") String deviceType);

    @Headers( "Content-Type: application/json" )
    @POST(ServiceConstants.RIDER_INFO)
    Call<RiderDetails> getRiderInfo(@Body JsonObject body);

    @Headers( "Content-Type: application/json" )
    @POST(ServiceConstants.BOOKING)
    Call<ApiResponse> bookRide(@Body JsonObject body);

    @GET(ServiceConstants.BUS)
    Call<BusResponse> getBusList(@Header(KeyConstants.AUTHORIZATION) String base64Auth);

    @GET(ServiceConstants.CURRENT_BOOKING)
    Call<BusResponse> getCurrentBookings(@Header(KeyConstants.AUTHORIZATION) String base64Auth);

    @GET(ServiceConstants.FUTURE_BOOKING)
    Call<BusResponse> getFutureBookings(@Header(KeyConstants.AUTHORIZATION) String base64Auth);

    @GET(ServiceConstants.COMPLETED_BOOKING)
    Call<BusResponse> getCompletedBookings(@Header(KeyConstants.AUTHORIZATION) String base64Auth);

    @GET(ServiceConstants.BOOKING)
    Call<BookingResponse> getBookings(@Header(KeyConstants.AUTHORIZATION) String base64Auth);

    @GET(ServiceConstants.BOOKING)
    Call<BookingResponse> getBookings(@Query(KeyConstants.KEY_STATUS) String status, @Query(KeyConstants.KEY_RIDER_ID) int riderId);

    @GET(ServiceConstants.BUS_DETAILS)
    Call<BusDetails> getBusDetails(@Header(KeyConstants.AUTHORIZATION) String base64Auth, @Path(KeyConstants.KEY_ID) int id);

    @GET(ServiceConstants.RIDER_DETAILS)
    Call<BusResponse> getRiderDetails(@Header(KeyConstants.AUTHORIZATION) String base64Auth, @Path(KeyConstants.KEY_ID) String id);

    @GET(ServiceConstants.BOOKING_DETAILS)
    Call<BusResponse> getBookingDetails(@Header(KeyConstants.AUTHORIZATION) String base64Auth, @Path(KeyConstants.KEY_ID) String id);

    @GET(ServiceConstants.VALIDATE_EMAIL)
    Call<BaseEntity> validateEmail(@Header(KeyConstants.KEY) String apiKey, @Header(KeyConstants.EMAIL) String email);

    @GET(ServiceConstants.FORGOT_PASSWORD)
    Call<BaseEntity> forgotPassword(@Header(KeyConstants.KEY) String apiKey, @Header(KeyConstants.EMAIL) String email);

    @GET(ServiceConstants.VERIFY_CODE)
    Call<BaseEntity> verifyCode(@Header(KeyConstants.KEY) String apiKey, @Header(KeyConstants.EMAIL) String email, @Header(KeyConstants.FORGOT_PASSWORD_CODE) String code);

    @FormUrlEncoded
    @POST(ServiceConstants.CHANGE_PASSWORD)
    Call<BaseEntity> changePassword(@Header(KeyConstants.KEY) String apiKey, @Field("email") String email, @Field("password") String password);

    @GET(ServiceConstants.USER_DETAIL)
    Call<UserProfile> getProfileData(@Header(KeyConstants.KEY) String apiKey, @Header(KeyConstants.AUTHORIZATION) String auth);

}
