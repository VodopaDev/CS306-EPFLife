package ch.epfl.sweng.zuluzulu;

import com.google.firebase.firestore.GeoPoint;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ch.epfl.sweng.zuluzulu.Structure.Utils;

import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(JUnit4.class)
public class UtilsTest {

    @Test
    public void nullSnapshotReturnFalse(){
        assertThat(false, equalTo(Utils.isValidSnapshot(null, Collections.EMPTY_LIST)));
    }

    @Test
    public void longToInt(){
        List<Long> array1 = Arrays.asList(1L,2L);
        List<Integer> array2 = Utils.longListToIntList(array1);
        assertThat(Arrays.asList(1,2), equalTo(array2));
    }

    @Test
    public void testDistanceBetween() {
        double epsilon = 1.0e-3;
        GeoPoint p1 = new GeoPoint(0, 0);
        GeoPoint p2 = new GeoPoint(1, 0);
        GeoPoint p3 = new GeoPoint(0, -1);
        double distance11 = Utils.distanceBetween(p1, p1);
        double distance12 = Utils.distanceBetween(p1, p2);
        double distance13 = Utils.distanceBetween(p1, p3);
        double distance23 = Utils.distanceBetween(p2, p3);
        assertEquals(0, distance11, epsilon);
        assertEquals(1, distance12, epsilon);
        assertEquals(1, distance13, epsilon);
        assertEquals(Math.sqrt(2), distance23, epsilon);
    }

}
