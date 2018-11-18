package ch.epfl.sweng.zuluzulu.Firebase;

public interface OnDataFetched<T> {
    void apply(T data);
}
