package ch.epfl.sweng.zuluzulu.Database;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Map;
import java.util.concurrent.Executor;

import ch.epfl.sweng.zuluzulu.Firebase.Database.DatabaseDocument;

public class DocumentMock implements DatabaseDocument {
    private static final String TAG = "DocumentMock";

    @Override
    public Task<Void> set(@NonNull Map<String, Object> data) {

        // Just print instead of pushing to firebase
        for(String key : data.keySet()) {
            Log.d(TAG, key);
        }

        return new TaskMock<Void>();
    }

    @Override
    public Task<Void> update(String field, Object value, Object... moreFieldAndValues) {
        Log.d(TAG, field + " -> " + value);
        return new TaskMock<>();
    }
}
