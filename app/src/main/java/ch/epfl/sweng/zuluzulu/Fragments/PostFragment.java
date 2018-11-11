package ch.epfl.sweng.zuluzulu.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import ch.epfl.sweng.zuluzulu.Adapters.PostArrayAdapter;
import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.OnFragmentInteractionListener;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.Structure.Channel;
import ch.epfl.sweng.zuluzulu.Structure.Post;
import ch.epfl.sweng.zuluzulu.Structure.User;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostFragment extends SuperFragment {
    public static final String TAG = "CHAT_TAG";
    private static final String ARG_USER = "ARG_USER";
    private static final String ARG_CHANNEL = "ARG_CHANNEL";

    private static final String CHANNEL_DOCUMENT_NAME = "channels/channel";
    private static final String POSTS_COLLECTION_NAME = "posts";

    private FirebaseFirestore db;

    private ListView listView;
    private Button chatButton;

    private List<Post> posts = new ArrayList<>();
    private PostArrayAdapter adapter;
    private String collection_path;

    private AuthenticatedUser user;
    private Channel channel;

    private boolean anonym;

    public PostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ChatFragment.
     */
    public static PostFragment newInstance(User user, Channel channel) {
        PostFragment fragment = new PostFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        args.putSerializable(ARG_CHANNEL, channel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (AuthenticatedUser) getArguments().getSerializable(ARG_USER);
            channel = (Channel) getArguments().getSerializable(ARG_CHANNEL);
            mListener.onFragmentInteraction(CommunicationTag.SET_TITLE, channel.getName());
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        listView = view.findViewById(R.id.chat_list_view);
        chatButton = view.findViewById(R.id.chat_button);

        collection_path = CHANNEL_DOCUMENT_NAME + channel.getId() + "/" + POSTS_COLLECTION_NAME;

        adapter = new PostArrayAdapter(view.getContext(), posts);
        listView.setAdapter(adapter);

        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        anonym = preferences.getBoolean(SettingsFragment.PREF_KEY_ANONYM, false);

        setUpDataOnChangeListener();
        setUpChatButton();

        return view;
    }

    /**
     * Add a onEventChange listener on the post list in the database
     */
    private void setUpDataOnChangeListener() {
        db = FirebaseFirestore.getInstance();
        db.collection(collection_path)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        updatePosts();
                    }
                });
    }

    /**
     * Add an onClick listener on the button to switch to the chat fragment
     */
    private void setUpChatButton() {
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFragmentInteraction(CommunicationTag.OPEN_CHAT_FRAGMENT, channel);
            }
        });
    }

    /**
     * Refresh the posts by reading in the database
     */
    private void updatePosts() {
        db.collection(collection_path)
                .orderBy("time", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            posts.clear();
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                FirebaseMapDecorator fmap = new FirebaseMapDecorator(document);
                                if (fmap.hasFields(Post.FIELDS)) {
                                    Post post = new Post(fmap);
                                    posts.add(post);
                                }
                            }
                            adapter.notifyDataSetChanged();
                            listView.setSelection(adapter.getCount() - 1);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
