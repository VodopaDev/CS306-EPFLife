package ch.epfl.sweng.zuluzulu.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.epfl.sweng.zuluzulu.OnFragmentInteractionListener;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Association;
import ch.epfl.sweng.zuluzulu.Structure.User;

public class AssociationDetailFragment extends Fragment{
    public static final String TAG = "ASSOCIATION_DETAIL__TAG";
    private static final String ARG_USER = "ARG_USER";
    private static final String ARG_ASSO = "ARG_ASSO";

    private OnFragmentInteractionListener mListener;

    private Association association;
    private User user;

    public static AssociationDetailFragment newInstance(User user, Association asso) {
        if(asso == null)
            throw new NullPointerException("Error creating an AssociationDetailFragment:\n" +
                    "Association is null");
        if(user == null)
            throw new NullPointerException("Error creating an AssociationDetailFragment:\n" +
                    "User is null");

        AssociationDetailFragment fragment = new AssociationDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        args.putSerializable(ARG_ASSO, asso);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_association, container, false);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(TAG, uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


}
