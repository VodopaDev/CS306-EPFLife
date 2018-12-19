package ch.epfl.sweng.zuluzulu.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import ch.epfl.sweng.zuluzulu.adapters.PostArrayAdapter;
import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.fragments.superFragments.SuperChatPostsFragment;
import ch.epfl.sweng.zuluzulu.structure.Channel;
import ch.epfl.sweng.zuluzulu.structure.Post;
import ch.epfl.sweng.zuluzulu.structure.user.User;

import static ch.epfl.sweng.zuluzulu.CommunicationTag.OPEN_CHAT_FRAGMENT;
import static ch.epfl.sweng.zuluzulu.CommunicationTag.OPEN_WRITE_POST_FRAGMENT;

/**
 * A {@link SuperChatPostsFragment} subclass.
 * This fragment is used to display the posts
 */
public class PostFragment extends SuperChatPostsFragment {
    private PostArrayAdapter adapter;

    private Button writePostButton;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView filterTimeButton;
    private ImageView filterRepliesButton;
    private ImageView filterUpsButton;

    private Comparator<Post> currentComparator;

    public PostFragment() {
        // Required empty public constructor
    }

    public static PostFragment newInstance(User user, Channel channel) {
        if(user == null)
            throw new IllegalArgumentException("user can't be null");
        if(channel == null)
            throw new IllegalArgumentException("channel can't be null");
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
        filterTimeButton = view.findViewById(R.id.post_filter_time);
        filterRepliesButton = view.findViewById(R.id.post_filter_nbReplies);
        filterUpsButton = view.findViewById(R.id.post_filter_nbUps);

        chatButton.setEnabled(true);
        chatButton.setBackgroundColor(getResources().getColor(R.color.colorGrayDarkTransparent));
        postsButton.setEnabled(false);
        postsButton.setBackgroundColor(getResources().getColor(R.color.white));

        adapter = new PostArrayAdapter(view.getContext(), messages, user);
        listView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(this::refresh);


        anonymous = Objects.requireNonNull(getActivity()).getPreferences(Context.MODE_PRIVATE).getBoolean(SettingsFragment.PREF_KEY_ANONYM, false);
        currentComparator = (Comparator<Post>) Post.decreasingTimeComparator();

        loadAllPosts();
        setUpChatButton();
        setUpNewPostButton();
        setUpFilterButtons();
        setUpReplyListener();
        setUpProfileListener();

        return view;
    }

    /**
     * Add an onClick listener on the button to switch to the chat fragment
     */
    private void setUpChatButton() {
        chatButton.setOnClickListener(v -> mListener.onFragmentInteraction(OPEN_CHAT_FRAGMENT, data));
    }

    /**
     * Load the posts from the database and notify the adapter of the changes
     */
    private void loadAllPosts() {
        DatabaseFactory.getDependency().getPostsFromChannel(data.getId(), result -> {
            messages.clear();
            messages.addAll(result);
            sortPostsWithCurrentComparator();
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    /**
     * Set up an onClick listener on the button to write a new post
     */
    private void setUpNewPostButton() {
        writePostButton.setOnClickListener(v -> mListener.onFragmentInteraction(OPEN_WRITE_POST_FRAGMENT, data));
    }

    /**
     * This function is called when the user swipes down to refresh the list of posts
     */
    private void refresh() {
        swipeRefreshLayout.setRefreshing(true);
        loadAllPosts();
    }

    /**
     * Set up the listener on a post to go to the reply fragment when we click on it
     */
    private void setUpReplyListener() {
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Post post = (Post) messages.get(position);
            Pair data = new Pair(this.data, post);
            mListener.onFragmentInteraction(CommunicationTag.OPEN_REPLY_FRAGMENT, data);
        });
    }

    /**
     * Set up the onClick listeners on the filter buttons
     */
    private void setUpFilterButtons() {
        filterTimeButton.setOnClickListener(v -> updateFilter((Comparator<Post>) Post.decreasingTimeComparator()));

        filterRepliesButton.setOnClickListener(v -> updateFilter(Post.decreasingNbRepliesComparator()));

        filterUpsButton.setOnClickListener(v -> updateFilter(Post.decreasingNbUpsComparator()));
    }

    /**
     * Update the current comparator by the new one
     *
     * @param newComparator The new comparator to apply
     */
    private void updateFilter(Comparator<Post> newComparator) {
        if (!newComparator.equals(currentComparator)) {
            filterTimeButton.setImageResource(newComparator.equals(Post.decreasingTimeComparator()) ? R.drawable.time_selected : R.drawable.time_notselected);
            filterRepliesButton.setImageResource(newComparator.equals(Post.decreasingNbRepliesComparator()) ? R.drawable.replies_selected : R.drawable.replies_notselected);
            filterUpsButton.setImageResource(newComparator.equals(Post.decreasingNbUpsComparator()) ? R.drawable.up_selected : R.drawable.up_notselected);

            currentComparator = newComparator;
            sortPostsWithCurrentComparator();
        }
    }

    /**
     * Sort the posts with the current comparator
     */
    private void sortPostsWithCurrentComparator() {
        Collections.sort((List<Post>) (List<?>) messages, currentComparator);
        adapter.notifyDataSetChanged();
        listView.setSelection(0);
    }
}
