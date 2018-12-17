package ch.epfl.sweng.zuluzulu.firebase.Database;

public interface Database {
    DatabaseCollection collection(String collectionPath);
}
