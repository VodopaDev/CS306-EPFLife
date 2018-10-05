package ch.epfl.sweng.zuluzulu;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import ch.epfl.sweng.zuluzulu.Structure.Association;

import static org.junit.Assert.assertEquals;

public class AssociationTest {

    // TODO: add a test for Location and Image

    @Test
    public void idTest() {
        int id = 4;
        Association asso = new Association(0);
        asso.setId(id);
        assertEquals(id, asso.getId());
    }

    @Test
    public void mainChatIdTest() {
        int chat_id = 4;
        Association asso = new Association(0);
        asso.setMain_chat_id(chat_id);
        assertEquals(chat_id, asso.getMain_chat_id());
    }

    @Test
    public void nameTest() {
        String name = "nom";
        Association asso = new Association(0);
        asso.setName(name);
        assertEquals(name, asso.getName());
    }

    @Test
    public void descriptionTest() {
        String desc = "random thing";
        Association asso = new Association(0);
        asso.setDescription(desc);
        assertEquals(desc, asso.getDescription());
    }

    @Test
    public void adminsTest() {
        List<Integer> admins = Arrays.asList(2, 3, 4, 5);

        Association asso = new Association(0);
        asso.setAdmins(admins);
        assertEquals(admins, asso.getAdmins());
    }

    @Test
    public void eventsTest() {
        List<Integer> events = Arrays.asList(3, 4, 5);

        Association asso = new Association(0);
        asso.setEvents(events);
        assertEquals(events, asso.getEvents());
    }

    @Test
    public void chatsTest() {
        List<Integer> chats = Arrays.asList(3, 4, 5);

        Association asso = new Association(0);
        asso.setChats(chats);
        assertEquals(chats, asso.getChats());
    }

    @Test
    public void Test() {
        int id = 4;
        Association asso = new Association(0);
        asso.setId(id);
        assertEquals(id, asso.getId());
    }
}
