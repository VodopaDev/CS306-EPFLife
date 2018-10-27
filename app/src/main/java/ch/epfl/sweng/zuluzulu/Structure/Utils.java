package ch.epfl.sweng.zuluzulu.Structure;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

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
}
