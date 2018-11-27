package ch.epfl.sweng.zuluzulu.Fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.IdlingResource.IdlingResourceFactory;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Association;
import ch.epfl.sweng.zuluzulu.Structure.Channel;
import ch.epfl.sweng.zuluzulu.Structure.Event;
import ch.epfl.sweng.zuluzulu.User.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.User.User;
import ch.epfl.sweng.zuluzulu.tequila.HttpUtils;

import static ch.epfl.sweng.zuluzulu.CommunicationTag.OPEN_ASSOCIATION_DETAIL_FRAGMENT;

public class EventDetailFragment extends SuperFragment {

    public static final String TAG = "EVENT_DETAIL__TAG";
    private static final String ARG_USER = "ARG_USER";
    private static final String ARG_EVENT = "ARG_EVENT";
    private static final String FAV_CONTENT = "This event is in your favorites";
    private static final String NOT_FAV_CONTENT = "This event isn't in your favorites";
    //MapView mMapView;
    private ImageView event_fav;
    private Button chat_event;
    private Channel channel;
    private Event event;
    private User user;
    //private GoogleMap googleMap;

    private Button chat_room;
    private Channel chat;

    private Button assos_but;
    private Association assos;
    private WebView webview;


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


        /*event_fav.setContentDescription(NOT_FAV_CONTENT);
        if (user.isConnected() && ((AuthenticatedUser)user).isFavEvent(event)) {
            loadFavImage(R.drawable.fav_on);
            event_fav.setContentDescription(FAV_CONTENT);
        }*/

        view.findViewById(R.id.event_detail_export).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportEventToCalendar();
            }
        });

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

        webview = (WebView) view.findViewById(R.id.epflMapView);
        webview.getSettings().setJavaScriptEnabled(false);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setSupportZoom(true);
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                webView.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed(); // Ignore SSL certificate errors
            }

        });
        webview.loadUrl(HttpUtils.urlEncode(event.getUrlPlaceAndRoom()));


        assos_but = view.findViewById(R.id.event_detail_but_assos);
        loadAssos();
        setAssosButtonBehavior();

        chat_room = view.findViewById(R.id.event_detail_chatRoom);
        loadMainChat();
        setMainChatButtonBehaviour();


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
        //mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
       // mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (webview != null) webview.destroy();
        //mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        //mMapView.onLowMemory();
    }

    private void setFavButtonBehaviour() {
        if (user.isConnected() && ((AuthenticatedUser) user).isFavEvent(event))
            loadFavImage(R.drawable.fav_on);
        else
            loadFavImage(R.drawable.fav_off);

        event_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.isConnected()) {
                    AuthenticatedUser auth = (AuthenticatedUser) user;
                    if (auth.isFavEvent(event)) {
                        auth.removeFavEvent(event);
                        loadFavImage(R.drawable.fav_off);
                        event_fav.setContentDescription(NOT_FAV_CONTENT);
                    } else {
                        auth.addFavEvent(event);
                        loadFavImage(R.drawable.fav_on);
                        event_fav.setContentDescription(FAV_CONTENT);
                    }
                } else {
                    Snackbar.make(getView(), "Login to access your favorite event", 5000).show();
                }
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
        intent.putExtra(CalendarContract.Events.DESCRIPTION, event.getShortDesc());
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
        // Fetch online data of the main_chat
        if (event.getChannelId() != 0) {
            IdlingResourceFactory.incrementCountingIdlingResource();
            FirebaseFirestore.getInstance()
                    .document("channels/channel" + event.getChannelId())
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    FirebaseMapDecorator fmap = new FirebaseMapDecorator(documentSnapshot);
                    if (fmap.hasFields(Channel.FIELDS)) {
                        chat = new Channel(fmap);
                        chat_room.setText(chat.getName() + " Chat");
                    } else
                        chat_room.setText("Error loading the chat :(");
                    IdlingResourceFactory.decrementCountingIdlingResource();
                }
            });
        } else {
            chat_room.setText("There is no chat :(");
        }
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
        // Fetch online data of the main_chat
        if (event.getAssosId() != 0) {
            IdlingResourceFactory.incrementCountingIdlingResource();
            FirebaseFirestore.getInstance()
                    .document("assos_info/" + event.getAssosId())
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    FirebaseMapDecorator fmap = new FirebaseMapDecorator(documentSnapshot);
                    if (fmap.hasFields(Association.FIELDS)) {
                        assos = new Association(fmap);
                        assos_but.setText(assos.getName());
                    } else
                        assos_but.setText("Error loading the association :(");
                    IdlingResourceFactory.decrementCountingIdlingResource();
                }
            });
        } else {
            assos_but.setText("There is no association :(");
        }
    }


}

