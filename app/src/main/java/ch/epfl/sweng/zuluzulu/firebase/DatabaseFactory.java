package ch.epfl.sweng.zuluzulu.firebase;

public class DatabaseFactory {
    private static Proxy dependency = FirebaseProxy.getInstance();

    public static Proxy getDependency() {
        return dependency;
    }

    public static void setDependency(Proxy dependency) {
        DatabaseFactory.dependency = dependency;
    }
}
