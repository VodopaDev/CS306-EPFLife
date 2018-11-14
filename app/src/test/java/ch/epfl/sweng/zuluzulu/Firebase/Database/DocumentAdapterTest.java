package ch.epfl.sweng.zuluzulu.Firebase.Database;

import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Test;

import static org.junit.Assert.*;

public class CollectionAdapterTest {

    @Test
    public void set() {
        CollectionAdapter db = new CollectionAdapter(FirebaseFirestore.getInstance().collection("co"));
        assertNotNull(db.document("test"));
    }
}