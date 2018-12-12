package ch.epfl.sweng.zuluzulu.Fragments;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ch.epfl.sweng.zuluzulu.Adapters.AssociationArrayAdapter;
import ch.epfl.sweng.zuluzulu.Adapters.EventArrayAdapter;
import ch.epfl.sweng.zuluzulu.Adapters.UpcomingEventArrayAdapter;
import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.OnFragmentInteractionListener;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Association;
import ch.epfl.sweng.zuluzulu.Structure.Event;
import ch.epfl.sweng.zuluzulu.Structure.GPS;
import ch.epfl.sweng.zuluzulu.User.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.User.User;

import static ch.epfl.sweng.zuluzulu.CommunicationTag.OPEN_LOGIN_FRAGMENT;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends SuperFragment {
    public static final String TAG = "MAIN_TAG";
    private static final String ARG_USER = "ARG_USER";
    List<Association> random_assos;
    AssociationArrayAdapter assos_adapter;
    Button sign_in_button;
    private User user;
    private Comparator<Event> currentComparator;
    private List<Event> upcoming_events;
    private UpcomingEventArrayAdapter event_adapter;
    private ArrayList<Association> associations_array;
    private ArrayList<Event> events_array;
    private AssociationArrayAdapter associations_adapter;
    private EventArrayAdapter events_adapter;

    public MainFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MainFragment.
     */
    public static MainFragment newInstance(User u) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, u);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mListener.onFragmentInteraction(CommunicationTag.SET_TITLE, "Home");
            user = (User) getArguments().getSerializable(ARG_USER);
        }

        upcoming_events = new ArrayList<>();
        event_adapter = new UpcomingEventArrayAdapter(getContext(), upcoming_events, mListener, user);
        currentComparator = Event.dateComparator();
        random_assos = new ArrayList<>();
        assos_adapter = new AssociationArrayAdapter(getContext(), random_assos, mListener);

        associations_array = new ArrayList<>();
        events_array = new ArrayList<>();
        associations_adapter = new AssociationArrayAdapter(getContext(), associations_array, mListener);
        events_adapter = new EventArrayAdapter(getContext(), events_array, mListener, user);

        if (user.isConnected()) {
            fillConnectedUserAssociationsList();
            fillConnectedUserEventsList();
        } else {
            fillUpcomingEventLists();
            fillRandomAssociationLists();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (user.isConnected()) {
            boolean hadPermissions = GPS.start(getContext());
            if (!hadPermissions) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, GPS.MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return createConnectedUserView(inflater, container);
        } else {
            return createNotConnectedUserView(inflater, container);
        }
    }


    private void fillUpcomingEventLists() {
        DatabaseFactory.getDependency().getEventsFromToday(result -> {
            if (result != null) {
                upcoming_events.addAll(result);
                event_adapter.notifyDataSetChanged();
            }
        }, 3);
    }

    private void sortWithCurrentComparator() {
        Collections.sort(upcoming_events, currentComparator);
        event_adapter.notifyDataSetChanged();
    }

    private void fillRandomAssociationLists() {
        random_assos.clear();
        DatabaseFactory.getDependency().getAllAssociations(result -> {
            if (result != null && !result.isEmpty()) {
                int rand = (int) (Math.random() * (result.size()));
                random_assos.add(result.get(rand));
                rand = (int) (Math.random() * (result.size()));
                if(!random_assos.contains(result.get(rand)))
                    random_assos.add(result.get(rand));
                assos_adapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Return a view visible by an authenticated user
     *
     * @param inflater
     * @param container
     * @return
     */
    public View createConnectedUserView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_main_user, container, false);
        ListView associations_listView = view.findViewById(R.id.main_fragment_followed_associations_listview);
        ListView events_listView = view.findViewById(R.id.main_fragment_followed_events_listview);
        associations_listView.setAdapter(associations_adapter);
        events_listView.setAdapter(events_adapter);
        return view;
    }

    /**
     * Fill the association_array with user's followed associations
     */
    private void fillConnectedUserAssociationsList() {
        DatabaseFactory.getDependency().getAllAssociations(result -> {
            if (result != null) {
                associations_array.clear();
                for (Association association : result) {
                    if (((AuthenticatedUser) user).isFollowedAssociation(association.getId()))
                        associations_array.add(association);
                }
                associations_adapter.notifyDataSetChanged();
            }
        });

    }

    /**
     * Fill the event_array with user's followed events
     */
    private void fillConnectedUserEventsList() {
        DatabaseFactory.getDependency().getAllEvents(result -> {
            if (result != null) {
                events_array.clear();
                for (Event event : result) {
                    if (((AuthenticatedUser) user).isFollowedEvent(event.getId()))
                        events_array.add(event);
                }
                Collections.sort(events_array, Event.dateComparator());
                events_adapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Create a view visible by a guest
     *
     * @param inflater
     * @param container
     * @return the guest's view
     */
    public View createNotConnectedUserView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        ListView listview_event = view.findViewById(R.id.main_page_list_event);
        listview_event.setAdapter(event_adapter);

        ListView listview_assos = view.findViewById(R.id.main_page_random_assos);
        listview_assos.setAdapter(assos_adapter);

        sign_in_button = view.findViewById(R.id.main_page_button_sign_in);

        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFragmentInteraction(OPEN_LOGIN_FRAGMENT, user);
            }
        });

        return view;

    }
}
