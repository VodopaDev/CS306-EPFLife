package ch.epfl.sweng.zuluzulu.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import ch.epfl.sweng.zuluzulu.Adapters.ChannelAdapter;
import ch.epfl.sweng.zuluzulu.OnFragmentInteractionListener;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.Structure.Channel;
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
    public static final String TAG = "CHANNEL_TAG";
    private static final String ARG_USER = "ARG_USER";
    private static final String CHANNELS_COLLECTION_NAME = "channels";

    private FirebaseFirestore db;

    private ListView listView;
    private ArrayList<Channel> listOfChannels = new ArrayList<>();
    private ChannelAdapter adapter;

    private User user;

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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_channel, container, false);
        listView = view.findViewById(R.id.channels_list_view);

        adapter = new ChannelAdapter(view.getContext(), listOfChannels);
        listView.setAdapter(adapter);

        getChannelsFromDatabase();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Channel selectedChannel = listOfChannels.get(position);
                mListener.onFragmentInteraction(TAG, selectedChannel.getId());
            }
        });

        return view;
    }

    /**
     * Read data from the database and get the list of the channels
     */
    private void getChannelsFromDatabase() {
        db = FirebaseFirestore.getInstance();
        db.collection(CHANNELS_COLLECTION_NAME)
                .orderBy("id", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            listOfChannels.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                if (Utils.isValidSnapshot(document, Channel.FIELDS)) {
                                    Channel channel = new Channel(document.getData());
                                    if (user.isConnected() && channel.canBeAccessedBy((AuthenticatedUser) user)) {
                                        listOfChannels.add(channel);
                                    }
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}