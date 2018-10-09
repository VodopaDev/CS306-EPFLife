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

    private Association asso1;// = Mockito.mock(Association.class);
    private Association asso2;
    private final Uri icon_uri = Mockito.mock(Uri.class);
    private final DocumentSnapshot mockedValidDataSnap1 = Mockito.mock(DocumentSnapshot.class);
    private final DocumentSnapshot mockedValidDataSnap2 = Mockito.mock(DocumentSnapshot.class);
    private final DocumentSnapshot mockedInvalidDataSnap = Mockito.mock(DocumentSnapshot.class);

    @Before
    public void createMockDocumentSnapshot(){
        Mockito.when(icon_uri.getPath()).thenReturn(ICON_URI_STRING);

        Mockito.when(mockedValidDataSnap1.getString("name")).thenReturn(NAME1);
        Mockito.when(mockedValidDataSnap1.getString("long_desc")).thenReturn(LONG_DESC);
        Mockito.when(mockedValidDataSnap1.getString("short_desc")).thenReturn(SHORT_DESC);
        Mockito.when(mockedValidDataSnap1.get("id")).thenReturn(1L);

        Mockito.when(mockedValidDataSnap2.getString("name")).thenReturn(NAME2);
        Mockito.when(mockedValidDataSnap2.getString("long_desc")).thenReturn(LONG_DESC);
        Mockito.when(mockedValidDataSnap2.getString("short_desc")).thenReturn(SHORT_DESC);
        Mockito.when(mockedValidDataSnap2.get("id")).thenReturn(1L);

        Mockito.when(mockedInvalidDataSnap.getString("name")).thenReturn(NAME1);
        Mockito.when(mockedInvalidDataSnap.getString("long_desc")).thenReturn(null);
        Mockito.when(mockedInvalidDataSnap.getString("short_desc")).thenReturn(SHORT_DESC);
        Mockito.when(mockedInvalidDataSnap.get("id")).thenReturn(1L);

        asso1 = new Association(mockedValidDataSnap1, icon_uri);
        asso2 = new Association(mockedValidDataSnap2, icon_uri);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidSnapThrowIllegalArgumentException(){
        new Association(mockedInvalidDataSnap);
    }

    @Test
    public void validSnapDoesntThrowException(){
        new Association(mockedValidDataSnap1, icon_uri);
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
        assertNotNull(asso1.getIcon().getPath());
        assertEquals(ICON_URI_STRING, asso1.getIcon().getPath());
    }

    @Test
    public void comparableToIsCorrect(){
        assertEquals(NAME1.compareTo(NAME2),
                asso1.compareTo(asso2));
    }

}
