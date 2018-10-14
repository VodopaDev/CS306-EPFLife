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

@RunWith(JUnit4.class)
public class UserTest {

    private Association asso = mock(Association.class);
    private Channel channel = mock(Channel.class);
    private Event event = mock(Event.class);

    @Before
    public void mockStructures(){
        when(asso.getId()).thenReturn(0);
        when(channel.getName()).thenReturn("channel");
        when(event.getId()).thenReturn(2);
    }

    @Test
    public void canCreateAuthenticatedUser() {
        User.UserBuilder builder = new User.UserBuilder();
        builder.setEmail("mail@epfl.ch");
        builder.setSciper("1212");
        builder.setGaspar("test");
        builder.setFirst_names("first_name");
        builder.setLast_names("last_name");

        AuthenticatedUser user = (AuthenticatedUser)builder.build();
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
    public void guestUserHasNullProperties(){
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
    public void authenticatedUserCanChangeFavAssos() {
        User.UserBuilder builder = new User.UserBuilder();
        builder.setEmail("mail@epfl.ch");
        builder.setSciper("1212");
        builder.setGaspar("test");
        builder.setFirst_names("first_name");
        builder.setLast_names("last_name");
        AuthenticatedUser user = (AuthenticatedUser) builder.build();

        assertFalse(user.isFavAssociation(asso));
        assertTrue(user.addFavAssociation(asso));
        assertFalse(user.addFavAssociation(asso));
        assertTrue(user.isFavAssociation(asso));
        assertTrue(user.removeFavAssociation(asso));
        assertFalse(user.removeFavAssociation(asso));
        assertFalse(user.isFavAssociation(asso));
    }

    @Test
    public void authenticatedUserCanChangeFollowedEvents() {
        User.UserBuilder builder = new User.UserBuilder();
        builder.setEmail("mail@epfl.ch");
        builder.setSciper("1212");
        builder.setGaspar("test");
        builder.setFirst_names("first_name");
        builder.setLast_names("last_name");
        AuthenticatedUser user = (AuthenticatedUser) builder.build();

        assertFalse(user.isFollowedEvent(event));
        assertTrue(user.addFollowedEvent(event));
        assertFalse(user.addFollowedEvent(event));
        assertTrue(user.isFollowedEvent(event));
        assertTrue(user.removeFollowedEvent(event));
        assertFalse(user.removeFollowedEvent(event));
        assertFalse(user.isFollowedEvent(event));
    }

    @Test
    public void authenticatedUserCanChangeFollowedChannels() {
        User.UserBuilder builder = new User.UserBuilder();
        builder.setEmail("mail@epfl.ch");
        builder.setSciper("1212");
        builder.setGaspar("test");
        builder.setFirst_names("first_name");
        builder.setLast_names("last_name");
        AuthenticatedUser user = (AuthenticatedUser) builder.build();

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
        User.UserBuilder builder = new User.UserBuilder();
        builder.setEmail("mail@epfl.ch");
        builder.setSciper("1212");
        builder.setGaspar("test");
        builder.setFirst_names("first_name");
        builder.setLast_names("last_name");
        AuthenticatedUser user = (AuthenticatedUser) builder.build();

        String expected = "first_name last_name"
                + "\nsciper: 1212"
                + "\ngaspar: test"
                + "\nemail: mail@epfl.ch";

        assertEquals(expected, user.toString());
        assertEquals("Guest user", new User.UserBuilder().buildGuestUser().toString());
    }

}
