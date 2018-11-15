package ch.epfl.sweng.zuluzulu.Firebase.Database;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import java.util.Map;

public class CollectionAdapter implements DatabaseCollection {

    private final CollectionReference collection;

    public CollectionAdapter(CollectionReference collection) {
        this.collection = collection;
    }

    @Override
    public DatabaseDocument document(String documentPath) {
        return new DocumentAdapter(collection.document(documentPath));
    }

    @Override
    public Task<DocumentReference> add(@NonNull Map<String, Object> data) {
        return collection.add(data);
    }
}
