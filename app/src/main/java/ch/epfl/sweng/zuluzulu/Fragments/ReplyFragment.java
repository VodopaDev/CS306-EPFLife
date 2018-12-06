package ch.epfl.sweng.zuluzulu.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.sweng.zuluzulu.Adapters.PostArrayAdapter;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Post;
import ch.epfl.sweng.zuluzulu.User.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.User.User;

public class ReplyFragment extends SuperFragment {
    private static final String ARG_USER = "ARG_USER";
    private static final String ARG_POST = "ARG_POST";
    private static final int REPLY_MAX_LENGTH = 100;
    private List<Post> replies = new ArrayList<>();
    private Post postOriginal;
    private PostArrayAdapter adapter;

    private ListView listView;
    private EditText replyText;
    private Button sendButton;
    private SwipeRefreshLayout swipeRefreshLayout;

    private AuthenticatedUser user;

    private boolean anonymous;

    public ReplyFragment() {
        // Required empty public constructor
    }

    public static ReplyFragment newInstance(User user, Post postOriginal) {
        ReplyFragment fragment = new ReplyFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        args.putSerializable(ARG_POST, postOriginal);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (AuthenticatedUser) getArguments().getSerializable(ARG_USER);
            postOriginal = (Post) getArguments().getSerializable(ARG_POST);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reply, container, false);

        listView = view.findViewById(R.id.reply_list_view);
        replyText = view.findViewById(R.id.reply_text_edit);
        sendButton = view.findViewById(R.id.reply_send_button);
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh_replies);

        replies.add(postOriginal);
        adapter = new PostArrayAdapter(view.getContext(), replies, user);
        listView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(this::refresh);

        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        anonymous = preferences.getBoolean(SettingsFragment.PREF_KEY_ANONYM, false);

        sendButton.setEnabled(false);

        setUpReplyText();
        setUpSendButton();
        loadReplies(false);

        return view;
    }

    /**
     * Set up the listener on the text edit field
     */
    private void setUpReplyText() {
        replyText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.toString().length();
                sendButton.setEnabled(0 < length && length < REPLY_MAX_LENGTH);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * Set up the listener on the send button
     */
    private void setUpSendButton() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Post post = new Post(
                        DatabaseFactory.getDependency().getNewPostId(postOriginal.getChannelId()),
                        postOriginal.getChannelId(),
                        postOriginal.getId(),
                        replyText.getText().toString().trim().replaceAll("([\\n\\r]+\\s*)*$", ""),
                        anonymous ? "" : user.getFirstNames(),
                        user.getSciper(),
                        Timestamp.now().toDate(),
                        postOriginal.getColor(),
                        0,
                        0,
                        new ArrayList<>(),
                        new ArrayList<>()
                );
                DatabaseFactory.getDependency().addReply(post);
                loadReplies(true);
                replyText.getText().clear();
            }
        });
    }

    /**
     * Refresh the replies by reading in the database
     */
    private void loadReplies(boolean newReply) {
        replies.clear();
        replies.add(postOriginal);
        DatabaseFactory.getDependency().getRepliesFromPost(postOriginal.getChannelId(), postOriginal.getId(), result -> {
            Log.d("REPLIES", result.size() + " replies");
            replies.addAll(result);
            Collections.sort(replies, (o1, o2) -> {
                if (o1.getTime().before(o2.getTime()))
                    return -1;
                else
                    return 1;
            });
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
            if (newReply) {
                listView.setSelection(adapter.getCount() - 1);
            }
        });
    }

    /**
     * This function is called when the user swipes down to refresh the list of replies
     */
    private void refresh() {
        swipeRefreshLayout.setRefreshing(true);
        loadReplies(false);
    }
}
