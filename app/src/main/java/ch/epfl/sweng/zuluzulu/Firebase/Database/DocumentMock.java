package ch.epfl.sweng.zuluzulu.Firebase.Database;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.Task;

import java.util.Map;

public class DocumentMock implements DatabaseDocument{
    private static final String TAG = "DocumentMock";

    @Override
    public Task<Void> set(@NonNull Map<String, Object> data) {

        // Just print instead of pushing to firebase
        for(String key : data.keySet()) {
            Log.d(TAG, key);
        }

        return null;
    }
}
