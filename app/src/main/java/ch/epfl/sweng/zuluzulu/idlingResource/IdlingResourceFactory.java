package ch.epfl.sweng.zuluzulu.idlingResource;

import android.support.test.espresso.idling.CountingIdlingResource;

public class IdlingResourceFactory {
    // This resource is used for tests
    // That's the recommended way to implement it
    // @see https://developer.android.com/training/testing/espresso/idling-resource#integrate-recommended-approach
    private static CountingIdlingResource resource = new CountingIdlingResource("Idling resource");

    /**
     * Increment the countingIdlingResource
     * Do this before a async task
     */
    public static void incrementCountingIdlingResource() {
        resource.increment();
    }

    /**
     * Decrement the countingIdlingResource
     * Do this after a async task
     */
    public static void decrementCountingIdlingResource() {
        resource.decrement();
    }

    /**
     * Return the resource for the tests
     *
     * @return resource
     */
    public static CountingIdlingResource getCountingIdlingResource() {
        return resource;
    }


}
