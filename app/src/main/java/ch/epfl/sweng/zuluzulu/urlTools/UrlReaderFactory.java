package ch.epfl.sweng.zuluzulu.urlTools;

public class UrlReaderFactory {
    private static UrlReader dependency = new UrlReader();

    public static UrlReader getDependency() {
        return dependency;
    }

    public static void setDependency(UrlReader dependency) {
        UrlReaderFactory.dependency = dependency;
    }
}
