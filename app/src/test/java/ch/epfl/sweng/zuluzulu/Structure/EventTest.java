package ch.epfl.sweng.zuluzulu.Structure;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.Structure.Event;
import ch.epfl.sweng.zuluzulu.Structure.EventBuilder;
import ch.epfl.sweng.zuluzulu.Structure.EventDate;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(JUnit4.class)
public class EventTest {
    private static final String NAME1 = "ForumEpfl";

    private static final String SHORT_DESC = "Beuverie à Zelig";
    private static final String LONG_DESC = "This is only random bla bla bla";
    private static final String TEST_URI_STRING = "https://firebasestorage.googleapis.com/v0/b/softdep-7cf7a.appspot.com/o/assos%2Fasso1_icon.png?alt=media&token=391a7bfc-1597-4935-9afe-e08ecd734e03";
    private static final Date START_DATE = new Date(2L);
    private static final Long LIKES_1 = 100L;
    private static final String ORGANIZER = "Pascal Martin";
    private static final String PLACE = "CE";
    private static final String CONTACT = "ME";
    private static final String SPEAKER = "DAHN";
    private static final String WEBSITE = "BRAZ";
    private static final String CATEGORY = "LES";
    private static final String URL_PLACE = "BRALES";
    private static final String ID = "1";
    private static final String ASSOCIATION_ID = "1";
    private static final String CHANNEL_ID = "1";


    private Event event0;

    private void initWorkingAssociation() {

        Map<String, Object> map = new HashMap<>();
        map.put("id", ID);
        map.put("channel_id", CHANNEL_ID);
        map.put("association_id", ASSOCIATION_ID);
        map.put("name", NAME1);
        map.put("short_description", SHORT_DESC);
        map.put("long_description", LONG_DESC);
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

    @Test(expected = IllegalArgumentException.class)
    public void invalidSnapThrowIllegalArgumentException() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", ID);

        FirebaseMapDecorator fmap = new FirebaseMapDecorator(map);
        new Event(fmap);
    }

    @Test
    public void idIsCorrect() {
        initWorkingAssociation();
        assertThat(event0.getId(), equalTo("1"));
    }

    @Test
    public void likesAreCorrect() {
        initWorkingAssociation();
        assertEquals(100, event0.getLikes().intValue());
    }

    @Test
    public void nameIsCorrect() {
        initWorkingAssociation();
        assertEquals(NAME1, event0.getName());
    }

    @Test
    public void longDescIsCorrect() {
        initWorkingAssociation();
        assertEquals(LONG_DESC, event0.getLongDescription());
    }

    @Test
    public void shortDescIsCorrect() {
        initWorkingAssociation();
        assertEquals(SHORT_DESC, event0.getShortDescription());
    }

    @Test
    public void uriAreCorrect() {
        initWorkingAssociation();
        assertEquals(null, event0.getIconUri());
        assertEquals(null, event0.getBannerUri());
        // always null in tests since Uri.parse is android function
        // doesn't work in tests
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
        assertEquals(NAME1.compareTo(NAME1),
                Event.nameComparator().compare(event0, event0));
    }

    @Test
    public void dateComparTest() {
        initWorkingAssociation();
        assertEquals(START_DATE.compareTo(START_DATE), Event.dateComparator().compare(event0, event0));
    }

    @Test
    public void likesComparTest() {
        initWorkingAssociation();
        assertEquals(0, Event.likeComparator().compare(event0, event0));
    }

    @Test
    public void increaseLikesTest() {
        initWorkingAssociation();
        event0.increaseLikes();
        assertEquals((LIKES_1.intValue()) + 1, event0.getLikes().intValue());
    }

    @Test
    public void decreaseLikesTest() {
        initWorkingAssociation();
        event0.decreaseLikes();
        assertEquals((LIKES_1.intValue()) - 1, event0.getLikes().intValue());
    }

    @Test
    public void organizerIsCorrect() {
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
        EventBuilder eb = new EventBuilder()
                .setId("0")
                .setDate(new EventDate(
                        START_DATE, START_DATE))
                .setUrlPlaceAndRoom(URL_PLACE)
                .setLikes(0)
                .setShortDesc(SHORT_DESC)
                .setLongDesc(LONG_DESC)
                .setOrganizer(ORGANIZER)
                .setPlace(PLACE)
                .setBannerUri("epfl.ch")
                .setIconUri("epfl.ch")
                .setWebsite(WEBSITE)
                .setContact(CONTACT)
                .setName(NAME1)
                .setCategory(CATEGORY)
                .setSpeaker(SPEAKER)
                .setChannelId("0")
                .setAssosId("0");

        Event event0 = eb.build();


        assertEquals("0", event0.getId());

        assertEquals(0, (int) event0.getLikes());

        assertEquals(NAME1, event0.getName());

        assertEquals(LONG_DESC, event0.getLongDescription());

        assertEquals(SHORT_DESC, event0.getShortDescription());

        assertEquals(START_DATE, event0.getStartDate());

        assertEquals(ORGANIZER, event0.getOrganizer());

        assertEquals(PLACE, event0.getPlace());

        assertEquals(SPEAKER, event0.getSpeaker());

        assertEquals(CATEGORY, event0.getCategory());

        assertEquals(CONTACT, event0.getContact());

        assertEquals(WEBSITE, event0.getWebsite());

        assertEquals(URL_PLACE, event0.getUrlPlaceAndRoom());

        assertEquals("0", event0.getChannelId());

        assertEquals("0", event0.getAssociationId());
    }

}
