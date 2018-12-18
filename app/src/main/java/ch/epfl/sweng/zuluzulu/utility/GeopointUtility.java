package ch.epfl.sweng.zuluzulu.utility;

import android.location.Location;

import com.google.firebase.firestore.GeoPoint;

public interface GeopointUtility {

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
}
