package ch.epfl.sweng.zuluzulu.firebase.Database;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public interface DatabaseCollection {
    DatabaseDocument document(String documentPath);

    DatabaseDocument document();

    Task<DocumentReference> add(@NonNull Map<String, Object> data);

    Task<QuerySnapshot> getAndAddOnSuccessListener(OperationWithFirebaseMapList listener);

    String getId();

    void addSnapshotListener(OperationWithFirebaseMapList listener);

    DatabaseQuery whereGreaterThan(String field, Object value);

    DatabaseQuery whereEqualTo(String field, Object value);

    DatabaseQuery orderBy(String field);
}
