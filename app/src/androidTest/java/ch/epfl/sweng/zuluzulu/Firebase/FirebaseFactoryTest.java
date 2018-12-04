package ch.epfl.sweng.zuluzulu.Firebase;

import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedReader;

import ch.epfl.sweng.zuluzulu.Database.FirebaseMock;
import ch.epfl.sweng.zuluzulu.Firebase.Database.Database;
import ch.epfl.sweng.zuluzulu.Firebase.Database.FirebaseFactory;
import ch.epfl.sweng.zuluzulu.Firebase.FirebaseProxy;
import ch.epfl.sweng.zuluzulu.URLTools.UrlReader;
import ch.epfl.sweng.zuluzulu.URLTools.UrlReaderFactory;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

public class FirebaseFactoryTest {

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