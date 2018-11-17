package ch.epfl.sweng.zuluzulu;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.StringReader;

import ch.epfl.sweng.zuluzulu.Database.FirebaseMock;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.Fragments.AssociationsGeneratorFragment;
import ch.epfl.sweng.zuluzulu.Fragments.MainFragment;
import ch.epfl.sweng.zuluzulu.TestingUtility.TestWithAdminAndFragment;
import ch.epfl.sweng.zuluzulu.URLTools.UrlReader;
import ch.epfl.sweng.zuluzulu.URLTools.UrlReaderFactory;


public class MementoFragmentTest extends TestWithAdminAndFragment<MainFragment> {

    @Override
    public void initFragment() {
        DatabaseFactory.setDependency(new FirebaseMock());

        UrlReader reader = new UrlReader() {
            @Override
            public BufferedReader read(String name) {
                return new BufferedReader(new StringReader(
                        "[\n" +
                                "    {\n" +
                                "        \"translation_id\": \"76800\",\n" +
                                "        \"title\": \"MoveOn - Free dance lessons\",\n" +
                                "        \"slug\": \"moveon-free-dance-lessons\",\n" +
                                "        \"absolute_slug\": \"https://memento.epfl.ch/event/moveon-free-dance-lessons\",\n" +
                                "        \"lang\": \"en\",\n" +
                                "        \"state\": \"Published\",\n" +
                                "        \"description\": \"<p>MoveOn, a commission of Agepoly, offer free disco-fox lessons. Disco-fox is a couple dance that is performed on current music, on what you can hear on the radio for example. The lessons take place in the Polydôme at EPFL on Tuesdays. <br>\\r\\n<br>\\r\\n18h30 : Hustle (4-count disco-fox, recommended for complete beginners)<br>\\r\\n19h45 : Disco-fox, with choreography <br>\\r\\n<br>\\r\\nThe lessons are for beginners. No registration, you can just come to the lesson. <br>\\r\\n<br>\\r\\nMore infos : fb.me/moveonepfl</p>\",\n" +
                                "        \"image_description\": \"\",\n" +
                                "        \"creation_date\": \"2018-10-05T15:57:04\",\n" +
                                "        \"last_modification_date\": \"2018-10-08T15:10:35\",\n" +
                                "        \"trash_date\": null,\n" +
                                "        \"delete_date\": null,\n" +
                                "        \"cancel_reason\": \"\",\n" +
                                "        \"event_id\": \"43453\",\n" +
                                "        \"event_visual_absolute_url\": null,\n" +
                                "        \"event_start_date\": \"2018-09-25\",\n" +
                                "        \"event_end_date\": \"2018-12-18\",\n" +
                                "        \"event_start_time\": \"18:30:00\",\n" +
                                "        \"event_end_time\": \"21:00:00\",\n" +
                                "        \"event_place_and_room\": \"PO 094.0\",\n" +
                                "        \"event_url_place_and_room\": \"https://plan.epfl.ch/?room=PO094.0\",\n" +
                                "        \"event_speaker\": \"Mike Bardet, Eva Lorendeaux\",\n" +
                                "        \"event_organizer\": \"Mike Bardet\",\n" +
                                "        \"event_contact\": \"Mike Bardet\",\n" +
                                "        \"event_theme\": \"\",\n" +
                                "        \"event_filters\": \"Danse, Cours, Disco-fox\",\n" +
                                "        \"event_canceled\": \"False\",\n" +
                                "        \"event_category_id\": \"4\",\n" +
                                "        \"event_category_code\": \"DIVERS\",\n" +
                                "        \"event_category_fr\": \"Divers\",\n" +
                                "        \"event_category_en\": \"Miscellaneous\",\n" +
                                "        \"event_is_internal\": \"False\",\n" +
                                "        \"event_vulgarization\": \"Tout public\",\n" +
                                "        \"event_invitation\": \"Libre\",\n" +
                                "        \"event_label_link\": \"\",\n" +
                                "        \"event_url_link\": \"\",\n" +
                                "        \"event_is_to_homepage\": \"False\",\n" +
                                "        \"event_is_from_migration\": \"False\"\n" +
                                "    }"
                        + "]"
                ));
            }
        };
        // Change the factory
        UrlReaderFactory.setDependency(reader);
    }

    @Test
    public void refuseNonAdmin() {
        nonAdminUser();
        Utility.checkFragmentIsClosed(R.id.associations_generator_fragment);
    }

    private void nonAdminUser() {
        mActivityRule.getActivity().openFragment(AssociationsGeneratorFragment.newInstance(Utility.createTestAuthenticated()));
    }


}