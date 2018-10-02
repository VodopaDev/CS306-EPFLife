package ch.epfl.sweng.zuluzulu;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

import ch.epfl.sweng.zuluzulu.Structure.User;


public class UserTest {

    @Test
    public void idTest(){
        int id = 4;
        User user = new User(0);
        user.setId(id);
        assertEquals(id, user.getId());
    }

    @Test
    public void firstNameTest(){
        String name = "name";
        User user = new User(0);
        user.setFirst_name(name);
        assertEquals(name, user.getFirst_name());
    }

    @Test
    public void lastNameTest(){
        String name = "name";
        User user = new User(0);
        user.setLast_name(name);
        assertEquals(name, user.getLast_name());
    }

    @Test
    public void assosTest(){
        List<Integer> list = Arrays.asList(1,2,3,4);
        User user = new User(0);
        user.setAssos_id(list);
        assertEquals(list, user.getAssos_id());
    }

    @Test
    public void chatsTest(){
        List<Integer> list = Arrays.asList(1,2,3,4);
        User user = new User(0);
        user.setChats_id(list);
        assertEquals(list, user.getChats_id());
    }

    @Test
    public void eventsTest(){
        List<Integer> list = Arrays.asList(1,2,3,4);
        User user = new User(0);
        user.setEvents_id(list);
        assertEquals(list, user.getEvents_id());
    }

}
