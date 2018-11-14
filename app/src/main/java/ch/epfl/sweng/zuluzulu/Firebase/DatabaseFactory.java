package ch.epfl.sweng.zuluzulu.Firebase;

import com.google.firebase.firestore.FirebaseFirestore;

public class DatabaseFactory {
    private static FirebaseFirestore dependency = FirebaseFirestore.getInstance();

    public static FirebaseFirestore getDependency() {
        return dependency;
    }

    public static void setDependency(FirebaseFirestore dependency) {
        DatabaseFactory.dependency = dependency;
    }
}
