package ch.epfl.sweng.zuluzulu;

import com.google.firebase.firestore.GeoPoint;

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
    private static Map data3 = new HashMap();

    private static final Long id1 = 1l;
    private static final String name1 = "Global";
    private static final String description1 = "Global chat for everyone";
    private static final Map<String, Object> restrictions1 = new HashMap<>();

    private static final Long id2 = 2l;
    private static final String name2 = "IN channel";
    private static final String description2 = "Channel just for the IN";
    private static final Map<String, Object> restrictions2 = new HashMap<>();

    private static final Long id3 = 3l;
    private static final String name3 = "SAT channel";
    private static final String description3 = "The only chat that matters";
    private static final Map<String, Object> restrictions3 = new HashMap<>();

    private Channel channelGlobal;
    private Channel channelIN;
    private Channel channelSAT;

    private AuthenticatedUser userIN;
    private AuthenticatedUser userSC;

    private GeoPoint SATPoint;
    private GeoPoint nullPoint;

    @Before
    public void init() {
        User.UserBuilder builder = Utility.createTestUserBuilder();
        builder.setSection("IN");
        userIN = Utility.createTestCustomUser(builder);

        builder.setSection("SC");
        userSC = Utility.createTestCustomUser(builder);

        SATPoint = new GeoPoint(46.520562, 6.567852);
        nullPoint = new GeoPoint(0, 0);

        restrictions1.put("section", null);
        restrictions1.put("location", null);

        restrictions2.put("section", "IN");
        restrictions2.put("location", null);

        restrictions3.put("section", null);
        restrictions3.put("location", SATPoint);

        data1.put("id", id1);
        data1.put("name", name1);
        data1.put("description", description1);
        data1.put("restrictions", restrictions1);

        data2.put("id", id2);
        data2.put("name", name2);
        data2.put("description", description2);
        data2.put("restrictions", restrictions2);

        data3.put("id", id3);
        data3.put("name", name3);
        data3.put("description", description3);
        data3.put("restrictions", restrictions3);

        channelGlobal = new Channel(data1);
        channelIN = new Channel(data2);
        channelSAT = new Channel(data3);
    }

    @Test
    public void testGettersAndSetters() {
        assertEquals(id1.intValue(), channelGlobal.getId());
        assertEquals(name1, channelGlobal.getName());
        assertEquals(description1, channelGlobal.getDescription());

        channelGlobal.setId(id2.intValue());
        channelGlobal.setName(name2);
        channelGlobal.setDescription(description2);

        assertEquals(id2.intValue(), channelGlobal.getId());
        assertEquals(name2, channelGlobal.getName());
        assertEquals(description2, channelGlobal.getDescription());
    }

    @Test
    public void testChannelWithSectionRestriction() {
        assertTrue(channelGlobal.canBeAccessedBy(userIN, nullPoint));
        assertTrue(channelGlobal.canBeAccessedBy(userSC, nullPoint));

        assertTrue(channelIN.canBeAccessedBy(userIN, nullPoint));
        assertFalse(channelIN.canBeAccessedBy(userSC, nullPoint));
    }

    @Test
    public void testChannelWithDistanceRestriction() {
        assertTrue(channelSAT.canBeAccessedBy(userIN, SATPoint));
        assertFalse(channelSAT.canBeAccessedBy(userIN, nullPoint));
    }
}
