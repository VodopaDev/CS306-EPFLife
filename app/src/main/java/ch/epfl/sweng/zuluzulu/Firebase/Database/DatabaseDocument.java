package ch.epfl.sweng.zuluzulu.Firebase.Database;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Task;

import java.util.Map;

public interface DatabaseDocument {
    public Task<Void> set(@NonNull Map<String, Object> data);
}
