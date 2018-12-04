package ch.epfl.sweng.zuluzulu.Database;

import org.junit.Test;

import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.Firebase.Proxy;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

public class DatabaseFactoryTest {

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