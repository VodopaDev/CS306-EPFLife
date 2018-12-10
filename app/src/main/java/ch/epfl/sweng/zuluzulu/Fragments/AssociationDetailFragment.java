package ch.epfl.sweng.zuluzulu.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Association;
import ch.epfl.sweng.zuluzulu.Structure.Channel;
import ch.epfl.sweng.zuluzulu.Structure.Event;
import ch.epfl.sweng.zuluzulu.User.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.User.User;

import static ch.epfl.sweng.zuluzulu.CommunicationTag.OPEN_EVENT_DETAIL_FRAGMENT;
import static ch.epfl.sweng.zuluzulu.Utility.ImageLoader.loadUriIntoImageView;

public class AssociationDetailFragment extends SuperFragment {
    public static final String TAG = "ASSOCIATION_DETAIL__TAG";
    private static final String ARG_USER = "ARG_USER";
    private static final String ARG_ASSO = "ARG_ASSO";
    private static final String FAV_CONTENT = "This association is in your favorites";
    private static final String NOT_FAV_CONTENT = "This association isn't in your favorites";

    private ImageButton asso_fav;

    private Event upcoming_event;
    private ConstraintLayout upcoming_event_layout;
    private ImageView upcoming_event_icon;
    private TextView upcoming_event_name;
    private TextView upcoming_event_date;

    private Channel main_chat;
    private ConstraintLayout main_chat_layout;
    private TextView main_chat_name;
    private TextView main_chat_desc;

    private Association asso;
    private User user;

    /**
     * Initialize a new AssociationDetailFragment
     *
     * @param user User of the app
     * @param asso Association to be displayed
     * @return A new AssociationDetailFragment displaying an association details
     */
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
            mListener.onFragmentInteraction(CommunicationTag.SET_TITLE, asso.getName());
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
        loadUriIntoImageView(asso_icon, asso.getIconUri(), getContext());

        // Association banner
        ImageView asso_banner = view.findViewById(R.id.association_detail_banner);
        loadUriIntoImageView(asso_banner, asso.getBannerUri(), getContext());

        // Upcoming event views
        upcoming_event_layout = view.findViewById(R.id.association_detail_upcoming_event);
        upcoming_event_name = view.findViewById(R.id.association_detail_upcoming_event_name);
        upcoming_event_icon = view.findViewById(R.id.association_detail_upcoming_event_icon);
        upcoming_event_date = view.findViewById(R.id.association_detail_upcoming_event_date);
        loadUpcomingEvent();
        setUpcomingEventButtonBehaviour();

        // Chat views
        main_chat_layout = view.findViewById(R.id.association_detail_chat);
        main_chat_name = view.findViewById(R.id.association_detail_chat_name);
        main_chat_desc = view.findViewById(R.id.association_detail_chat_desc);
        loadMainChat();
        setMainChatButtonBehaviour();

        return view;
    }

    /**
     * Load a drawable id to the fav icon
     * @param drawable id of the drawable
     */
    private void loadFavImage(int drawable) {
        Glide.with(getContext())
                .load(drawable)
                .centerCrop()
                .into(asso_fav);
    }

    /**
     * Set up the favorite button behaviour
     * If the user isn't authenticated, stay the same
     * Else it favorites/unfavorites the association
     */
    private void setFavButtonBehaviour() {
        if (user.isConnected() && ((AuthenticatedUser) user).isFollowedAssociation(asso.getId()))
            loadFavImage(R.drawable.fav_on);
        else
            loadFavImage(R.drawable.fav_off);

        asso_fav.setOnClickListener(v -> {
            if (user.isConnected()) {
                AuthenticatedUser auth = (AuthenticatedUser) user;
                if (auth.isFollowedAssociation(asso.getId())) {
                    auth.removeFavAssociation(asso.getId());
                    loadFavImage(R.drawable.fav_off);
                    asso_fav.setContentDescription(NOT_FAV_CONTENT);
                } else {
                    auth.addFollowedAssociation(asso.getId());
                    loadFavImage(R.drawable.fav_on);
                    asso_fav.setContentDescription(FAV_CONTENT);
                }

                DatabaseFactory.getDependency().updateUser(auth);
            } else {
                Snackbar.make(getView(), "Login to access your favorite associations", 5000).show();
            }
        });
    }

    /**
     * Set up the upcoming event clicking behaviour to go on the event detailed page
     */
    private void setUpcomingEventButtonBehaviour() {
        upcoming_event_layout.setOnClickListener(v -> {
            if (upcoming_event != null)
                mListener.onFragmentInteraction(OPEN_EVENT_DETAIL_FRAGMENT, upcoming_event);
        });
    }

    /**
     * Use Firebase to load the upcoming event data in the variable upcoming_event
     * If there is no upcoming event (ie the association has no event), upcoming_event will stay null
     */
    private void loadUpcomingEvent() {
        // Fetch online data of the upcoming event
        if (asso.getShortDescription().isEmpty())
            upcoming_event_name.setText("No upcoming event :(");
        else
            DatabaseFactory.getDependency().getEventsFromIds(asso.getUpcomingEvents(), result -> {
                if (result != null && !result.isEmpty()) {
                    upcoming_event = result.get(0);
                    for (Event event : result) {
                        if (event.getStartDate().before(upcoming_event.getStartDate()))
                            upcoming_event = event;
                    }
                    upcoming_event_name.setText(upcoming_event.getName());
                    upcoming_event_date.setText(upcoming_event.getStartDate().toString());
                    loadUriIntoImageView(upcoming_event_icon, upcoming_event.getIconUri(), getContext());
                }
            });
    }

    /**
     * Use Firebase to load the main chat data in the variable main_chat
     * If there is no main chat (ie the association has no chat), main_chat will stay null
     */
    private void loadMainChat() {
        // Fetch online data of the main_chat
        if (asso.getChannelId() == null)
            main_chat_name.setText("There is no chat :(");
        else
            DatabaseFactory.getDependency().getChannelFromId(asso.getChannelId(), result -> {
                if (result != null) {
                    main_chat = result;
                    main_chat_name.setText(main_chat.getName());
                    main_chat_desc.setText(main_chat.getShortDescription());
                }
            });
    }

    /**
     * Set up the Chat button behaviour to redirect to the chat page
     */
    private void setMainChatButtonBehaviour() {
        main_chat_layout.setOnClickListener(v -> {
            if (main_chat != null) {
                if (user.isConnected())
                    mListener.onFragmentInteraction(CommunicationTag.OPEN_CHAT_FRAGMENT, main_chat);
                else
                    Snackbar.make(getView(), "Login to access chat room", 5000).show();
            }
        });
    }

}
