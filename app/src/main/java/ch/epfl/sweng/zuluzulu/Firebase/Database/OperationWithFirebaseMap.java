package ch.epfl.sweng.zuluzulu.Firebase.Database;

import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;

public interface OperationWithFirebaseMap {
    void apply(FirebaseMapDecorator fmap);
}
