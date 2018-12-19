package ch.epfl.sweng.zuluzulu.structure;

import android.net.Uri;

import com.google.firebase.firestore.GeoPoint;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.R;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class ChannelTest {
    private static final String id1 = "1";
    private static final String name1 = "Global";
    private static final String description1 = "Global chat for everyone";
    private static final Map<String, Object> restrictions1 = new HashMap<>();

    private static final String id2 = "2";
    private static final String name2 = "IN channel";
    private static final String description2 = "Channel just for the IN";
    private static final Map<String, Object> restrictions2 = new HashMap<>();

    private static final String id3 = "3";
    private static final String name3 = "SAT channel";
    private static final String description3 = "The only chat that matters";
    private static final Map<String, Object> restrictions3 = new HashMap<>();


    final GeoPoint SATPoint = new GeoPoint(46.520562, 6.567852);
    final GeoPoint nullPoint = new GeoPoint(0, 0);

    private Channel channelGlobal;
    private Channel channelIN;
    private Channel channelSAT;

    private final String section1 = "IN";

    @Before
    public void init() {

        restrictions1.put("section", null);
        restrictions1.put("location", null);

        restrictions2.put("section", "IN");
        restrictions2.put("location", null);

        restrictions3.put("section", null);
        restrictions3.put("location", SATPoint);

        channelGlobal = new Channel(
                id1,
                name1,
                description1,
                restrictions1,
                null);
        channelIN = new Channel(
                id2,
                name2,
                description2,
                restrictions2,
                "test_uri");
        channelSAT = new Channel(
                id3,
                name3,
                description3,
                restrictions3,
                "test_uri");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadMapForConstructor() {
        new Channel(new FirebaseMapDecorator(Collections.singletonMap("id", "100")));
    }

    @Test
    public void fmapConstructorTest() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id1);
        map.put("name", name1);
        map.put("short_description", description1);
        map.put("restrictions", restrictions1);
        map.put("icon_uri", "test");
        Map<String, Object> result = new Channel(new FirebaseMapDecorator(map)).getData();
        assertEquals(map, result);
    }

    @Test
    public void testGetters() {
        assertEquals(id1, channelGlobal.getId());
        assertEquals(name1, channelGlobal.getName());
        assertEquals(description1, channelGlobal.getShortDescription());
        assertEquals(Uri.parse("test_uri"), channelIN.getIconUri());
        assertEquals(Uri.parse("android.resource://ch.epfl.sweng.zuluzulu/" + R.drawable.default_icon), channelGlobal.getIconUri());

        assertTrue(channelIN.isAccessible());
        assertThat(channelGlobal.getDistance(), equalTo(0d));
    }

    @Test
    public void testChannelWithSectionRestriction() {
        assertTrue(channelGlobal.canBeSeenBy(section1, nullPoint));
        String section2 = "SC";
        assertTrue(channelGlobal.canBeSeenBy(section2, nullPoint));

        assertFalse(channelIN.canBeSeenBy(null, nullPoint));
        assertTrue(channelIN.canBeSeenBy(section1, nullPoint));
        assertFalse(channelIN.canBeSeenBy(section2, nullPoint));
    }

    @Test
    public void testChannelWithDistanceRestriction() {
        assertTrue(channelSAT.canBeSeenBy(section1, SATPoint));
        assertFalse(channelSAT.canBeSeenBy(section1, nullPoint));
    }
}
