package ch.epfl.sweng.zuluzulu;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.Collections;

import ch.epfl.sweng.zuluzulu.Structure.Guest;
import ch.epfl.sweng.zuluzulu.Structure.User;
import ch.epfl.sweng.zuluzulu.Structure.UserRole;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

/**
 * All authenticated user test have been moved t
 */
@RunWith(JUnit4.class)
public class UserTest {

    @Test
    public void checkUserRole(){
        User user = createAuthenticatedUser();
        assertTrue(user.hasRole(UserRole.USER));
    }

    @Test
    public void canCreateAdmin(){
        User.UserBuilder builder = new User.UserBuilder();
        builder.setEmail("admin@epfl.ch");
        builder.setSciper("121212");
        builder.setGaspar("admin");
        builder.setFirst_names("admin_first_name");
        builder.setLast_names("admin_last_name");

        AuthenticatedUser user = (AuthenticatedUser) builder.buildAdmin();


        assertNotNull(user);
        assertTrue(user.isConnected());
        assertTrue(user.hasRole(UserRole.ADMIN));
    }

    @Test
    public void canCreateAuthenticatedUser() {
        User user = createAuthenticatedUser();
        assertTrue(user.isConnected());

        assertEquals(user.getEmail(), "mail@epfl.ch");
        assertEquals(user.getSciper(), "1212");
        assertEquals(user.getGaspar(), "test");
        assertEquals(user.getFirstNames(), "first_name");
        assertEquals(user.getLastNames(), "last_name");
    }

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

    @Test
    public void correctString(){
        assertThat("Guest user", equalTo(new User.UserBuilder().buildGuestUser().toString())); 
    }
  
    public void authenticatedUserCanChangeFavAssos() {
        AuthenticatedUser user = createAuthenticatedUser();
        assertFalse(user.isFavAssociation(asso));
        assertTrue(user.addFavAssociation(asso));
        assertFalse(user.addFavAssociation(asso));
        assertTrue(user.isFavAssociation(asso));
        assertTrue(user.removeFavAssociation(asso));
        assertFalse(user.removeFavAssociation(asso));
        assertFalse(user.isFavAssociation(asso));
    }

    @Test
    public void authenticatedUserCanChangeFollowedChannels() {
        AuthenticatedUser user = createAuthenticatedUser();

        assertFalse(user.isFollowedChat(channel));
        assertTrue(user.addFollowedChat(channel));
        assertFalse(user.addFollowedChat(channel));
        assertTrue(user.isFollowedChat(channel));
        assertTrue(user.removeFollowedChat(channel));
        assertFalse(user.removeFollowedChat(channel));
        assertFalse(user.isFollowedChat(channel));
    }

    @Test
    public void toStringIsCorrect() {
        User user = createAuthenticatedUser();

        String expected = "first_name last_name"
                + "\nsciper: 1212"
                + "\ngaspar: test"
                + "\nemail: mail@epfl.ch";

        assertEquals(expected, user.toString());
        assertEquals("Guest user", new User.UserBuilder().buildGuestUser().toString());
    }

}
