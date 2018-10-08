package ch.epfl.sweng.zuluzulu;

import android.content.Context;
import android.content.pm.InstrumentationInfo;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Test;
import ch.epfl.sweng.zuluzulu.Structure.Association;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AssociationTest {
    private static final String NAME = "Agepoly";
    private static final String SHORT_DESC = "Representing all students at EPFL";
    private static final String LONG_DESC = "Blah blah blah blah blah";

    private Association asso;

    @Before
    public void createAssociation(){
        asso = Association.fromId(1);
        while(!asso.hasLoaded()){}
    }

    @Test
    public void nameIsCorrect(){
        assertEquals(NAME, asso.getName());
    }

    @Test
    public void longDescIsCorrect(){
        assertEquals(LONG_DESC, asso.getLongDesc());
    }

    @Test
    public void shortDescIsCorrect(){
        assertEquals(SHORT_DESC, asso.getShortDesc());
    }

    @Test
    public void uriIsCorrect(){
        assertNotNull(asso.getIcon());
    }

}
