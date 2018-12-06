package ch.epfl.sweng.zuluzulu.Database;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

import ch.epfl.sweng.zuluzulu.Firebase.Database.DatabaseCollection;
import ch.epfl.sweng.zuluzulu.Firebase.Database.DatabaseDocument;
import ch.epfl.sweng.zuluzulu.Firebase.Database.DatabaseQuery;

public class CollectionMock implements DatabaseCollection {
    @Override
    public DatabaseDocument document(String documentPath) {
        return new DocumentMock();
    }

    @Override
    public DatabaseDocument document() {
        return new DocumentMock();
    }

    @Override
    public Task<DocumentReference> add(@NonNull Map<String, Object> data) {
        return new TaskMock<DocumentReference>();
    }

    @Override
    public Task<QuerySnapshot> get() {
        return new TaskMock<>();
    }

    @Override
    public String getId() {
        return "1";
    }

    @Override
    public void addSnapshotListener(@NonNull EventListener<QuerySnapshot> listener) {
    }

    @Override
    public DatabaseQuery whereGreaterThan(String field, Object value) {
        return new QueryMock();
    }
}
