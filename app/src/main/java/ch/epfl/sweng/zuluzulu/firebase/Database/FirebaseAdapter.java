package ch.epfl.sweng.zuluzulu.firebase.Database;

import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseAdapter implements Database {
    private FirebaseFirestore firebase;

    public FirebaseAdapter() {
        firebase = FirebaseFirestore.getInstance();
    }

    @Override
    public DatabaseCollection collection(String collectionPath) {
        return new CollectionAdapter(firebase.collection(collectionPath));
    }
}
