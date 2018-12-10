package ch.epfl.sweng.zuluzulu.Firebase.Database;

import java.util.List;

import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;

public interface OperationWithFirebaseMapList {
    public void applyList(List<FirebaseMapDecorator> list);
}
