package ch.epfl.sweng.zuluzulu.Structure;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.zuluzulu.MainActivity;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class GPSTest {
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    private GPS gps;

    @Before
    public void init() throws Throwable {
        mActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gps = new GPS(mActivityRule.getActivity());
                gps.start();
            }
        });
    }

    @Test
    public void testIsWorkingAfterStart() {
        assertTrue(gps.isWorking());
    }

    @Test
    public void testIsNotWorkingAfterStop() {
        gps.stop();
        assertFalse(gps.isWorking());
    }
}
