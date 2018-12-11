package ch.epfl.sweng.zuluzulu.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.Timestamp;
import com.imanoweb.calendarview.CalendarListener;
import com.imanoweb.calendarview.CustomCalendarView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ch.epfl.sweng.zuluzulu.Adapters.EventArrayAdapter;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Event;
import ch.epfl.sweng.zuluzulu.User.AuthenticatedUser;


public class CalendarFragment extends SuperFragment {

    private static final String ARG_USER = "ARG_USER";
    private AuthenticatedUser user;

    private Date today;
    private Date tomorrow;

    private List<Event> followedEvents;
    private List<Event> selectedDayEvents;

    private EventArrayAdapter eventAdapter;
    private ListView list;

    private CustomCalendarView calendarView;
    private Calendar calendar;

    public CalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CalendarFragment.
     */
    public static CalendarFragment newInstance(AuthenticatedUser user) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (AuthenticatedUser) getArguments().getSerializable(ARG_USER);
        }

        followedEvents = new ArrayList<>();
        selectedDayEvents = new ArrayList<>();
        eventAdapter = new EventArrayAdapter(getContext(), selectedDayEvents, mListener, user);
        Pair<Date, Date> pair = getStartAndEndOfTheDay(Timestamp.now().toDate());
        today = pair.first;
        tomorrow = pair.second;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        list = view.findViewById(R.id.calendar_list);
        list.setAdapter(eventAdapter);

        //Initialize CustomCalendarView from layout
        calendarView = view.findViewById(R.id.calendar_view);
        //Initialize calendarView with date
        calendar = Calendar.getInstance(Locale.getDefault());
        //Show Monday as first date of week
        calendarView.setFirstDayOfWeek(Calendar.MONDAY);
        //Show/hide overflow days of a month
        calendarView.setShowOverflowDate(true);
        //call refreshCalendar to update calendarView the view
        calendarView.refreshCalendar(calendar);
        //Handling custom calendarView events
        calendarView.setCalendarListener(new CalendarListener() {
            @Override
            public void onDateSelected(Date date) {
                Pair<Date, Date> pair = getStartAndEndOfTheDay(date);
                selectedDayEvents.clear();
                for (Event event : followedEvents) {
                    if (isCurrentlyGoing(event, pair.first, pair.second))
                        selectedDayEvents.add(event);
                }
                eventAdapter.notifyDataSetChanged();
                calendarView.markDayAsSelectedDay(date);
            }

            //Required method, but not used here
            @Override
            public void onMonthChanged(Date date) {
            }
        });
        //Set the day decorator
        calendarView.setDecorators(Collections.singletonList(dayView -> {
            Pair<Date, Date> pair = getStartAndEndOfTheDay(dayView.getDate());
            for (Event event : followedEvents) {
                if (isCurrentlyGoing(event, pair.first, pair.second))
                    dayView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
            }
        }));
        //Fill the followed events list from user's preferences
        fillFollowedEventsList();

        return view;
    }

    /**
     * Fill the event list from the database
     */
    private void fillFollowedEventsList() {
        DatabaseFactory.getDependency().getEventsFromIds(user.getFollowedEvents(), result -> {
            for (Event event : result) {
                followedEvents.add(event);
                if (isCurrentlyGoing(event, today, tomorrow))
                    selectedDayEvents.add(event);
            }

            eventAdapter.notifyDataSetChanged();
            if (getContext() != null)
                calendarView.refreshCalendar(calendar);
            calendarView.markDayAsSelectedDay(today);
        });
    }

    /**
     * Return if an event is going on between two dates
     *
     * @param event event to check
     * @param start lower_bound date
     * @param end   upper_bound date
     * @return true if an event is going on between the two dates
     */
    private boolean isCurrentlyGoing(Event event, Date start, Date end) {
        assert (start.before(end));
        return (event.getStartDate().after(start) && event.getStartDate().before(end)) ||
                (event.getEndDate().after(start) && event.getStartDate().before(end)) ||
                (event.getStartDate().before(start) && event.getEndDate().after(end));
    }

    /**
     * Return the start and end of a day of a date
     *
     * @param date date
     * @return the beginning and ending of the day in a pair
     */
    private Pair<Date, Date> getStartAndEndOfTheDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 1);
        Date start = calendar.getTime();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        Date end = calendar.getTime();
        Log.d("TODAY and TOMORROW", start.toString() + "   " + end.toString());
        return new Pair<>(start, end);
    }
}
