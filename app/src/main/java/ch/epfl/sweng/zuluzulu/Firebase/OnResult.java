package ch.epfl.sweng.zuluzulu.Firebase;

public interface OnResult<T> {
    void apply(T result);
}
