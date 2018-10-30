package ch.epfl.sweng.zuluzulu;

import android.location.Location;

import com.google.firebase.firestore.GeoPoint;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

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
        assertTrue(geoPoint.getLatitude() == location.getLatitude());
        assertTrue(geoPoint.getLongitude() == location.getLongitude());
    }

    @Test(expected = NullPointerException.class)
    public void testToGeoPointNullException() {
        GeoPoint geoPoint = Utils.toGeoPoint(null);
    }
}
