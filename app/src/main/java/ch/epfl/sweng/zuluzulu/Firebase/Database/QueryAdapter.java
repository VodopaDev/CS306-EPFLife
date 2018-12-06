package ch.epfl.sweng.zuluzulu.Firebase.Database;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class QueryAdapter implements DatabaseQuery {
    private final Query query;

    QueryAdapter(Query query) {
        this.query = query;
    }


    @Override
    public DatabaseQuery orderBy(String field) {
        return new QueryAdapter(query.orderBy(field));
    }

    @Override
    public DatabaseQuery limit(int limit) {
        return new QueryAdapter(query.limit(limit));
    }

    @Override
    public Task<QuerySnapshot> get() {
        return query.get();
    }
}
