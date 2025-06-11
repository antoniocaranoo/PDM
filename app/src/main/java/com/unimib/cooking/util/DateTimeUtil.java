package com.unimib.cooking.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeUtil {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    private DateTimeUtil() {}

    public static String getDate(String dateTime) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_PATTERN, Locale.getDefault());
        SimpleDateFormat outputDateFormat = null;

        Locale italianLocale = new Locale("it","IT","");

        if (Locale.getDefault().equals(italianLocale)) {
            outputDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
        } else {
            outputDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US);
        }

        Date parsedDate = null;

        try {
            parsedDate = simpleDateFormat.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (parsedDate != null) {
            return outputDateFormat.format(parsedDate);
        }

        return null;
    }

    public static String getDateDelta(String dateTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_PATTERN, Locale.getDefault());
        SimpleDateFormat outputDateFormat = null;

        Locale italianLocale = new Locale("it","IT","");

        if (Locale.getDefault().equals(italianLocale)) {
            outputDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
        } else {
            outputDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US);
        }

        outputDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Rome"));

        Date parsedDate = null;

        try {
            parsedDate = simpleDateFormat.parse(dateTime);
            Long delta = System.currentTimeMillis() - parsedDate.getTime();

            if (delta < 1000 * 60) {
                return "1m";
            } else if (delta < 5 * 1000 * 60) {
                return "5m";
            } else if (delta < 10 * 1000 * 60) {
                return "10m";
            } else if (delta < 60 * 1000 * 60) {
                return "1h";
            } else if (delta < 2 * 60 * 1000 * 60) {
                return "2h";
            } else if (delta < 2 * 60 * 1000 * 60) {
                return "Today";
            } else {
                Calendar c = Calendar.getInstance();
                c.setTime(parsedDate);

                return (c.get(Calendar.DAY_OF_MONTH)) + "/" +
                        (1 + c.get(Calendar.MONTH)) + "/" +
                        (c.get(Calendar.YEAR));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static long getDateMillis(String dateTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_PATTERN, Locale.getDefault());
        SimpleDateFormat outputDateFormat = null;

        Locale italianLocale = new Locale("it", "IT", "");

        if (Locale.getDefault().equals(italianLocale)) {
            outputDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
        } else {
            outputDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US);
        }

        outputDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Rome"));

        Date parsedDate = null;

        try {
            parsedDate = simpleDateFormat.parse(dateTime);
            return parsedDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }
}