package ch.epfl.sweng.zuluzulu.Database;

import ch.epfl.sweng.zuluzulu.Firebase.Database.Database;
import ch.epfl.sweng.zuluzulu.Firebase.Database.DatabaseCollection;

public class FirebaseMock implements Database {

    @Override
    public DatabaseCollection collection(String collectionPath) {
        return new CollectionMock();
    }
}
