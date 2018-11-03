package ch.epfl.sweng.zuluzulu.Fragments;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.OnFragmentInteractionListener;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.GPS;
import ch.epfl.sweng.zuluzulu.Structure.User;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends SuperFragment {
    public static final String TAG = "MAIN_TAG";
    private static final String ARG_USER = "ARG_USER";

    private User user;

    public MainFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MainFragment.
     */

    public static MainFragment newInstance(User u) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, u);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mListener.onFragmentInteraction(CommunicationTag.SET_TITLE, "Home");
            user = (User) getArguments().getSerializable(ARG_USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (user.isConnected()) {
            boolean hadPermissions = GPS.start(getContext());
            if (!hadPermissions) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, GPS.MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return inflater.inflate(R.layout.fragment_main_user, container, false);
        } else {
            return inflater.inflate(R.layout.fragment_main, container, false);
        }

    }
}
