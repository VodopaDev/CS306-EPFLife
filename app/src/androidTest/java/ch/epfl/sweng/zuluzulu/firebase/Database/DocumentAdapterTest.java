package ch.epfl.sweng.zuluzulu.firebase.Database;

import android.support.test.rule.ActivityTestRule;

import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;

import ch.epfl.sweng.zuluzulu.database.FirebaseMock;
import ch.epfl.sweng.zuluzulu.MainActivity;

import static org.junit.Assert.*;

public class DocumentAdapterTest {
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);
    private DocumentAdapter db;

    @Before
    public void setUp() {
        FirebaseFactory.setDependency(new FirebaseMock());
        db = new DocumentAdapter(FirebaseFirestore.getInstance().collection("co").document("do"));
    }

    @Test
    public void set() {
        assertNotNull(db.set(new HashMap<>()));
    }

    @Test
    public void update() {
        assertNotNull(db.update(new HashMap<>()));
    }

    @Test
    public void update1() {
        assertNotNull(db.update("field", null));
    }

    @Test
    public void get() {
        assertNotNull(db.getAndAddOnSuccessListener(x -> {
        }));
    }

    @Test
    public void collection() {
        assertNotNull(db.collection("collection"));
    }

    @Test
    public void getId() {
        assertNotNull(db.getId());
    }
}