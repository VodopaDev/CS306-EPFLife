package ch.epfl.sweng.zuluzulu.Firebase;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class FirebaseMapDecoratorTest {
    Map<String, Object> map;

    @Before
    public void initMap() {
        map = new HashMap<>();
        map.put("int", 1L);
        map.put("long", 1L);
        map.put("string", "test");
        map.put("map", Collections.EMPTY_MAP);
        map.put("geopoint", new GeoPoint(1, 1));
        map.put("date", new Date(1L));
        map.put("list", Collections.EMPTY_LIST);
        map.put("long_list", Arrays.asList(1L, 2L));
        map.put("non_existent_list", null);
    }

    @Test
    public void hasFieldsTest() {
        FirebaseMapDecorator fmap = new FirebaseMapDecorator(map);
        assertThat(true, equalTo(fmap.hasFields(Arrays.asList("int", "geopoint"))));
        assertThat(false, equalTo(fmap.hasFields(Arrays.asList("int", "carotte"))));
    }

    @Test
    public void gettersTest() {
        FirebaseMapDecorator fmap = new FirebaseMapDecorator(map);
        assertThat(1, equalTo(fmap.getInteger("int")));
        assertThat(1L, equalTo(fmap.getLong("long")));
        assertThat("test", equalTo(fmap.getString("string")));
        assertThat(true, equalTo(fmap.getList("list").isEmpty()));
        assertThat(true, equalTo(fmap.getMap("map").isEmpty()));
        assertThat(1L, equalTo(fmap.getDate("date").getTime()));
        assertThat(1d, equalTo(fmap.getGeoPoint("geopoint").getLatitude()));
        assertThat(true, equalTo(fmap.get("date") instanceof Date));
        assertThat(Arrays.asList(1L, 2L), equalTo(fmap.getLongList("long_list")));
        assertThat(Arrays.asList(1, 2), equalTo(fmap.getIntegerList("long_list")));
        assertThat(new ArrayList<Integer>(), equalTo(fmap.getIntegerList("non_existent_list")));
    }

    @Test
    public void snapshotConstructor() {
        DocumentSnapshot snap = mock(DocumentSnapshot.class);
        when(snap.getData()).thenReturn(Collections.singletonMap("string", "test"));
        FirebaseMapDecorator fmap2 = new FirebaseMapDecorator(snap);

        assertThat("test", equalTo(fmap2.getString("string")));
    }

}
