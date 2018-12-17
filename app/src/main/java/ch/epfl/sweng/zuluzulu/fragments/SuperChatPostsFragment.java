package ch.epfl.sweng.zuluzulu.fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.structure.Channel;
import ch.epfl.sweng.zuluzulu.structure.SuperMessage;
import ch.epfl.sweng.zuluzulu.structure.user.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.structure.user.User;

/**
 * A simple {@link SuperFragment} subclass.
 */
public abstract class SuperChatPostsFragment extends SuperFragment {

    public static final String VISIT_PROFILE_STRING = "Visiter le profile de ";
    protected static final String ARG_USER = "ARG_USER";
    protected static final String ARG_CHANNEL = "ARG_CHANNEL";
    protected static final String ARG_POST = "ARG_POST";
    protected List<SuperMessage> messages = new ArrayList<>();

    protected ListView listView;
    protected Button chatButton;
    protected Button postsButton;

    protected AuthenticatedUser user;
    protected Channel channel;

    protected boolean anonymous;

    public static SuperChatPostsFragment newInstanceOf(String type, User user, Channel channel) {
        if(type == null)
            throw new IllegalArgumentException("type can't be null");
        if(user == null)
            throw new IllegalArgumentException("user can't be null");
        if(channel == null)
            throw new IllegalArgumentException("channel can't be null");
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

    /**
     * Set up long click listener on the posts or messages
     */
    protected void setUpProfileListener() {
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            SuperMessage message = messages.get(position);
            if (!message.isAnonymous() && !message.isOwnMessage(user.getSciper())) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                AlertDialog dlg = builder.setTitle(VISIT_PROFILE_STRING + message.getSenderName() + " ?")
                        .setPositiveButton("Oui", (dialog, which) -> DatabaseFactory.getDependency().getUserWithIdOrCreateIt(message.getSenderSciper(), result -> {
                            System.out.println(result.getSciper());
                            mListener.onFragmentInteraction(CommunicationTag.OPEN_PROFILE_FRAGMENT, result);
                        }))
                        .create();
                dlg.setCanceledOnTouchOutside(true);
                dlg.show();
            }
            return true;
        });
    }
}
