package ch.epfl.sweng.zuluzulu.firebase;

public interface OnResult<T> {
    void apply(T result);
}
