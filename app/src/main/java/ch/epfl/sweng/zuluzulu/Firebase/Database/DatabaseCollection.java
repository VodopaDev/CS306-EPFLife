package ch.epfl.sweng.zuluzulu.Firebase.Database;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

import java.util.Map;

public interface DatabaseCollection {
    public DatabaseDocument document(String documentPath);

    public Task<DocumentReference> add(@NonNull Map<String, Object> data);
}
