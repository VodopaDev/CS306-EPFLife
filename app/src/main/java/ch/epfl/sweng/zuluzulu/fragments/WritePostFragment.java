package ch.epfl.sweng.zuluzulu.fragments;


import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.Timestamp;

import java.util.ArrayList;

import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.fragments.superFragments.FragmentWithUser;
import ch.epfl.sweng.zuluzulu.fragments.superFragments.FragmentWithUserAndData;
import ch.epfl.sweng.zuluzulu.fragments.superFragments.SuperFragment;
import ch.epfl.sweng.zuluzulu.structure.Channel;
import ch.epfl.sweng.zuluzulu.structure.Post;
import ch.epfl.sweng.zuluzulu.structure.user.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.structure.user.User;
import ch.epfl.sweng.zuluzulu.utility.PostColor;

import static ch.epfl.sweng.zuluzulu.CommunicationTag.OPEN_POST_FRAGMENT;

/**
 * A {@link SuperFragment} subclass.
 * This fragment is used to write new posts
 */
public class WritePostFragment extends FragmentWithUserAndData<AuthenticatedUser, Channel> {
    private static final int POST_MAX_LENGTH = 200;

    private ConstraintLayout layout;
    private EditText editText;
    private Button sendButton;
    private PostColor color;
    private boolean anonymous;

    public WritePostFragment() {
        // Required empty public constructor
    }

    public static WritePostFragment newInstance(User user, Channel channel) {
        if(user == null)
            throw new IllegalArgumentException("user can't be null");
        if(channel == null)
            throw new IllegalArgumentException("channel can't be null");
        WritePostFragment fragment = new WritePostFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        args.putSerializable(ARG_DATA, channel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mListener.onFragmentInteraction(CommunicationTag.SET_TITLE, data.getName());
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_write_post, container, false);

        color = PostColor.getRandomColor();
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        anonymous = preferences.getBoolean(SettingsFragment.PREF_KEY_ANONYM, false);

        layout = view.findViewById(R.id.write_post_layout);
        editText = view.findViewById(R.id.write_post_textEdit);
        sendButton = view.findViewById(R.id.write_post_send_button);

        layout.setBackgroundColor(Color.parseColor(color.getValue()));
        sendButton.setEnabled(false);

        setUpSendButton();
        setUpColorListener();
        setUpTextEditListener();

        return view;
    }

    /**
     * Set up an onClick listener on the send button
     */
    private void setUpSendButton() {
        sendButton.setOnClickListener(v -> {
            Post post = new Post(
                    DatabaseFactory.getDependency().getNewPostId(data.getId()),
                    data.getId(),
                    null,
                    editText.getText().toString().trim().replaceAll("([\\n\\r]+\\s*)*$", ""),
                    anonymous ? "" : user.getFirstNames(),
                    user.getSciper(),
                    Timestamp.now().toDate(),
                    color.getValue(),
                    new ArrayList<>(),
                    new ArrayList<>(),
                    new ArrayList<>()
            );
            DatabaseFactory.getDependency().addPost(post);
            mListener.onFragmentInteraction(OPEN_POST_FRAGMENT, data);
        });
    }

    /**
     * Set up an onClick listener on the layout to be able to change the color of the post
     */
    private void setUpColorListener() {
        layout.setOnClickListener(v -> {
            int oldColor = Color.parseColor(color.getValue());
            color = PostColor.getRandomColorButNot(color);
            int newColor = Color.parseColor(color.getValue());
            ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), oldColor, newColor);
            colorAnimation.addUpdateListener(animator -> layout.setBackgroundColor((Integer) animator.getAnimatedValue()));
            colorAnimation.start();
        });
    }

    /**
     * Set up an onTextChanged listener on the message field to know when the post is ready to be sent
     */
    private void setUpTextEditListener() {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int messageLength = s.toString().length();
                boolean correctFormat = 0 < messageLength && messageLength < POST_MAX_LENGTH;
                sendButton.setEnabled(correctFormat);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}
