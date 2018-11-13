package ch.epfl.sweng.zuluzulu.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.OnFragmentInteractionListener;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.Structure.Event;
import ch.epfl.sweng.zuluzulu.Structure.User;

public class EventDetailFragment extends SuperFragment{

    public static final String TAG = "EVENT_DETAIL__TAG";
    private static final String ARG_USER = "ARG_USER";
    private static final String ARG_EVENT = "ARG_EVENT";
    private static final String FAV_CONTENT = "This event is in your favorites";
    private static final String NOT_FAV_CONTENT = "This event isn't in your favorites";

    private ImageView event_fav;

    private Event event;
    private User user;

    public static EventDetailFragment newInstance(User user, Event event) {
        if(event == null)
            throw new NullPointerException("Error creating an EventDetailFragment:\n" +
                    "Event is null");
        if(user == null)
            throw new NullPointerException("Error creating an EventDetailFragment:\n" +
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
            user = (User) getArguments().getSerializable(ARG_USER);
            event = (Event) getArguments().getSerializable(ARG_EVENT);
            mListener.onFragmentInteraction(CommunicationTag.SET_TITLE, event.getName());
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_detail, container, false);

        // Event name
        TextView event_name = view.findViewById(R.id.event_detail_name);
        event_name.setText(event.getName());

        // Favorite button
        event_fav = view.findViewById(R.id.event_detail_fav);
        //setFavButtonBehaviour();
        event_fav.setContentDescription(NOT_FAV_CONTENT);
        if(user.isConnected() /*&& ((AuthenticatedUser)user).isFavEvent(event)*/) {
            loadFavImage(R.drawable.fav_on);
            event_fav.setContentDescription(FAV_CONTENT);
        }

        TextView event_like = view.findViewById(R.id.event_detail_tv_numberLikes);
        event_like.setText("" + event.getLikes());

        TextView event_desc = view.findViewById(R.id.event_detail_desc);
        event_desc.setText(event.getLongDesc());

        TextView event_date = view.findViewById(R.id.event_detail_date);
        event_date.setText("" + event.getStartDateString());


        TextView event_organizer = view.findViewById(R.id.event_detail_organizer);
        event_organizer.setText(event.getOrganizer());

        TextView event_place = view.findViewById(R.id.event_detail_place);
        event_place.setText(event.getPlace());

        // Event icon
        ImageView event_icon = view.findViewById(R.id.event_detail_icon);
        Glide.with(getContext())
                .load(event.getIconUri())
                .centerCrop()
                .into(event_icon);

        ImageView event_banner = view.findViewById(R.id.event_detail_banner);
        Glide.with(getContext())
                .load(event.getBannerUri())
                .centerCrop()
                .into(event_banner);


        return view;
    }

    private void loadFavImage(int drawable){
        Glide.with(getContext())
                .load(drawable)
                .centerCrop()
                .into(event_fav);
    }

    /*private void setFavButtonBehaviour(){
        event_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.isConnected()){
                    AuthenticatedUser auth = (AuthenticatedUser)user;
                    if(auth.isFavEvent(event)){
                        auth.removeFavEvent(event);
                        loadFavImage(R.drawable.fav_off);
                        event_fav.setContentDescription(NOT_FAV_CONTENT);
                    }
                    else{
                        auth.addFavEvent(event);
                        loadFavImage(R.drawable.fav_on);
                        event_fav.setContentDescription(FAV_CONTENT);
                    }
                }
                else {
                    Snackbar.make(getView(), "Login to access your favorite events", 5000).show();
                }
            }
        });
    }*/



}
