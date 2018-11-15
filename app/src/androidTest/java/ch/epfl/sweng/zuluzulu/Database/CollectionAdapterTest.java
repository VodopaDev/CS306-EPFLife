package ch.epfl.sweng.zuluzulu.Database;

import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Test;

import ch.epfl.sweng.zuluzulu.Firebase.Database.CollectionAdapter;

import static org.junit.Assert.*;

public class CollectionAdapterTest {

    @Test
    public void document() {
        CollectionAdapter db = new CollectionAdapter(FirebaseFirestore.getInstance().collection("co"));
        assertNotNull(db.document("test"));
    }
}