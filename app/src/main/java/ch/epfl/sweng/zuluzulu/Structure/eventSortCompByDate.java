package ch.epfl.sweng.zuluzulu.Structure;

import java.util.Comparator;

public class eventSortCompByDate  implements Comparator<Event>
{
    public int compare(Event a, Event b)
    {
        return a.getStartDate().compareTo(b.getStartDate());
    }
}
