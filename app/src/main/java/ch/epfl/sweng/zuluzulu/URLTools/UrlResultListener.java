package ch.epfl.sweng.zuluzulu.URLTools;

public interface UrlResultListener<T> {
    void onFinished(T result);
}
