package ch.epfl.sweng.zuluzulu.Database;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.zuluzulu.Firebase.Database.FirebaseAdapter;
import ch.epfl.sweng.zuluzulu.MainActivity;

import static org.junit.Assert.*;

public class FirebaseAdapterTest {
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void collection() {
 android.util.Log.d("Function called", "collection");
        assertNotNull(new FirebaseAdapter().collection("path"));
    }
}