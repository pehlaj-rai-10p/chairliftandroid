package com.tejani.pehlaj.chairlift.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created on 4/8/2016.
 */
public final class DateAndTimeUtility {

    public static final int MIN_DAYS_IN_MONTH = 28;
    public static final int MAX_DAYS_IN_MONTH = 31;
    public static final int MONTHS_IN_YEAR = 12;
    public static final int SECONDS_IN_HOUR = 3600;
    public static final int SECONDS_IN_MINUTE = 60;

    public static final String UTC_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String UTC_DATE_FORMAT_WITH_MILLIS = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static final String MM_DD_FORMAT = "MM/dd";
    public static final String TWO_DIGITS_DATE_FORMAT = "MM/dd/yy";

    public static final String TWO_DIGITS_DATE_FORMAT_2 = "MM/dd/yyyy";
    public static final String DD_DATE_FORMAT = "dd";

    public static final String WIFI_TEST_DATE_FORMAT = "MMM dd";
    public static final String WIFI_TEST_TIME_FORMAT = "hh:mm a";

    public static final String DATE_FORMAT = "yyyyMMdd";
    public static final String DATE_FORMAT_MMM_dd_yyyy = "MMM dd, yyyy";

    public static final String INPUT_DATE_FORMAT = "MM/dd/yyyy";
    public static final String OUTPUT_DATE_FORMAT = "MMMM d, yyyy";

    public static final String INVOICE_INPUT_DATE_FORMAT = "dd-MMM-yy";
    public static final String INVOICE_OUTPUT_DATE_FORMAT = "MMM dd, yyyy";

    public static final String TICKET_INPUT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String CHAIRLIFT_DATE_FORMAT = "MM/dd/yyyy hh:mm a";

    public static final String BILLING_INPUT_DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";
    public static final String BILLING_OUTPUT_DATE_FORMAT = "MMM dd, yyyy";

    public static final String UTC = "UTC";
    public static final String SNOOZE_TIMER_FORMAT = "%02d:%02d:%02d";
    public static final String STRING_FORMAT_DDD = "%d/%d/%d";
    public static final String STRING_FORMAT_SSS = "%s%s%s";
    public static final String STRING_FORMAT_TIME_FOR_SECONDS = "%02d:%02d";

    public static final String DATE_TIME_FORMAT_MX = "yyyy-MM-dd HH:mm a";
    public static final String DATE_TIME_INPUT_FORMAT_MX = "yyyy-mm-ddThh:mm:ss.fff+|-hhmm";


    private DateAndTimeUtility() {

    }

    public static Date parseDate(String dateString, String inputDateFormat, boolean isUTC) {

        if (TextUtils.isEmpty(dateString) || TextUtils.isEmpty(inputDateFormat)) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(inputDateFormat, Locale.ENGLISH);

        if (isUTC) {
            sdf.setTimeZone(TimeZone.getTimeZone(UTC));
        }

        try {
            return sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Date parseDate(String dateString, String inputDateFormat) {

        return parseDate(dateString, inputDateFormat, false);

    }

    public static String formatDateWithEnglishLocale(Date date, String inputDateFormat) {

        return new SimpleDateFormat(inputDateFormat, Locale.ENGLISH).format(date);
    }

    public static String formatDate(Date date, String outputDateFormat, boolean isUTC) {

        if ((date == null) || TextUtils.isEmpty(outputDateFormat)) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(outputDateFormat, Locale.ENGLISH);

        if (isUTC) {
            sdf.setTimeZone(TimeZone.getTimeZone(UTC));
        }

        String formattedDate = sdf.format(date);

        if (!TextUtils.isEmpty(formattedDate)) {
            formattedDate = String.format("%s%s", formattedDate.substring(0, 1).toUpperCase(), formattedDate.substring(1));
        }

        return formattedDate;
    }

    public static String formatDate(Date date, String outputDateFormat) {

        return formatDate(date, outputDateFormat, false);
    }

    public static String formatDate(String dateString, String inputDateFormat, String outputDateFormat, boolean isUTC) {

        if (TextUtils.isEmpty(outputDateFormat) || TextUtils.isEmpty(dateString)) {
            return "";
        }

        Date date = parseDate(dateString, inputDateFormat, isUTC);

        return formatDate(date, outputDateFormat, isUTC);
    }

    public static String formatDate(String dateString, String inputDateFormat, String outputDateFormat) {

        return formatDate(dateString, inputDateFormat, outputDateFormat, false);
    }

    public static String getTimeFromSeconds(int totalSeconds) {

        int minutes = (totalSeconds / SECONDS_IN_MINUTE) % SECONDS_IN_MINUTE;
        int hours = totalSeconds / SECONDS_IN_HOUR;

        return String.format(Locale.ENGLISH, STRING_FORMAT_TIME_FOR_SECONDS, hours, minutes);

    }

    public static boolean checkIfDateIn13Months(String invoiceDate) {

        if (TextUtils.isEmpty(invoiceDate)) {
            return false;
        }

        SimpleDateFormat inputFormat = new SimpleDateFormat(INVOICE_INPUT_DATE_FORMAT, Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -1 * MONTHS_IN_YEAR);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        try {
            Date date = inputFormat.parse(invoiceDate);
            if (date.after(calendar.getTime()) || date.equals(calendar.getTime())) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static Date getLocalDateFromUTCString(String time) {

        Date date = null;
        SimpleDateFormat inputFormat = new SimpleDateFormat(UTC_DATE_FORMAT_WITH_MILLIS, Locale.ENGLISH);
        inputFormat.setTimeZone(TimeZone.getTimeZone(UTC));

        try {

            date = inputFormat.parse(time);
        } catch (Exception e) {

            e.printStackTrace();

            inputFormat = new SimpleDateFormat(UTC_DATE_FORMAT, Locale.ENGLISH);
            inputFormat.setTimeZone(TimeZone.getTimeZone(UTC));

            try {

                date = inputFormat.parse(time);
            } catch (ParseException e1) {

                e1.printStackTrace();
            }
        }

        return date;
    }

    public static boolean isCurrentDateToday(String dateStr, String inputDateFormat) {

        if (TextUtils.isEmpty(dateStr) || TextUtils.isEmpty(inputDateFormat)) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(inputDateFormat, Locale.ENGLISH);
        String date = dateStr;

        if (dateStr.length() != DATE_FORMAT.length()) {
            try {
                date = sdf.format(new SimpleDateFormat(DATE_FORMAT_MMM_dd_yyyy, Locale.ENGLISH).parse(dateStr));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return date.equals(sdf.format(new Date()));
    }

    public static String getFormattedDate(Date date, String format, Locale locale) {

        SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
        return sdf.format(date);
    }

    public static boolean isBefore(Date date) {

        if (date == null) {
            date = new Date();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.before(Calendar.getInstance());
    }

    public static Calendar getDateWithoutTime(Calendar date) {

        if (date == null) {
            date = Calendar.getInstance();
        }

        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);

        return date;
    }

    public static String getPreviousMonthEndDate(String startDate) {

        Date tempEndDate = parseDate(startDate, INPUT_DATE_FORMAT);

        if (TextUtils.isEmpty(startDate) || (tempEndDate == null)) {
            return "";
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(tempEndDate);
        cal.add(Calendar.DATE, -1);

        tempEndDate = cal.getTime();
        return formatDate(tempEndDate, INPUT_DATE_FORMAT);
    }

    public static long getTimeInMillis() {

        return Calendar.getInstance().getTimeInMillis();
    }

    public static String getFormattedTime(long durationSeconds) {

        return String.format(Locale.ENGLISH, SNOOZE_TIMER_FORMAT, durationSeconds / SECONDS_IN_HOUR, (durationSeconds % SECONDS_IN_HOUR) / SECONDS_IN_MINUTE, durationSeconds % SECONDS_IN_MINUTE);
    }

    public static String formatThisMonthEndMonth(Date date, String outputFormat) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return formatDate(calendar.getTime(), outputFormat);
    }

    public static boolean is24HoursLater(long dateTimeWithMillis) {

        Date date = new Date(dateTimeWithMillis);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 1);

        return Calendar.getInstance().getTime().after(calendar.getTime());

    }

    public static void getDateTimeFormatterISO(String inputDate) {

    }
}
