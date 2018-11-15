package ch.epfl.sweng.zuluzulu.Database;

import org.junit.Test;

import ch.epfl.sweng.zuluzulu.Firebase.Database.FirebaseAdapter;

import static org.junit.Assert.*;

public class FirebaseAdapterTest {
    @Test
    public void collection() {
        assertNotNull(new FirebaseAdapter().collection("path"));
    }
}