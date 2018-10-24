package ch.epfl.sweng.zuluzulu;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

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

    private static Map data1 = new HashMap();
    private static Map data2 = new HashMap();

    private static final int id1 = 1;
    private static final String name1 = "Global";
    private static final String description1 = "Global chat for everyone";
    private static final Map<String, Object> restrictions1 = new HashMap<>();

    private static final int id2 = 2;
    private static final String name2 = "IN channel";
    private static final String description2 = "Channel just for the IN";
    private static final Map<String, Object> restrictions2 = new HashMap<>();

    private Channel channelGlobal;
    private Channel channelIN;
    private AuthenticatedUser userIN;
    private AuthenticatedUser userSC;

    @Before
    public void init() {
        User.UserBuilder builder = Utility.createTestUserBuilder();
        builder.setSection("IN");
        userIN = Utility.createTestCustomUser(builder);

        builder.setSection("SC");
        userSC = Utility.createTestCustomUser(builder);

        restrictions1.put("section", null);
        restrictions1.put("location", null);

        restrictions2.put("section", "IN");
        restrictions2.put("location", null);

        data1.put("id", id1);
        data1.put("name", name1);
        data1.put("description", description1);
        data1.put("restrictions", restrictions1);

        data2.put("id", id2);
        data2.put("name", name2);
        data2.put("description", description2);
        data2.put("restrictions", restrictions2);

        channelGlobal = new Channel(data1);
        channelIN = new Channel(data2);
    }

    @Test
    public void testGuettersAndSetters() {
        assertEquals(id1, channelGlobal.getId());
        assertEquals(name1, channelGlobal.getName());
        assertEquals(description1, channelGlobal.getDescription());

        channelGlobal.setId(id2);
        channelGlobal.setName(name2);
        channelGlobal.setDescription(description2);

        assertEquals(id2, channelGlobal.getId());
        assertEquals(name2, channelGlobal.getName());
        assertEquals(description2, channelGlobal.getDescription());
    }

    @Test
    public void testChannelWithRestrictions() {
        assertTrue(channelGlobal.canBeAccessedBy(userIN));
        assertTrue(channelGlobal.canBeAccessedBy(userSC));

        assertTrue(channelIN.canBeAccessedBy(userIN));
        assertFalse(channelIN.canBeAccessedBy(userSC));
    }
}
