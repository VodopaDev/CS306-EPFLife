package ch.epfl.sweng.zuluzulu.Firebase;

import com.google.firebase.firestore.FirebaseFirestore;

import ch.epfl.sweng.zuluzulu.Firebase.Database.Database;
import ch.epfl.sweng.zuluzulu.Firebase.Database.FirebaseAdapter;

public class DatabaseFactory {
    private static Database dependency = new FirebaseAdapter();

    public static Database getDependency() {
        return dependency;
    }

    public static void setDependency(Database dependency) {
        DatabaseFactory.dependency = dependency;
    }
}
