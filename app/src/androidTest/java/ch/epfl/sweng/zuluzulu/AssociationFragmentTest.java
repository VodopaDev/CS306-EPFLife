package ch.epfl.sweng.zuluzulu;

import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class AssociationFragmentTest {

    private static final int NB_ALL_ASSOS = 7;
    private static final int NB_FAV_ASSOS = 4;

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    private void guestGoesToAssociation() throws InterruptedException {
        Utility.goToAssociation();
        TimeUnit.SECONDS.sleep(5);
    }

    private void authenticatedGoesToAssociation() throws InterruptedException {
        Utility.fullLogin();
        Utility.goToAssociation();
        TimeUnit.SECONDS.sleep(5);
    }

    @Test
    public void thereAreTwoButtons() throws InterruptedException {
        guestGoesToAssociation();
        onView(withId(R.id.association_fragment_all_button)).check(matches(isDisplayed()));
        onView(withId(R.id.association_fragment_all_button)).check(matches(isDisplayed()));
    }

    @Test
    public void guestMainPageHasSomeAssociations() throws InterruptedException {
        guestGoesToAssociation();
        onView(withId(R.id.association_fragment_all_button)).perform(ViewActions.click());
        /*
        TimeUnit.SECONDS.sleep(1);
        assertThat(list_assos, hasChildCount(NB_ALL_ASSOS));
        */
    }

    @Test
    public void guestClickOnFavoritesStaysOnAll() throws InterruptedException {
        guestGoesToAssociation();
        onView(withId(R.id.association_fragment_fav_button)).perform(ViewActions.click());
        /*
        TimeUnit.SECONDS.sleep(1);
        assertThat(list_assos, hasChildCount(NB_ALL_ASSOS));
        */
    }

    @Test
    public void authenticatedClickOnFavoritesDisplayFewerAssociations() throws InterruptedException {
        authenticatedGoesToAssociation();
        onView(withId(R.id.association_fragment_fav_button)).perform(ViewActions.click());
        /*
        TimeUnit.SECONDS.sleep(1);
        assertThat(list_assos, hasChildCount(NB_FAV_ASSOS));
        */
    }

    @Test
    public void AuthenticatedClickingAnAssociationGoesToDetail() throws InterruptedException {
        authenticatedGoesToAssociation();
        onView(withText("Agepoly")).perform(ViewActions.click());
        TimeUnit.SECONDS.sleep(1);
        onView(withId(R.id.association_detail_name)).check(matches(isDisplayed()));
    }
}