package ch.epfl.sweng.zuluzulu.firebase.Database;

public class FirebaseFactory {
    private static Database dependency = new FirebaseAdapter();

    public static Database getDependency() {
        return dependency;
    }

    public static void setDependency(Database dependency) {
        FirebaseFactory.dependency = dependency;
    }
}
