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

    @Test
    public void testOnLocationChanged() throws Throwable {
        mActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                GPS.start(mActivityRule.getActivity());
                GPS.getListener().onLocationChanged(new Location(LocationManager.GPS_PROVIDER));
                GPS.getListener().onLocationChanged(new Location(LocationManager.NETWORK_PROVIDER));
                GPS.stop();
            }
        });
    }
}
