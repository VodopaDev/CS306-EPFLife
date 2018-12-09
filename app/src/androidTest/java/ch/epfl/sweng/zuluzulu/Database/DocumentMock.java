package ch.epfl.sweng.zuluzulu.Database;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.Firebase.Database.DatabaseCollection;
import ch.epfl.sweng.zuluzulu.Firebase.Database.DatabaseDocument;
import ch.epfl.sweng.zuluzulu.Firebase.Database.OperationWithFirebaseMap;
import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.IdlingResource.IdlingResourceFactory;

public class DocumentMock implements DatabaseDocument {
    private static final String TAG = "DocumentMock";

    @Override
    public Task<Void> set(@NonNull Map<String, Object> data) {

        // Just print instead of pushing to firebase
        for (String key : data.keySet()) {
            Log.d(TAG, key);
        }

        return new TaskMock<Void>();
    }

    @Override
    public Task<Void> update(String field, Object value, Object... moreFieldAndValues) {
        Log.d(TAG, field + " -> " + value);
        return new TaskMock<>();
    }

    @Override
    public Task<Void> update(Map<String, Object> data) {
        return new TaskMock<>();
    }

    @Override
    public Task<DocumentSnapshot> getAndAddOnSuccessListener(OperationWithFirebaseMap listener) {
        HashMap<String, Object> map = new HashMap<>();
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


        FirebaseMapDecorator fmap = new FirebaseMapDecorator(map);
        listener.apply(fmap);
        IdlingResourceFactory.incrementCountingIdlingResource();
        return new TaskMock<>();
    }


    @Override
    public DatabaseCollection collection(String messages) {
        return new CollectionMock();
    }

    @Override
    public String getId() {
        return "1";
    }
}
