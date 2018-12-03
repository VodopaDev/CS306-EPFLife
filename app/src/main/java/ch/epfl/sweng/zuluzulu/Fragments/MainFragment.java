package ch.epfl.sweng.zuluzulu.Fragments;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.epfl.sweng.zuluzulu.Adapters.AssociationArrayAdapter;
import ch.epfl.sweng.zuluzulu.Adapters.EventArrayAdapter;
import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.OnFragmentInteractionListener;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Association;
import ch.epfl.sweng.zuluzulu.Structure.Event;
import ch.epfl.sweng.zuluzulu.Structure.GPS;
import ch.epfl.sweng.zuluzulu.User.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.User.User;
import ch.epfl.sweng.zuluzulu.Utility.FirebaseUtils;

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
    private List<Event> upcoming_events;
    private EventArrayAdapter event_adapter;
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
        upcoming_events = new ArrayList<>();
        event_adapter = new EventArrayAdapter(getContext(), upcoming_events, mListener, user);
        //fillUpcomingEventFromDatabase();
        FirebaseUtils.fillEventFromDatabase("events_info", "start_date", upcoming_events, event_adapter);

        random_assos = new ArrayList<>();



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

            return view;
        } else {
            View view = inflater.inflate(R.layout.fragment_main, container, false);

            listview_event = view.findViewById(R.id.main_page_list_event);
            listview_event.setAdapter(event_adapter);

            return view;
        }


    }

    void fillUpcomingEventFromDatabase() {
        FirebaseFirestore.getInstance().collection("events_info").orderBy("start_date").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> snap_list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot snap : snap_list) {
                            FirebaseMapDecorator fmap = new FirebaseMapDecorator(snap);
                            if (fmap.hasFields(Event.FIELDS) && fmap.getDate("start_date").compareTo(new Date(System.currentTimeMillis())) >= 0) {
                                Event event = new Event.EventBuilder().build(fmap);
                                upcoming_events.add(event);
                                event_adapter.notifyDataSetChanged();
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(getView(), "Loading error, check your connection", 5000).show();
                        Log.e("EVENT_LIST", "Error fetching event date\n" + e.getMessage());
                    }
                });
    }
}
