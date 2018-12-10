package ch.epfl.sweng.zuluzulu.Firebase.Database;

import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;

public interface OperationWithFirebaseMap {
    public void apply(FirebaseMapDecorator fmap);
}
