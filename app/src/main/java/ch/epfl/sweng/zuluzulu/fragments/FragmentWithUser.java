package ch.epfl.sweng.zuluzulu.fragments;

import android.os.Bundle;

import java.util.ArrayList;

import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.adapters.AssociationArrayAdapter;
import ch.epfl.sweng.zuluzulu.adapters.EventArrayAdapter;
import ch.epfl.sweng.zuluzulu.structure.user.User;

public class FragmentWithUser extends SuperFragment {
    private static final String ARG_USER = "ARG_USER";
    User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable(ARG_USER);
        }
    }

}
