package ch.epfl.sweng.zuluzulu.Firebase;

import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Ignore;
import org.junit.Test;

import ch.epfl.sweng.zuluzulu.Firebase.Database.Database;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

public class DatabaseFactoryTest {

    @Test
    @Ignore
    public void getDependency() {
        assertThat(DatabaseFactory.getDependency(), instanceOf(Database.class));
    }

    @Test
    @Ignore
    public void setDependency() {
       // DatabaseFactory.setDependency()
    }
}