package ch.epfl.sweng.zuluzulu.Database;

import ch.epfl.sweng.zuluzulu.Firebase.Database.DatabaseCollection;
import ch.epfl.sweng.zuluzulu.Firebase.Database.DatabaseDocument;

public class CollectionMock implements DatabaseCollection {
    @Override
    public DatabaseDocument document(String documentPath) {
        return new DocumentMock();
    }
}
