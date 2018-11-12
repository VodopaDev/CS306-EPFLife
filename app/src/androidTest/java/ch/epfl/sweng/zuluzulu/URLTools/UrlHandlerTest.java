package ch.epfl.sweng.zuluzulu.URLTools;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.util.Pair;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import ch.epfl.sweng.zuluzulu.Fragments.AssociationsGeneratorFragment;
import ch.epfl.sweng.zuluzulu.MainActivity;
import ch.epfl.sweng.zuluzulu.URLTools.AssociationsParser;
import ch.epfl.sweng.zuluzulu.URLTools.UrlHandler;
import ch.epfl.sweng.zuluzulu.Utility;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UrlHandlerTest {
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    private CountingIdlingResource resource;

    private UrlHandler object;

    private boolean succes = false;

    @Before
    public void setUp() {

        // Register a new countingIdlingResource
        resource = new CountingIdlingResource("URL Handler test");

        // Register it
        IdlingRegistry.getInstance().register(resource);

        // Increment it. The test will NOT TERMINATE until it's not decremented to zero
        resource.increment();

        // Create the object
        /*
         * In my async task, I give to it the lambda function that will be executed.
         * Here it's a test made function. In the Fragment, it's the function that will get the datas
         * and print them in the view.
         *
         * This test function handler() will be executed when the action is finished
         */
        this.object = new UrlHandler(this::handler, new AssociationsParser());
    }

    @Test
    public void worksWithGoodURL() {
        // Execute our async task
        object.execute(AssociationsGeneratorFragment.EPFL_URL); // Test with real URL

        // We need to open a View for the test to start waiting...
        Utility.openMenu();

        // wait....

        // stop waiting on resource.decrement()   OR timeout !

        assertTrue(succes);
    }

    /**
     * This function will get the datas
     *
     * @param result arrayList
     * @return Void
     */
    private Void handler(Pair<String, List<String>> result) {
        // Do any logic. Here we want result not to be null
        succes = result != null;

        // Free the resource with decrement. Now the test stop waiting !
        resource.decrement();

        return null;
    }


    @Test
    public void testNotValidURL() {
        object.execute("wrong_url"); // Test with not valid URL

        Utility.openMenu();

        assertFalse(succes);
    }

    @Test
    public void testWrongPage() {
        object.execute("http://epfl.ch/another_page_404"); // Test with wrong page

        Utility.openMenu();

        assertFalse(succes);
    }
}