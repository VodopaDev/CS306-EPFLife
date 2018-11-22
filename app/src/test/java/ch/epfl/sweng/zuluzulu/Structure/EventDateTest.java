package ch.epfl.sweng.zuluzulu.Structure;

import org.junit.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class EventDateTest {

    @Test
    public void getStartDateString() {
        EventDate date = new EventDate("1111-11-11", "11:11:11", "1111-11-11", "11:11:11");
        assertThat(date.getStartDateString(), is("1111-11-11 11:11:11"));
    }

    @Test
    public void getEndDateString() {
        EventDate date = new EventDate("1111-11-11", "11:11:11", "1111-11-11", "11:11:11");
        assertThat(date.getEndDateString(), is("1111-11-11 11:11:11"));
    }

    @Test
    public void getWithNoTime() {
        EventDate date = new EventDate("1111-11-11", "1111-11-11");
        assertThat(date.getEndDateString(), is("1111-11-11 23:59:59"));
        assertThat(date.getStartDateString(), is("1111-11-11 00:00:01"));
    }

    @Test
    public void getDate() {
        Date d = new Date();
        EventDate date = new EventDate(d, d);
        assertThat(date.getStartDate(), is(d));
        assertThat(date.getEndDate(), is(d));
    }

    @Test(expected = AssertionError.class)
    public void getWithNullStartTime() {
        EventDate date = new EventDate("1111-11-11", "11:11:11", "1111-11-11", null);
    }

    @Test(expected = AssertionError.class)
    public void getWithNullEndTime() {
        EventDate date = new EventDate("1111-11-11", null, "1111-11-11", "11:11:11");
    }

    @Test(expected = IllegalArgumentException.class)
    public void getWithNullDateStart() {
        EventDate date = new EventDate(null, "11:11:11", "1111-11-11", "11:11:11");
    }

    @Test(expected = IllegalArgumentException.class)
    public void getWithNullDateEnd() {
        EventDate date = new EventDate("1111-11-11", "11:11:11", null, "11:11:11");
    }

    @Test(expected = IllegalArgumentException.class)
    public void fakeStartDate() {
        EventDate date = new EventDate("1111-11-11", "11:11:11", "1111-a9-11", "11:11:11");
    }

    @Test(expected = IllegalArgumentException.class)
    public void fakeEndDate() {
        EventDate date = new EventDate("1111-99-9a", "11:11:11", "1111-11-11", "11:11:11");
    }
}