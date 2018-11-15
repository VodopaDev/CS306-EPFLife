package ch.epfl.sweng.zuluzulu.Fragments;

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
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;


public class AssociationsGeneratorFragmentTest extends TestWithAdminAndFragment<MainFragment> {

    @Override
    public void initFragment() {
        DatabaseFactory.setDependency(new FirebaseMock());

        fragment = MainFragment.newInstance(user);
    }

    @Test
    public void canLoadURLs() {
        // Change the UrlReader to avoid HTTP request
        UrlReader reader = new UrlReader() {
            @Override
            public BufferedReader read(String name) {
                return new BufferedReader(new StringReader("&#8211; <a href=\"http://example.com\">Other</a> (Other)<br />\n"
                                +"&#8211; <a href=\"http://lauzhack.com\">LauzHack</a> (Organisation d&#8217;un Hackaton)<br />\n"
                                +"<link rel=\"icon\" type=\"image/png\" href=\"images/favicon.png\" sizes=\"16x16\">"));
            }
        };
        // Change the factory
        UrlReaderFactory.setDependency(reader);

        adminUser();
        onView(withId(R.id.nbr_icon)).perform(replaceText("lauzhack"));
        onView(withId(R.id.load_icon_button)).perform(click());

        onView(withId(R.id.associations_generator_list_values)).check(matches(withText(containsString("favicon.png"))));
    }


    @Test
    public void canChangeEpflLogo() {
        // Change the UrlReader to avoid HTTP request
        UrlReader reader = new UrlReader() {
            @Override
            public BufferedReader read(String name) {
                return new BufferedReader(new StringReader("&#8211; <a href=\"http://lauzhack.com\">LauzHack</a> (Organisation d&#8217;un Hackaton)<br />"
                        +"<link rel=\"icon\" type=\"image/png\" href=\"www.epfl.ch/favicon.ico\" sizes=\"16x16\">"));
            }
        };
        // Change the factory
        UrlReaderFactory.setDependency(reader);
        adminUser();
        onView(withId(R.id.nbr_icon)).perform(replaceText("lauzhack"));
        onView(withId(R.id.load_icon_button)).perform(click());

        onView(withId(R.id.associations_generator_list_values)).check(matches(withText(containsString("EPFL-Logo.jpg"))));
    }

    @Test
    public void buttonDoesNothingIfTextEmpty() {
        // Change the UrlReader to avoid HTTP request
        UrlReader reader = new UrlReader() {
            @Override
            public BufferedReader read(String name) {
                return new BufferedReader(new StringReader("&#8211; <a href=\"http://lauzhack.com\">LauzHack</a> (Organisation d&#8217;un Hackaton)<br /><link rel=\"icon\" type=\"image/png\" href=\"images/favicon.png\" sizes=\"16x16\">"));
            }
        };
        // Change the factory
        UrlReaderFactory.setDependency(reader);
        adminUser();
        onView(withId(R.id.load_icon_button)).perform(click());

        onView(withId(R.id.associations_generator_list_values)).check(matches(withText(containsString("lauzhack"))));
    }

    @Test
    public void showNoResults() {
        // Change the UrlReader to avoid HTTP request
        UrlReader reader = new UrlReader() {
            @Override
            public BufferedReader read(String name) {
                return new BufferedReader(new StringReader("nothing !"));
            }
        };
        // Change the factory
        UrlReaderFactory.setDependency(reader);
        adminUser();
        onView(withId(R.id.nbr_icon)).perform(replaceText("test"));
        onView(withId(R.id.load_icon_button)).perform(click());

        // check no association found
        onView(withId(R.id.associations_generator_list_values)).check(matches(withText(R.string.no_association_found)));
    }

    /**
     * Test that an icon doesn't load then the association url is fake
     */
    @Test
    public void dontLoadIconWhenFakeUrl() {
        // Change the UrlReader to avoid HTTP request
        UrlReader reader = new UrlReader() {
            @Override
            public BufferedReader read(String name) {
                return new BufferedReader(new StringReader("&#8211; <a href=\"not_url\">FakeAssos</a> (FakeAssos)<br /><link rel=\"icon\" type=\"image/png\" href=\"images/favicon.png\" sizes=\"16x16\">"));
            }
        };
        // Change the factory
        UrlReaderFactory.setDependency(reader);
        adminUser();
        onView(withId(R.id.nbr_icon)).perform(replaceText("FakeAssos"));
        onView(withId(R.id.load_icon_button)).perform(click());

        // check no icon found
        onView(withId(R.id.associations_generator_list_values)).check(matches(not(withText("favicon.png"))));
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
}