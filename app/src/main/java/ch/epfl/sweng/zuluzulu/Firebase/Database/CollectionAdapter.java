package ch.epfl.sweng.zuluzulu.Firebase.Database;

import com.google.firebase.firestore.CollectionReference;

public class CollectionAdapter implements DatabaseCollection {

    private final CollectionReference collection;

    public CollectionAdapter(CollectionReference collection){
        this.collection = collection;
    }

    @Override
    public DatabaseDocument document(String documentPath) {
        return new DocumentAdapter(collection.document(documentPath));
    }
}
