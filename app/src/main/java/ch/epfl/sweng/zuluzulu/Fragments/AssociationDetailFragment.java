package ch.epfl.sweng.zuluzulu.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.AndroidException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import ch.epfl.sweng.zuluzulu.OnFragmentInteractionListener;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Association;
import ch.epfl.sweng.zuluzulu.Structure.User;

public class AssociationDetailFragment extends Fragment{
    public static final String TAG = "ASSOCIATION_DETAIL__TAG";
    private static final String ARG_USER = "ARG_USER";
    private static final String ARG_ASSO = "ARG_ASSO";

    private OnFragmentInteractionListener mListener;
    private Association asso;
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
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable(ARG_USER);
            asso = (Association) getArguments().getSerializable(ARG_ASSO);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_association_detail, container, false);

        // Association name
        TextView asso_name = view.findViewById(R.id.association_detail_name);
        asso_name.setText(asso.getName());
        int width = asso.getName().length() * (int)asso_name.getTextSize();
        Log.d("WIDTH", String.valueOf(width));
        asso_name.setWidth(width);

        // Favorite button
        Button asso_fav_button = view.findViewById(R.id.association_detail_fav_button);
        asso_fav_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.isConnected()){
                    // TODO: do the favorite adding/removing
                }
                else {
                    // TODO: prompt error message
                }
            }
        });

        // Association icon
        ImageView asso_icon = view.findViewById(R.id.association_detail_icon);
        Glide.with(getContext())
                .load(asso.getIconUri())
                .centerCrop()
                .into(asso_icon);


        return view;
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
