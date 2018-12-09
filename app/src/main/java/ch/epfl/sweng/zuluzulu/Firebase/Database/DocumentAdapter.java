package ch.epfl.sweng.zuluzulu.Firebase.Database;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Map;

import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;

public class DocumentAdapter implements DatabaseDocument {
    private final DocumentReference document;

    public DocumentAdapter(DocumentReference document) {
        this.document = document;
    }

    @Override
    public Task<Void> set(@NonNull Map<String, Object> data) {
        return document.set(data);
    }

    @Override
    public Task<Void> update(String field, Object value, Object... moreFieldAndValues) {
        return document.update(field, value, moreFieldAndValues);
    }

    @Override
    public Task<Void> update(Map<String, Object> data) {
        return document.update(data);
    }

    @Override
    public Task<DocumentSnapshot> getAndAddOnSuccessListener(OperationWithFirebaseMap listener) {
        Task<DocumentSnapshot> task = document.get();
        task.addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.exists()) {
                listener.apply(new FirebaseMapDecorator(documentSnapshot));
            } else {
                listener.apply(null);
            }
        });
        return task;
    }

    @Override
    public DatabaseCollection collection(String messages) {
        return new CollectionAdapter(document.collection(messages));
    }

    @Override
    public String getId() {
        return document.getId();
    }
}
