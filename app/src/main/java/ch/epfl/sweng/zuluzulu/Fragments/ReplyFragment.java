package ch.epfl.sweng.zuluzulu.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import ch.epfl.sweng.zuluzulu.Firebase.FirebaseProxy;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Post;
import ch.epfl.sweng.zuluzulu.User.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.User.User;

public class ReplyFragment extends SuperFragment {
    private static final String TAG = "REPLY_TAG";
    private static final String ARG_USER = "ARG_USER";
    private static final String ARG_POST = "ARG_POST";
    private static final int REPLY_MAX_LENGTH = 100;

    private List<Post> replies = new ArrayList<>();
    private Post postOriginal;
    private PostArrayAdapter adapter;

    private EditText replyText;
    private Button sendButton;

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

        ListView postView = view.findViewById(R.id.reply_original_post);
        ListView listView = view.findViewById(R.id.reply_list_view);
        replyText = view.findViewById(R.id.reply_text_edit);
        sendButton = view.findViewById(R.id.reply_send_button);

        PostArrayAdapter adapterOriginalPost = new PostArrayAdapter(view.getContext(), Collections.singletonList(postOriginal), user);
        postView.setAdapter(adapterOriginalPost);

        adapter = new PostArrayAdapter(view.getContext(), replies, user);
        listView.setAdapter(adapter);

        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        anonymous = preferences.getBoolean(SettingsFragment.PREF_KEY_ANONYM, false);

        sendButton.setEnabled(false);

        setUpReplyText();
        setUpSendButton();
        loadReplies();
        return view;
    }

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

    private void setUpSendButton() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Post post = new Post(
                        FirebaseProxy.getInstance().getNewPostId(postOriginal.getChannelId()),
                        postOriginal.getChannelId(),
                        postOriginal.getId(),
                        replyText.getText().toString(),
                        anonymous ? "" : user.getFirstNames(),
                        user.getSciper(),
                        Timestamp.now().toDate(),
                        postOriginal.getColor(),
                        0,
                        1,
                        Collections.singletonList(user.getSciper()),
                        new ArrayList<>()
                );
                FirebaseProxy.getInstance().addReply(post);
                loadReplies();
            }
        });
    }

    /**
     * Refresh the posts by reading in the database
     */
    private void loadReplies() {
        replies.clear();
        FirebaseProxy.getInstance().getRepliesFromPost(postOriginal.getChannelId(), postOriginal.getId(), result -> {
            Log.d("REPLIES", result.size() + " replies");
            replies.addAll(result);
            Collections.sort(replies, (o1, o2) -> {
                if (o1.getTime().before(o2.getTime()))
                    return -1;
                else
                    return 1;
            });
            adapter.notifyDataSetChanged();
        });
    }

}
