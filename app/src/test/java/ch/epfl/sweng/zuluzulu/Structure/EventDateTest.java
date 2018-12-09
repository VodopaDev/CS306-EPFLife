package ch.epfl.sweng.zuluzulu.Structure;

import org.junit.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.instanceOf;
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

    @Test
    public void getDateTime() {
        Date d = new Date();
        EventDate date = new EventDate(d, d);
        assertThat(date.getDateTimeUser(), instanceOf(String.class));
    }

    @Test
    public void getDateTimeLater() {
        Date d = new Date();
        d.setYear(d.getYear()+1);
        EventDate date = new EventDate(d, d);
        assertThat(date.getDateTimeUser(), instanceOf(String.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void setWithNullEndDate() {
        new EventDate("1111-11-11", "11:11:11", "1111-11-11", "11:11:11").setEndDate(null);
    }

    @Test(expected = AssertionError.class)
    public void getWithNullStartTime() {
        new EventDate("1111-11-11", "11:11:11", "1111-11-11", null);
    }

    @Test(expected = AssertionError.class)
    public void getWithNullEndTime() {
        new EventDate("1111-11-11", null, "1111-11-11", "11:11:11");
    }

    @Test(expected = IllegalArgumentException.class)
    public void getWithNullDateStart() {
        new EventDate(null, "11:11:11", "1111-11-11", "11:11:11");
    }

    @Test(expected = IllegalArgumentException.class)
    public void getWithNullDateEnd() {
        new EventDate("1111-11-11", "11:11:11", null, "11:11:11");
    }

    @Test(expected = IllegalArgumentException.class)
    public void fakeStartDate() {
        new EventDate("1111-11-11", "11:11:11", "1111-a9-11", "11:11:11");
    }

    @Test(expected = IllegalArgumentException.class)
    public void fakeEndDate() {
        new EventDate("1111-99-9a", "11:11:11", "1111-11-11", "11:11:11");
    }
}