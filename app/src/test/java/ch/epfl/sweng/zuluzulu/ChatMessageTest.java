package ch.epfl.sweng.zuluzulu;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import ch.epfl.sweng.zuluzulu.Structure.ChatMessage;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class ChatMessageTest {

    private static final String senderName1 = "James";
    private static final String sciper1 = "111111";
    private static final String message1 = "James message";
    private static final Timestamp time = Timestamp.now();
    private static final String senderName2 = "Bond";
    private static final String sciper2 = "222222";
    private static final String message2 = "Bond's message";

    private static final String userSciper = sciper1;

    private ChatMessage chatMessage1;
    private ChatMessage chatMessage2;

    private final DocumentSnapshot mocked_valid_datasnap1 = Mockito.mock(DocumentSnapshot.class);
    private final DocumentSnapshot mocked_valid_datasnap2 = Mockito.mock(DocumentSnapshot.class);
    private final DocumentSnapshot mocked_invalid_datasnap = Mockito.mock(DocumentSnapshot.class);

    @Before
    public void init() {
        Mockito.when(mocked_valid_datasnap1.get("senderName")).thenReturn(senderName1);
        Mockito.when(mocked_valid_datasnap1.get("sciper")).thenReturn(sciper1);
        Mockito.when(mocked_valid_datasnap1.get("message")).thenReturn(message1);
        Mockito.when(mocked_valid_datasnap1.get("time")).thenReturn(time);

        Mockito.when(mocked_valid_datasnap2.get("senderName")).thenReturn(senderName2);
        Mockito.when(mocked_valid_datasnap2.get("sciper")).thenReturn(sciper2);
        Mockito.when(mocked_valid_datasnap2.get("message")).thenReturn(message2);
        Mockito.when(mocked_valid_datasnap2.get("time")).thenReturn(time);

        Mockito.when(mocked_invalid_datasnap.get("senderName")).thenReturn(senderName1);
        Mockito.when(mocked_invalid_datasnap.get("sciper")).thenReturn(null);
        Mockito.when(mocked_invalid_datasnap.get("message")).thenReturn(message1);
        Mockito.when(mocked_invalid_datasnap.get("time")).thenReturn(time);

        Mockito.when(mocked_valid_datasnap1.getString("senderName")).thenReturn(senderName1);
        Mockito.when(mocked_valid_datasnap1.getString("sciper")).thenReturn(sciper1);
        Mockito.when(mocked_valid_datasnap1.getString("message")).thenReturn(message1);
        Mockito.when(mocked_valid_datasnap1.getTimestamp("time")).thenReturn(time);

        Mockito.when(mocked_valid_datasnap2.getString("senderName")).thenReturn(senderName2);
        Mockito.when(mocked_valid_datasnap2.getString("sciper")).thenReturn(sciper2);
        Mockito.when(mocked_valid_datasnap2.getString("message")).thenReturn(message2);
        Mockito.when(mocked_valid_datasnap2.getTimestamp("time")).thenReturn(time);

        Mockito.when(mocked_invalid_datasnap.getString("senderName")).thenReturn(senderName1);
        Mockito.when(mocked_invalid_datasnap.getString("sciper")).thenReturn(null);
        Mockito.when(mocked_invalid_datasnap.getString("message")).thenReturn(message1);
        Mockito.when(mocked_valid_datasnap1.getTimestamp("time")).thenReturn(time);

        chatMessage1 = new ChatMessage(mocked_valid_datasnap1, userSciper);
        chatMessage2 = new ChatMessage(mocked_valid_datasnap2, userSciper);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidSnapThrowIllegalArgumentException() {
        new ChatMessage(mocked_invalid_datasnap, userSciper);
    }

    @Test
    public void testGuettersAndSetters() {
        assertEquals(senderName1, chatMessage1.getSenderName());
        assertEquals(sciper1, chatMessage1.getSciper());
        assertEquals(message1, chatMessage1.getMessage());
        assertEquals(time, chatMessage1.getTime());

        chatMessage1.setSenderName(senderName2);
        chatMessage1.setSciper(sciper2);
        chatMessage1.setMessage(message2);
        chatMessage1.setTime(time);

        assertEquals(senderName2, chatMessage1.getSenderName());
        assertEquals(sciper2, chatMessage1.getSciper());
        assertEquals(message2, chatMessage1.getMessage());
        assertEquals(time, chatMessage1.getTime());
    }

    @Test
    public void testIsOwnMessage() {
        assertEquals(true, chatMessage1.isOwnMessage());
        assertEquals(false, chatMessage2.isOwnMessage());

        chatMessage2.setOwnMessage(true);
        assertEquals(true, chatMessage2.isOwnMessage());
    }

}
