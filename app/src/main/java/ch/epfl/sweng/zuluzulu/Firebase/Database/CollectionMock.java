package ch.epfl.sweng.zuluzulu.Firebase.Database;

public class CollectionMock implements DatabaseCollection {
    @Override
    public DatabaseDocument document(String documentPath) {
        return new DocumentMock();
    }
}
