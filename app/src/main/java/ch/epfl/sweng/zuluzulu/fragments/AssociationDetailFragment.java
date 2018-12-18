package ch.epfl.sweng.zuluzulu.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.fragments.superFragments.FragmentWithUser;
import ch.epfl.sweng.zuluzulu.fragments.superFragments.FragmentWithUserAndData;
import ch.epfl.sweng.zuluzulu.structure.Association;
import ch.epfl.sweng.zuluzulu.structure.Channel;
import ch.epfl.sweng.zuluzulu.structure.user.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.structure.user.User;
import ch.epfl.sweng.zuluzulu.utility.ImageLoader;

import static ch.epfl.sweng.zuluzulu.utility.ImageLoader.loadUriIntoImageView;

public class AssociationDetailFragment extends FragmentWithUserAndData<User, Association> {
    public static final String TAG = "ASSOCIATION_DETAIL__TAG";
    private static final String FAV_CONTENT = "Cette association est dans tes favoris";
    private static final String NOT_FAV_CONTENT = "Cette association n'est pas dans tes favoris";

    private ImageButton asso_fav;
    private Button eventsButton;
    private Button chatButton;
    private Channel channel;

    /**
     * Initialize a new AssociationDetailFragment
     *
     * @param user User of the app
     * @param asso Association to be displayed
     * @return A new AssociationDetailFragment displaying an association details
     */
    public static AssociationDetailFragment newInstance(User user, Association asso) {
        if (asso == null)
            throw new IllegalArgumentException("Error creating an AssociationDetailFragment:\n" +
                    "Association is null");
        if (user == null)
            throw new IllegalArgumentException("Error creating an AssociationDetailFragment:\n" +
                    "User is null");

        AssociationDetailFragment fragment = new AssociationDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        args.putSerializable(ARG_DATA, asso);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mListener.onFragmentInteraction(CommunicationTag.SET_TITLE, data.getName());
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_association_detail, container, false);

        // Favorite button
        asso_fav = view.findViewById(R.id.association_detail_fav);
        setFavButtonBehaviour();

        // Association icon
        ImageView asso_icon = view.findViewById(R.id.association_detail_icon);
        loadUriIntoImageView(asso_icon, data.getIconUri(), getContext());

        // Association banner
        ImageView asso_banner = view.findViewById(R.id.association_detail_banner);
        loadUriIntoImageView(asso_banner, data.getBannerUri(), getContext());

        eventsButton = view.findViewById(R.id.association_detail_events_button);
        chatButton = view.findViewById(R.id.association_detail_chat_button);

        loadChat();
        setUpChatButton();
        setUpEventsButton();

        return view;
    }

    /**
     * Set up the favorite button's behaviour
     */
    private void setFavButtonBehaviour() {
        if (user.isConnected() && ((AuthenticatedUser) user).isFollowedAssociation(data.getId()))
            ImageLoader.loadDrawableIntoImageView(asso_fav, R.drawable.fav_on, getContext());
        else
            ImageLoader.loadDrawableIntoImageView(asso_fav, R.drawable.fav_off, getContext());

        asso_fav.setOnClickListener(v -> {
            if (user.isConnected()) {
                AuthenticatedUser auth = (AuthenticatedUser) user;
                if (auth.isFollowedAssociation(data.getId())) {
                    auth.removeFavAssociation(data.getId());
                    ImageLoader.loadDrawableIntoImageView(asso_fav, R.drawable.fav_off, getContext());
                    asso_fav.setContentDescription(NOT_FAV_CONTENT);
                } else {
                    auth.addFollowedAssociation(data.getId());
                    ImageLoader.loadDrawableIntoImageView(asso_fav, R.drawable.fav_on, getContext());
                    asso_fav.setContentDescription(FAV_CONTENT);
                }

                DatabaseFactory.getDependency().updateUser(auth);
            } else {
                Snackbar.make(getView(), "Connecte-toi pour accéder à tes associations favorites", 5000).show();
            }
        });
    }

    /**
     * Fetch the appropriate chat channel from the database
     */
    private void loadChat() {
        if (data.getChannelId() == null) {
            chatButton.setEnabled(false);
        } else {
            DatabaseFactory.getDependency().getChannelFromId(data.getChannelId(), result -> {
                if (result != null) {
                    channel = result;
                } else {
                    chatButton.setEnabled(false);
                }
            });
        }
    }

    /**
     * Set up the chat button to redirect to the correct chat page
     */
    private void setUpChatButton() {
        chatButton.setOnClickListener(v -> {
            if (channel != null) {
                if (user.isConnected())
                    mListener.onFragmentInteraction(CommunicationTag.OPEN_CHAT_FRAGMENT, channel);
                else
                    Snackbar.make(getView(), "Connecte-toi pour accéder au chat de l'association", 5000).show();
            }
        });
    }

    /**
     * Set up the events button to redirect to the list of related events
     */
    private void setUpEventsButton() {
        eventsButton.setOnClickListener(v -> {
            mListener.onFragmentInteraction(CommunicationTag.OPEN_EVENT_FRAGMENT, user);
        });
    }

}
