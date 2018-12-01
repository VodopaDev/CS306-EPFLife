package ch.epfl.sweng.zuluzulu.Structure;


import android.net.Uri;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.Structure.Association;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(JUnit4.class)
public class AssociationTest {

    private static final String ID1 = "0";
    private static final String NAME1 = "Agepoly";

    private static final String ID2 = "1";
    private static final String NAME2 = "Bgepoly";
    private static List<Map<String, Object>> EVENT2 = new ArrayList<>();

    private static final String CHANNEL_ID = "000";
    private static final String SHORT_DESC = "Representing all students at EPFL";
    private static final String LONG_DESC = "Blah blah blah blah blah";

    private static final String TEST_URI_STRING = "test";
    private static final Uri DEFAULT_BANNER_URI = Uri.parse("android.resource://ch.epfl.sweng.zuluzulu/" + R.drawable.default_banner);
    private static final Uri DEFAULT_ICON_URI = Uri.parse("android.resource://ch.epfl.sweng.zuluzulu/" + R.drawable.default_icon);

    private Association asso1;
    private Association asso2;

    @Before
    public void init() {
        asso1 = new Association(
                ID1,
                NAME1,
                SHORT_DESC,
                LONG_DESC,
                TEST_URI_STRING,
                TEST_URI_STRING,
                Collections.EMPTY_LIST,
                CHANNEL_ID
        );

        HashMap<String,Object> map1 = new HashMap<>();
        map1.put("id", 200);
        map1.put("start", new Date(1000));
        HashMap<String,Object> map2 = new HashMap<>();
        map2.put("id", 188);
        map2.put("start", new Date(100));
        EVENT2.add(map1);
        EVENT2.add(map2);

        asso1 = new Association(
                ID2,
                NAME2,
                SHORT_DESC,
                LONG_DESC,
                null,
                null,
                EVENT2,
                CHANNEL_ID
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidMapThrowIllegalArgumentException() {
        new Association(new FirebaseMapDecorator(Collections.singletonMap("id", "lol")));
    }

    @Test
    public void fmapConstructorTest(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("id",ID1);
        map.put("name", NAME1);
        map.put("short_description", SHORT_DESC);
        map.put("long_description", LONG_DESC);
        map.put("icon_uri", TEST_URI_STRING);
        map.put("banner_uri", TEST_URI_STRING);
        map.put("channel_id", CHANNEL_ID);
        map.put("upcoming_events", EVENT2);

        assertThat(new Association(new FirebaseMapDecorator(map)).getData(), equalTo(map));
    }

    @Test
    public void idIsCorrect() {
        assertThat(asso1.getId(), equalTo(ID1));
    }

    @Test
    public void nameIsCorrect() {
        assertEquals(NAME1, asso1.getName());
    }

    @Test
    public void longDescIsCorrect() {
        assertEquals(LONG_DESC, asso1.getLongDescription());
    }

    @Test
    public void shortDescIsCorrect() {
        assertEquals(SHORT_DESC, asso1.getShortDescription());
    }

    @Test
    public void closestEventIsCorrect() {
        assertThat(asso2.getClosestEventId(), equalTo("188"));
        assertThat(asso1.getClosestEventId(), equalTo(null));
    }

    @Test
    public void iconUriIsCorrect() {
        assertEquals(Uri.parse(TEST_URI_STRING), asso1.getIconUri());
        assertEquals(DEFAULT_ICON_URI, asso2.getIconUri());
    }

    @Test
    public void bannerUriIsCorrect() {
        assertEquals(Uri.parse(TEST_URI_STRING), asso1.getBannerUri());
        assertEquals(DEFAULT_BANNER_URI, asso2.getBannerUri());
    }

    @Test
    public void comparableToIsCorrect() {
        assertEquals(NAME1.compareTo(NAME2),
                asso1.compareTo(asso2));
    }

    @Test
    public void channelIdIsCorrect() {
        assertThat(asso1.getChannelId(), equalTo(CHANNEL_ID));
    }


}
