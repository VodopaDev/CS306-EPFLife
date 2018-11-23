package ch.epfl.sweng.zuluzulu.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.zuluzulu.Adapters.PostArrayAdapter;
import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Channel;
import ch.epfl.sweng.zuluzulu.Structure.Post;
import ch.epfl.sweng.zuluzulu.User.User;

/**
 * A {@link SuperChatPostsFragment} subclass.
 * This fragment is used to display the posts
 */
public class PostFragment extends SuperChatPostsFragment {
    private static final String TAG = "POST_TAG";

    private static final String POSTS_COLLECTION_NAME = "posts";

    private List<Post> posts = new ArrayList<>();
    private PostArrayAdapter adapter;

    private Button writePostButton;
    private SwipeRefreshLayout swipeRefreshLayout;

    public PostFragment() {
        // Required empty public constructor
    }

    public static PostFragment newInstance(User user, Channel channel) {
        return (PostFragment) newInstanceOf("post", user, channel);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        listView = view.findViewById(R.id.posts_list_view);
        chatButton = view.findViewById(R.id.chat_button);
        postsButton = view.findViewById(R.id.posts_button);
        writePostButton = view.findViewById(R.id.posts_new_post_button);
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh_post);

        chatButton.setEnabled(true);
        postsButton.setEnabled(false);

        String collectionPath = CHANNEL_DOCUMENT_NAME + channel.getId() + "/" + POSTS_COLLECTION_NAME;
        collectionReference = db.collection(collectionPath);
        mockableCollection = DatabaseFactory.getDependency().collection(collectionPath);

        adapter = new PostArrayAdapter(view.getContext(), posts);
        listView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(this::refresh);

        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        anonymous = preferences.getBoolean(SettingsFragment.PREF_KEY_ANONYM, false);

        updatePosts();
        setUpChatButton();
        setUpNewPostButton();
        setUpReplyListener();

        return view;
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
        collectionReference
                .orderBy("time", Query.Direction.DESCENDING)
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
                                    Post post = new Post(fmap, user.getSciper(), channel.getId());
                                    posts.add(post);
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
     * Set up an onClick listener on the button to write a new post
     */
    private void setUpNewPostButton() {
        writePostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFragmentInteraction(CommunicationTag.OPEN_WRITE_POST_FRAGMENT, channel);
            }
        });
    }

    /**
     * This function is called when the user swipes down to refresh the list of posts
     */
    private void refresh() {
        swipeRefreshLayout.setRefreshing(true);
        updatePosts();
    }

    /**
     * Set up the listener on a post to go to the reply fragment when we click on it
     */
    private void setUpReplyListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Post post = posts.get(position);
                mListener.onFragmentInteraction(CommunicationTag.OPEN_REPLY_FRAGMENT, post);
            }
        });
    }
}
