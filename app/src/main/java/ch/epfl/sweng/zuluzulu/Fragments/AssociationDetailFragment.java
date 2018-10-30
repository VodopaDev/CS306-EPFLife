package ch.epfl.sweng.zuluzulu.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import ch.epfl.sweng.zuluzulu.OnFragmentInteractionListener;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Association;
import ch.epfl.sweng.zuluzulu.Structure.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.Structure.Event;
import ch.epfl.sweng.zuluzulu.Structure.User;

public class AssociationDetailFragment extends Fragment {
    public static final String TAG = "ASSOCIATION_DETAIL__TAG";
    private static final String ARG_USER = "ARG_USER";
    private static final String ARG_ASSO = "ARG_ASSO";
    private static final String FAV_CONTENT = "This association is in your favorites";
    private static final String NOT_FAV_CONTENT = "This association isn't in your favorites";

    private ImageButton asso_fav;

    private ImageView upcoming_event_icon;
    private TextView upcoming_event_name;
    private TextView upcoming_event_date;
    private Event upcoming_event;

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

        // Upcoming event name
        upcoming_event_name = view.findViewById(R.id.association_detail_upcoming_event_name);
        upcoming_event_icon = view.findViewById(R.id.association_detail_upcoming_event_icon);
        upcoming_event_date = view.findViewById(R.id.association_detail_upcoming_event_date);
        if (asso.getClosestEventId() != 0) {
            FirebaseFirestore.getInstance()
                    .document("events_info/event" + asso.getClosestEventId())
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    upcoming_event = new Event(documentSnapshot);
                    upcoming_event_name.setText(upcoming_event.getName());
                    upcoming_event_date.setText(upcoming_event.getStartDateString());
                    Glide.with(getContext())
                            .load(upcoming_event.getIconUri())
                            .centerCrop()
                            .into(upcoming_event_icon);
                }
            });
        } else {
            upcoming_event_name.setText("Pas d'event :(");
        }


        return view;
    }

    private void loadFavImage(int drawable) {
        Glide.with(getContext())
                .load(drawable)
                .centerCrop()
                .into(asso_fav);
    }

    private void setFavButtonBehaviour() {
        if (user.isConnected() && ((AuthenticatedUser) user).isFavAssociation(asso))
            loadFavImage(R.drawable.fav_on);
        else
            loadFavImage(R.drawable.fav_off);

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

    public Association getAsso() {
        return asso;
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
