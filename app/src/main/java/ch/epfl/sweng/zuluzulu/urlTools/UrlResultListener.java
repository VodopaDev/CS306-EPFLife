package ch.epfl.sweng.zuluzulu.urlTools;

public interface UrlResultListener<T> {
    void onFinished(T result);
}
