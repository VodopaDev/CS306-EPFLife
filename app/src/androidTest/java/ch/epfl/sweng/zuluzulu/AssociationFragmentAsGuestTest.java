package ch.epfl.sweng.zuluzulu;

import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.Gravity;
import android.widget.ListView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import ch.epfl.sweng.zuluzulu.Fragments.AssociationFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.hasChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class AssociationFragmentAsGuestTest {

    private static final int NB_ALL_ASSOS = 7;
    private AssociationFragment fragment;
    private ListView list_assos;

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void goToAssociationList() throws InterruptedException {
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_associations));
        TimeUnit.SECONDS.sleep(10);
        fragment = (AssociationFragment)mActivityRule.getActivity().getCurrentFragment();
        list_assos = fragment.getListviewAssos();
    }

    @Test
    public void thereAreTwoButtons() throws InterruptedException {
        onView(withId(R.id.association_fragment_all_button)).check(matches(isDisplayed()));
        TimeUnit.SECONDS.sleep(1);
        onView(withId(R.id.association_fragment_all_button)).check(matches(isDisplayed()));
    }

    @Test
    public void mainPageHasSomeAssociations() throws InterruptedException {
        onView(withId(R.id.association_fragment_all_button)).perform(ViewActions.click());
        TimeUnit.SECONDS.sleep(1);
        //assertThat(list_assos, hasChildCount(NB_ALL_ASSOS));
    }

    @Test
    public void clickOnFavoritesStaysOnAll() throws InterruptedException {
        onView(withId(R.id.association_fragment_fav_button)).perform(ViewActions.click());
        TimeUnit.SECONDS.sleep(1);
        //assertThat(list_assos, hasChildCount(NB_ALL_ASSOS));
    }
}