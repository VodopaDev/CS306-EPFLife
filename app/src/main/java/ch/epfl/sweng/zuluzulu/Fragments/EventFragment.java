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
    private ImageButton button_event_add;

    private CheckBox checkbox_event_sort_name;
    private CheckBox checkbox_event_sort_date;
    private CheckBox checkbox_event_sort_like;

    private EditText event_fragment_from_date;
    private EditText event_fragment_to_date;
    private EditText event_search_bar;

    private Calendar eventCalendar;
    private Date dateFrom;

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
        eventsToFilter = new ArrayList<>();
        eventsFiltered = new ArrayList<>();
        event_adapter = new EventArrayAdapter(getContext(), eventsFiltered, mListener, user);
        fillEventLists();
        eventCalendar = Calendar.getInstance();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);

        listview_event = view.findViewById(R.id.event_fragment_listview);
        listview_event.setAdapter(event_adapter);

        button_event_fav = view.findViewById(R.id.event_fragment_fav_button);
        button_event_all = view.findViewById(R.id.event_fragment_all_button);
        button_event_add = view.findViewById(R.id.event_add_button);

        if (user.hasRole(UserRole.ADMIN)) {
            button_event_add.setVisibility(View.VISIBLE);
            button_event_add.setOnClickListener(v -> mListener.onFragmentInteraction(CREATE_EVENT, null));
        }

        button_event_fav.setOnClickListener(v -> {
            if (user.isConnected())
                updateListView(button_event_fav, button_event_all, followedEvents, listview_event);
            else
                Snackbar.make(getView(), "Login to access your favorite event", 5000).show();
        });

        button_event_all.setOnClickListener(v -> updateListView(button_event_all, button_event_fav, allEvents, listview_event));

        checkbox_event_sort_name = view.findViewById(R.id.event_fragment_checkBox_sort_name);
        checkbox_event_sort_date = view.findViewById(R.id.event_fragment_checkBox_sort_date);
        checkbox_event_sort_like = view.findViewById(R.id.event_fragment_checkbox_sort_like);
        checkbox_event_sort_name.setChecked(true);
        checkbox_event_sort_name.setEnabled(false);

        event_fragment_from_date = view.findViewById(R.id.event_fragment_from_date);
        event_fragment_to_date = view.findViewById(R.id.event_fragment_to_date);

        event_search_bar = view.findViewById(R.id.event_fragment_search_bar);

        filter_button = view.findViewById(R.id.event_fragment_filter_button);
        event_filter_constraint_layout = view.findViewById(R.id.even_filter_constraintLayout);

        setToggleFilterBehaviour();
        setSortingBehaviourOnCheckbox(checkbox_event_sort_date, Event.dateComparator());
        setSortingBehaviourOnCheckbox(checkbox_event_sort_like, Event.likeComparator());
        setSortingBehaviourOnCheckbox(checkbox_event_sort_name, Event.nameComparator());

        setFilterWithSearchBar();

        event_fragment_from_date.setOnClickListener(v -> selectDate(event_fragment_from_date));
        event_fragment_to_date.setOnClickListener(v -> selectDate(event_fragment_to_date));

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
            Collections.sort(allEvents, Event.nameComparator());
            Collections.sort(followedEvents, Event.nameComparator());
            eventsToFilter = allEvents;
            eventsFiltered.addAll(eventsToFilter);
            event_adapter.notifyDataSetChanged();
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
                    sortWithComparator(comparator);
                }
            }
        });
    }

    /**
     * Sort the displayed list of event using a comparator
     * @param comparator comparator to use for sorting
     */
    private void sortWithComparator(Comparator<Event> comparator){
        eventsFiltered.clear();
        eventsFiltered.addAll(eventsToFilter);
        Collections.sort(eventsFiltered, comparator);
        event_adapter.notifyDataSetChanged();
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
     * @param new_selected the new selected button
     * @param new_unselected the new unselected button
     * @param data
     * @param list
     */
    private void updateListView(Button new_selected, Button new_unselected, ArrayList<Event> data, ListView list) {
        new_selected.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
        new_unselected.setBackgroundColor(getResources().getColor(R.color.colorGrayDarkTransparent));
        event_adapter = new EventArrayAdapter(getContext(), data, mListener, user);
        list.setAdapter(event_adapter);
        event_adapter.notifyDataSetChanged();
    }

    /**
     * Set the toggler of filter options
     * When clicked, display all filtering options. Clicking again hide the interface.
     */
    public void setToggleFilterBehaviour(){
        filter_button.setOnClickListener(v -> {
            if (event_filter_constraint_layout.getVisibility() == View.VISIBLE)
                event_filter_constraint_layout.setVisibility(View.GONE);
            else
                event_filter_constraint_layout.setVisibility(View.VISIBLE);
        });
    }

    private void selectDate(EditText date) {
        checkbox_event_sort_name.setChecked(false);
        checkbox_event_sort_name.setEnabled(true);
        checkbox_event_sort_like.setChecked(false);
        checkbox_event_sort_like.setEnabled(true);
        checkbox_event_sort_date.setChecked(false);
        checkbox_event_sort_date.setEnabled(true);
        event_search_bar.getText().clear();
        event_search_bar.clearFocus();

        if (date == event_fragment_from_date) {
            event_fragment_to_date.getText().clear();
            event_fragment_to_date.clearFocus();
        } else if (event_fragment_from_date.getText().length() == 0) {
                event_fragment_to_date.getText().clear();
                event_fragment_from_date.clearFocus();
                Snackbar.make(getView(), "Please first select from date", 5000).show();
                checkbox_event_sort_date.setChecked(true);
                checkbox_event_sort_date.setEnabled(false);
                setSortByLike();
                return;
        }

        DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                eventCalendar.set(Calendar.YEAR, year);
                eventCalendar.set(Calendar.MONTH, monthOfYear);
                eventCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelDate(date);
            }
        };

        new DatePickerDialog(getContext(), datePicker, eventCalendar
                .get(Calendar.YEAR), eventCalendar.get(Calendar.MONTH),
                eventCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateLabelDate(EditText date) {
        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        String stringDate = sdf.format(eventCalendar.getTime());

        date.setText(stringDate);
<<<<<<< HEAD
        if (date == event_fragment_from_date)
            sortByFromDate();
        else
            sortByFromAndToDate();
    }

    private void sortByFromDate(){
        eventsFiltered.clear();
        for (Event event: eventsToFilter)
            if (event.getStartDate().compareTo(eventCalendar.getTime()) >= 0) {
                eventsToFilter.add(event);
        }
=======

        if (date == event_fragment_from_date) sortByFromDate();
        else sortByFromAndToDate();
    }

    private void sortByFromDate() {
        emptySortedEventList();

        sortEventLists(Event.dateComparator());

        selectEventFrom(event_all, event_all_sorted);
        selectEventFrom(event_fav, event_fav_sorted);

        if (isFavDisplayed) event_adapter = new EventArrayAdapter(getContext(), event_fav_sorted, mListener, user);
        else event_adapter = new EventArrayAdapter(getContext(), event_all_sorted, mListener, user);

>>>>>>> origin/master
        listview_event.setAdapter(event_adapter);
        event_adapter.notifyDataSetChanged();
        dateFrom = eventCalendar.getTime();
    }

<<<<<<< HEAD
    private void sortByFromAndToDate(){
        eventsFiltered.clear();
        for (Event event: eventsToFilter) {
            if (event.getStartDate().compareTo(dateFrom) >= 0 && event.getStartDate().compareTo(eventCalendar.getTime()) <= 0) {
                eventsToFilter.add(event);
            }
        }
        listview_event.setAdapter(event_adapter);
        event_adapter.notifyDataSetChanged();
    }

<<<<<<< HEAD
    private void setFilterWithSearchBar() {
=======
    public void selectEventFromTo(ArrayList<Event> inputList, ArrayList<Event> sortedList){
        for (Event event: inputList) {
            if (event.getStartDate().compareTo(dateFrom) >= 0 && event.getStartDate().compareTo(eventCalendar.getTime()) <= 0) {
                sortedList.add(event);
            }
        }
    }

    private void sortWithSearchBar() {
        event_search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkbox_event_sort_name.setChecked(false);
                checkbox_event_sort_name.setEnabled(true);
                checkbox_event_sort_like.setChecked(false);
                checkbox_event_sort_like.setEnabled(true);
                checkbox_event_sort_date.setChecked(false);
                checkbox_event_sort_date.setEnabled(true);
                event_fragment_from_date.getText().clear();
                event_fragment_from_date.clearFocus();
                event_fragment_to_date.getText().clear();
                event_fragment_to_date.clearFocus();

                String keyWord = s.toString().toLowerCase();
                eventsFiltered.clear();
                for (Event event : eventsToFilter) {
                    if (event.getName().toLowerCase().contains(keyWord))
                        eventsFiltered.add(event);
                    else if (event.getShortDescription().toLowerCase().contains(keyWord))
                        eventsFiltered.add(event);
                    else if(event.getLongDescription().toLowerCase().contains((keyWord)))
                        eventsFiltered.add(event);
                }
                event_adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}