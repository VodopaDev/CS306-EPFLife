package ch.epfl.sweng.zuluzulu.Firebase;

public interface mapToObject<T> {
    public T apply(FirebaseMapDecorator map);
}
