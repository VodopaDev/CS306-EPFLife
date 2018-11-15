package ch.epfl.sweng.zuluzulu;

import android.location.Location;

import com.google.firebase.firestore.GeoPoint;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Date;

import ch.epfl.sweng.zuluzulu.Structure.Utils;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

@RunWith(JUnit4.class)
public class UtilsTest {

    @Test
    public void testDistanceBetween() {
        double epsilon = 1;
        GeoPoint p = new GeoPoint(0, 0);
        double distance = Utils.distanceBetween(p, p);
        assertEquals(0, distance, epsilon);
    }

    @Test
    public void testToGeoPoint() {
        Location location = new Location("");
        location.setLatitude(1);
        location.setLongitude(1);

        GeoPoint geoPoint = Utils.toGeoPoint(location);
        assertEquals(geoPoint.getLatitude(), location.getLatitude(), 0.0);
        assertEquals(geoPoint.getLongitude(), location.getLongitude(), 0.0);
    }

    @Test(expected = NullPointerException.class)
    public void testToGeoPointNullException() {
        Utils.toGeoPoint(null);
    }

    @Test
    public void testRandomIntGoodRange() {
        int min = -5;
        int max = 5;
        int random = Utils.randomInt(min, max);
        assertTrue(min <= random && random <= max);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomIntWrongRange() {
        Utils.randomInt(3, -3);
    }

    @Test
    public void testMillisecondsSinceCorrect() {
        Date now = com.google.firebase.Timestamp.now().toDate();
        int oneSecond = 1000;
        assertTrue(Utils.getMillisecondsSince(now) < oneSecond);
    }

    @Test(expected = NullPointerException.class)
    public void testMillisecondSinceWithNullDate() {
        Utils.getMillisecondsSince(null);
    }
}
