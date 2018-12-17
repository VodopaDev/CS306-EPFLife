package ch.epfl.sweng.zuluzulu.firebase.Database;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.idlingResource.IdlingResourceFactory;

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
    public Task<QuerySnapshot> getAndAddOnSuccessListener(OperationWithFirebaseMapList listener) {
        Task<QuerySnapshot> task = collection.get();

        task.addOnSuccessListener(queryDocumentSnapshots -> {
                    List<FirebaseMapDecorator> list = new ArrayList<>();
                    for (DocumentSnapshot snap : queryDocumentSnapshots.getDocuments()) {
                        list.add(new FirebaseMapDecorator(snap));
                    }
                    listener.applyList(list);
                }
        );

        return task;
    }

    @Override
    public String getId() {
        return collection.getId();
    }

    @Override
    public void addSnapshotListener(OperationWithFirebaseMapList listener) {
        collection.addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null || queryDocumentSnapshots == null)
                        System.err.println("Listen failed: " + e);
                    else {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            IdlingResourceFactory.incrementCountingIdlingResource();

                            List<FirebaseMapDecorator> list = new ArrayList<>();
                            for (DocumentSnapshot snap : queryDocumentSnapshots.getDocuments()) {
                                list.add(new FirebaseMapDecorator(snap));
                            }
                            listener.applyList(list);

                            IdlingResourceFactory.decrementCountingIdlingResource();
                        }
                    }
                }

        );
    }

    @Override
    public DatabaseQuery whereGreaterThan(String field, Object value) {
        return new QueryAdapter(collection.whereGreaterThan(field, value));
    }

    @Override
    public DatabaseQuery whereEqualTo(String field, Object value) {
        return new QueryAdapter(collection.whereEqualTo(field, value));
    }

    @Override
    public DatabaseQuery orderBy(String field) {
        return new QueryAdapter(collection.orderBy(field));
    }
}
