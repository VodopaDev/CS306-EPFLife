package ch.epfl.sweng.zuluzulu;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ch.epfl.sweng.zuluzulu.Structure.Channel;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class ChannelTest {

    private static final String channelName1 = "Global";
    private static final String channelDescription1 = "Global chat for everyone";
    private static final String channelName2 = "Test";
    private static final String channelDescription2 = "A chat just to test stuff";

    @Test
    public void testGuettersAndSetters() {
        Channel channel = new Channel(channelName1, channelDescription1);
        assertEquals(channelName1, channel.getName());
        assertEquals(channelDescription1, channel.getDescription());

        channel.setName(channelName2);
        channel.setDescription(channelDescription2);
        assertEquals(channelName2, channel.getName());
        assertEquals(channelDescription2, channel.getDescription());
    }


}
