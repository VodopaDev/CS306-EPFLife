package ch.epfl.sweng.zuluzulu.Fragments;


import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.Firebase.Database.DatabaseCollection;
import ch.epfl.sweng.zuluzulu.Structure.Channel;
import ch.epfl.sweng.zuluzulu.User.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.User.User;

/**
 * A simple {@link SuperFragment} subclass.
 */
public abstract class SuperChatPostsFragment extends SuperFragment {

    private static final String ARG_USER = "ARG_USER";
    private static final String ARG_CHANNEL = "ARG_CHANNEL";

    protected ListView listView;
    protected Button chatButton;
    protected Button postsButton;

    protected AuthenticatedUser user;
    protected Channel channel;

    protected boolean anonymous;

    public static SuperChatPostsFragment newInstanceOf(String type, User user, Channel channel) {
        SuperChatPostsFragment fragment;
        switch (type) {
            case "chat":
                fragment = new ChatFragment();
                break;
            case "post":
                fragment = new PostFragment();
                break;
            default:
                throw new IllegalArgumentException("The type is incorrect");
        }
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
}
