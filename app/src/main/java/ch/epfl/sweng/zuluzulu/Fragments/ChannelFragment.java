package ch.epfl.sweng.zuluzulu.Fragments;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.zuluzulu.Adapters.ChannelArrayAdapter;
import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.OnFragmentInteractionListener;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.Structure.Channel;
import ch.epfl.sweng.zuluzulu.Structure.GPS;
import ch.epfl.sweng.zuluzulu.Structure.User;
import ch.epfl.sweng.zuluzulu.Structure.Utils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChannelFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChannelFragment extends SuperFragment {
    private static final String TAG = "CHANNEL_TAG";
    private static final String ARG_USER = "ARG_USER";
    private static final String CHANNELS_COLLECTION_NAME = "channels";

    private View view;
    private SwipeRefreshLayout swipeRefreshLayout;

    private List<Channel> listOfChannels = new ArrayList<>();
    private ChannelArrayAdapter adapter;

    private User user;
    private GeoPoint userLocation;

    public ChannelFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ChannelFragment.
     */
    public static ChannelFragment newInstance(User user) {
        ChannelFragment fragment = new ChannelFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable(ARG_USER);
            mListener.onFragmentInteraction(CommunicationTag.SET_TITLE, "Channels");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_channel, container, false);
        ListView listView = view.findViewById(R.id.channels_list_view);

        adapter = new ChannelArrayAdapter(view.getContext(), listOfChannels);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Channel selectedChannel = listOfChannels.get(position);
                mListener.onFragmentInteraction(CommunicationTag.OPEN_CHAT_FRAGMENT, selectedChannel);
            }
        });

        swipeRefreshLayout = view.findViewById(R.id.swiperefresh_channel);
        swipeRefreshLayout.setOnRefreshListener(this::refresh);

        refreshPosition();
        getChannelsFromDatabase();

        return view;
    }

    /**
     * Read data from the database and get the list of the channels
     */
    private void getChannelsFromDatabase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(CHANNELS_COLLECTION_NAME).orderBy("id", Query.Direction.ASCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            listOfChannels.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                FirebaseMapDecorator fmap = new FirebaseMapDecorator(document);
                                if (fmap.hasFields(Channel.FIELDS)) {
                                    Channel channel = new Channel(fmap);
                                    if (user.isConnected() && channel.canBeAccessedBy((AuthenticatedUser) user, userLocation)) {
                                        listOfChannels.add(channel);
                                    }
                                }
                            }
                            adapter.notifyDataSetChanged();
                            swipeRefreshLayout.setRefreshing(false);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    /**
     * Refresh the list of the channels
     */
    private void refresh() {
        if (!GPS.isActivated()) {
            Snackbar.make(view, "Please activate your GPS to have access to all channels", 2000).show();
            userLocation = null;
        }
        swipeRefreshLayout.setRefreshing(true);
        refreshPosition();
        getChannelsFromDatabase();
    }

    /**
     * Refresh the current position
     */
    private void refreshPosition() {
        Location gpsLocation = GPS.getLocation();
        if (gpsLocation != null) {
            userLocation = Utils.toGeoPoint(gpsLocation);
        }
    }
}