package ch.epfl.sweng.zuluzulu;


import android.net.Uri;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import ch.epfl.sweng.zuluzulu.Structure.Association;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class AssociationTest {
    private static final String NAME1 = "Agepoly";
    private static final String NAME2 = "Bgepoly";

    private static final String SHORT_DESC = "Representing all students at EPFL";
    private static final String LONG_DESC = "Blah blah blah blah blah";

    private static final String TEST_URI_STRING = "https://firebasestorage.googleapis.com/v0/b/softdep-7cf7a.appspot.com/o/assos%2Fasso1_icon.png?alt=media&token=391a7bfc-1597-4935-9afe-e08ecd734e03";
    private static final Uri DEFAULT_BANNER_URI = Uri.parse("android.resource://ch.epfl.sweng.zuluzulu/" + R.drawable.default_banner);
    private static final Uri DEFAULT_ICON_URI = Uri.parse("android.resource://ch.epfl.sweng.zuluzulu/" + R.drawable.default_icon);

    private final DocumentSnapshot mocked_valid_datasnap = mock(DocumentSnapshot.class);
    private final DocumentSnapshot mocked_default_datasnap = mock(DocumentSnapshot.class);
    private final DocumentSnapshot mocked_invalid_datasnap = mock(DocumentSnapshot.class);
    private Association asso1;
    private Association asso2;

    @Before
    public void init() {
        Map<String, Object> map1 = new HashMap<>();
        Date close_date = new Date(2020,10,2);
        map1.put("id", 1L);
        map1.put("start", close_date);

        Map<String, Object> map2 = new HashMap<>();
        Date far_date = new Date(2056,10,2);
        map2.put("id", 2L);
        map2.put("start", far_date);

        when(mocked_valid_datasnap.getString("name")).thenReturn(NAME1);
        when(mocked_valid_datasnap.getString("long_desc")).thenReturn(LONG_DESC);
        when(mocked_valid_datasnap.getString("short_desc")).thenReturn(SHORT_DESC);
        when(mocked_valid_datasnap.getString("icon_uri")).thenReturn(TEST_URI_STRING);
        when(mocked_valid_datasnap.getString("banner_uri")).thenReturn(TEST_URI_STRING);
        when(mocked_valid_datasnap.getLong("id")).thenReturn(1L);
        when(mocked_valid_datasnap.get("name")).thenReturn(NAME1);
        when(mocked_valid_datasnap.get("long_desc")).thenReturn(LONG_DESC);
        when(mocked_valid_datasnap.get("short_desc")).thenReturn(SHORT_DESC);
        when(mocked_valid_datasnap.get("id")).thenReturn(1L);
        when(mocked_valid_datasnap.get("events")).thenReturn(Arrays.asList(map2, map1));

        when(mocked_default_datasnap.getString("name")).thenReturn(NAME2);
        when(mocked_default_datasnap.getString("long_desc")).thenReturn(LONG_DESC);
        when(mocked_default_datasnap.getString("short_desc")).thenReturn(SHORT_DESC);
        when(mocked_default_datasnap.getString("icon_uri")).thenReturn(null);
        when(mocked_default_datasnap.getString("banner_uri")).thenReturn(null);
        when(mocked_default_datasnap.getLong("id")).thenReturn(1L);
        when(mocked_default_datasnap.get("name")).thenReturn(NAME2);
        when(mocked_default_datasnap.get("long_desc")).thenReturn(LONG_DESC);
        when(mocked_default_datasnap.get("short_desc")).thenReturn(SHORT_DESC);
        when(mocked_default_datasnap.get("id")).thenReturn(1L);
        when(mocked_default_datasnap.get("events")).thenReturn(new ArrayList<Map<String, Object>>());

        when(mocked_invalid_datasnap.get("name")).thenReturn(null);
        when(mocked_invalid_datasnap.get("long_desc")).thenReturn(null);
        when(mocked_invalid_datasnap.get("short_desc")).thenReturn(null);
        when(mocked_invalid_datasnap.get("id")).thenReturn(null);

        asso1 = new Association(mocked_valid_datasnap);
        asso2 = new Association(mocked_default_datasnap);
    }

    @Test(expected = IllegalArgumentException .class)
    public void invalidSnapThrowIllegalArgumentException() {
        new Association(mocked_invalid_datasnap);
    }

    @Test
    public void validSnapNoThrowException() {
        new Association(mocked_valid_datasnap);
    }

    @Test
    public void idIsCorrect() {
        assertEquals(1, asso1.getId());
    }

    @Test
    public void nameIsCorrect() {
        assertEquals(NAME1, asso1.getName());
    }

    @Test
    public void longDescIsCorrect() {
        assertEquals(LONG_DESC, asso1.getLongDesc());
    }

    @Test
    public void shortDescIsCorrect() {
        assertEquals(SHORT_DESC, asso1.getShortDesc());
    }

    @Test
    public void closestEventIsCorrect(){
        assertThat(0, equalTo(asso2.getClosestEventId()));
        assertThat(1, equalTo(asso1.getClosestEventId()));
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
                Association.getComparator().compare(asso1, asso2));
    }

}
