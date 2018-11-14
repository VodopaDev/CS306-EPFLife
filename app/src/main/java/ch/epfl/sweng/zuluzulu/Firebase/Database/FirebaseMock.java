package ch.epfl.sweng.zuluzulu.Firebase.Database;

public class FirebaseMock implements Database {

    @Override
    public DatabaseCollection collection(String collectionPath) {
        return new CollectionMock();
    }
}
