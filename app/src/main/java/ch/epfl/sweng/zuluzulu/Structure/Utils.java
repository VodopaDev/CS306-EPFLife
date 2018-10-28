package ch.epfl.sweng.zuluzulu.Structure;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

/**
 * Class that contains general useful functions
 */
public abstract class Utils {

    public static void addIdToList(String path, String field, Integer id) {
        FirebaseFirestore.getInstance().document(path).update(field, FieldValue.arrayUnion(id));
    }

    public static void removeIdFromList(String path, String field, Integer id) {
        FirebaseFirestore.getInstance().document(path).update(field, FieldValue.arrayRemove(id));
    }

    /**
     * Return the distance between two GeoPoints
     * @param p1 The first GeoPoint
     * @param p2 The second GeoPoint
     * @return The distance between the two GeoPoints
     */
    public static double distanceBetween(GeoPoint p1, GeoPoint p2) {
        double x = p1.getLatitude() - p2.getLatitude();
        double y = p1.getLongitude() - p2.getLongitude();
        return Math.sqrt(x*x + y*y);
    }
}
