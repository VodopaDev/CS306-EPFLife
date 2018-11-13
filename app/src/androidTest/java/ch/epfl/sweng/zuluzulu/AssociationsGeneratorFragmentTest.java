package ch.epfl.sweng.zuluzulu;

import android.support.test.espresso.IdlingRegistry;

import org.junit.Before;
import org.junit.Test;

import ch.epfl.sweng.zuluzulu.Fragments.AssociationsGeneratorFragment;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.TestWithAdmin;
import ch.epfl.sweng.zuluzulu.Utility;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class AssociationsGeneratorFragmentTest extends TestWithAdmin {

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
        mActivityRule.getActivity().openFragment(AssociationsGeneratorFragment.newInstance(Utility.createTestAuthenticated()));
    }

    /**
     * Create the fragment with admin user
     */
    private void adminUser() {
        mActivityRule.getActivity().openFragment(AssociationsGeneratorFragment.newInstance(getUser()));
    }
}