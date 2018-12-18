package ch.epfl.sweng.zuluzulu.fragments.adminFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.zuluzulu.adapters.EventArrayAdapter;
import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.fragments.FragmentWithUser;
import ch.epfl.sweng.zuluzulu.fragments.SuperFragment;
import ch.epfl.sweng.zuluzulu.idlingResource.IdlingResourceFactory;
import ch.epfl.sweng.zuluzulu.OnFragmentInteractionListener;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.structure.Event;
import ch.epfl.sweng.zuluzulu.structure.EventBuilder;
import ch.epfl.sweng.zuluzulu.structure.EventDate;
import ch.epfl.sweng.zuluzulu.structure.user.Admin;
import ch.epfl.sweng.zuluzulu.urlTools.MementoParser;
import ch.epfl.sweng.zuluzulu.urlTools.UrlHandler;
import ch.epfl.sweng.zuluzulu.structure.user.User;
import ch.epfl.sweng.zuluzulu.structure.user.UserRole;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MementoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MementoFragment extends FragmentWithUser<Admin> {
    final static public String EPFL_MEMENTO_URL = "https://memento.epfl.ch/api/jahia/mementos/epfl/events/fr/?format=json";
    final static public String ASSOCIATION_MEMENTO_URL = "https://memento.epfl.ch/api/jahia/mementos/associations/events/fr/?format=json";
    private static final String TAG = "MEMENTO_FRAGMENT";
    private static final UserRole ROLE_REQUIRED = UserRole.ADMIN;
    private EventArrayAdapter eventAdapter;
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
        bundle.putSerializable(ARG_USER, user);

        // Transmit data
        MementoFragment fragment = new MementoFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.events = new ArrayList<>();
        this.eventAdapter = new EventArrayAdapter(getContext(), events, mListener, user);


        mListener.onFragmentInteraction(CommunicationTag.SET_TITLE, "Memento loader");
        UrlHandler urlHandler = new UrlHandler(this::handleMemento, new MementoParser());
        urlHandler.execute(ASSOCIATION_MEMENTO_URL, EPFL_MEMENTO_URL);

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
            for (int i = 0; i < result.size(); i++) {
                if (result.get(i) != null && !result.get(i).isEmpty()) {
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

    private Event createEvent(JSONObject jsonobject) throws JSONException, IllegalArgumentException, ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return new EventBuilder()
                // Use this to avoid collision
                .setId(Integer.toString(jsonobject.getString("title").hashCode()))
                .setDate(new EventDate(
                        simpleDateFormat.parse(jsonobject.getString("event_start_date") + " " + jsonobject.getString("event_start_time")),
                        simpleDateFormat.parse(jsonobject.getString("event_end_date") + " " + jsonobject.getString("event_end_time"))))
                .setUrlPlaceAndRoom(jsonobject.getString("event_url_place_and_room"))
                .setAssosId("0")
                .setChannelId(DatabaseFactory.getDependency().getNewChannelId())
                .setFollowers(new ArrayList<>())
                .setShortDesc(jsonobject.getString("description"))
                .setName(jsonobject.getString("title"))
                .setLongDesc(jsonobject.getString("description"))
                .setOrganizer(jsonobject.getString("event_organizer"))
                .setPlace(jsonobject.getString("event_place_and_room"))
                .setBannerUri(null)
                .setIconUri(jsonobject.getString("event_visual_absolute_url"))
                .setWebsite(jsonobject.getString("event_url_link"))
                .setContact(jsonobject.getString("event_contact"))
                .setCategory(jsonobject.getString("event_category_fr"))
                .setSpeaker(jsonobject.getString("event_speaker"))
                .build();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_memento, container, false);

        ListView list = view.findViewById(R.id.memento_list_view);
        list.setAdapter(eventAdapter);

        return view;
    }
}
