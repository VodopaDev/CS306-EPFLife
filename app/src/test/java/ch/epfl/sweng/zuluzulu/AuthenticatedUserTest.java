package ch.epfl.sweng.zuluzulu;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.epfl.sweng.zuluzulu.Structure.Association;
import ch.epfl.sweng.zuluzulu.Structure.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.Structure.Channel;
import ch.epfl.sweng.zuluzulu.Structure.Event;
import ch.epfl.sweng.zuluzulu.Structure.User;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class AuthenticatedUserTest {
    private AuthenticatedUser user;

    private final Association mocked_asso = mock(Association.class);
    private final Event mocked_event = mock(Event.class);
    private final Channel mocked_channel = mock(Channel.class);

    private static final String sciper = "000001";
    private static final String email = "nico@epfl.ch";
    private static final String gaspar = "jomeau@epfl.ch";
    private static final String first_name = "nicolas";
    private final String last_name = "jomeau";
    private List<Integer> fav_assos;
    private List<Integer> fol_events;
    private List<Integer> fol_chats;

    @Before
    public void createUser(){
        fav_assos = new ArrayList<>();
        fol_events = new ArrayList<>();
        fol_chats = new ArrayList<>();

        when(mocked_asso.getId()).thenReturn(1);
        when(mocked_channel.getId()).thenReturn(1);
        when(mocked_event.getId()).thenReturn(1);


        User.UserBuilder builder = new User.UserBuilder();
        builder.setEmail(email);
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
    public void gettersTest(){
        assertThat(first_name, equalTo(user.getFirstNames()));
        assertThat(last_name, equalTo(user.getLastNames()));
        assertThat(sciper, equalTo(user.getSciper()));
        assertThat(email, equalTo(user.getEmail()));
        assertThat(gaspar, equalTo(user.getGaspar()));
    }

    @Test
    public void followTest(){
        assertThat(false, equalTo(user.isFavAssociation(mocked_asso)));
        assertThat(false, equalTo(user.isFollowedChat(mocked_channel)));
        assertThat(false, equalTo(user.isFollowedEvent(mocked_event)));
    }

    @Test
    public void setList(){
        user.setFollowedChats(Arrays.asList(1));
        user.setFavAssos(Arrays.asList(1));
        user.setFollowedEvents(Arrays.asList(1));
        assertThat(true, equalTo(user.isFavAssociation(mocked_asso)));
        assertThat(true, equalTo(user.isFollowedChat(mocked_channel)));
        assertThat(true, equalTo(user.isFollowedEvent(mocked_event)));
    }

    @Test
    public void connected(){
        assertThat(true, equalTo(user.isConnected()));
    }

    @Test
    public void correctString(){
        assertThat(first_name + " " + last_name
                + "\nsciper: " + sciper
                + "\ngaspar: " + gaspar
                + "\nemail: " + email
        , equalTo(user.toString()));
    }
}
