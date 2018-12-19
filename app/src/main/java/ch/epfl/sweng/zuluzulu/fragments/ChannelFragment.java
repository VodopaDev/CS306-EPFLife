package ch.epfl.sweng.zuluzulu.fragments;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ch.epfl.sweng.zuluzulu.adapters.ChannelArrayAdapter;
import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.OnFragmentInteractionListener;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.fragments.superFragments.FragmentWithUser;
import ch.epfl.sweng.zuluzulu.structure.Channel;
import ch.epfl.sweng.zuluzulu.structure.GPS;
import ch.epfl.sweng.zuluzulu.structure.user.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.utility.GeopointUtility;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChannelFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChannelFragment extends FragmentWithUser<AuthenticatedUser> {
    private static final List<String> GLOBAL_CHANNEL_IDS = new ArrayList(Arrays.asList("Global", "Section IN", "Section SC", "Sat"));

    private View view;
    private SwipeRefreshLayout swipeRefreshLayout;

    private final List<Channel> listOfChannels = new ArrayList<>();
    private ChannelArrayAdapter adapter;
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
    public static ChannelFragment newInstance(AuthenticatedUser user) {
        if(user == null)
            throw new IllegalArgumentException("user can't be null");
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

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Channel selectedChannel = listOfChannels.get(position);
            if (selectedChannel.isAccessible()) {
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
     * Read data from the database and getAndAddOnSuccessListener the list of the channels
     */
    private void getChannelsFromDatabase() {
        List<String> ids = new ArrayList<>();
        ids.addAll(GLOBAL_CHANNEL_IDS);
        ids.addAll(user.getFollowedChannels());

        DatabaseFactory.getDependency().getChannelsFromIds(ids, result -> {
            listOfChannels.clear();
            List<Channel> listOfGlobalChannels = new ArrayList<>();
            List<Channel> listOfFollowedChannels = new ArrayList<>();
            for (Channel channel : result) {
                if (channel.canBeSeenBy(user.getSection(), userLocation)) {
                    if (GLOBAL_CHANNEL_IDS.contains(channel.getId())) {
                        listOfGlobalChannels.add(channel);
                    } else {
                        listOfFollowedChannels.add(channel);
                    }
                }
            }
            Collections.sort(listOfGlobalChannels, (o1, o2) -> o1.getName().compareTo(o2.getName()));
            Collections.sort(listOfFollowedChannels, (o1, o2) -> o1.getName().compareTo(o2.getName()));
            listOfChannels.addAll(listOfGlobalChannels);
            listOfChannels.addAll(listOfFollowedChannels);
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    /**
     * Refresh the list of the channels
     */
    private void refresh() {
        if (!GPS.isActivated()) {
            Snackbar.make(view, "Active ton GPS pour avoir accès à tous les canaux", 2000).show();
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
            userLocation = GeopointUtility.toGeoPoint(gpsLocation);
        }
    }
}