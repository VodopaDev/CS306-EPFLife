package ch.epfl.sweng.zuluzulu.Fragments;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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

    private User user;
    private Comparator<Event> currentComparator;

    private List<Event> actual_events;
    private List<Event> upcoming_events;
    private UpcomingEventArrayAdapter event_adapter;
    private ListView listview_event;

    List<Association> random_assos;
    AssociationArrayAdapter assos_adapter;
    private ListView listview_assos;

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

        actual_events = new ArrayList<>();
        upcoming_events = new ArrayList<>();
        event_adapter = new UpcomingEventArrayAdapter(getContext(), upcoming_events, mListener, user);
        currentComparator = Event.dateComparator();
        fillEventLists();

        random_assos = new ArrayList<>();
        assos_adapter = new AssociationArrayAdapter(getContext(), random_assos, mListener);
        fillAssociationLists();



    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (user.isConnected()) {
            boolean hadPermissions = GPS.start(getContext());
            if (!hadPermissions) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, GPS.MY_PERMISSIONS_REQUEST_LOCATION);
            }
            View view = inflater.inflate(R.layout.fragment_main_user, container, false);

            listview_event = view.findViewById(R.id.main_page_list_event);
            listview_event.setAdapter(event_adapter);

            listview_assos = view.findViewById(R.id.main_page_random_assos);
            listview_assos.setAdapter(assos_adapter);

            return view;
        } else {
            View view = inflater.inflate(R.layout.fragment_main, container, false);

            listview_event = view.findViewById(R.id.main_page_list_event);
            listview_event.setAdapter(event_adapter);

            listview_assos = view.findViewById(R.id.main_page_random_assos);
            listview_assos.setAdapter(assos_adapter);

            return view;
        }


    }

    private void fillEventLists() {
        DatabaseFactory.getDependency().getAllEvents(result -> {
            upcoming_events.clear();
            List<Event> temp = new ArrayList<>(result);
            for(Event e: temp) {
                if(e.getStartDate().compareTo(new Date()) <= 0) {
                    result.remove(e);
                }
            }
            Collections.sort(result, currentComparator);
            if(result.size() > 2) {
                result = new ArrayList<>(result.subList(0, 2));
            }

            upcoming_events.addAll(result);
            event_adapter.notifyDataSetChanged();
        });
    }

    private void sortWithCurrentComparator() {
        Collections.sort(upcoming_events, currentComparator);
        event_adapter.notifyDataSetChanged();
    }

    private void fillAssociationLists() {
        random_assos.clear();
        DatabaseFactory.getDependency().getAllAssociations(result -> {
            int rand = (int)(Math.random() * (result.size()));
            random_assos.add(result.get(rand));
            assos_adapter.notifyDataSetChanged();
        });
    }
}
