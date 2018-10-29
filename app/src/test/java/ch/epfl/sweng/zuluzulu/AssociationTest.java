package ch.epfl.sweng.zuluzulu;


import android.net.Uri;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.Structure.Association;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class AssociationTest {
    private static final String NAME1 = "Agepoly";
    private static final String NAME2 = "Bgepoly";

    private static final String SHORT_DESC = "Representing all students at EPFL";
    private static final String LONG_DESC = "Blah blah blah blah blah";

    private static final String TEST_URI_STRING = "https://firebasestorage.googleapis.com/v0/b/softdep-7cf7a.appspot.com/o/assos%2Fasso1_icon.png?alt=media&token=391a7bfc-1597-4935-9afe-e08ecd734e03";
    private static final Uri DEFAULT_BANNER_URI = Uri.parse("android.resource://ch.epfl.sweng.zuluzulu/" + R.drawable.default_banner);
    private static final Uri DEFAULT_ICON_URI = Uri.parse("android.resource://ch.epfl.sweng.zuluzulu/" + R.drawable.default_icon);

    private Association asso1;
    private Association asso2;


    private void initWorkingAssociation() {
        Map<String, Object> event1 = new HashMap<>();
        Date close_date = new Date(2020,10,2);
        event1.put("id", 1L);
        event1.put("start", close_date);

        Map<String, Object> event2 = new HashMap<>();
        Date far_date = new Date(2056,10,2);
        event2.put("id", 2L);
        event2.put("start", far_date);

        Map<String,Object> map = new HashMap<>();
        map.put("id",1L);
        map.put("name", NAME1);
        map.put("short_desc", SHORT_DESC);
        map.put("long_desc", LONG_DESC);
        map.put("icon_uri", TEST_URI_STRING);
        map.put("banner_uri", TEST_URI_STRING);
        map.put("channel_id", 1L);
        map.put("events", Arrays.asList(event2,event1));

        asso1 = new Association(new FirebaseMapDecorator(map));
    }

    private void initDefaultAssociation(){
        Map<String,Object> map = new HashMap<>();
        map.put("id",1L);
        map.put("name", NAME2);
        map.put("short_desc", SHORT_DESC);
        map.put("long_desc", LONG_DESC);

        asso2 = new Association(new FirebaseMapDecorator(map));
    }

    @Test(expected = IllegalArgumentException .class)
    public void invalidMapThrowIllegalArgumentException() {
        Map<String,Object> map = new HashMap<>();
        map.put("id", 1L);

        FirebaseMapDecorator fmap = new FirebaseMapDecorator(map);
        new Association(fmap);
    }

    @Test
    public void idIsCorrect() {
        initWorkingAssociation();
        assertEquals(1, asso1.getId());
    }

    @Test
    public void nameIsCorrect() {
        initWorkingAssociation();
        assertEquals(NAME1, asso1.getName());
    }

    @Test
    public void longDescIsCorrect() {
        initWorkingAssociation();
        assertEquals(LONG_DESC, asso1.getLongDesc());
    }

    @Test
    public void shortDescIsCorrect() {
        initWorkingAssociation();
        assertEquals(SHORT_DESC, asso1.getShortDesc());
    }

    @Test
    public void closestEventIsCorrect(){
        initWorkingAssociation();
        initDefaultAssociation();
        assertThat(0, equalTo(asso2.getClosestEventId()));
        assertThat(1, equalTo(asso1.getClosestEventId()));
    }

    @Test
    public void iconUriIsCorrect() {
        initWorkingAssociation();
        initDefaultAssociation();
        assertEquals(Uri.parse(TEST_URI_STRING), asso1.getIconUri());
        assertEquals(DEFAULT_ICON_URI, asso2.getIconUri());
    }

    @Test
    public void bannerUriIsCorrect() {
        initWorkingAssociation();
        initDefaultAssociation();
        assertEquals(Uri.parse(TEST_URI_STRING), asso1.getBannerUri());
        assertEquals(DEFAULT_BANNER_URI, asso2.getBannerUri());
    }

    @Test
    public void comparableToIsCorrect() {
        initWorkingAssociation();
        initDefaultAssociation();
        assertEquals(NAME1.compareTo(NAME2),
                Association.getComparator().compare(asso1, asso2));
    }

    @Test
    public void channelIdIsCorrect(){
        initWorkingAssociation();
        initDefaultAssociation();
        assertThat(asso1.getChannelId(), equalTo(1));
        assertThat(asso2.getChannelId(), equalTo(0));
    }


}
