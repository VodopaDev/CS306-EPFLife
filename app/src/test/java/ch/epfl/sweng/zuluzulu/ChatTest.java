package ch.epfl.sweng.zuluzulu;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import ch.epfl.sweng.zuluzulu.Structure.Chat;

import static org.junit.Assert.assertEquals;

public class ChatTest {

    @Test
    public void idTest(){
        int id = 4;
        Chat chat = new Chat(0);
        chat.setId(id);
        assertEquals(id, chat.getId());
    }

    @Test
    public void nameTest(){
        String name = "name";
        Chat chat = new Chat(0);
        chat.setName(name);
        assertEquals(name, chat.getName());
    }

    @Test
    public void usersTest(){
        List<Integer> list = Arrays.asList(0,2,3,5);
        Chat chat = new Chat(0);
        chat.setUsers(list);
        assertEquals(list, chat.getUsers());
    }

    @Test
    public void adminsTest(){
        List<Integer> list = Arrays.asList(0,2,3,5);
        Chat chat = new Chat(0);
        chat.setAdmins(list);
        assertEquals(list, chat.getAdmins());
    }

    @Test
    public void messagesTest(){
        List<String> list = Arrays.asList("l","wkdsk");
        Chat chat = new Chat(0);
        chat.setMessages(list);
        assertEquals(list, chat.getMessages());
    }

    @Test
    public void assoIdTest(){
        int id = 4;
        Chat chat = new Chat(0);
        chat.setAsso_id(id);
        assertEquals(id, chat.getAsso_id());
    }

    @Test
    public void eventIdTest(){
        int id = 4;
        Chat chat = new Chat(0);
        chat.setEvent_id(id);
        assertEquals(id,chat.getEvent_id());
    }
}
