package ch.epfl.sweng.zuluzulu.utility;

import android.location.Location;

import com.google.firebase.firestore.GeoPoint;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Date;

import static ch.epfl.sweng.zuluzulu.utility.GeopointUtility.distanceBetween;
import static ch.epfl.sweng.zuluzulu.utility.GeopointUtility.toGeoPoint;
import static ch.epfl.sweng.zuluzulu.utility.TimeUtility.getMillisecondsSince;
import static ch.epfl.sweng.zuluzulu.utility.Utils.*;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
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

    @Test
    public void testHourAndMinutes() {
        for (int hour = 0; hour <= 23; hour++) {
            for (int minute = 0; minute <= 59; minute++) {
                String strHour = String.valueOf(hour);
                String strMinute = String.valueOf(minute);
                if (strHour.length() < 2) {
                    strHour = "0" + strHour;
                }
                if (strMinute.length() < 2) {
                    strMinute = "0" + strMinute;
                }
                String expected = strHour + ":" + strMinute;
                assertEquals(expected, TimeUtility.hourAndMinutesFrom(hour, minute));
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalHourInHourAndMinutes() {
        TimeUtility.hourAndMinutesFrom(-2, 45);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalMinuteInHourAndMinutes() {
        TimeUtility.hourAndMinutesFrom(12, 103);
    }

    @Test
    public void testValidHour() {
        assertTrue(TimeUtility.validHour(12));
        assertFalse(TimeUtility.validHour(-2));
        assertFalse(TimeUtility.validHour(24));
    }

    @Test
    public void testValidMinute() {
        assertTrue(TimeUtility.validMinute(50));
        assertFalse(TimeUtility.validMinute(-1));
        assertFalse(TimeUtility.validMinute(60));
    }
}
