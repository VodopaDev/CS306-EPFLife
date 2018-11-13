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

import static org.junit.Assert.assertEquals;

//import java.util.Date;

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

    private Event event0;
    private Event event1;

    private void initWorkingAssociation() {

        Map<String, Object> map = new HashMap<>();
        map.put("id", 1L);
        map.put("name", NAME1);
        map.put("short_desc", SHORT_DESC);
        map.put("long_desc", LONG_DESC);
        map.put("icon_uri", TEST_URI_STRING);
        map.put("banner_uri", TEST_URI_STRING);
        map.put("start_date", START_DATE);

        event0 = new Event(new FirebaseMapDecorator(map));
    }

    private void initDefaultAssociation() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1L);
        map.put("name", NAME2);
        map.put("short_desc", SHORT_DESC);
        map.put("long_desc", LONG_DESC);
        map.put("start_date", START_DATE);

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
    }

    @Test
    public void comparableToIsCorrect() {
        initWorkingAssociation();
        initDefaultAssociation();
        assertEquals(NAME1.compareTo(NAME2),
                Event.assoNameComparator().compare(event0, event1));
    }

}
