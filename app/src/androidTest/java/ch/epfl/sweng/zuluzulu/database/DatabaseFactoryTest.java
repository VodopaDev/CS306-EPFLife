package ch.epfl.sweng.zuluzulu.database;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.zuluzulu.firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.firebase.Proxy;
import ch.epfl.sweng.zuluzulu.MainActivity;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

public class DatabaseFactoryTest {
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void getDependency() {
        assertThat(DatabaseFactory.getDependency(), instanceOf(Proxy.class));

    }

    @Test
    public void setDependency() {
        DatabaseFactory.setDependency(new MockedProxy());
        assertThat(DatabaseFactory.getDependency(), instanceOf(Proxy.class));
    }
}