package ch.epfl.sweng.zuluzulu;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import ch.epfl.sweng.zuluzulu.Structure.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.Structure.User;


public class UserTest {

    @Test
    public void canCreateAuthenticatedUser() {
        User.UserBuilder builder = new User.UserBuilder();
        builder.setEmail("mail@epfl.ch");
        builder.setSciper("1212");
        builder.setGaspar("test");
        builder.setFirst_names("first_name");
        builder.setLast_names("last_name");

        User user = builder.build();

        assertTrue(user instanceof AuthenticatedUser);

        assertEquals(user.getEmail(),"mail@epfl.ch");
        assertEquals(user.getSciper(),"1212");
        assertEquals(user.getGaspar(),"test");
        assertEquals(user.getFirst_names(),"first_name");
        assertEquals(user.getLast_names(),"last_name");
    }
}
