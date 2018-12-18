package ch.epfl.sweng.zuluzulu.fragments;



import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.structure.Association;
import ch.epfl.sweng.zuluzulu.structure.Channel;
import ch.epfl.sweng.zuluzulu.structure.Event;
import ch.epfl.sweng.zuluzulu.structure.user.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.structure.user.User;
import ch.epfl.sweng.zuluzulu.utility.ImageLoader;
import ch.epfl.sweng.zuluzulu.utility.Utils;

import static ch.epfl.sweng.zuluzulu.CommunicationTag.OPEN_ASSOCIATION_DETAIL_FRAGMENT;

public class EventDetailFragment extends FragmentWithUser<User> {
    public static final String TAG = "EVENT_DETAIL__TAG";
    private static final String ARG_EVENT = "ARG_EVENT";

    private TextView event_like;
    private Event event;


    private Button channelButton;
    private Channel channel;


    private Button associationButton;
    private Association association;



    public static EventDetailFragment newInstance(User user, Event event) {
        if (event == null)
            throw new IllegalArgumentException("Error creating an EventDetailFragment:\n" +
                    "Event is null");
        if (user == null)
            throw new IllegalArgumentException("Error creating an EventDetailFragment:\n" +
                    "User is null");

        EventDetailFragment fragment = new EventDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        args.putSerializable(ARG_EVENT, event);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            event = (Event) getArguments().getSerializable(ARG_EVENT);
            mListener.onFragmentInteraction(CommunicationTag.SET_TITLE, event.getName());
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_detail, container, false);


        view.findViewById(R.id.event_detail_export).setOnClickListener(v -> exportEventToCalendar());

        event_like = view.findViewById(R.id.event_detail_tv_numberLikes);
        event_like.setText(String.valueOf(event.getLikes()));

        // Favorite button
        ImageButton event_like_button = view.findViewById(R.id.event_detail_like_button);
        setLikeButtonBehaviour(event_like_button);

        TextView event_desc = view.findViewById(R.id.event_detail_desc);
        event_desc.setText(event.getLongDescription());

        TextView event_date = view.findViewById(R.id.event_detail_date);
        event_date.setText(event.getDateTimeUser(true));

        TextView event_organizer = view.findViewById(R.id.event_detail_organizer);
        event_organizer.setText(event.getOrganizer());

        TextView event_place = view.findViewById(R.id.event_detail_place);
        event_place.setText(event.getPlace());


        TextView event_contact = view.findViewById(R.id.event_detail_contact);
        event_contact.setText(event.getContact());

        TextView event_website = view.findViewById(R.id.event_detail_website);
        event_website.setText(event.getWebsite());

        TextView event_speaker = view.findViewById(R.id.event_detail_speaker);
        event_speaker.setText(event.getSpeaker());

        TextView event_category = view.findViewById(R.id.event_detail_category);
        event_category.setText(event.getCategory());

        WebView myWebView = view.findViewById(R.id.webview);
        myWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // do your handling codes here, which url is the requested url
                // probably you need to open that url rather than redirect:
                view.loadUrl(url);
                return false; // then it is not handled by default action
            }
        });
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.loadUrl(event.getUrlPlaceAndRoom());

        // load event icon and banner
        ImageLoader.loadUriIntoImageView(view.findViewById(R.id.event_detail_icon), event.getIconUri(), getContext());
        ImageLoader.loadUriIntoImageView(view.findViewById(R.id.event_detail_banner), event.getBannerUri(), getContext());

        channelButton = view.findViewById(R.id.event_detail_chatRoom);
        associationButton = view.findViewById(R.id.event_detail_but_assos);
        loadChannel();
        loadAssociation();

        return view;
    }

    private void setLikeButtonBehaviour(ImageButton event_like_button) {

        event_like_button.setSelected(user.isConnected() && ((AuthenticatedUser) user).isFollowedEvent(event.getId()));


        event_like_button.setOnClickListener(view -> {
            if (user.isConnected()) {
                AuthenticatedUser auth = (AuthenticatedUser) user;
                Snackbar snack;
                if (auth.isFollowedEvent(event.getId())) {
                    auth.removeFollowedEvent(event.getId());
                    auth.removeFollowedChannel(event.getChannelId());
                    event.removeFollower(user.getSciper());
                    DatabaseFactory.getDependency().removeEventFromUserFollowedEvents(event, auth);
                    DatabaseFactory.getDependency().removeChannelFromUserFollowedChannels(channel, auth);
                    event_like_button.setSelected(false);
                    Toast.makeText(getActivity(), getContext().getString(R.string.event_unfollowed), Toast.LENGTH_SHORT).show();
                } else {
                    auth.addFollowedEvent(event.getId());
                    auth.addFollowedChannel(event.getChannelId());
                    event.addFollower(user.getSciper());
                    DatabaseFactory.getDependency().addEventToUserFollowedEvents(event, auth);
                    DatabaseFactory.getDependency().addChannelToUserFollowedChannels(channel, auth);
                    event_like_button.setSelected(true);
                    Toast.makeText(getActivity(), getContext().getString(R.string.event_followed), Toast.LENGTH_SHORT).show();
                }
            } else {
                Utils.showConnectSnackbar(getView());
            }
        });
    }

    /**
     * Launch an Intent to open the Phone calendar app and register the event
     */
    private void exportEventToCalendar() {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType("vnd.android.cursor.item/event");

        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, event.getStartDate().getTime());
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, event.getEndDate().getTime());
        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false);

        intent.putExtra(CalendarContract.Events.TITLE, event.getName());
        intent.putExtra(CalendarContract.Events.DESCRIPTION, event.getLongDescription());
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, event.getPlace());
        intent.putExtra(CalendarContract.Events.ORGANIZER, event.getOrganizer());

        startActivity(intent);
    }

    /**
     * Set up the channel button to redirect to the event's channel when clicked
     */
    private void setChannelButtonBehaviour() {
        channelButton.setOnClickListener(v -> {
                    if (user.isConnected())
                        mListener.onFragmentInteraction(CommunicationTag.OPEN_CHAT_FRAGMENT, channel);
                    else
                        Utils.showConnectSnackbar(getView());
                }
        );
    }

    /**
     * Set up the association button to redirect to the event's association when clicked
     */
    private void setAssociationButtonBehavior() {
        associationButton.setOnClickListener(v -> mListener.onFragmentInteraction(OPEN_ASSOCIATION_DETAIL_FRAGMENT, association));
    }

    /**
     * Load the event's channel and set up the channel button if such a channel exists
     */
    private void loadChannel() {
        DatabaseFactory.getDependency().getChannelFromId(event.getChannelId(), result -> {
            channel = result;
            String str = "Chat room";
            channelButton.setText(str);
            setChannelButtonBehaviour();
        });
    }

    /**
     * Load the event's association and set up the association button if such an association exists
     */
    private void loadAssociation() {
        DatabaseFactory.getDependency().getAssociationFromId(event.getAssociationId(), result -> {
            association = result;
            associationButton.setText(association.getName());
            setAssociationButtonBehavior();
        });
    }
}

