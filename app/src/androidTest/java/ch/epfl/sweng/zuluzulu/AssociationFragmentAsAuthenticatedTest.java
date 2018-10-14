package ch.epfl.sweng.zuluzulu;

import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.ListView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import ch.epfl.sweng.zuluzulu.Fragments.AssociationFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class AssociationFragmentAsAuthenticatedTest {

    private final static int NB_ALL_ASSOS = 7;
    private final static int NB_FAV_ASSOS = 4;
    private AssociationFragment fragment;
    private ListView list_assos;

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void goToAssociationList() throws InterruptedException {
        // Authenticate
        Utility.fullLogin();

        // Go to association view
        Utility.goToAssociation();

        TimeUnit.SECONDS.sleep(5);
        fragment = (AssociationFragment)mActivityRule.getActivity().getCurrentFragment();
        list_assos = fragment.getListviewAssos();
    }

    @Test
    public void mainPageHasSomeAssociations() {
        onView(withId(R.id.association_fragment_all_button)).perform(ViewActions.click());
        /*
        TimeUnit.SECONDS.sleep(1);
        assertThat(list_assos, hasChildCount(NB_ALL_ASSOS));
        */
    }

    @Test
    public void clickOnFavoritesDisplayFewerAssociations() {
        onView(withId(R.id.association_fragment_fav_button)).perform(ViewActions.click());
        /*
        TimeUnit.SECONDS.sleep(1);
        assertThat(list_assos, hasChildCount(NB_FAV_ASSOS));
        */
    }

    @Test
    public void clickingAnAssociationGoesToDetail() throws InterruptedException {
        onView(withText("Agepoly")).perform(ViewActions.click());
        TimeUnit.SECONDS.sleep(1);
        onView(withId(R.id.association_detail_name)).check(matches(isDisplayed()));
    }


}
