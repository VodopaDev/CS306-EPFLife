package ch.epfl.sweng.zuluzulu.Structure;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that contains general usefull functions
 */
public final class Utils {

    private Utils() {
    }

    /**
     * Check if a snapshot is valid given a list of fields
     *
     * @return whether the snapshot is valid or not
     */
    public static boolean isValidSnapshot(DocumentSnapshot snap, List<String> fields) {
        if (snap == null) {
            return false;
        }
        for (String field : fields) {
            if (snap.get(field) == null) {
                return false;
            }
        }
        return true;
    }

    public static List<Integer> longListToIntList(List<Long> src) {
        List<Integer> res = new ArrayList<>();
        assert (src != null);
        for (Long item : src) {
            res.add(item.intValue());
        }
        return res;
    }

    public static void addIdToList(String path, String field, Integer id) {
        FirebaseFirestore.getInstance().document(path).update(field, FieldValue.arrayUnion(id));
    }

    public static void removeIdFromList(String path, String field, Integer id) {
        FirebaseFirestore.getInstance().document(path).update(field, FieldValue.arrayRemove(id));
    }

    public static double distanceBetween(GeoPoint p1, GeoPoint p2) {
        double x = p1.getLatitude() - p2.getLatitude();
        double y = p1.getLongitude() - p2.getLongitude();
        return Math.sqrt(x*x + y*y);
    }
}
