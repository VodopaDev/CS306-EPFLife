package ch.epfl.sweng.zuluzulu.URLTools;

import android.util.Pair;

public interface UrlResultListener<T> {
    public void onFinished(T result);
}
