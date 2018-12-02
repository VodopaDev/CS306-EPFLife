package ch.epfl.sweng.zuluzulu.Structure;

import android.net.Uri;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Date;

import ch.epfl.sweng.zuluzulu.R;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class EventBuilderTest {

    @Test(expected = IllegalArgumentException.class)
    public void nullIdThrowsException(){
        new EventBuilder().setId(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullNameThrowsException(){
        new EventBuilder().setName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullShortDescriptionThrowsException(){
        new EventBuilder().setShortDesc(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullLongDescThrowsException(){
        new EventBuilder().setLongDesc(null);
    }

    @Test
    public void longShortDescIsTrimmed(){
        final String NAME1 = "ForumEpfl";
        final String SHORT_DESC = "aaaaaaaaaaaaaaaa aaaaaaaaaaaaaaaaaaa aaaaaaaaaaaaaaaaaaaa aaaaaaaaaaaa aaaaaaaaaaaaaaaaaaaa aaaaaaaaaaaaaaaaaaaaaa aaaaaaaaaaaaaaaaaaaaaa aaaaaaaaaaaaaaaaaa aaaaaaaaaa aaaaaaaaaaaaaaaa aaaaaaaaaaaaaaaa aaaaaaaaaaaaa aaaaaaaaaaa aaaaaaaaaaaaaa";
        final String LONG_DESC = "This is only random bla bla bla";
        final Date START_DATE = new Date(2L);
        final int LIKES_1 = 100;
        final String ORGANIZER = "Pascal Martin";
        final String PLACE = "CE";
        final String CONTACT = "ME";
        final String SPEAKER = "DAHN";
        final String WEBSITE = "BRAZ";
        final String CATEGORY = "LES";
        final String URL_PLACE = "BRALES";
        final String ID = "1";
        final String ASSOCIATION_ID = "1";
        final String CHANNEL_ID = "1";

        EventBuilder eb = new EventBuilder()
                .setId(ID)
                .setDate(new EventDate(
                        START_DATE, START_DATE))
                .setUrlPlaceAndRoom(URL_PLACE)
                .setLikes(LIKES_1)
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
                .setChannelId(CHANNEL_ID)
                .setAssosId(ASSOCIATION_ID);
        assertTrue(SHORT_DESC.length() > eb.build().getShortDescription().length());
    }

    @Test
    public void testBuilder() {
        final String NAME1 = "ForumEpfl";
        final String SHORT_DESC = "Beuverie à Zelig";
        final String LONG_DESC = "This is only random bla bla bla";
        final Date START_DATE = new Date(2L);
        final int LIKES_1 = 100;
        final String ORGANIZER = "Pascal Martin";
        final String PLACE = "CE";
        final String CONTACT = "ME";
        final String SPEAKER = "DAHN";
        final String WEBSITE = "BRAZ";
        final String CATEGORY = "LES";
        final String URL_PLACE = "BRALES";
        final String ID = "1";
        final String ASSOCIATION_ID = "1";
        final String CHANNEL_ID = "1";

        EventBuilder eb = new EventBuilder()
                .setId(ID)
                .setDate(new EventDate(
                        START_DATE, START_DATE))
                .setUrlPlaceAndRoom(URL_PLACE)
                .setLikes(LIKES_1)
                .setShortDesc(SHORT_DESC)
                .setLongDesc(LONG_DESC)
                .setOrganizer(ORGANIZER)
                .setPlace(PLACE)
                .setBannerUri("normal.ch")
                .setIconUri("normal.ch")
                .setWebsite(WEBSITE)
                .setContact(CONTACT)
                .setName(NAME1)
                .setCategory(CATEGORY)
                .setSpeaker(SPEAKER)
                .setChannelId(CHANNEL_ID)
                .setAssosId(ASSOCIATION_ID);

        Event event0 = eb.build();
        assertEquals(ID, event0.getId());
        assertEquals(LIKES_1, event0.getLikes().intValue());
        assertEquals(NAME1, event0.getName());
        assertEquals(LONG_DESC, event0.getLongDescription());
        assertEquals(SHORT_DESC, event0.getShortDescription());
        assertEquals(START_DATE, event0.getStartDate());
        assertEquals(Uri.parse("normal.ch"), event0.getBannerUri());
        assertEquals(Uri.parse("normal.ch"), event0.getIconUri());
        assertEquals(ORGANIZER, event0.getOrganizer());
        assertEquals(PLACE, event0.getPlace());
        assertEquals(SPEAKER, event0.getSpeaker());
        assertEquals(CATEGORY, event0.getCategory());
        assertEquals(CONTACT, event0.getContact());
        assertEquals(WEBSITE, event0.getWebsite());
        assertEquals(URL_PLACE, event0.getUrlPlaceAndRoom());
        assertEquals(CHANNEL_ID, event0.getChannelId());
        assertEquals(ASSOCIATION_ID, event0.getAssociationId());
    }

    @Test
    public void nonConformUriAreChangedToDefaultUri(){
        final String DEFAULT_URI = "android.resource://ch.epfl.sweng.zuluzulu/" + R.drawable.default_icon;
        final String NAME1 = "ForumEpfl";
        final String SHORT_DESC = "aaaaaaaaaaaaaaaa aaaaaaaaaaaaaaaaaaa aaaaaaaaaaaaaaaaaaaa aaaaaaaaaaaa aaaaaaaaaaaaaaaaaaaa aaaaaaaaaaaaaaaaaaaaaa aaaaaaaaaaaaaaaaaaaaaa aaaaaaaaaaaaaaaaaa aaaaaaaaaa aaaaaaaaaaaaaaaa aaaaaaaaaaaaaaaa aaaaaaaaaaaaa aaaaaaaaaaa aaaaaaaaaaaaaa";
        final String LONG_DESC = "This is only random bla bla bla";
        final Date START_DATE = new Date(2L);
        final int LIKES_1 = 100;
        final String ORGANIZER = "Pascal Martin";
        final String PLACE = "CE";
        final String CONTACT = "ME";
        final String SPEAKER = "DAHN";
        final String WEBSITE = "BRAZ";
        final String CATEGORY = "LES";
        final String URL_PLACE = "BRALES";
        final String ID = "1";
        final String ASSOCIATION_ID = "1";
        final String CHANNEL_ID = "1";

        EventBuilder eb = new EventBuilder()
                .setId(ID)
                .setDate(new EventDate(
                        START_DATE, START_DATE))
                .setUrlPlaceAndRoom(URL_PLACE)
                .setLikes(LIKES_1)
                .setShortDesc(SHORT_DESC)
                .setLongDesc(LONG_DESC)
                .setOrganizer(ORGANIZER)
                .setPlace(PLACE)
                .setBannerUri("epfl")
                .setIconUri("epfl")
                .setWebsite(WEBSITE)
                .setContact(CONTACT)
                .setName(NAME1)
                .setCategory(CATEGORY)
                .setSpeaker(SPEAKER)
                .setChannelId(CHANNEL_ID)
                .setAssosId(ASSOCIATION_ID);
        Event event = eb.build();
        assertEquals(Uri.parse(DEFAULT_URI), event.getIconUri());
        assertEquals(Uri.parse(DEFAULT_URI), event.getBannerUri());

        eb.setBannerUri(null).setIconUri(null);
        event = eb.build();
        assertEquals(Uri.parse(DEFAULT_URI), event.getIconUri());
        assertEquals(Uri.parse(DEFAULT_URI), event.getBannerUri());
    }


}
