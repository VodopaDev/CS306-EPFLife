package ch.epfl.sweng.zuluzulu.Database;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

import java.util.Map;

import ch.epfl.sweng.zuluzulu.Firebase.Database.DatabaseCollection;
import ch.epfl.sweng.zuluzulu.Firebase.Database.DatabaseDocument;

public class CollectionMock implements DatabaseCollection {
    @Override
    public DatabaseDocument document(String documentPath) {
        return new DocumentMock();
    }

    @Override
    public Task<DocumentReference> add(@NonNull Map<String, Object> data) {
        return new TaskMock<DocumentReference>();
    }
}
