package ch.epfl.sweng.zuluzulu.Fragments;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.zuluzulu.MainActivity;
import ch.epfl.sweng.zuluzulu.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class AssociationsGeneratorFragmentTest {
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);


    @Before
    public void setUp() {
        mActivityRule.getActivity().openFragment(AssociationsGeneratorFragment.newInstance());

        // Register the idling resource
        IdlingRegistry.getInstance().register(mActivityRule.getActivity().getCountingIdlingResource());
    }

    @Test
    public void canLoadURLs() {
        onView(withId(R.id.associations_generator_list_values)).check(matches(isDisplayed()));
    }

}