package ch.epfl.sweng.zuluzulu;

import android.net.Uri;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.Structure.Event;
import ch.epfl.sweng.zuluzulu.Structure.EventDate;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class EventTest {
    private static final String NAME1 = "ForumEpfl";
    private static final String NAME2 = "JuniorEnterprise";

    private static final String SHORT_DESC = "Beuverie à Zelig";
    private static final String LONG_DESC = "This is only random bla bla bla";
    private static final String TEST_URI_STRING = "https://firebasestorage.googleapis.com/v0/b/softdep-7cf7a.appspot.com/o/assos%2Fasso1_icon.png?alt=media&token=391a7bfc-1597-4935-9afe-e08ecd734e03";
    private static final Uri DEFAULT_BANNER_URI = Uri.parse("android.resource://ch.epfl.sweng.zuluzulu/" + R.drawable.default_banner);
    private static final Uri DEFAULT_ICON_URI = Uri.parse("android.resource://ch.epfl.sweng.zuluzulu/" + R.drawable.default_icon);
    private static final Date START_DATE = new Date(2L);
    private static final Date START_DATE_1 = new Date(3L);
    private static final long LIKES_1 = 100L;
    private static final long LIKES_2 = 3L;
    private static final String ORGANIZER = "Pascal Martin";
    private static final String PLACE = "CE";
    private static final String CONTACT = "ME";
    private static final String SPEAKER = "DAHN";
    private static final String WEBSITE = "BRAZ";
    private static final String CATEGORY = "LES";
    private static final String URL_PLACE = "BRALES";


    private Event event0;
    private Event event1;

    private void initWorkingAssociation() {

        Map<String, Object> map = new HashMap<>();
        map.put("id", 1L);
        map.put("channel_id", 1L);
        map.put("name", NAME1);
        map.put("short_desc", SHORT_DESC);
        map.put("long_desc", LONG_DESC);
        map.put("icon_uri", TEST_URI_STRING);
        map.put("banner_uri", TEST_URI_STRING);
        map.put("start_date", START_DATE);
        map.put("end_date", START_DATE);
        map.put("likes", LIKES_1);
        map.put("organizer", ORGANIZER);
        map.put("place", PLACE);
        map.put("contact", CONTACT);
        map.put("speaker", SPEAKER);
        map.put("website", WEBSITE);
        map.put("category", CATEGORY);
        map.put("url_place_and_room", URL_PLACE);

        event0 = new Event(new FirebaseMapDecorator(map));
    }

    private void initDefaultAssociation() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1L);
        map.put("channel_id", 1L);
        map.put("name", NAME2);
        map.put("short_desc", SHORT_DESC);
        map.put("long_desc", LONG_DESC);
        map.put("start_date", START_DATE_1);
        map.put("end_date", START_DATE_1);
        map.put("likes", LIKES_2);
        map.put("contact", CONTACT);
        map.put("speaker", SPEAKER);
        map.put("website", WEBSITE);
        map.put("category", CATEGORY);
        map.put("url_place_and_room", URL_PLACE);

        event1 = new Event(new FirebaseMapDecorator(map));
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidSnapThrowIllegalArgumentException() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1L);

        FirebaseMapDecorator fmap = new FirebaseMapDecorator(map);
        new Event(fmap);
    }

    @Test
    public void idIsCorrect() {
        initWorkingAssociation();
        assertEquals(1, event0.getId());
    }

    @Test
    public void likesAreCorrect() {
        initWorkingAssociation();
        assertEquals(100, (int) event0.getLikes());
    }

    @Test
    public void nameIsCorrect() {
        initWorkingAssociation();
        assertEquals(NAME1, event0.getName());
    }

    @Test
    public void longDescIsCorrect() {
        initWorkingAssociation();
        assertEquals(LONG_DESC, event0.getLongDesc());
    }

    @Test
    public void shortDescIsCorrect() {
        initWorkingAssociation();
        assertEquals(SHORT_DESC, event0.getShortDesc());
    }

    @Test
    public void uriAreCorrect() {
        initWorkingAssociation();
        initDefaultAssociation();
        assertEquals(Uri.parse(TEST_URI_STRING), event0.getIconUri());
        assertEquals(Uri.parse(TEST_URI_STRING), event0.getBannerUri());
        assertEquals(DEFAULT_ICON_URI, event1.getIconUri());
        assertEquals(DEFAULT_BANNER_URI, event1.getBannerUri());
    }

    @Test
    public void startDateIsCorrect() {
        initWorkingAssociation();
        assertEquals(START_DATE, event0.getStartDate());
        assertEquals(START_DATE, event0.getEndDate());
    }

    @Test
    public void comparableToIsCorrect() {
        initWorkingAssociation();
        initDefaultAssociation();
        assertEquals(NAME1.compareTo(NAME2),
                Event.assoNameComparator().compare(event0, event1));
    }

    @Test
    public void dateComparTest () {
        initWorkingAssociation();
        initDefaultAssociation();
        assertEquals(START_DATE.compareTo(START_DATE_1), Event.dateComparator().compare(event0, event1));
    }

    @Test
    public void likesComparTest () {
        initWorkingAssociation();
        initDefaultAssociation();
        assertEquals(-1, Event.likeComparator().compare(event0, event1));
    }

    @Test
    public void increaseLikesTest () {
        initWorkingAssociation();
        event0.increaseLikes();
        assertEquals(((int) LIKES_1) + 1, (int) event0.getLikes());
    }

    @Test
    public void decreaseLikesTest () {
        initWorkingAssociation();
        event0.decreaseLikes();
        assertEquals(((int) LIKES_1) - 1, (int) event0.getLikes());
    }

    @Test
    public void organizorIsCorrect() {
        initWorkingAssociation();
        assertEquals(ORGANIZER, event0.getOrganizer());
    }

    @Test
    public void placeIsCorrect() {
        initWorkingAssociation();
        assertEquals(PLACE, event0.getPlace());
    }

    @Test
    public void speakerIsCorrect() {
        initWorkingAssociation();
        assertEquals(SPEAKER, event0.getSpeaker());
    }
    @Test
    public void categoryIsCorrect() {
        initWorkingAssociation();
        assertEquals(CATEGORY, event0.getCategory());
    }
    @Test
    public void contactIsCorrect() {
        initWorkingAssociation();
        assertEquals(CONTACT, event0.getContact());
    }
    @Test
    public void websiteIsCorrect() {
        initWorkingAssociation();
        assertEquals(WEBSITE, event0.getWebsite());
    }
    @Test
    public void placeUrlIsCorrect() {
        initWorkingAssociation();
        assertEquals(URL_PLACE, event0.getUrlPlaceAndRoom());
    }

    @Test
    public void testBuilder() {
        Event.EventBuilder eb = new Event.EventBuilder()
                .setId(1)
                .setDate(new EventDate(
                        START_DATE, START_DATE))
                .setUrlPlaceAndRoom(URL_PLACE)
                .setLikes(      0)
                .setShortDesc(  SHORT_DESC)
                .setLongDesc(   LONG_DESC)
                .setOrganizer(  ORGANIZER)
                .setPlace(      PLACE)
                .setBannerUri(  "epfl.ch")
                .setIconUri(    "epfl.ch")
                .setWebsite(    WEBSITE)
                .setContact(    CONTACT)
                .setName(       NAME1)
                .setCategory(   CATEGORY)
                .setSpeaker(    SPEAKER)
                .setChannelId(  0)
                .setAssosId(    0);

        Event event0 = eb.build();


        assertEquals(1, event0.getId());

        assertEquals(0, (int) event0.getLikes());

        assertEquals(NAME1, event0.getName());

        assertEquals(LONG_DESC, event0.getLongDesc());

        assertEquals(SHORT_DESC, event0.getShortDesc());

        assertEquals(START_DATE, event0.getStartDate());

        assertEquals(ORGANIZER, event0.getOrganizer());

        assertEquals(PLACE, event0.getPlace());

        assertEquals(SPEAKER, event0.getSpeaker());

        assertEquals(CATEGORY, event0.getCategory());

        assertEquals(CONTACT, event0.getContact());

        assertEquals(WEBSITE, event0.getWebsite());

        assertEquals(URL_PLACE, event0.getUrlPlaceAndRoom());

        assertEquals(0, event0.getChannelId());

        assertEquals(0, event0.getAssosId());
    }

}
