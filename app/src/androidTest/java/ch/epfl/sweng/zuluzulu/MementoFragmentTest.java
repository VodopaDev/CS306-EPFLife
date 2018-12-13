package ch.epfl.sweng.zuluzulu;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.concurrent.TimeUnit;

import ch.epfl.sweng.zuluzulu.Database.MockedProxy;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.Fragments.AdminFragments.MementoFragment;
import ch.epfl.sweng.zuluzulu.Fragments.MainFragment;
import ch.epfl.sweng.zuluzulu.TestingUtility.TestWithAdminAndFragment;
import ch.epfl.sweng.zuluzulu.URLTools.UrlReader;
import ch.epfl.sweng.zuluzulu.URLTools.UrlReaderFactory;


public class MementoFragmentTest {
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);
    @Before
    public void initFragment() {
        DatabaseFactory.setDependency(new MockedProxy());
    }

    @Test
    public void canOpenFragment() {
        UrlReader reader = new UrlReader() {
            @Override
            public BufferedReader read(String name) {
                return new BufferedReader(new StringReader("[{\n" +
                        "        \"translation_id\": \"76058\",\n" +
                        "        \"title\": \"Nocturne de JDR-Poly\",\n" +
                        "        \"slug\": \"nocturne-de-jdr-poly-35\",\n" +
                        "        \"absolute_slug\": \"https://memento.epfl.ch/event/nocturne-de-jdr-poly-35\",\n" +
                        "        \"lang\": \"fr\",\n" +
                        "        \"state\": \"Published\",\n" +
                        "        \"description\": \"<strong>Ouvertes à tous, les Nocturnes proposées par JDR-Poly sont de nouveau de la partie ! L'association de jeu de rôle de l'EPFL le plaisir de vous convier à l'une de ses soirées d'initiation, ouvertes à tous. Que vous soyez un débutant, un vétéran ou même un maître du jeu, nous serons heureux de vous accueillir en INM 202 à 19h10 afin de passer une soirée dans la bonne humeur, ponctuée par les doux bruits des dés. Plus d'informations :<a href=\\\"https://jdrpoly.ch/events/1\\\"> https://jdrpoly.ch/events/1</a></strong>\",\n" +
                        "        \"image_description\": \"\",\n" +
                        "        \"creation_date\": \"2018-09-11T19:27:52\",\n" +
                        "        \"last_modification_date\": \"2018-09-11T19:30:18\",\n" +
                        "        \"trash_date\": null,\n" +
                        "        \"delete_date\": null,\n" +
                        "        \"cancel_reason\": \"\",\n" +
                        "        \"event_id\": \"43016\",\n" +
                        "        \"event_visual_absolute_url\": \"https://memento.epfl.ch/image/12316/112x112.jpg\",\n" +
                        "        \"event_start_date\": \"2018-12-19\",\n" +
                        "        \"event_end_date\": \"2018-12-19\",\n" +
                        "        \"event_start_time\": \"19:10:00\",\n" +
                        "        \"event_end_time\": \"23:50:00\",\n" +
                        "        \"event_place_and_room\": \"EPFL, INM 202\",\n" +
                        "        \"event_url_place_and_room\": \"http://plan.epfl.ch/?room=INM%20202\",\n" +
                        "        \"event_speaker\": \"\",\n" +
                        "        \"event_organizer\": \"JDR-Poly\",\n" +
                        "        \"event_contact\": \"communication@jdrpoly.ch\",\n" +
                        "        \"event_theme\": \"Jeu de rôle, Association\",\n" +
                        "        \"event_filters\": \"Jeu de rôle, JDR, JDR-Poly\",\n" +
                        "        \"event_canceled\": \"False\",\n" +
                        "        \"event_category_id\": \"9\",\n" +
                        "        \"event_category_code\": \"MANIF\",\n" +
                        "        \"event_category_fr\": \"Manifestations culturelles\",\n" +
                        "        \"event_category_en\": \"Cultural events\",\n" +
                        "        \"event_is_internal\": \"False\",\n" +
                        "        \"event_vulgarization\": \"Tout public\",\n" +
                        "        \"event_invitation\": \"Libre\",\n" +
                        "        \"event_label_link\": \"\",\n" +
                        "        \"event_url_link\": \"\",\n" +
                        "        \"event_is_to_homepage\": \"False\",\n" +
                        "        \"event_is_from_migration\": \"False\"\n" +
                        "    }\n" +
                        "]"));
            }
        };
        // Change the factory
        UrlReaderFactory.setDependency(reader);
        adminUser();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Utility.openMenu();
    }

    @Test
    public void doNotCrashWithFakeJson() {
        UrlReader reader = new UrlReader() {
            @Override
            public BufferedReader read(String name) {
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
        UrlReader reader = new UrlReader() {
            @Override
            public BufferedReader read(String name) {
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
        nonAdminUser();
        Utility.checkFragmentIsClosed(R.id.memento_fragment);
    }

    private void nonAdminUser() {
        mActivityRule.getActivity().openFragment(MementoFragment.newInstance(Utility.createTestAuthenticated()));
    }

    private void adminUser() {
        mActivityRule.getActivity().openFragment(MementoFragment.newInstance(Utility.createTestAdmin()));
    }


}
