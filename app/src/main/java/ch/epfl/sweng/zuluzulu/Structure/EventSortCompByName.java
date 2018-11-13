package ch.epfl.sweng.zuluzulu.Structure;

import java.util.Comparator;

public class EventSortCompByName implements Comparator<Event>
{
    public int compare(Event a, Event b)
    {
        return a.getName().compareTo(b.getName());
    }
}

