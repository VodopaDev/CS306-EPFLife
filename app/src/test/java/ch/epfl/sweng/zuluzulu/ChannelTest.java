package ch.epfl.sweng.zuluzulu;

import com.google.firebase.firestore.DocumentSnapshot;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import ch.epfl.sweng.zuluzulu.Structure.Channel;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class ChannelTest {

    private static final Long id1 = 1l;
    private static final String channelName1 = "Global";
    private static final String channelDescription1 = "Global chat for everyone";
    private static final Long id2 = 2l;
    private static final String channelName2 = "Test";
    private static final String channelDescription2 = "A chat just to test stuff";

    private Channel channel;

    private final DocumentSnapshot mocked_valid_datasnap1 = Mockito.mock(DocumentSnapshot.class);
    private final DocumentSnapshot mocked_invalid_datasnap = Mockito.mock(DocumentSnapshot.class);

    @Before
    public void init() {
        Mockito.when(mocked_valid_datasnap1.get("id")).thenReturn(id1);
        Mockito.when(mocked_valid_datasnap1.get("name")).thenReturn(channelName1);
        Mockito.when(mocked_valid_datasnap1.get("description")).thenReturn(channelDescription1);

        Mockito.when(mocked_invalid_datasnap.get("id")).thenReturn(id1);
        Mockito.when(mocked_invalid_datasnap.get("name")).thenReturn(null);
        Mockito.when(mocked_invalid_datasnap.get("description")).thenReturn(channelDescription1);

        Mockito.when(mocked_valid_datasnap1.getLong("id")).thenReturn(id1);
        Mockito.when(mocked_valid_datasnap1.getString("name")).thenReturn(channelName1);
        Mockito.when(mocked_valid_datasnap1.getString("description")).thenReturn(channelDescription1);

        Mockito.when(mocked_invalid_datasnap.getLong("id")).thenReturn(id1);
        Mockito.when(mocked_invalid_datasnap.getString("name")).thenReturn(null);
        Mockito.when(mocked_invalid_datasnap.getString("description")).thenReturn(channelDescription1);

        channel = new Channel(mocked_valid_datasnap1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidSnapThrowIllegalArgumentException() {
        new Channel(mocked_invalid_datasnap);
    }

    @Test
    public void testGuettersAndSetters() {
        assertEquals(id1.intValue(), channel.getId());
        assertEquals(channelName1, channel.getName());
        assertEquals(channelDescription1, channel.getDescription());

        channel.setId(id2.intValue());
        channel.setName(channelName2);
        channel.setDescription(channelDescription2);

        assertEquals(id2.intValue(), channel.getId());
        assertEquals(channelName2, channel.getName());
        assertEquals(channelDescription2, channel.getDescription());
    }
}
