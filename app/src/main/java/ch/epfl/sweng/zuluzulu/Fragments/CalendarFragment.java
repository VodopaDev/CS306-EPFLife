package ch.epfl.sweng.zuluzulu.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.epfl.sweng.zuluzulu.Adapters.EventArrayAdapter;
import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Association;
import ch.epfl.sweng.zuluzulu.Structure.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.Structure.Event;
import ch.epfl.sweng.zuluzulu.Structure.User;
import ch.epfl.sweng.zuluzulu.Structure.Utils;


public class CalendarFragment extends SuperFragment {

    private static final String ARG_USER = "ARG_USER";
    private AuthenticatedUser user;

    private List<Event> followedEvents;
    private List<Event> selectedDayEvents;

    private EventArrayAdapter eventAdapter;
    private ListView list;

    private CalendarView calendar;

    public CalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
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
            user = (AuthenticatedUser)getArguments().getSerializable(ARG_USER);
        }

        followedEvents = new ArrayList<>();
        selectedDayEvents = new ArrayList<>();
        eventAdapter = new EventArrayAdapter(getContext(), selectedDayEvents, mListener);
        fillFollowedEventsList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        calendar = view.findViewById(R.id.calendar);
        list = view.findViewById(R.id.calendar_list);
        list.setAdapter(eventAdapter);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedDayEvents.removeAll(selectedDayEvents);
                String selectedDateString = Utils.dateFormat.format(new Date(year - 1900,month,dayOfMonth));
                Log.d("CALENDAR", "selected date " + selectedDateString);

                for(Event event: followedEvents){
                    Log.d("CALENDAR", "followed event with date " + event.getStartDateString());
                    if(selectedDateString.equals(event.getStartDateString()))
                        selectedDayEvents.add(event);
                }
                eventAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    private void fillFollowedEventsList() {
        FirebaseFirestore.getInstance().collection("events_info").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> snap_list = queryDocumentSnapshots.getDocuments();
                        String now = Utils.dateFormat.format(Timestamp.now().toDate());
                        for (DocumentSnapshot snap : snap_list) {
                            FirebaseMapDecorator fmap = new FirebaseMapDecorator(snap);
                            if (fmap.hasFields(Event.FIELDS)) {
                                Event event = new Event(fmap);
                                if (user.isFollowedEvent(event)) {
                                    followedEvents.add(event);
                                    Log.d("CALENDAR", "added a new followed event with date " + event.getStartDateString());
                                    if(now.equals(event.getStartDateString()))
                                        selectedDayEvents.add(event);
                                }
                            }
                        }
                        eventAdapter.notifyDataSetChanged();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(getView(), "Loading error, check your connection", 5000).show();
                        Log.e("EVENT_LIST", "Error fetching event\n" + e.getMessage());
                    }
                });
    }
}
