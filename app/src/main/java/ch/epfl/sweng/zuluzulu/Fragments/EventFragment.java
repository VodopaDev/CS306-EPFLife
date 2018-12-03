package ch.epfl.sweng.zuluzulu.Fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import ch.epfl.sweng.zuluzulu.Adapters.EventArrayAdapter;
import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.OnFragmentInteractionListener;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Event;
import ch.epfl.sweng.zuluzulu.User.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.User.User;
import ch.epfl.sweng.zuluzulu.User.UserRole;

import static ch.epfl.sweng.zuluzulu.CommunicationTag.CREATE_EVENT;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventFragment extends SuperFragment {
    private static final String ARG_USER = "ARG_USER";

    private User user;

    private ArrayList<Event> allEvents;
    private ArrayList<Event> followedEvents;
    private ArrayList<Event> eventsToFilter;
    private ArrayList<Event> eventsFiltered;

    private EventArrayAdapter event_adapter;

    private ListView listview_event;
    private Button button_event_all;
    private Button button_event_fav;

    private CheckBox checkbox_event_sort_name;
    private CheckBox checkbox_event_sort_date;
    private CheckBox checkbox_event_sort_like;
    private Comparator<Event> currentComparator;

    private EditText event_search_bar;

    private Calendar eventCalendar;
    private EditText event_fragment_from_date;
    private EditText event_fragment_to_date;
    private Date dateFrom;
    private Date dateTo;

    private ImageButton filter_button;
    private ConstraintLayout event_filter_constraint_layout;

    public EventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param user user.
     * @return A new instance of fragment EventFragment.
     */
    public static EventFragment newInstance(User user) {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable(ARG_USER);
            mListener.onFragmentInteraction(CommunicationTag.SET_TITLE, "Events");
        }

        allEvents = new ArrayList<>();
        followedEvents = new ArrayList<>();
        eventsToFilter = allEvents;
        eventsFiltered = new ArrayList<>();
        event_adapter = new EventArrayAdapter(getContext(), eventsFiltered, mListener, user);
        currentComparator = Event.nameComparator();
        eventCalendar = Calendar.getInstance();
        fillEventLists();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        listview_event = view.findViewById(R.id.event_fragment_listview);
        listview_event.setAdapter(event_adapter);

        // Add event button displayed only if user is an Admin
        if (user.hasRole(UserRole.ADMIN)) {
            Button button_event_add = view.findViewById(R.id.event_add_button);
            button_event_add.setVisibility(View.VISIBLE);
            button_event_add.setOnClickListener(v -> mListener.onFragmentInteraction(CREATE_EVENT, null));
        }

        // Main buttons to switch between the all events or only followed events
        button_event_fav = view.findViewById(R.id.event_fragment_fav_button);
        button_event_all = view.findViewById(R.id.event_fragment_all_button);
        button_event_fav.setOnClickListener(v -> {
            if (user.isConnected())
                updateListView(button_event_fav, followedEvents);
            else
                Snackbar.make(getView(), "Login to access your favorite event", 5000).show();
        });
        button_event_all.setOnClickListener(v -> updateListView(button_event_all, allEvents));

        checkbox_event_sort_name = view.findViewById(R.id.event_fragment_checkBox_sort_name);
        checkbox_event_sort_date = view.findViewById(R.id.event_fragment_checkBox_sort_date);
        checkbox_event_sort_like = view.findViewById(R.id.event_fragment_checkbox_sort_like);
        selectClickedCheckbox(checkbox_event_sort_name);

        event_fragment_from_date = view.findViewById(R.id.event_fragment_from_date);
        event_fragment_to_date = view.findViewById(R.id.event_fragment_to_date);

        event_search_bar = view.findViewById(R.id.event_fragment_search_bar);

        filter_button = view.findViewById(R.id.event_fragment_filter_button);
        event_filter_constraint_layout = view.findViewById(R.id.even_filter_constraintLayout);

        // All method the set the behaviour of the checkboxes, date selecting and name/description matching
        setFilteringWithText();
        setFilteringWithDate();
        setToggleFilterVisibilityBehaviour();
        setSortingBehaviourOnCheckbox(checkbox_event_sort_date, Event.dateComparator());
        setSortingBehaviourOnCheckbox(checkbox_event_sort_like, Event.likeComparator());
        setSortingBehaviourOnCheckbox(checkbox_event_sort_name, Event.nameComparator());

        return view;
    }

    /**
     * Fill the event list with the data from the Proxy
     * Before doing so, clear all event lists of the class to avoid duplication
     */
    private void fillEventLists() {
        DatabaseFactory.getDependency().getAllEvents(result -> {
            allEvents.clear();
            followedEvents.clear();
            eventsFiltered.clear();
            allEvents.addAll(result);
            for (Event event : allEvents) {
                if (user.isConnected() && ((AuthenticatedUser) user).isFollowedEvent(event.getId()))
                    followedEvents.add(event);
            }
            eventsToFilter = allEvents;
            eventsFiltered.addAll(eventsToFilter);
            sortWithCurrentComparator();
        });
    }

    /**
     * Set the sorting behaviour of a Checkbox for the displayed list of event
     * @param checkBox checkbox that will be clicked
     * @param comparator comparator to use when the checkbox is clicked
     */
    private void setSortingBehaviourOnCheckbox(CheckBox checkBox, Comparator<Event> comparator){
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.isEnabled()) {
                    selectClickedCheckbox(checkBox);
                    currentComparator = comparator;
                    sortWithCurrentComparator();
                }
            }
        });
    }

    /**
     * Sort the displayed list of event using the currently selected comparator
     */
    private void sortWithCurrentComparator(){
        Collections.sort(eventsFiltered, currentComparator);
        event_adapter.notifyDataSetChanged();
    }

    private void setFilteringWithText() {
        event_search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterWithText(s.toString());
                sortWithCurrentComparator();
            }

            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        });
    }

    /**
     * Deselect all checkboxes and then select+disable the currently used checkbox
     * @param checkBox checkbox to select and disable
     */
    private void selectClickedCheckbox(CheckBox checkBox){
        checkbox_event_sort_date.setEnabled(true);
        checkbox_event_sort_like.setEnabled(true);
        checkbox_event_sort_name.setEnabled(true);
        checkbox_event_sort_date.setChecked(false);
        checkbox_event_sort_like.setChecked(false);
        checkbox_event_sort_name.setChecked(false);

        checkBox.setChecked(true);
        checkBox.setEnabled(false);
    }

    /**
     *
     * @param selectedButton the new selected button
     * @param newEventsToFilter the new events to filter
     */
    private void updateListView(Button selectedButton, ArrayList<Event> newEventsToFilter) {
        event_search_bar.getText().clear();
        event_search_bar.clearFocus();
        // TODO: remove date from the calendar

        button_event_all.setBackgroundColor(getResources().getColor(R.color.colorGrayDarkTransparent));
        button_event_fav.setBackgroundColor(getResources().getColor(R.color.colorGrayDarkTransparent));
        selectedButton.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
        eventsToFilter = newEventsToFilter;
        sortWithCurrentComparator();
    }

    /**
     * Set the toggle of filter options
     * When clicked, display all filtering options. Clicking again hide the interface.
     */
    public void setToggleFilterVisibilityBehaviour(){
        filter_button.setOnClickListener(v -> {
            if (event_filter_constraint_layout.getVisibility() == View.VISIBLE)
                event_filter_constraint_layout.setVisibility(View.GONE);
            else
                event_filter_constraint_layout.setVisibility(View.VISIBLE);
        });
    }

    /**
     * Filter the eventsToFilter using a string
     * @param s substring that must be contained in the name/description of the event
     */
    private void filterWithText(String s){
        dateFrom = null;
        dateTo = null;
        event_fragment_from_date.clearFocus();
        event_fragment_from_date.setText("");
        event_fragment_to_date.clearFocus();
        event_fragment_to_date.setText("");
        String keyWord = s.toLowerCase();
        eventsFiltered.clear();
        for (Event event : eventsToFilter) {
            if (event.getName().toLowerCase().contains(keyWord) ||
                    event.getShortDescription().toLowerCase().contains(keyWord) ||
                    event.getLongDescription().toLowerCase().contains((keyWord)))
                eventsFiltered.add(event);
        }
    }

    private void setFilteringWithDate(){
        event_fragment_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        eventCalendar.set(Calendar.YEAR, year);
                        eventCalendar.set(Calendar.MONTH, monthOfYear);
                        eventCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        dateFrom = eventCalendar.getTime();
                        //filterWithDate();
                        sanitizeDates();
                    }
                };

                if(getContext() != null)
                    new DatePickerDialog(getContext(), datePicker, eventCalendar
                            .get(Calendar.YEAR), eventCalendar.get(Calendar.MONTH),
                            eventCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        event_fragment_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        eventCalendar.set(Calendar.YEAR, year);
                        eventCalendar.set(Calendar.MONTH, monthOfYear);
                        eventCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        dateTo = eventCalendar.getTime();
                        filterWithDate();
                    }
                };

                if(getContext() != null)
                    new DatePickerDialog(getContext(), datePicker, eventCalendar
                            .get(Calendar.YEAR), eventCalendar.get(Calendar.MONTH),
                            eventCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void filterWithDate(){
        sanitizeDates();
        event_search_bar.setText("");
        eventsFiltered.clear();
        for(Event event: eventsToFilter){
            if(event.getStartDate().after(dateFrom) &&
                    event.getStartDate().before(dateTo))
                eventsFiltered.add(event);
        }
    }

    private void sanitizeDates(){
        if(dateTo == null)
            dateTo = new Date(Long.MAX_VALUE-1);
        if(dateFrom == null)
            dateFrom = new Date(1L);

        if(dateTo.before(dateFrom)){
            Date tempDate = dateFrom;
            dateFrom = dateTo;
            dateTo = tempDate;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        event_fragment_from_date.setText(dateFrom.equals(new Date(0L)) ? "" : sdf.format(dateFrom));
        event_fragment_to_date.setText(dateTo.equals(new Date(Long.MAX_VALUE)) ? "" : sdf.format(dateTo));
    }
}