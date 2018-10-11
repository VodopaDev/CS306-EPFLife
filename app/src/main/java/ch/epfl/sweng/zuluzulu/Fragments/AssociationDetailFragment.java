package ch.epfl.sweng.zuluzulu.Fragments;

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
    private static final String TAG = "ASSOCIATION_DETAIL__TAG";
    private OnFragmentInteractionListener mListener;

    private User user;
    private Association association;

    public static AssociationDetailFragment newInstance(User user, Association association) {
        if(user == null || association == null)
            throw new NullPointerException("Error creating an AssociationDetailFragment:\n" +
                    "At least one of the parameters is null");

        AssociationDetailFragment fragment = new AssociationDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.setUser(user);
        fragment.setAssociation(association);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_association, container, false);
        return view;
    }

    private void setUser(User user){
        assert(user != null);
        this.user = user;
    }

    private void setAssociation(Association association){
        assert (association != null);
        this.association = association;
    }


}
