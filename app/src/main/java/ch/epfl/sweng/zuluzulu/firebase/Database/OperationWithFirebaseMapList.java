package ch.epfl.sweng.zuluzulu.firebase.Database;

import java.util.List;

import ch.epfl.sweng.zuluzulu.firebase.FirebaseMapDecorator;

public interface OperationWithFirebaseMapList {
    void applyList(List<FirebaseMapDecorator> list);
}
