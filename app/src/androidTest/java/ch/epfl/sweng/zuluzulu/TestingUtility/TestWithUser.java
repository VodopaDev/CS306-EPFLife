package ch.epfl.sweng.zuluzulu.TestingUtility;
import ch.epfl.sweng.zuluzulu.Structure.User;

public interface TestWithUser<T extends User> {

    T setUser();
}
