package ch.epfl.sweng.zuluzulu;


import android.net.Uri;
import com.google.firebase.firestore.DocumentSnapshot;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import ch.epfl.sweng.zuluzulu.Structure.Association;

@RunWith(JUnit4.class)
public class AssociationTest {
    private static final String NAME1 = "Agepoly";
    private static final String NAME2 = "Bgepoly";

    private static final String SHORT_DESC = "Representing all students at EPFL";
    private static final String LONG_DESC = "Blah blah blah blah blah";
    private static final String ICON_URI_STRING = "https://firebasestorage.googleapis.com/v0/b/softdep-7cf7a.appspot.com/o/assos%2Fasso1_icon.png?alt=media&token=391a7bfc-1597-4935-9afe-e08ecd734e03";

    private Association asso1;
    private Association asso2;
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
        Mockito.when(mocked_valid_datasnap1.get("id")).thenReturn(1L);

        Mockito.when(mocked_valid_datasnap2.getString("name")).thenReturn(NAME2);
        Mockito.when(mocked_valid_datasnap2.getString("long_desc")).thenReturn(LONG_DESC);
        Mockito.when(mocked_valid_datasnap2.getString("short_desc")).thenReturn(SHORT_DESC);
        Mockito.when(mocked_valid_datasnap2.get("id")).thenReturn(1L);

        Mockito.when(mocked_invalid_datasnap.getString("name")).thenReturn(NAME1);
        Mockito.when(mocked_invalid_datasnap.getString("long_desc")).thenReturn(null);
        Mockito.when(mocked_invalid_datasnap.getString("short_desc")).thenReturn(SHORT_DESC);
        Mockito.when(mocked_invalid_datasnap.get("id")).thenReturn(1L);

        asso1 = new Association(mocked_valid_datasnap1);
        asso2 = new Association(mocked_valid_datasnap2);
    }

    @Test(expected = NullPointerException.class)
    public void invalidSnapThrowIllegalArgumentException(){
        new Association(mocked_invalid_datasnap);
    }

    @Test
    public void validSnapNoThrowException(){
        new Association(mocked_valid_datasnap1);
    }

    @Test
    public void idIsCorrect(){
        assertEquals(1, asso1.getId());
    }

    @Test
    public void nameIsCorrect(){
        assertEquals(NAME1, asso1.getName());
    }

    @Test
    public void longDescIsCorrect(){
        assertEquals(LONG_DESC, asso1.getLongDesc());
    }

    @Test
    public void shortDescIsCorrect(){
        assertEquals(SHORT_DESC, asso1.getShortDesc());
    }

    @Test
    public void uriIsCorrect(){
        assertNotNull(asso1.getIconUri().getPath());
        assertEquals(ICON_URI_STRING, asso1.getIconUri().getPath());
    }

    @Test
    public void comparableToIsCorrect(){
        assertEquals(NAME1.compareTo(NAME2),
                Association.getComparator().compare(asso1,asso2));
    }

}
