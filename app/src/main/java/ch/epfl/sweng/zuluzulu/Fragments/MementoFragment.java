package ch.epfl.sweng.zuluzulu.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.zuluzulu.Adapters.EventArrayAdapter;
import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.IdlingResource.IdlingResourceFactory;
import ch.epfl.sweng.zuluzulu.OnFragmentInteractionListener;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Event;
import ch.epfl.sweng.zuluzulu.Structure.EventBuilder;
import ch.epfl.sweng.zuluzulu.Structure.EventDate;
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
    final static public String ENAC_MEMENTO_URL = "https://memento.epfl.ch/api/jahia/mementos/enac/events/fr/?format=json";
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
        urlHandler.execute(ENAC_MEMENTO_URL, EPFL_MEMENTO_URL, ASSOCIATION_MEMENTO_URL);

        // Send increment to wait async execution in test
        IdlingResourceFactory.incrementCountingIdlingResource();
    }

    /**
     * Handle memento data
     *
     * @param result Json aray datas
     */
    private void handleMemento(List<String> result) {
        if (result != null) {
            for(int i = 0; i < result.size(); i++) {
                if(result.get(i) != null && !result.get(i).isEmpty() ) {
                    addEvent(result.get(i));
                }
            }
        }

        addDatabase();
        IdlingResourceFactory.decrementCountingIdlingResource();
    }

    private void addDatabase() {
        for (Event event : events) {
            DatabaseFactory.getDependency().addEvent(event);
        }
    }

    private void addEvent(String datas) {
        if (datas == null || datas.isEmpty()) {
            return;
        }

        JSONArray jsonarray;
        try {
            jsonarray = new JSONArray(datas);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);

                this.events.add(createEvent(jsonobject));
                eventAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Could not parse json");
        }
    }

    private Event createEvent(JSONObject jsonobject) throws JSONException, IllegalArgumentException {
        return new EventBuilder()
                // Use this to avoid collision
                .setId(Integer.toString(jsonobject.getString("title").hashCode()))
                .setDate(new EventDate(
                        jsonobject.getString("event_start_date"), jsonobject.getString("event_start_time"),
                        jsonobject.getString("event_end_date"), jsonobject.getString("event_end_time")))
                .setUrlPlaceAndRoom(jsonobject.getString("event_url_place_and_room"))
                .setAssosId("0")
                .setChannelId(DatabaseFactory.getDependency().getNewChannelId())
                .setFollowers(new ArrayList<>())
                .setShortDesc(jsonobject.getString("description"))
                .setName(jsonobject.getString("title"))
                .setLongDesc(jsonobject.getString("description"))
                .setOrganizer(jsonobject.getString("event_organizer"))
                .setPlace(jsonobject.getString("event_place_and_room"))
                .setBannerUri(jsonobject.getString("event_visual_absolute_url"))
                .setIconUri(jsonobject.getString("event_visual_absolute_url"))
                .setWebsite(jsonobject.getString("event_url_link"))
                .setContact(jsonobject.getString("event_contact"))
                .setCategory(jsonobject.getString("event_category_fr"))
                .setSpeaker(jsonobject.getString("event_speaker"))
                .build();
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