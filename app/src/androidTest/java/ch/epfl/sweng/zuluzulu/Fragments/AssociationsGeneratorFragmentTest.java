package ch.epfl.sweng.zuluzulu.Fragments;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.zuluzulu.MainActivity;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.TestWithAdminLogin;
import ch.epfl.sweng.zuluzulu.TestWithLogin;
import ch.epfl.sweng.zuluzulu.Utility;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertFalse;

public class AssociationsGeneratorFragmentTest extends TestWithAdminLogin {

    @Before
    public void init() {
        // Register the idling resource
        IdlingRegistry.getInstance().register(mActivityRule.getActivity().getCountingIdlingResource());
    }

    @Test
    public void canLoadURLs() {
        adminUser();
        onView(withId(R.id.associations_generator_list_values)).check(matches(isDisplayed()));
    }

    @Test
    public void refuseNonAdmin() {
        nonAdminUser();
        Utility.checkFragmentIsClosed(R.id.associations_generator_fragment);
    }

    /**
     * Create a fragment with non admin user
     */
    private void nonAdminUser() {
        mActivityRule.getActivity().openFragment(AssociationsGeneratorFragment.newInstance(Utility.createTestUser()));
    }

    /**
     * Create the fragment with admin user
     */
    private void adminUser() {
        mActivityRule.getActivity().openFragment(AssociationsGeneratorFragment.newInstance(getUser()));
    }
}