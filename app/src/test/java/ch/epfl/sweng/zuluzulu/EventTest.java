package ch.epfl.sweng.zuluzulu;

import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import ch.epfl.sweng.zuluzulu.Structure.Event;

import static org.junit.Assert.assertEquals;


public class EventTest {

    // TODO: Add a test for Location and Image

    @Test
    public void idTest() {
        int id = 4;
        Event event = new Event(0);
        event.setId(id);
        assertEquals(id, event.getId());
    }

    @Test
    public void mainChatIdTest() {
        int chat_id = 4;
        Event event = new Event(0);
        event.setChat_id(chat_id);
        assertEquals(chat_id, event.getChat_id());
    }

    @Test
    public void nameTest() {
        String name = "nom";
        Event event = new Event(0);
        event.setName(name);
        assertEquals(name, event.getName());
    }

    @Test
    public void descriptionTest() {
        String desc = "random thing";
        Event event = new Event(0);
        event.setDescription(desc);
        assertEquals(desc, event.getDescription());
    }

    @Test
    public void adminsTest() {
        List<Integer> admins = Arrays.asList(2, 3, 4, 5);

        Event event = new Event(0);
        event.setAdmins(admins);
        assertEquals(admins, event.getAdmins());
    }

    @Test
    public void startDateTest() {
        Date start = new Date(2);
        Event event = new Event(0);
        event.setStart_date(start);
        assertEquals(start, event.getStart_date());
    }

    @Test
    public void endDateTest() {
        Date end = new Date(2);
        Event event = new Event(0);
        event.setEnd_date(end);
        assertEquals(end, event.getEnd_date());
    }

    @Test
    public void assoIdTest() {
        int asso_id = 4;
        Event event = new Event(0);
        event.setAsso_id(asso_id);
        assertEquals(asso_id, event.getAsso_id());
    }


}
