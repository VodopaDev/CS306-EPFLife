package ch.epfl.sweng.zuluzulu.fragments;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

import ch.epfl.sweng.zuluzulu.adapters.AssociationArrayAdapter;
import ch.epfl.sweng.zuluzulu.adapters.EventArrayAdapter;
import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.OnFragmentInteractionListener;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.structure.Association;
import ch.epfl.sweng.zuluzulu.structure.Event;
import ch.epfl.sweng.zuluzulu.structure.GPS;
import ch.epfl.sweng.zuluzulu.structure.user.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.structure.user.User;

import static ch.epfl.sweng.zuluzulu.CommunicationTag.OPEN_LOGIN_FRAGMENT;
import static ch.epfl.sweng.zuluzulu.fragments.SuperChatPostsFragment.ARG_USER;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends FragmentWithUser {
    public static final String TAG = "MAIN_TAG";

    private ArrayList<Association> associations_array;
    private ArrayList<Event> events_array;
    private AssociationArrayAdapter associations_adapter;
    private EventArrayAdapter events_adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public MainFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MainFragment.
     */
    public static MainFragment newInstance(User user) {
        if(user == null)
            throw new IllegalArgumentException("user can't be null");
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mListener.onFragmentInteraction(CommunicationTag.SET_TITLE, "Home");
        }

        associations_array = new ArrayList<>();
        events_array = new ArrayList<>();
        associations_adapter = new AssociationArrayAdapter(getContext(), associations_array, mListener);
        events_adapter = new EventArrayAdapter(getContext(), events_array, mListener, user);

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view;
        if (user.isConnected()) {
            boolean hadPermissions = GPS.start(getContext());
            if (!hadPermissions) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, GPS.MY_PERMISSIONS_REQUEST_LOCATION);
            }
            view = createConnectedUserView(inflater, container);

            swipeRefreshLayout = view.findViewById(R.id.swiperefresh_main_user);
            swipeRefreshLayout.setOnRefreshListener(this::refresh);

            fillConnectedUserAssociationsList(view);
            fillConnectedUserEventsList(view);

        } else {
            view = createNotConnectedUserView(inflater, container);

            swipeRefreshLayout = view.findViewById(R.id.swiperefresh_main);
            swipeRefreshLayout.setOnRefreshListener(this::refresh);

            fillUpcomingEventLists(view);
            fillRandomAssociationLists(view);
        }

        return view;
    }

    /**
     * Refresh the list of the channels
     */
    private void refresh() {
        swipeRefreshLayout.setRefreshing(true);
        if (user.isConnected()) {
            fillConnectedUserAssociationsList(getView());
            fillConnectedUserEventsList(getView());
        } else {
            fillUpcomingEventLists(getView());
            fillRandomAssociationLists(getView());
        }
    }

    private void fillUpcomingEventLists(View view) {
        events_array.clear();
        events_adapter.notifyDataSetChanged();

        LinearLayout progressBar = view.findViewById(R.id.linlaHeaderProgress_event);
        progressBar.setVisibility(View.VISIBLE);

        DatabaseFactory.getDependency().getEventsFromToday(result -> {
            if (result != null) {
                events_array.clear();
                events_array.addAll(result);
                events_adapter.notifyDataSetChanged();
                Collections.sort(events_array, Event.dateComparator());
            }
            progressBar.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
        }, 2);
    }

    private void fillRandomAssociationLists(View view) {
        associations_array.clear();
        associations_adapter.notifyDataSetChanged();

        LinearLayout progressBar = view.findViewById(R.id.linlaHeaderProgress_assos);
        progressBar.setVisibility(View.VISIBLE);

        DatabaseFactory.getDependency().getAllAssociations(result -> {
            if (result != null && !result.isEmpty()) {
                int rand = (int) (Math.random() * (result.size()));
                associations_array.add(result.get(rand));
                rand = (int) (Math.random() * (result.size()));
                if (!associations_array.contains(result.get(rand)))
                    associations_array.add(result.get(rand));
                associations_adapter.notifyDataSetChanged();
            }
            progressBar.setVisibility(View.GONE);
        });
    }

    /**
     * Return the view for a connected user
     * @param inflater inflater
     * @param container container to inflate
     * @return a connected user specific view
     */
    private View createConnectedUserView(LayoutInflater inflater, ViewGroup container) {
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
    private void fillConnectedUserAssociationsList(View view) {
        associations_array.clear();
        associations_adapter.notifyDataSetChanged();

        LinearLayout progressBar = view.findViewById(R.id.linlaHeaderProgress_user_assos);
        progressBar.setVisibility(View.VISIBLE);

        DatabaseFactory.getDependency().getAllAssociations(result -> {
            if (result != null) {
                for (Association association : result) {
                    if (((AuthenticatedUser) user).isFollowedAssociation(association.getId()))
                        associations_array.add(association);
                }
                associations_adapter.notifyDataSetChanged();
            }
            progressBar.setVisibility(View.GONE);
        });

    }

    /**
     * Fill the event_array with user's followed events
     */
    private void fillConnectedUserEventsList(View view) {
        events_array.clear();
        events_adapter.notifyDataSetChanged();

        LinearLayout progressBar = view.findViewById(R.id.linlaHeaderProgress_user_event);
        progressBar.setVisibility(View.VISIBLE);

        DatabaseFactory.getDependency().getAllEvents(result -> {
            if (result != null) {
                for (Event event : result) {
                    if (((AuthenticatedUser) user).isFollowedEvent(event.getId()))
                        events_array.add(event);
                }
                Collections.sort(events_array, Event.dateComparator());
                events_adapter.notifyDataSetChanged();
            }
            progressBar.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    /**
     * Create a view visible by a guest
     *
     * @param inflater
     * @param container
     * @return the guest's view
     */
    private View createNotConnectedUserView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        ListView listview_event = view.findViewById(R.id.main_page_list_event);
        listview_event.setAdapter(events_adapter);

        ListView listview_assos = view.findViewById(R.id.main_page_random_assos);
        listview_assos.setAdapter(associations_adapter);

        Button sign_in_button = view.findViewById(R.id.main_page_button_sign_in);

        sign_in_button.setOnClickListener(v -> mListener.onFragmentInteraction(OPEN_LOGIN_FRAGMENT, user));

        return view;
    }
}
