package ch.epfl.sweng.zuluzulu.Database;

import android.support.test.rule.ActivityTestRule;

import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;

import ch.epfl.sweng.zuluzulu.Firebase.Database.CollectionAdapter;
import ch.epfl.sweng.zuluzulu.Firebase.Database.FirebaseFactory;
import ch.epfl.sweng.zuluzulu.MainActivity;

import static org.junit.Assert.*;

public class CollectionAdapterTest {
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);
    private CollectionAdapter db;

    @Before
    public void setUp(){
        FirebaseFactory.setDependency(new FirebaseMock());
        db = new CollectionAdapter(FirebaseFirestore.getInstance().collection("co"));
    }

    @Test
    public void document() {
        assertNotNull(db.document("test"));
        assertNotNull(db.document());
    }

    @Test
    public void add() {
        assertNotNull(db.add(new HashMap<>()));
    }

    @Test
    public void get() {
        assertNotNull(db.getAndAddOnSuccessListener(x -> {}));
    }

    @Test
    public void getId() {
        assertNotNull(db.getId());
    }

    @Test
    public void orderBy() {
        assertNotNull(db.orderBy("field"));
    }

    @Test
    public void whereEqualTo() {
        assertNotNull(db.whereEqualTo("vield", "vvalue"));
    }

    @Test
    public void whereGreaterThan() {
        assertNotNull(db.whereGreaterThan("field", "val"));
    }

    @Test
    public void listenet() {
        db.addSnapshotListener((x) -> {});
    }
}