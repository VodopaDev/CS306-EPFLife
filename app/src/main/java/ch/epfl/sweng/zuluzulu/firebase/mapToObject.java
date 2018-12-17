package ch.epfl.sweng.zuluzulu.firebase;

public interface mapToObject<T> {
    T apply(FirebaseMapDecorator map);
}
