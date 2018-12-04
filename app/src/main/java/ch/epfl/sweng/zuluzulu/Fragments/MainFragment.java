package ch.epfl.sweng.zuluzulu.Fragments;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

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

    private ListView associations_listView;
    private ListView events_listView;
    private ArrayList<Association> associations_array;
    private ArrayList<Event> events_array;

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

        associations_array = new ArrayList<>();
        events_array = new ArrayList<>();
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

    /*
     * connected user
     */
    public View createConnectedUserView(LayoutInflater inflater, ViewGroup container){
        View view = inflater.inflate(R.layout.fragment_main_user, container, false);

        fillAssociationsList();
//        fillEventsList();

        associations_listView = view.findViewById(R.id.main_fragment_followed_associations_listview);
        events_listView = view.findViewById(R.id.main_fragment_followed_events_listview);

        return view;
    }

    private void fillAssociationsList() {
        DatabaseFactory.getDependency().getAllAssociations(result -> {
            associations_array.clear();
            associations_array.addAll(result);
            for (Association association : associations_array) {
                if (((AuthenticatedUser) user).isFollowedAssociation(association.getId()))
                    associations_array.add(association);
            }
        });
    }

    /*
     * guest user
     */
    public View createNotConnectedUserView(LayoutInflater inflater, ViewGroup container){
        return inflater.inflate(R.layout.fragment_main, container, false);
    }
}
