package ch.epfl.sweng.zuluzulu.Database;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.Firebase.Proxy;
import ch.epfl.sweng.zuluzulu.MainActivity;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

public class DatabaseFactoryTest {
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void getDependency() {
 android.util.Log.d("Function called", "getDependency");
        assertThat(DatabaseFactory.getDependency(), instanceOf(Proxy.class));

    }

    @Test
    public void setDependency() {
 android.util.Log.d("Function called", "setDependency");
        DatabaseFactory.setDependency(new MockedProxy());
        assertThat(DatabaseFactory.getDependency(), instanceOf(Proxy.class));
    }
}