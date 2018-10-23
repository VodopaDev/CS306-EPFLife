package ch.epfl.sweng.zuluzulu;

import android.net.Uri;
import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

//import java.util.Date;

import java.text.SimpleDateFormat;
import java.util.Date;

import ch.epfl.sweng.zuluzulu.Structure.Event;

@RunWith(JUnit4.class)
public class EventTest {
    private static final String NAME1 = "forumEpfl  ";
    private static final String NAME2 = "JuniorEnterprise";

    private static final String SHORT_DESC = "Beuverie à Zelig";
    private static final String LONG_DESC = "This is only random bla bla bla";
    private static final String ICON_URI_STRING = "https://firebasestorage.googleapis.com/v0/b/softdep-7cf7a.appspot.com/o/assos%2Fasso4_icon.png?alt=media&token=e2c5206d-32a6-43e1-a9a8-da941f553e64";
    private static final Date START_DATE = new Date();

    private Event event0;
    private Event event1;
    private final Uri mocked_icon_uri = Mockito.mock(Uri.class);
    private final DocumentSnapshot mocked_valid_datasnap1 = Mockito.mock(DocumentSnapshot.class);
    private final DocumentSnapshot mocked_valid_datasnap2 = Mockito.mock(DocumentSnapshot.class);
    private final DocumentSnapshot mocked_invalid_datasnap = Mockito.mock(DocumentSnapshot.class);

    @Before
    public void init(){
        Mockito.when(mocked_icon_uri.getPath()).thenReturn(ICON_URI_STRING);

        Mockito.when(mocked_valid_datasnap1.getString("name")).thenReturn(NAME1);
        Mockito.when(mocked_valid_datasnap1.getString("long_desc")).thenReturn(LONG_DESC);
        Mockito.when(mocked_valid_datasnap1.getString("short_desc")).thenReturn(SHORT_DESC);
        Mockito.when(mocked_valid_datasnap1.getString("icon_uri")).thenReturn(ICON_URI_STRING);
        Mockito.when(mocked_valid_datasnap1.getDate("start_date")).thenReturn(START_DATE);
        Mockito.when(mocked_valid_datasnap1.get("id")).thenReturn(1L);

        Mockito.when(mocked_valid_datasnap2.getString("name")).thenReturn(NAME2);
        Mockito.when(mocked_valid_datasnap2.getString("long_desc")).thenReturn(LONG_DESC);
        Mockito.when(mocked_valid_datasnap2.getString("short_desc")).thenReturn(SHORT_DESC);
        Mockito.when(mocked_valid_datasnap2.getString("icon_uri")).thenReturn(ICON_URI_STRING);
        Mockito.when(mocked_valid_datasnap2.get("id")).thenReturn(1L);
        Mockito.when(mocked_valid_datasnap2.getDate("start_date")).thenReturn(START_DATE);

        Mockito.when(mocked_invalid_datasnap.getString("name")).thenReturn(NAME1);
        Mockito.when(mocked_invalid_datasnap.getString("long_desc")).thenReturn(null);
        Mockito.when(mocked_invalid_datasnap.getString("short_desc")).thenReturn(SHORT_DESC);
        Mockito.when(mocked_invalid_datasnap.getString("icon_uri")).thenReturn(null);
        Mockito.when(mocked_invalid_datasnap.get("id")).thenReturn(1L);
        Mockito.when(mocked_invalid_datasnap.getDate("start_date")).thenReturn(START_DATE);

        event0 = new Event(mocked_valid_datasnap1);
        event1 = new Event(mocked_valid_datasnap2);
    }

    @Test(expected = NullPointerException.class)
    public void invalidSnapThrowIllegalArgumentException(){
        new Event(mocked_invalid_datasnap);
    }

    @Test
    public void validSnapNoThrowException(){ new Event(mocked_valid_datasnap1); }

    @Test
    public void idIsCorrect(){
        assertEquals(1, event0.getId());
    }

    @Test
    public void nameIsCorrect(){
        assertEquals(NAME1, event0.getName());
    }

    @Test
    public void longDescIsCorrect(){
        assertEquals(LONG_DESC, event0.getLong_desc());
    }

    @Test
    public void shortDescIsCorrect(){
        assertEquals(SHORT_DESC, event0.getShort_desc());
    }

    @Test
    public void uriIsCorrect(){
        assertEquals(Uri.parse(ICON_URI_STRING), event0.getIconUri());
    }

    @Test
    public void dateIsCorrect() {
        assert START_DATE.equals(event0.getStart_date());
    }

    @Test
    public void comparableToIsCorrect(){
        assertEquals(NAME1.compareTo(NAME2),
                Event.getComparator().compare(event0,event1));
    }

}
