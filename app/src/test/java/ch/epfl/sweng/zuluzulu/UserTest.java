package ch.epfl.sweng.zuluzulu;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ch.epfl.sweng.zuluzulu.Structure.Association;
import ch.epfl.sweng.zuluzulu.Structure.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.Structure.Channel;
import ch.epfl.sweng.zuluzulu.Structure.Event;
import ch.epfl.sweng.zuluzulu.Structure.Guest;
import ch.epfl.sweng.zuluzulu.Structure.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * All authenticated user test have been moved t
 */



@RunWith(JUnit4.class)
public class UserTest {

    @Test
    public void canCreateGuestUser() {
        User.UserBuilder builder = new User.UserBuilder();

        assertFalse(builder.buildGuestUser().isConnected());
        assertFalse(builder.build().isConnected());
    }

    @Test
    public void guestUserHasNullProperties() {
        Guest guest = new User.UserBuilder().buildGuestUser();
        assertNull(guest.getEmail());
        assertNull(guest.getFirstNames());
        assertNull(guest.getGaspar());
        assertNull(guest.getLastNames());
        assertNull(guest.getSciper());
    }


    @Test
    public void refuseFakeMail() {
        User.UserBuilder builder = new User.UserBuilder();
        builder.setEmail("fake_mail");
        builder.setSciper("1212");
        builder.setGaspar("test");
        builder.setFirst_names("first_name");
        builder.setLast_names("last_name");

        User user = builder.build();
        assertFalse(user.isConnected());
    }

}
