package ch.epfl.sweng.zuluzulu.Structure;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class Utils {

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
