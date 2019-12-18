package com.pehlaj.chairlift.constants;

/**
 * @author Pehlaj
 * @since 6/5/2017.
 */
public final class ServiceConstants {

	private ServiceConstants() {}

	public static final String LOGIN                   = "login";

	public static final String SIGN_UP                 = "register";

	public static final String RIDER_INFO				= "rider/info";
    public static final String BUS                      = "bus";
    public static final String RIDER                    = "rider";
	public static final String BOOKING                  = "booking";
    public static final String FUTURE_BOOKING           = "booking/Idle";
    public static final String COMPLETED_BOOKING        = "booking/Complete";
    public static final String CURRENT_BOOKING          = "booking/InProgress";
	public static final String BUS_DETAILS              = "bus/{id}";
    public static final String RIDER_DETAILS            = "rider/{id}";
    public static final String BOOKING_DETAILS         = "booking/{id}";

	public static final String VALIDATE_EMAIL          = "user/email/validate";

	public static final String PRODUCT_COMMISSIONS     = "chairlift/commission/info";

	public static final String USER_DETAIL             = "user/detail";

	public static final String UPDATE_USER             = "user/update";

	public static final String USER_COMMISSION         = "user/commission";

	public static final String USER_COMMISSION_DETAILS = "user/commission/detail";

	public static final String VALIDATE_IMEI           = "imei/validate";

	public static final String REGISTER_IMEI           = "imei/register";

	public static final String FORGOT_PASSWORD         = "user/forgot/password";

	public static final String VERIFY_CODE             = "user/forgot/password/code";

	public static final String CHANGE_PASSWORD         = "user/forgot/password/update";

	public static final String SOCIAL_LOGIN            = "user/social/login";

	public static final String VALIDATE_SOCIAL_ACCOUNT = "user/social/validate";
}
