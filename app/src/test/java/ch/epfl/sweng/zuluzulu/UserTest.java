package ch.epfl.sweng.zuluzulu;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Collections;

import ch.epfl.sweng.zuluzulu.User.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.User.Guest;
import ch.epfl.sweng.zuluzulu.User.User;
import ch.epfl.sweng.zuluzulu.User.UserRole;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
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
    public void checkUserRole() {
        User user = Utility.createTestUser();
        assertTrue(user.hasRole(UserRole.USER));
    }

    @Test
    public void incompleteBuilderBuildNullUser() {
        assertNull(new User.UserBuilder().buildAdmin());
    }

    @Test
    public void canCreateAdmin() {
        User.UserBuilder builder = new User.UserBuilder();
        builder.setEmail("admin@epfl.ch");
        builder.setSection("IN");
        builder.setSemester("BA5");
        builder.setSciper("121212");
        builder.setGaspar("admin");
        builder.setFirst_names("admin_first_name");
        builder.setLast_names("admin_last_name");
        builder.setFollowedChats(Collections.EMPTY_LIST);
        builder.setFavAssos(Collections.EMPTY_LIST);
        builder.setFollowedEvents(Collections.EMPTY_LIST);

        AuthenticatedUser user = builder.buildAdmin();


        assertNotNull(user);
        assertTrue(user.isConnected());
        assertTrue(user.hasRole(UserRole.ADMIN));
        assertTrue(builder.build().isConnected());
    }

    @Test
    public void guestHasNoRole() {
        assertThat(false, equalTo(new User.UserBuilder().buildGuestUser().hasRole(UserRole.ADMIN)));
        assertThat(false, equalTo(new User.UserBuilder().buildGuestUser().hasRole(UserRole.USER)));
        assertThat(false, equalTo(new User.UserBuilder().buildGuestUser().hasRole(UserRole.MODERATOR)));
    }

    @Test
    public void canCreateAuthenticatedUser() {
        User user = Utility.createTestUser();
        assertTrue(user.isConnected());
        assertEquals(user.getEmail(), "test@epfl.ch");
        assertEquals(user.getSection(), "IN");
        assertEquals(user.getSemester(), "BA5");
        assertEquals(user.getSciper(), "123456");
        assertEquals(user.getGaspar(), "gaspar");
        assertEquals(user.getFirstNames(), "James");
        assertEquals(user.getLastNames(), "Bond");
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
        assertNull(guest.getSection());
        assertNull(guest.getFirstNames());
        assertNull(guest.getGaspar());
        assertNull(guest.getLastNames());
        assertNull(guest.getSciper());
        assertNull(guest.getSemester());
    }

    @Test
    public void refuseFakeMail() {
        User.UserBuilder builder = new User.UserBuilder();
        builder.setEmail("fake_mail");
        builder.setSection("section");
        builder.setSciper("1212");
        builder.setGaspar("test");
        builder.setFirst_names("first_name");
        builder.setLast_names("last_name");
        builder.setFollowedChats(Collections.EMPTY_LIST);
        builder.setFavAssos(Collections.EMPTY_LIST);
        builder.setFollowedEvents(Collections.EMPTY_LIST);

        User user = builder.build();
        assertFalse(user.isConnected());
    }

    @Test
    public void correctString() {
        assertThat("Guest user", equalTo(new User.UserBuilder().buildGuestUser().toString()));
    }

    @Test
    public void toStringIsCorrect() {
        User user = Utility.createTestUser();

        String expected = "James Bond"
                + "\nsciper: 123456"
                + "\ngaspar: gaspar"
                + "\nemail: test@epfl.ch"
                + "\nsection: IN"
                + "\nsemester: BA5";

        assertEquals(expected, user.toString());
        assertEquals("Guest user", new User.UserBuilder().buildGuestUser().toString());
    }
}
