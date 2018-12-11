package ch.epfl.sweng.zuluzulu.Database;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.Firebase.Database.DatabaseCollection;
import ch.epfl.sweng.zuluzulu.Firebase.Database.DatabaseDocument;
import ch.epfl.sweng.zuluzulu.Firebase.Database.DatabaseQuery;
import ch.epfl.sweng.zuluzulu.Firebase.Database.OperationWithFirebaseMapList;
import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.IdlingResource.IdlingResourceFactory;

public class CollectionMock implements DatabaseCollection {
    private Map<String, Object> map;

    CollectionMock(){
        map = new HashMap<>();
        map.put("id", "");
        map.put("name", "");
        map.put("short_description", "");
        map.put("long_description", "");
        map.put("category", "");
        map.put("icon_uri", "");
        map.put("banner_uri", "");
        map.put("followers", "");
        map.put("channel_id", "");
        map.put("association_id", "");
        map.put("start_date", "");
        map.put("end_date", "");
        map.put("place", "");
        map.put("organizer", "");
        map.put("url_place_and_room", "");
        map.put("website", "");
        map.put("contact", "");
        map.put("speaker", "");
        map.put("followed_associations", new ArrayList<String>());
        map.put("followed_events", new ArrayList<String>());
        map.put("followed_channels", new ArrayList<String>());
        map.put("roles", Arrays.asList("USER"));
        map.put("first_name", "");
        map.put("last_name", "");
        map.put("section", "");
        map.put("semester", "");
        map.put("gaspar", "");
        map.put("email", "");
        map.put("sciper", "");
        map.put("sender_name", "");
        map.put("sender_sciper", "");
        map.put("message", "");
        map.put("time", new Date());
        map.put("restrictions", "");
        map.put("color", "");
        map.put("nb_ups", 1L);
        map.put("nb_responses", 1L);
        map.put("up_scipers", new ArrayList<String>());
        map.put("down_scipers", new ArrayList<String>());
    }

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
    public Task<QuerySnapshot> getAndAddOnSuccessListener(OperationWithFirebaseMapList listener) {
        FirebaseMapDecorator fmap = new FirebaseMapDecorator(map);
        List<FirebaseMapDecorator> list = new ArrayList<>();
        list.add(fmap);
        listener.applyList(list);
        IdlingResourceFactory.incrementCountingIdlingResource();
        return new TaskMock<>();
    }

    @Override
    public String getId() {
        return "1";
    }

    @Override
    public void addSnapshotListener(OperationWithFirebaseMapList listener) {
        FirebaseMapDecorator fmap = new FirebaseMapDecorator(map);
        List<FirebaseMapDecorator> list = new ArrayList<>();
        list.add(fmap);
        listener.applyList(list);
    }

    @Override
    public DatabaseQuery whereGreaterThan(String field, Object value) {
        return new QueryMock();
    }

    @Override
    public DatabaseQuery whereEqualTo(String field, Object value) {
        return new QueryMock();
    }

    @Override
    public DatabaseQuery orderBy(String field) {
        return new QueryMock();
    }

}
