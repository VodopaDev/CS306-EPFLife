package ch.epfl.sweng.zuluzulu.Firebase.Database;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

import java.util.Map;

public class DocumentAdapter implements DatabaseDocument{
    private final DocumentReference document;

    public DocumentAdapter(DocumentReference document){
        this.document = document;
    }

    @Override
    public Task<Void> set(@NonNull Map<String, Object> data) {
        return document.set(data);
    }
}
