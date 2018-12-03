package ch.epfl.sweng.zuluzulu.Fragments;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Association;
import ch.epfl.sweng.zuluzulu.Structure.Channel;
import ch.epfl.sweng.zuluzulu.Structure.Event;
import ch.epfl.sweng.zuluzulu.User.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.User.User;

import static ch.epfl.sweng.zuluzulu.CommunicationTag.OPEN_ASSOCIATION_DETAIL_FRAGMENT;

public class EventDetailFragment extends SuperFragment {

    public static final String TAG = "EVENT_DETAIL__TAG";
    private static final String ARG_USER = "ARG_USER";
    private static final String ARG_EVENT = "ARG_EVENT";
    private static final String FAV_CONTENT = "This event is in your favorites";
    private static final String NOT_FAV_CONTENT = "This event isn't in your favorites";
    MapView mMapView;
    private ImageView event_fav;
    private Button chat_event;
    private Channel channel;
    private Event event;
    private User user;
    private GoogleMap googleMap;

    private Button chat_room;
    private Channel chat;

    private Button assos_but;
    private Association assos;


    public static EventDetailFragment newInstance(User user, Event event) {
        if (event == null)
            throw new NullPointerException("Error creating an EventDetailFragment:\n" +
                    "Event is null");
        if (user == null)
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
        setFavButtonBehaviour();

        view.findViewById(R.id.event_detail_export).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportEventToCalendar();
            }
        });

        TextView event_like = view.findViewById(R.id.event_detail_tv_numberLikes);
        event_like.setText("" + event.getLikes());

        TextView event_desc = view.findViewById(R.id.event_detail_desc);
        event_desc.setText(event.getLongDescription());

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

        //google map integration
        mMapView = view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        mMapView.getMapAsync(mMap -> {
            googleMap = mMap;

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            } else {
                // For showing a move to my location button
                googleMap.setMyLocationEnabled(true);
            }

            // For dropping a marker at a point on the Map
            LatLng epfl = new LatLng(46.520537, 6.570930);
            LatLng co = new LatLng(46.520135, 6.565263);
            googleMap.addMarker(new MarkerOptions().position(epfl).title("ce").snippet("ce"));
            googleMap.addMarker(new MarkerOptions().position(co).title("co").snippet("co"));


            // For zooming automatically to the location of the marker
            CameraPosition cameraPosition = new CameraPosition.Builder().target(epfl).zoom(12).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        });

        chat_room = view.findViewById(R.id.event_detail_chatRoom);
        loadMainChat();
        setMainChatButtonBehaviour();

        assos_but = view.findViewById(R.id.event_detail_but_assos);
        loadAssos();
        setAssosButtonBehavior();



        return view;
    }

    private void loadFavImage(int drawable) {
        Glide.with(getContext())
                .load(drawable)
                .centerCrop()
                .into(event_fav);
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    private void setFavButtonBehaviour() {
        if (user.isConnected() && ((AuthenticatedUser) user).isFollowedEvent(event.getId()))
            loadFavImage(R.drawable.fav_on);
        else
            loadFavImage(R.drawable.fav_off);

        event_fav.setOnClickListener(v -> {
            if (user.isConnected()) {
                AuthenticatedUser auth = (AuthenticatedUser) user;
                if (auth.isFollowedEvent(event.getId())) {
                    auth.removeFollowedChannel(event.getId());
                    loadFavImage(R.drawable.fav_off);
                    event_fav.setContentDescription(NOT_FAV_CONTENT);
                } else {
                    auth.addFollowedEvent(event.getId());
                    loadFavImage(R.drawable.fav_on);
                    event_fav.setContentDescription(FAV_CONTENT);
                }
            } else {
                Snackbar.make(getView(), "Login to access your favorite event", 5000).show();
            }
        });
    }

    private void exportEventToCalendar() {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType("vnd.android.cursor.item/event");

        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, event.getStartDate().getTime());
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, event.getStartDate().getTime() + 3600 * 2);
        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);

        intent.putExtra(CalendarContract.Events.TITLE, event.getName());
        intent.putExtra(CalendarContract.Events.DESCRIPTION,  event.getShortDescription());
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, "To be precised");

        startActivity(intent);
    }

    private void setMainChatButtonBehaviour() {
        chat_room.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (chat != null) {
                    if (user.isConnected())
                        mListener.onFragmentInteraction(CommunicationTag.OPEN_CHAT_FRAGMENT, chat);
                    else
                        Snackbar.make(getView(), "Login to access chat room", 5000).show();
                }
            }
        });
    }

    private void loadMainChat() {
        DatabaseFactory.getDependency().getChannelFromId(event.getChannelId(), result -> {
            if(result != null) {
                chat = result;
                chat_room.setText(chat.getName() + "'s chat");
            }
        });
    }

    public void setAssosButtonBehavior() {
        assos_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (assos != null)
                    mListener.onFragmentInteraction(OPEN_ASSOCIATION_DETAIL_FRAGMENT, assos);
            }
        });
    }

    private void loadAssos() {
        DatabaseFactory.getDependency().getAssociationFromId(event.getAssociationId(), result -> {
            if(result != null) {
                assos = result;
                assos_but.setText(assos.getName());
            }
        });
    }
}

