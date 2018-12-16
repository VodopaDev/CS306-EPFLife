package ch.epfl.sweng.zuluzulu.Utility;

import android.location.Location;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.google.firebase.firestore.GeoPoint;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import ch.epfl.sweng.zuluzulu.R;

/**
 * Interface that contains general useful functions
 */
public interface Utils {

    /**
     * Create a GeoPoint from a Location
     *
     * @param location The location to convert
     * @return The GeoPoint
     */
    static GeoPoint toGeoPoint(Location location) {
        if (location == null) {
            throw new NullPointerException();
        }
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        return new GeoPoint(latitude, longitude);
    }

    /**
     * Return the distance between two GeoPoints in meters
     *
     * @param p1 The first GeoPoint
     * @param p2 The second GeoPoint
     * @return The distance between the two GeoPoints in meters
     */
    static double distanceBetween(GeoPoint p1, GeoPoint p2) {
        double lat1 = p1.getLatitude();
        double lat2 = p2.getLatitude();
        double lon1 = p1.getLongitude();
        double lon2 = p2.getLongitude();

        double R = 6371e3; // meters
        double phi1 = Math.toRadians(lat1);
        double phi2 = Math.toRadians(lat2);
        double deltaphi = Math.toRadians(lat2 - lat1);
        double deltalambda = Math.toRadians(lon2 - lon1);

        double a = Math.sin(deltaphi / 2) * Math.sin(deltaphi / 2) + Math.cos(phi1) * Math.cos(phi2) * Math.sin(deltalambda / 2) * Math.sin(deltalambda / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    /**
     * Return a random integer in the range [min, max]
     *
     * @param min the smallest integer you can getAndAddOnSuccessListener
     * @param max the biggest integer you can getAndAddOnSuccessListener
     * @return the random integer
     */
    static int randomInt(int min, int max) {
        if (max < min) {
            throw new IllegalArgumentException("Max must be bigger than min");
        }
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    /**
     * Return the time passed in milliseconds since the given date
     *
     * @param date the date
     * @return the time passed since the given date
     */
    static long getMillisecondsSince(Date date) {
        if (date == null) {
            throw new NullPointerException();
        }
        long dateTime = date.getTime();
        long now = Calendar.getInstance().getTimeInMillis();
        return now - dateTime;
    }

    /**
     * Draw a Snackbar
     *
     * @param container The container view
     */
    static void showConnectSnackbar(View container) {
        Snackbar.make(container, container.getContext().getString(R.string.login_for_more_features), 5000).show();
    }

    /**
     * Convert hour and minute from integer to beautiful string
     *
     * @param hour   The hour
     * @param minute The minutes
     * @return The string of the hour and the minutes
     */
    static String hourAndMinutesFrom(int hour, int minute) {
        if (!validHour(hour) || !validMinute(minute)) {
            throw new IllegalArgumentException();
        }
        String strHour = String.valueOf(hour);
        String strMinute = String.valueOf(minute);
        if (strHour.length() < 2) {
            strHour = "0" + strHour;
        }
        if (strMinute.length() < 2) {
            strMinute = "0" + strMinute;
        }
        return strHour + ":" + strMinute;
    }

    /**
     * Check whether a hour is valid
     *
     * @param hour The hour to check
     * @return whether the hour is valid
     */
    static boolean validHour(int hour) {
        return 0 <= hour && hour <= 23;
    }

    /**
     * Check whether a minute is valid
     *
     * @param minute The minute to check
     * @return whether the minute is valid
     */
    static boolean validMinute(int minute) {
        return 0 <= minute && minute <= 59;
    }

}
