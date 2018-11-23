package ch.epfl.sweng.zuluzulu.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Post;
import ch.epfl.sweng.zuluzulu.User.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.User.User;

public class ReplyFragment extends SuperFragment {

    private static final String ARG_USER = "ARG_USER";
    private static final String ARG_POST = "ARG_POST";

    private List<Post> replies = new ArrayList<>();

    private AuthenticatedUser user;
    private Post post;

    public ReplyFragment() {
        // Required empty public constructor
    }

    public static ReplyFragment newInstance(User user, Post post) {
        ReplyFragment fragment = new ReplyFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        args.putSerializable(ARG_POST, post);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (AuthenticatedUser) getArguments().getSerializable(ARG_USER);
            post = (Post) getArguments().getSerializable(ARG_POST);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reply, container, false);

        View postView = view.findViewById(R.id.reply_post_view);
        inflater.inflate(R.layout.post, container, false);

        return view;
    }

}
