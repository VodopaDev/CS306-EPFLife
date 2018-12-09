package ch.epfl.sweng.zuluzulu.Utility;

import android.location.Location;

import com.google.firebase.firestore.GeoPoint;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Date;

import static ch.epfl.sweng.zuluzulu.Utility.Utils.*;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

@RunWith(JUnit4.class)
public class UtilsTest {

    @Test
    public void testDistanceBetween() {
        double epsilon = 1;
        GeoPoint p = new GeoPoint(0, 0);
        double distance = distanceBetween(p, p);
        assertEquals(0, distance, epsilon);
    }

    @Test
    public void testToGeoPoint() {
        Location location = new Location("");
        location.setLatitude(1);
        location.setLongitude(1);

        GeoPoint geoPoint = toGeoPoint(location);
        assertEquals(geoPoint.getLatitude(), location.getLatitude(), 0.0);
        assertEquals(geoPoint.getLongitude(), location.getLongitude(), 0.0);
    }

    @Test(expected = NullPointerException.class)
    public void testToGeoPointNullException() {
        toGeoPoint(null);
    }

    @Test
    public void testRandomIntGoodRange() {
        int min = -5;
        int max = 5;
        int random = randomInt(min, max);
        assertTrue(min <= random && random <= max);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomIntWrongRange() {
        randomInt(3, -3);
    }

    @Test
    public void testMillisecondsSinceCorrect() {
        Date now = com.google.firebase.Timestamp.now().toDate();
        int oneSecond = 1000;
        assertTrue(getMillisecondsSince(now) < oneSecond);
    }

    @Test(expected = NullPointerException.class)
    public void testMillisecondSinceWithNullDate() {
        getMillisecondsSince(null);
    }
}
