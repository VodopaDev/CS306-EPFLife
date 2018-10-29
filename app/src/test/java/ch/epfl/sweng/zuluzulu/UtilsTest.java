package ch.epfl.sweng.zuluzulu;

import com.google.firebase.firestore.GeoPoint;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ch.epfl.sweng.zuluzulu.Structure.Utils;

import static junit.framework.TestCase.assertEquals;

@RunWith(JUnit4.class)
public class UtilsTest {

    @Test
    public void testDistanceBetween() {
        double epsilon = 1;
        GeoPoint p = new GeoPoint(0, 0);
        double distance = Utils.distanceBetween(p, p);
        assertEquals(0, distance, epsilon);
    }

}
