package ch.epfl.sweng.zuluzulu.Structure;

import android.location.Location;
import android.location.LocationManager;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;

import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.zuluzulu.MainActivity;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class GPSTest {
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Rule public GrantPermissionRule permissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Test
    public void testOnLocationChanged() throws Throwable {
        mActivityRule.runOnUiThread(() -> {
            GPS.start(mActivityRule.getActivity());
            assertTrue(GPS.isActivated());
            GPS.getListener().onLocationChanged(new Location(""));
            GPS.getListener().onLocationChanged(new Location(LocationManager.GPS_PROVIDER));
            GPS.getListener().onLocationChanged(new Location(LocationManager.NETWORK_PROVIDER));
            GPS.stop();
            assertFalse(GPS.isActivated());
        });
    }
}
