package ch.epfl.sweng.zuluzulu.database;

import ch.epfl.sweng.zuluzulu.firebase.Database.Database;
import ch.epfl.sweng.zuluzulu.firebase.Database.DatabaseCollection;

public class FirebaseMock implements Database {

    @Override
    public DatabaseCollection collection(String collectionPath) {
        return new CollectionMock();
    }
}
