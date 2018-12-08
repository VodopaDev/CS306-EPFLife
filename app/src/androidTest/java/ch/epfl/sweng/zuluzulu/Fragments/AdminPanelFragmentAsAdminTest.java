package ch.epfl.sweng.zuluzulu.Fragments;

import android.support.test.espresso.action.ViewActions;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.StringReader;

import ch.epfl.sweng.zuluzulu.Database.MockedProxy;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.Firebase.FirebaseProxy;
import ch.epfl.sweng.zuluzulu.Fragments.AdminFragments.AdminPanelFragment;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.TestingUtility.TestWithAdminAndFragment;
import ch.epfl.sweng.zuluzulu.URLTools.UrlReader;
import ch.epfl.sweng.zuluzulu.URLTools.UrlReaderFactory;
import ch.epfl.sweng.zuluzulu.Utility;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class AdminPanelFragmentAsAdminTest extends TestWithAdminAndFragment {

    @Override
    public void initFragment() {
        DatabaseFactory.setDependency(new MockedProxy());
        fragment = AdminPanelFragment.newInstance();
    }

    @Test
    public void canOpenMementoFragment() {
        UrlReader reader = new UrlReader() {
            @Override
            public BufferedReader read(String name) {
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
        onView(withId(R.id.panel_memento)).perform(ViewActions.click());
        Utility.checkFragmentIsOpen(R.id.memento_fragment);
    }

    @Test
    public void canOpenAssociationGeneratorFragment() {
        changeFactory("&#8211; <a href=\"http://example.com\">Other</a> (Other)<br />\n"
                + "&#8211; <a href=\"http://lauzhack.com\">LauzHack</a> (Organisation d&#8217;un Hackaton)<br />\n"
                + "<link rel=\"icon\" type=\"image/png\" href=\"images/favicon.png\" sizes=\"16x16\">");

        onView(withId(R.id.panel_association)).perform(ViewActions.click());
        Utility.checkFragmentIsOpen(R.id.associations_generator_fragment);
    }

    @Test
    public void canOpenChannelManagerFragment() {
        onView(withId(R.id.panel_channel)).perform(ViewActions.click());
        Utility.checkFragmentIsOpen(R.id.admin_panel_fragment);
    }

    @Test
    public void canOpenUserRoleFragment() {
        onView(withId(R.id.panel_user)).perform(ViewActions.click());
        Utility.checkFragmentIsOpen(R.id.user_role_fragment);
    }

    @Test
    public void canOpenAddEventFragment() {
        onView(withId(R.id.panel_create_event)).perform(ViewActions.click());
        Utility.checkFragmentIsOpen(R.id.add_event_fragment);
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


}
