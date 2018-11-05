package ch.epfl.sweng.zuluzulu.Structure;

import android.location.Location;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.firebase.firestore.GeoPoint;

/**
 * Class that contains general useful functions
 */
public final class Utils {

    private Utils() {}

    public static void addIdToList(String path, String field, Integer id) {
        FirebaseFirestore.getInstance().document(path).update(field, FieldValue.arrayUnion(id));
    }

    public static void removeIdFromList(String path, String field, Integer id) {
        FirebaseFirestore.getInstance().document(path).update(field, FieldValue.arrayRemove(id));
    }

    /**
     * Create a GeoPoint from a Location
     *
     * @param location The location to convert
     * @return The GeoPoint
     */
    public static GeoPoint toGeoPoint(Location location) {
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
    public static double distanceBetween(GeoPoint p1, GeoPoint p2) {
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

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
    public static SimpleDateFormat stringToDateFormat = new SimpleDateFormat("dd/MM/yyyy");
}
