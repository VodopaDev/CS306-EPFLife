package ch.epfl.sweng.zuluzulu.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.Adapters.EventArrayAdapter;
import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.IdlingResource.IdlingResourceFactory;
import ch.epfl.sweng.zuluzulu.OnFragmentInteractionListener;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Event;
import ch.epfl.sweng.zuluzulu.URLTools.MementoParser;
import ch.epfl.sweng.zuluzulu.URLTools.UrlHandler;
import ch.epfl.sweng.zuluzulu.User.User;
import ch.epfl.sweng.zuluzulu.User.UserRole;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MementoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MementoFragment extends SuperFragment {
    final static public String EPFL_MEMENTO_URL = "https://memento.epfl.ch/api/jahia/mementos/epfl/events/fr/?format=json";
    final static public String ASSOCIATION_MEMENTO_URL = "https://memento.epfl.ch/api/jahia/mementos/associations/events/fr/?format=json";
    private static final String TAG = "MEMENTO_FRAGMENT";
    private static final UserRole ROLE_REQUIRED = UserRole.ADMIN;
    private EventArrayAdapter eventAdapter;
    private User user;
    private ArrayList<Event> events;

    public MementoFragment() {
        // Required empty public constructor
    }

    /**
     * New instance
     *
     * @param user User
     * @return The fragment
     */
    public static MementoFragment newInstance(User user) {
        if (!user.hasRole(ROLE_REQUIRED)) {
            return null;
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG, user);

        // Transmit data
        MementoFragment fragment = new MementoFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            this.user = (User) getArguments().getSerializable(TAG);
        }

        this.events = new ArrayList<Event>();

        this.eventAdapter = new EventArrayAdapter(getContext(), events, mListener, user);


        mListener.onFragmentInteraction(CommunicationTag.SET_TITLE, "Memento loader");
        UrlHandler urlHandler = new UrlHandler(this::handleMemento, new MementoParser());
        urlHandler.execute(EPFL_MEMENTO_URL, ASSOCIATION_MEMENTO_URL);

        // Send increment to wait async execution in test
        IdlingResourceFactory.incrementCountingIdlingResource();
        IdlingResourceFactory.incrementCountingIdlingResource();
    }

    /**
     * Handle memento data
     *
     * @param result Json aray datas
     */
    private void handleMemento(List<String> result) {

        if (result != null && !result.isEmpty() && !result.get(0).isEmpty()) {
            String datas = result.get(0);
            addEvent(datas);
        }

        addDatabase();
        IdlingResourceFactory.decrementCountingIdlingResource();
    }

    private void addDatabase(){
        int i = 0;
        for (Event event : events
             ) {
            String pattern = "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

            Date date = null;
            try {
                date = simpleDateFormat.parse(event.getStartDateString());
            } catch (ParseException e) {
                date = new Date();
            }
            //the map for the event
            Map<String, Object> docData = new HashMap<>();
            docData.put("icon_uri", event.getIconUri());
            docData.put("banner_uri", event.getIconUri());
            docData.put("id", i);
            docData.put("assos_id", 0);
            docData.put("channel_id", 1);
            docData.put("likes", event.getLikes());
            docData.put("long_desc", event.getLongDesc());
            docData.put("name", event.getName());
            docData.put("organizer", event.getOrganizer());
            docData.put("place", event.getPlace());
            docData.put("short_desc", event.getShortDesc());
            docData.put("start_date", date);
            DatabaseFactory.getDependency().collection("events_info").document("event" + Integer.toString(i)).set(docData).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Snackbar.make(getView(), "DONE", Snackbar.LENGTH_LONG).show();
                }
            });
            }
    }

    private void addEvent(String datas) {
        if (datas == null || datas.isEmpty()) {
            return;
        }

        JSONArray jsonarray = null;
        try {
            jsonarray = new JSONArray(datas);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
               /* System.out.println("end_date_string => " + jsonobject.getString("event_end_date"));
                System.out.println("start_time_string => " + jsonobject.getString("event_start_time"));
                System.out.println("end_time_string => " + jsonobject.getString("event_end_time"));
               */ // nom de l'association qui organise !

                Event event = new Event(i,
                        jsonobject.getString("title"),
                        jsonobject.getString("description"), jsonobject.getString("description"),
                        jsonobject.getString("event_start_date") + " " + jsonobject.getString("event_start_time"),
                        0,
                        jsonobject.getString("event_organizer"),
                        jsonobject.getString("event_place_and_room"),
                        jsonobject.getString("event_visual_absolute_url"), jsonobject.getString("event_visual_absolute_url"),
                        jsonobject.getString("event_url_place_and_room"), jsonobject.getString("event_url_link"), jsonobject.getString("event_contact"));
                this.events.add(event);
                eventAdapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "Could not parse json");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_memento, container, false);

        ListView list = view.findViewById(R.id.memento_list_view);
        list.setAdapter(eventAdapter);

        return view;
    }
}
