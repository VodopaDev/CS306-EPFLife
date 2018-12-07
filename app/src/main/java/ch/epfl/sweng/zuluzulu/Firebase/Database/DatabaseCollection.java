package ch.epfl.sweng.zuluzulu.Firebase.Database;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.Map;

public interface DatabaseCollection {
    DatabaseDocument document(String documentPath);

    DatabaseDocument document();

    Task<DocumentReference> add(@NonNull Map<String, Object> data);

    Task<QuerySnapshot> get();

    String getId();

    void addSnapshotListener(@NonNull EventListener<QuerySnapshot> listener);

    DatabaseQuery whereGreaterThan(String field, Object value);

    DatabaseQuery whereEqualTo(String field, Object value);
}
