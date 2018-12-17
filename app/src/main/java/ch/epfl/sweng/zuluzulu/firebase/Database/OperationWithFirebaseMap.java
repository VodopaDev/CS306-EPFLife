package ch.epfl.sweng.zuluzulu.firebase.Database;

import ch.epfl.sweng.zuluzulu.firebase.FirebaseMapDecorator;

public interface OperationWithFirebaseMap {
    void apply(FirebaseMapDecorator fmap);
}
