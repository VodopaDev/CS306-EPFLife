package ch.epfl.sweng.zuluzulu.Database;

import android.support.test.rule.ActivityTestRule;

import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.zuluzulu.Firebase.Database.CollectionAdapter;
import ch.epfl.sweng.zuluzulu.MainActivity;

import static org.junit.Assert.*;

public class CollectionAdapterTest {
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void document() {
        CollectionAdapter db = new CollectionAdapter(FirebaseFirestore.getInstance().collection("co"));
        assertNotNull(db.document("test"));
    }
}