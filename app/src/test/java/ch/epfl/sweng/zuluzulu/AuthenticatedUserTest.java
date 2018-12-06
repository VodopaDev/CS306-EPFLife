package ch.epfl.sweng.zuluzulu;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.sweng.zuluzulu.User.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.User.User;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(JUnit4.class)
public class AuthenticatedUserTest {
    private static final String sciper = "000001";
    private static final String email = "nico@epfl.ch";
    private static final String section = "IN";
    private static final String semester = "BA5";
    private static final String gaspar = "jomeau@epfl.ch";
    private static final String first_name = "nicolas";

    private final String ASSOCIATION_ID = "1";
    private final String EVENT_ID = "1";
    private final String CHANNEL_ID = "1";
    private final String last_name = "jomeau";
    private AuthenticatedUser user;
    private List<String> fav_assos;
    private List<String> fol_events;
    private List<String> fol_chats;

    @Before
    public void createUser() {
        fav_assos = new ArrayList<>();
        fol_events = new ArrayList<>();
        fol_chats = new ArrayList<>();

        User.UserBuilder builder = new User.UserBuilder();
        builder.setEmail(email);
        builder.setSection(section);
        builder.setSemester(semester);
        builder.setSciper(sciper);
        builder.setGaspar(gaspar);
        builder.setFirst_names(first_name);
        builder.setLast_names(last_name);
        builder.setFollowedChannels(fol_chats);
        builder.setFollowedAssociations(fav_assos);
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
    public void setList() {
        assertFalse(user.isFollowedAssociation(ASSOCIATION_ID));
        assertFalse(user.isFollowedEvent(EVENT_ID));
        assertFalse(user.isFollowedChannel(CHANNEL_ID));
        user.setFollowedChannels(Collections.singletonList(CHANNEL_ID));
        user.setFollowedAssociation(Collections.singletonList(ASSOCIATION_ID));
        user.setFollowedEvents(Collections.singletonList(EVENT_ID));
        assertTrue(user.isFollowedAssociation(ASSOCIATION_ID));
        assertTrue(user.isFollowedEvent(EVENT_ID));
        assertTrue(user.isFollowedChannel(CHANNEL_ID));
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
