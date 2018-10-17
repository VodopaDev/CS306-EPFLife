package ch.epfl.sweng.zuluzulu.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import ch.epfl.sweng.zuluzulu.OnFragmentInteractionListener;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Association;
import ch.epfl.sweng.zuluzulu.Structure.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.Structure.User;

public class AssociationDetailFragment extends Fragment {
    public static final String TAG = "ASSOCIATION_DETAIL__TAG";
    private static final String ARG_USER = "ARG_USER";
    private static final String ARG_ASSO = "ARG_ASSO";
    private static final String FAV_CONTENT = "This association is in your favorites";
    private static final String NOT_FAV_CONTENT = "This association isn't in your favorites";

    private ImageView asso_fav;

    private OnFragmentInteractionListener mListener;
    private Association asso;
    private User user;

    public static AssociationDetailFragment newInstance(User user, Association asso) {
        if (asso == null)
            throw new NullPointerException("Error creating an AssociationDetailFragment:\n" +
                    "Association is null");
        if (user == null)
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

        // Favorite button
        asso_fav = view.findViewById(R.id.association_detail_fav);
        setFavButtonBehaviour();
        asso_fav.setContentDescription(NOT_FAV_CONTENT);
        if (user.isConnected() && ((AuthenticatedUser) user).isFavAssociation(asso)) {
            loadFavImage(R.drawable.fav_on);
            asso_fav.setContentDescription(FAV_CONTENT);
        }

        // Association icon
        ImageView asso_icon = view.findViewById(R.id.association_detail_icon);
        Glide.with(getContext())
                .load(asso.getIconUri())
                .centerCrop()
                .into(asso_icon);

        // Association banner
        ImageView asso_banner = view.findViewById(R.id.association_detail_banner);
        Glide.with(getContext())
                .load(asso.getBannerUri())
                .centerCrop()
                .into(asso_banner);

        return view;
    }

    private void loadFavImage(int drawable) {
        Glide.with(getContext())
                .load(drawable)
                .centerCrop()
                .into(asso_fav);
    }

    private void setFavButtonBehaviour() {
        asso_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.isConnected()) {
                    AuthenticatedUser auth = (AuthenticatedUser) user;
                    if (auth.isFavAssociation(asso)) {
                        auth.removeFavAssociation(asso);
                        loadFavImage(R.drawable.fav_off);
                        asso_fav.setContentDescription(NOT_FAV_CONTENT);
                    } else {
                        auth.addFavAssociation(asso);
                        loadFavImage(R.drawable.fav_on);
                        asso_fav.setContentDescription(FAV_CONTENT);
                    }
                } else {
                    Snackbar.make(getView(), "Login to access your favorite associations", 5000).show();
                }
            }
        });
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
