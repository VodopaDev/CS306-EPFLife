package ch.epfl.sweng.zuluzulu;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.StringReader;

import ch.epfl.sweng.zuluzulu.Database.MockedProxy;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.Fragments.AdminFragments.MementoFragment;
import ch.epfl.sweng.zuluzulu.URLTools.UrlReader;
import ch.epfl.sweng.zuluzulu.URLTools.UrlReaderFactory;


public class MementoFragmentTest {
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);
    @Before
    public void initFragment() {
 android.util.Log.d("Function called", "initFragment");
        DatabaseFactory.setDependency(new MockedProxy());
    }

    @Test
    public void canOpenFragment() {
 android.util.Log.d("Function called", "canOpenFragment");
        UrlReader reader = new UrlReader() {
            @Override
            public BufferedReader read(String name) {
 android.util.Log.d("Function called", "read");
                return new BufferedReader(new StringReader("[{\n" +
                        "        \"title\": \"MoveOn - Free dance lessons\"," +
                        "        \"description\": \"<p>MoveOn, a commission of Agepoly, offer free disco-fox lessons. Disco-fox is a couple dance that is performed on current music, on what you can hear on the radio for example. The lessons take place in the Polydôme at EPFL on Tuesdays. <br>\\r\\n<br>\\r\\n18h30 : Hustle (4-count disco-fox, recommended for complete beginners)<br>\\r\\n19h45 : Disco-fox, with choreography <br>\\r\\n<br>\\r\\nThe lessons are for beginners. No registration, you can just come to the lesson. <br>\\r\\n<br>\\r\\nMore infos : fb.me/moveonepfl</p>\",\n" +
                        "        \"event_id\": \"43453\"," +
                        "        \"event_start_date\": \"2018-09-25\"," +
                        "        \"event_end_date\": \"2018-12-18\"," +
                        "        \"event_start_time\": \"18:30:00\"," +
                        "        \"event_end_time\": \"21:00:00\"," +
                        "        \"event_place_and_room\": \"PO 094.0\"," +
                        "        \"event_url_place_and_room\": \"https://plan.epfl.ch/?room=PO094.0\"," +
                        "        \"event_visual_absolute_url\": \"https://memento.epfl.ch/image/11476/112x112.jpg\"," +
                        "        \"event_speaker\": \"Mike Bardet, Eva Lorendeaux\"," +
                        "        \"event_organizer\": \"Mike Bardet\"," +
                        "        \"event_contact\": \"Mike Bardet\"," +
                        "        \"event_url_link\": \"\"," +
                        "        \"event_url_place_and_room\": \"https://plan.epfl.ch/?room=PO094.0\",\n" +
                        "        \"event_category_fr\": \"Divers\",\n" + "\"event_speaker\": \"Roda Fawaz\"\n" + "    }]"));
            }
        };
        // Change the factory
        UrlReaderFactory.setDependency(reader);
        adminUser();
        Utility.openMenu();
    }

    @Test
    public void doNotCrashWithFakeJson() {
 android.util.Log.d("Function called", "doNotCrashWithFakeJson");
        UrlReader reader = new UrlReader() {
            @Override
            public BufferedReader read(String name) {
 android.util.Log.d("Function called", "read");
                return new BufferedReader(new StringReader(
                        "[fakejson"
                ));
            }
        };
        // Change the factory
        UrlReaderFactory.setDependency(reader);
        adminUser();
        Utility.openMenu();
    }

    @Test
    public void doNotCrashWithFakeUrl() {
 android.util.Log.d("Function called", "doNotCrashWithFakeUrl");
        UrlReader reader = new UrlReader() {
            @Override
            public BufferedReader read(String name) {
 android.util.Log.d("Function called", "read");
                return null;
            }
        };
        // Change the factory
        UrlReaderFactory.setDependency(reader);
        adminUser();
        Utility.openMenu();
    }

    @Test
    public void refuseNonAdmin() {
 android.util.Log.d("Function called", "refuseNonAdmin");
        nonAdminUser();
        Utility.checkFragmentIsClosed(R.id.memento_fragment);
    }

    private void nonAdminUser() {
 android.util.Log.d("Function called", "nonAdminUser");
        mActivityRule.getActivity().openFragment(MementoFragment.newInstance(Utility.createTestAuthenticated()));
    }

    private void adminUser() {
 android.util.Log.d("Function called", "adminUser");
        mActivityRule.getActivity().openFragment(MementoFragment.newInstance(Utility.createTestAdmin()));
    }


}
