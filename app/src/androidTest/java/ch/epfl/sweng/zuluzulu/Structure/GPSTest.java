package ch.epfl.sweng.zuluzulu.Structure;

import android.location.Location;
import android.location.LocationManager;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.zuluzulu.MainActivity;

public class GPSTest {
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    private GPS gps;

    @Test
    public void testOnLocationChanged() throws Throwable {
        mActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gps = GPS.getInstance(mActivityRule.getActivity());
                gps.start();
                gps.onLocationChanged(new Location(LocationManager.GPS_PROVIDER));
                gps.stop();
            }
        });
    }
}
