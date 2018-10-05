package ch.epfl.sweng.zuluzulu;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import ch.epfl.sweng.zuluzulu.Structure.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.Structure.Guest;
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

        assertTrue(user.isConnected());

        assertEquals(user.getEmail(),"mail@epfl.ch");
        assertEquals(user.getSciper(),"1212");
        assertEquals(user.getGaspar(),"test");
        assertEquals(user.getFirst_names(),"first_name");
        assertEquals(user.getLast_names(),"last_name");
    }

    @Test
    public void canCreateGuestUser() {
        User.UserBuilder builder = new User.UserBuilder();
        builder.setLast_names("last_name");

        User user = builder.build();

        Guest user2 = builder.buildGuestUser();
        assertNotNull(user2);

        assertFalse(user.isConnected());

        assertEquals(user.getEmail(),null);
        assertEquals(user.getSciper(),null);
        assertEquals(user.getGaspar(),null);
        assertEquals(user.getFirst_names(),null);
        assertEquals(user.getLast_names(),null);
    }


    @Test
    public void refuseFakeMail() {
        User.UserBuilder builder = new User.UserBuilder();
        builder.setEmail("fakemail");
        builder.setSciper("1212");
        builder.setGaspar("test");
        builder.setFirst_names("first_name");
        builder.setLast_names("last_name");

        User user = builder.build();

        assertFalse(user.isConnected());
    }

}
