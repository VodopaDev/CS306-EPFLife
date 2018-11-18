package ch.epfl.sweng.zuluzulu.URLTools;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.util.Pair;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import ch.epfl.sweng.zuluzulu.Fragments.AssociationsGeneratorFragment;
import ch.epfl.sweng.zuluzulu.MainActivity;
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

    }

    @Test
    public void worksWithGoodURL() {
        // Change the UrlReader to avoid HTTP request
        UrlReader reader = new UrlReader() {
            @Override
            public BufferedReader read(String name) {
                return new BufferedReader(new StringReader("&#8211; <a href=\"http://lauzhack.com\">LauzHack</a> (Organisation d&#8217;un Hackaton)<br />"));
            }
        };
        // Change the factory
        UrlReaderFactory.setDependency(reader);


        // Create the object
        /*
         * In my async task, I give to it the lambda function that will be executed.
         * Here it's a test made function. In the Fragment, it's the function that will get the datas
         * and print them in the view.
         *
         * This test function handler() will be executed when the action is finished
         */
        this.object = new UrlHandler(this::handler, new AssociationsParser());

        // Execute our async task
        object.execute(AssociationsGeneratorFragment.EPFL_URL); // Test with real URL

        // We need to open a View for the test to start waiting...
        Utility.openMenu();

        // wait....

        // stop waiting on resource.decrement()   OR timeout !

        assertTrue(succes);
    }

    @Test
    public void failWithClosedBf() {
        // Change the UrlReader to avoid HTTP request
        UrlReader reader = new UrlReader() {
            @Override
            public BufferedReader read(String name) {
                BufferedReader bf = new BufferedReader(new StringReader(""));
                try {
                    bf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return bf;
            }
        };
        // Change the factory
        UrlReaderFactory.setDependency(reader);

        this.object = new UrlHandler(this::handler, new AssociationsParser());

        object.execute("url");

        Utility.openMenu();

        assertFalse(succes);
    }

    @Test
    public void failWithNull() {
        this.object = new UrlHandler(this::handler, new AssociationsParser());

        object.execute(); // Test with real URL

        Utility.openMenu();

        assertFalse(succes);
    }


    @Test
    public void failWithNullBufferedReader() {
        // Change the UrlReader to avoid HTTP request
        UrlReader reader = new UrlReader() {
            @Override
            public BufferedReader read(String name) {
                return null;
            }
        };
        // Change the factory
        UrlReaderFactory.setDependency(reader);

        this.object = new UrlHandler(this::handler, new AssociationsParser());

        object.execute(AssociationsGeneratorFragment.EPFL_URL); // Test with real URL

        Utility.openMenu();

        assertFalse(succes);
    }

    /**
     * This function will get the datas
     *
     * @param result arrayList
     * @return Void
     */
    private Void handler(List<String> result) {
        // Do any logic. Here we want result not to be null
        succes = false;
        if(result != null && !result.isEmpty()){
            succes = result.get(0).toLowerCase().contains("lauzhack");
        }

        // Free the resource with decrement. Now the test stop waiting !
        resource.decrement();

        return null;
    }

}