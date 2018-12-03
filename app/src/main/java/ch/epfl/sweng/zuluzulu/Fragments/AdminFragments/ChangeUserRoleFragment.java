package ch.epfl.sweng.zuluzulu.Fragments.AdminFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.epfl.sweng.zuluzulu.Fragments.SuperFragment;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.User.User;

/**
 * A simple {@link SuperFragment} subclass.
 * Use the {@link ChangeUserRoleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangeUserRoleFragment extends SuperFragment {

    public ChangeUserRoleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ChangeUserRoleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChangeUserRoleFragment newInstance(User user) {
        ChangeUserRoleFragment fragment = new ChangeUserRoleFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_user_role, container, false);
    }
}
