package ch.epfl.sweng.zuluzulu.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.imanoweb.calendarview.CalendarListener;
import com.imanoweb.calendarview.CustomCalendarView;
import com.imanoweb.calendarview.DayDecorator;
import com.imanoweb.calendarview.DayView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ch.epfl.sweng.zuluzulu.Adapters.EventArrayAdapter;
<<<<<<< HEAD
import ch.epfl.sweng.zuluzulu.Firebase.FirebaseProxy;
=======
import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.IdlingResource.IdlingResourceFactory;
>>>>>>> master
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Event;
import ch.epfl.sweng.zuluzulu.Structure.Utils;
import ch.epfl.sweng.zuluzulu.User.AuthenticatedUser;


public class CalendarFragment extends SuperFragment {

    private static final String ARG_USER = "ARG_USER";
    private AuthenticatedUser user;

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
                selectedDayEvents.clear();
                for (Event event : followedEvents) {
                    if (event.getStartDateString().equals(Utils.dateFormat.format(date)))
                        selectedDayEvents.add(event);
                }
                eventAdapter.notifyDataSetChanged();
                calendarView.refreshCalendar(calendar);
                calendarView.markDayAsSelectedDay(date);
            }

            //Required method, but not used here
            @Override
            public void onMonthChanged(Date date) {
            }
        });
        //Set the day decorator
        calendarView.setDecorators(Collections.singletonList(dayView -> {
            Date dayDate = dayView.getDate();
            for (Event event : followedEvents) {
                if (event.getStartDateString().equals(Utils.dateFormat.format(dayDate))
                        && user.isFollowedEvent(event.getId()))
                    dayView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
            }
        }));
        //Fill the followed events list from user's preferences
        fillFollowedEventsList();

        return view;
    }

    private void fillFollowedEventsList() {
        FirebaseProxy.getInstance().getEventsFromIds(user.getFollowedEvents(), result -> {
            Date date = Timestamp.now().toDate();
            String now = Utils.dateFormat.format(date);
            for(Event event: result) {
                followedEvents.add(event);
                if (now.equals(event.getStartDateString()))
                    selectedDayEvents.add(event);
            }

            eventAdapter.notifyDataSetChanged();
            if (getContext() != null)
                calendarView.refreshCalendar(calendar);
            calendarView.markDayAsSelectedDay(date);
        });
    }
}
