package ch.epfl.sweng.zuluzulu.Firebase.Database;

import org.junit.Test;

import static org.junit.Assert.*;

public class FirebaseAdapterTest {
    @Test
    public void collection() {
        assertNotNull(new FirebaseAdapter().collection("path"));
    }
}