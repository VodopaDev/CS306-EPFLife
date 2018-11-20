package ch.epfl.sweng.zuluzulu.Fragments;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Test;

import ch.epfl.sweng.zuluzulu.Database.FirebaseMock;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.R;

import java.io.BufferedReader;
import java.io.StringReader;

import ch.epfl.sweng.zuluzulu.TestingUtility.TestWithAdminAndFragment;
import ch.epfl.sweng.zuluzulu.URLTools.UrlReader;
import ch.epfl.sweng.zuluzulu.URLTools.UrlReaderFactory;
import ch.epfl.sweng.zuluzulu.Utility;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;


public class AssociationsGeneratorFragmentTest extends TestWithAdminAndFragment<MainFragment> {

    @Override
    public void initFragment() {
        DatabaseFactory.setDependency(new FirebaseMock());

        fragment = MainFragment.newInstance(user);
    }

    @Test
    public void canLoadURLs() {
        changeFactory("&#8211; <a href=\"http://example.com\">Other</a> (Other)<br />\n"
                + "&#8211; <a href=\"http://lauzhack.com\">LauzHack</a> (Organisation d&#8217;un Hackaton)<br />\n"
                + "<link rel=\"icon\" type=\"image/png\" href=\"images/favicon.png\" sizes=\"16x16\">");

        adminUser();
        onView(withId(R.id.associations_generator_recyclerview)).perform(
                RecyclerViewActions.actionOnItem(hasDescendant(withText("LauzHack")), new MyViewAction().clickChildViewWithId(R.id.add_card_add_button)));
        onView(withId(R.id.associations_generator_recyclerview)).perform(
                RecyclerViewActions.actionOnItem(hasDescendant(withText("Other")), new MyViewAction().clickChildViewWithId(R.id.add_card_add_button)));

    }

    @Test
    public void canChangeEpflLogo() {
        changeFactory("&#8211; <a href=\"http://lauzhack.com\">LauzHack</a> (Organisation d&#8217;un Hackaton)<br />"
                + "<link rel=\"icon\" type=\"image/png\" href=\"www.epfl.ch/favicon.ico\" sizes=\"16x16\">");
        adminUser();
        onView(withId(R.id.associations_generator_recyclerview)).perform(
                RecyclerViewActions.actionOnItem(hasDescendant(withText("LauzHack")), new MyViewAction().clickChildViewWithId(R.id.add_card_add_button)));
    }

    private void changeFactory(String s) {
        UrlReader reader = new UrlReader() {
            @Override
            public BufferedReader read(String name) {
                return new BufferedReader(new StringReader(s));
            }
        };
        UrlReaderFactory.setDependency(reader);
    }

    @Test
    public void defaultLogoOnFailAssociationUrl() {
        changeFactory("&#8211; <a href=\"faaake\">FAKE</a> (fake)<br /><link rel=\"icon\" type=\"image/png\" href=\"www.epfl.ch/favicon.ico\" sizes=\"16x16\">");
        adminUser();
        onView(withId(R.id.associations_generator_recyclerview)).perform(
                RecyclerViewActions.actionOnItem(hasDescendant(withText("FAKE")), new MyViewAction().clickChildViewWithId(R.id.add_card_add_button)));
    }

    @Test
    public void showNoResults() {
        changeFactory("nothing");
        adminUser();

        onView(withId(R.id.associations_generator_recyclerview)).check(matches(isDisplayed()));
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
        mActivityRule.getActivity().openFragment(AssociationsGeneratorFragment.newInstance(user));
    }

    public class MyViewAction {

        public ViewAction clickChildViewWithId(final int id) {
            return new ViewAction() {
                @Override
                public Matcher<View> getConstraints() {
                    return null;
                }

                @Override
                public String getDescription() {
                    return "Click on a child view with specified id.";
                }

                @Override
                public void perform(UiController uiController, View view) {
                    View v = view.findViewById(id);
                    v.performClick();
                }
            };
        }

    }
}