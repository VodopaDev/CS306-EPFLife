package ch.epfl.sweng.zuluzulu.Firebase.Database;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.QuerySnapshot;

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
    public DatabaseDocument document() {
        return new DocumentAdapter(collection.document());
    }

    @Override
    public Task<DocumentReference> add(@NonNull Map<String, Object> data) {
        return collection.add(data);
    }

    @Override
    public Task<QuerySnapshot> get() {
        return collection.get();
    }

    @Override
    public String getId() {
        return collection.getId();
    }

    @Override
    public void addSnapshotListener(@NonNull EventListener<QuerySnapshot> listener) {
        collection.addSnapshotListener(listener);
    }

    @Override
    public DatabaseQuery whereGreaterThan(String field, Object value) {
        return new QueryAdapter(collection.whereGreaterThan(field, value));
    }
}
