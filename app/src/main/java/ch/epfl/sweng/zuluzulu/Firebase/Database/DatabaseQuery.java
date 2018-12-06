package ch.epfl.sweng.zuluzulu.Firebase.Database;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

public interface DatabaseQuery {
    DatabaseQuery orderBy(String field);

    DatabaseQuery limit(int limit);

    Task<QuerySnapshot> get();
}
