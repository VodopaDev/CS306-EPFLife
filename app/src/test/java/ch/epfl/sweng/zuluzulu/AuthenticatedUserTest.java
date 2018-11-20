package ch.epfl.sweng.zuluzulu;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.sweng.zuluzulu.Structure.Association;
import ch.epfl.sweng.zuluzulu.User.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.Structure.Channel;
import ch.epfl.sweng.zuluzulu.Structure.Event;
import ch.epfl.sweng.zuluzulu.User.User;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class AuthenticatedUserTest {
    private static final String sciper = "000001";
    private static final String email = "nico@epfl.ch";
    private static final String section = "IN";
    private static final String semester = "BA5";
    private static final String gaspar = "jomeau@epfl.ch";
    private static final String first_name = "nicolas";
    private final Association mocked_asso = mock(Association.class);
    private final Event mocked_event = mock(Event.class);
    private final Channel mocked_channel = mock(Channel.class);
    private final String last_name = "jomeau";
    private AuthenticatedUser user;
    private List<Long> fav_assos;
    private List<Long> fol_events;
    private List<Long> fol_chats;

    @Before
    public void createUser() {
        fav_assos = new ArrayList<>();
        fol_events = new ArrayList<>();
        fol_chats = new ArrayList<>();

        when(mocked_asso.getId()).thenReturn(1L);
        when(mocked_channel.getId()).thenReturn(1);
        when(mocked_event.getId()).thenReturn(1L);


        User.UserBuilder builder = new User.UserBuilder();
        builder.setEmail(email);
        builder.setSection(section);
        builder.setSemester(semester);
        builder.setSciper(sciper);
        builder.setGaspar(gaspar);
        builder.setFirst_names(first_name);
        builder.setLast_names(last_name);
        builder.setFollowedChats(fol_chats);
        builder.setFavAssos(fav_assos);
        builder.setFollowedEvents(fol_events);
        user = builder.buildAuthenticatedUser();
    }

    @Test
    public void gettersTest() {
        assertThat(first_name, equalTo(user.getFirstNames()));
        assertThat(last_name, equalTo(user.getLastNames()));
        assertThat(sciper, equalTo(user.getSciper()));
        assertThat(email, equalTo(user.getEmail()));
        assertThat(section, equalTo(user.getSection()));
        assertThat(semester, equalTo(user.getSemester()));
        assertThat(gaspar, equalTo(user.getGaspar()));
    }

    @Test
    public void followTest() {
        assertThat(false, equalTo(user.isFavAssociation(mocked_asso)));
        assertThat(false, equalTo(user.isFollowedChat(mocked_channel)));
        assertThat(false, equalTo(user.isFollowedEvent(mocked_event)));
    }

    @Test
    public void setList() {
        user.setFollowedChats(Collections.singletonList(1L));
        user.setFavAssos(Collections.singletonList(1L));
        user.setFollowedEvents(Collections.singletonList(1L));
        assertThat(true, equalTo(user.isFavAssociation(mocked_asso)));
        assertThat(true, equalTo(user.isFollowedChat(mocked_channel)));
        assertThat(true, equalTo(user.isFollowedEvent(mocked_event)));
    }

    @Test
    public void connected() {
        assertThat(true, equalTo(user.isConnected()));
    }

    @Test
    public void correctString() {
        assertThat(first_name + " " + last_name
                + "\nsciper: " + sciper
                + "\ngaspar: " + gaspar
                + "\nemail: " + email
                + "\nsection: " + section
                + "\nsemester: " + semester
        , equalTo(user.toString()));

    }
}
