package ch.epfl.sweng.zuluzulu;

import com.google.firebase.firestore.DocumentSnapshot;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.Structure.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.Structure.Channel;
import ch.epfl.sweng.zuluzulu.Structure.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class ChannelTest {

    private static final Long id1 = 1l;
    private static final String channelName1 = "Global";
    private static final String channelDescription1 = "Global chat for everyone";
    private static final Map<String, Object> restrictions1 = new HashMap<>();
    private static final Long id2 = 2l;
    private static final String channelName2 = "IN channel";
    private static final String channelDescription2 = "Channel just for the IN";
    private static final Map<String, Object> restrictions2 = new HashMap<>();

    private Channel channelGlobal;
    private Channel channelIN;
    private AuthenticatedUser userIN;
    private AuthenticatedUser userSC;

    private final DocumentSnapshot mocked_valid_datasnap1 = Mockito.mock(DocumentSnapshot.class);
    private final DocumentSnapshot mocked_valid_datasnap2 = Mockito.mock(DocumentSnapshot.class);
    private final DocumentSnapshot mocked_invalid_datasnap = Mockito.mock(DocumentSnapshot.class);

    @Before
    public void init() {
        User.UserBuilder builder = new User.UserBuilder();
        builder.setSciper("123456");
        builder.setGaspar("gaspar");
        builder.setEmail("test@epfl.ch");
        builder.setSection("IN");
        builder.setFirst_names("james");
        builder.setLast_names("bond");
        userIN = builder.buildAuthenticatedUser();

        builder.setSection("SC");
        userSC = builder.buildAuthenticatedUser();

        restrictions1.put("section", null);
        restrictions1.put("location", null);

        restrictions2.put("section", "IN");
        restrictions2.put("location", null);

        Mockito.when(mocked_valid_datasnap1.get("id")).thenReturn(id1);
        Mockito.when(mocked_valid_datasnap1.get("name")).thenReturn(channelName1);
        Mockito.when(mocked_valid_datasnap1.get("description")).thenReturn(channelDescription1);
        Mockito.when(mocked_valid_datasnap1.get("restrictions")).thenReturn(restrictions1);

        Mockito.when(mocked_invalid_datasnap.get("id")).thenReturn(id1);
        Mockito.when(mocked_invalid_datasnap.get("name")).thenReturn(null);
        Mockito.when(mocked_invalid_datasnap.get("description")).thenReturn(channelDescription1);
        Mockito.when(mocked_invalid_datasnap.get("restrictions")).thenReturn(restrictions1);

        Mockito.when(mocked_valid_datasnap1.getLong("id")).thenReturn(id1);
        Mockito.when(mocked_valid_datasnap1.getString("name")).thenReturn(channelName1);
        Mockito.when(mocked_valid_datasnap1.getString("description")).thenReturn(channelDescription1);

        Mockito.when(mocked_invalid_datasnap.getLong("id")).thenReturn(id1);
        Mockito.when(mocked_invalid_datasnap.getString("name")).thenReturn(null);
        Mockito.when(mocked_invalid_datasnap.getString("description")).thenReturn(channelDescription1);

        Mockito.when(mocked_valid_datasnap1.get("id")).thenReturn(id2);
        Mockito.when(mocked_valid_datasnap1.get("name")).thenReturn(channelName2);
        Mockito.when(mocked_valid_datasnap1.get("description")).thenReturn(channelDescription2);
        Mockito.when(mocked_valid_datasnap1.get("restrictions")).thenReturn(restrictions2);

        Mockito.when(mocked_valid_datasnap1.getLong("id")).thenReturn(id2);
        Mockito.when(mocked_valid_datasnap1.getString("name")).thenReturn(channelName2);
        Mockito.when(mocked_valid_datasnap1.getString("description")).thenReturn(channelDescription2);

        channelGlobal = new Channel(mocked_valid_datasnap1);
        channelIN = new Channel(mocked_valid_datasnap2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidSnapThrowIllegalArgumentException() {
        new Channel(mocked_invalid_datasnap);
    }

    @Test
    public void testGuettersAndSetters() {
        assertEquals(id1.intValue(), channelGlobal.getId());
        assertEquals(channelName1, channelGlobal.getName());
        assertEquals(channelDescription1, channelGlobal.getDescription());

        channelGlobal.setId(id2.intValue());
        channelGlobal.setName(channelName2);
        channelGlobal.setDescription(channelDescription2);

        assertEquals(id2.intValue(), channelGlobal.getId());
        assertEquals(channelName2, channelGlobal.getName());
        assertEquals(channelDescription2, channelGlobal.getDescription());
    }

    @Test
    public void testChannelWithRestrictions() {
        assertTrue(channelGlobal.canBeAccessedBy(userIN));
        assertTrue(channelGlobal.canBeAccessedBy(userSC));

        assertTrue(channelIN.canBeAccessedBy(userIN));
        assertFalse(channelIN.canBeAccessedBy(userSC));
    }
}
