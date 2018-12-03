package ch.epfl.sweng.zuluzulu.Firebase;

public abstract class ProxyDependency {
    private static String dependency = "real";

    public static String getDependency() {
        return dependency;
    }

    public static void setDependency(String dep) {
        if (dep != null && dep.equals("test"))
            dependency = "test";
        else if (dep != null && dep.equals("real"))
            dependency = "real";
    }
}
