package ch.epfl.sweng.zuluzulu.firebase;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.zuluzulu.database.FirebaseMock;
import ch.epfl.sweng.zuluzulu.firebase.Database.Database;
import ch.epfl.sweng.zuluzulu.firebase.Database.FirebaseFactory;
import ch.epfl.sweng.zuluzulu.MainActivity;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

public class FirebaseFactoryTest {
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void getDependency() {
        assertThat(FirebaseFactory.getDependency(), instanceOf(Database.class));
    }

    @Test
    public void setDependency() {
        FirebaseFactory.setDependency(new FirebaseMock());
        assertThat(FirebaseFactory.getDependency(), instanceOf(Database.class));
    }
}