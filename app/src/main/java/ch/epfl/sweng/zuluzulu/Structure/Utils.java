package ch.epfl.sweng.zuluzulu.Structure;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

/**
 * Class that contains general usefull functions
 */
public final class Utils {

    private Utils() {}

    /**
     * Check if a snapshot is valid given a list of fields
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
}
