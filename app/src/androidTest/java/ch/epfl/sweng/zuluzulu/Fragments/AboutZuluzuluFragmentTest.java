package ch.epfl.sweng.zuluzulu.Fragments;

import android.support.test.rule.ActivityTestRule;
import android.support.v4.app.Fragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.zuluzulu.MainActivity;
import ch.epfl.sweng.zuluzulu.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class AboutZuluzuluFragmentTest {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init() {
        Fragment fragment = AboutZuluzuluFragment.newInstance(());
        mActivityRule.getActivity().openFragment(fragment);
    }

    @Test
    public void canSendEmail() {
        onView(withId(R.id.send_mail)).perform(click());
    }
}