package ch.epfl.sweng.zuluzulu.Firebase;

public interface mapToObject<T> {
    T apply(FirebaseMapDecorator map);
}
