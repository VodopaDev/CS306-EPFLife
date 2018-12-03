package ch.epfl.sweng.zuluzulu.Fragments;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.Adapters.EventArrayAdapter;
import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.IdlingResource.IdlingResourceFactory;
import ch.epfl.sweng.zuluzulu.OnFragmentInteractionListener;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Event;
import ch.epfl.sweng.zuluzulu.User.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.User.User;
import ch.epfl.sweng.zuluzulu.User.UserRole;

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

    private ArrayList<Event> event_all;
    private ArrayList<Event> event_fav;
    private EventArrayAdapter event_adapter;

    private ListView listview_event;
    private Button button_event_all;
    private Button button_event_fav;
    private ImageButton button_event_add;

    private CheckBox checkbox_event_sort_name;
    private CheckBox checkbox_event_sort_date;
    private CheckBox checkbox_event_sort_like;
    private String default_sort_option;

    private EditText event_fragment_from_date;
    private EditText event_fragment_to_date;

    private ArrayList<Event> event_all_sorted;

//    private ArrayList<Event> event_fav_sorted;
    private List<Event> event_archive;

    private ArrayList<Event> event_fav_sorted;

    private boolean isFavDisplayed;

    private EditText event_search_bar;

    private Calendar eventCalendar;
    private Date dateFrom;

    private DatePickerDialog.OnDateSetListener datePicker;

    private ImageButton filter_button;
    private ConstraintLayout event_filter_constraint_layout;

    private Button archive;

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

        event_all = new ArrayList<>();
        event_fav = new ArrayList<>();
        event_archive = new ArrayList<>();
        event_adapter = new EventArrayAdapter(getContext(), event_all, mListener, user);

        default_sort_option = "name";

        fillEventLists(default_sort_option);
        //fillEventArchiveLists(default_sort_option);
        //Log.e("sizeEventAll", event_all.size()+"");
        //keepOnlyDateBeforeNow();

        //Log.e("sizeArchive", event_archive.size()+"");

        //updateEventDataBase("events_archive");
        //updateEventDataBase("events_info");

        isFavDisplayed = false;

        event_all_sorted = new ArrayList<>();
        event_fav_sorted = new ArrayList<>();

        eventCalendar = Calendar.getInstance();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("sizeEventAll", event_all.size()+"");
        View view = inflater.inflate(R.layout.fragment_event, container, false);

        listview_event = view.findViewById(R.id.event_fragment_listview);
        listview_event.setAdapter(event_adapter);

        button_event_fav = view.findViewById(R.id.event_fragment_fav_button);
        button_event_all = view.findViewById(R.id.event_fragment_all_button);
        button_event_add = view.findViewById(R.id.event_add_button);

        if (user.hasRole(UserRole.ADMIN)) {
            button_event_add.setVisibility(View.VISIBLE);
            button_event_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onFragmentInteraction(CommunicationTag.CREATE_EVENT, null);
                }
            });
        }

        button_event_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.isConnected())
                    updateListView(button_event_fav, button_event_all, event_fav, listview_event);
                else
                    Snackbar.make(getView(), "Login to access your favorite event", 5000).show();
            }
        });

        button_event_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateListView(button_event_all, button_event_fav, event_all, listview_event);
            }
        });

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

        openCloseFilterOption();

        sortByName();

        sortByLike();

        sortByDate();

        sortWithSearchBar();

        event_fragment_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate(event_fragment_from_date);
            }
        });

        event_fragment_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate(event_fragment_to_date);
            }
        });

        archive = view.findViewById(R.id.event_fragment_archive);
        printOldEvent();

        return view;
    }

    private void emptySortedEventList() {
        event_all_sorted.clear();
        event_fav_sorted.clear();
    }

    private void fillEventLists(String sortOption) {
        FirebaseFirestore.getInstance().collection("events_info").orderBy(sortOption).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> snap_list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot snap : snap_list) {
                            FirebaseMapDecorator fmap = new FirebaseMapDecorator(snap);
                            if (fmap.hasFields(Event.FIELDS) && fmap.getDate("start_date").compareTo(new Date(System.currentTimeMillis())) >= 0) {
                                Event event = new Event.EventBuilder().build(fmap);
                                event_all.add(event);
                                if (user.isConnected() && ((AuthenticatedUser) user).isFavEvent(event))
                                    event_fav.add(event);
                                event_adapter.notifyDataSetChanged();
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(getView(), "Loading error, check your connection", 5000).show();
                        Log.e("EVENT_LIST", "Error fetching event date\n" + e.getMessage());
                    }
                });
    }

    private void updateListView(Button new_selected, Button new_unselected, ArrayList<Event> data, ListView list) {
        new_selected.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
        new_unselected.setBackgroundColor(getResources().getColor(R.color.colorGrayDarkTransparent));
        if (new_selected == button_event_fav) isFavDisplayed = true;
        else isFavDisplayed = false;
        event_adapter = new EventArrayAdapter(getContext(), data, mListener, user);
        list.setAdapter(event_adapter);
        event_adapter.notifyDataSetChanged();
    }

    public void openCloseFilterOption() {
        filter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (event_filter_constraint_layout.getVisibility() == View.VISIBLE) {
                    event_filter_constraint_layout.setVisibility(View.GONE);
                } else {
                    event_filter_constraint_layout.setVisibility(View.VISIBLE);
                }
            }
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
        } else {
            if (event_fragment_from_date.getText().length() == 0) {
                event_fragment_to_date.getText().clear();
                event_fragment_from_date.clearFocus();
                Snackbar.make(getView(), "Please first select from date", 5000).show();
                checkbox_event_sort_date.setChecked(true);
                checkbox_event_sort_date.setEnabled(false);
                sortByLike();
                return;
            }
        }

        datePicker = new DatePickerDialog.OnDateSetListener() {

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

        listview_event.setAdapter(event_adapter);
        event_adapter.notifyDataSetChanged();

        dateFrom = eventCalendar.getTime();
    }

    public void selectEventFrom(ArrayList<Event> inputList, ArrayList<Event> sortedList){
        for (Event event: inputList) {
            if (event.getStartDate().compareTo(eventCalendar.getTime()) >= 0) {
                sortedList.add(event);
            }
        }
    }

    private void sortByFromAndToDate() {
        emptySortedEventList();

        sortEventLists(Event.dateComparator());

        selectEventFromTo(event_all, event_all_sorted);
        selectEventFromTo(event_fav, event_fav_sorted);

        if (isFavDisplayed) event_adapter = new EventArrayAdapter(getContext(), event_fav_sorted, mListener, user);
        else event_adapter = new EventArrayAdapter(getContext(), event_all_sorted, mListener, user);

        listview_event.setAdapter(event_adapter);
        event_adapter.notifyDataSetChanged();
    }

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
                emptySortedEventList();

                selectEventWithKeyword(event_all, event_all_sorted, keyWord);
                selectEventWithKeyword(event_fav, event_fav_sorted, keyWord);

                if (isFavDisplayed) event_adapter = new EventArrayAdapter(getContext(), event_fav_sorted, mListener, user);
                else event_adapter = new EventArrayAdapter(getContext(), event_all_sorted, mListener, user);

                listview_event.setAdapter(event_adapter);
                event_adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void selectEventWithKeyword(ArrayList<Event> inputList, ArrayList<Event> sortedList, String keyWord){
        for (Event event: inputList) {
            if (event.getName().toLowerCase().contains(keyWord)) {
                sortedList.add(event);
            } else if (event.getShortDesc().toLowerCase().contains(keyWord)) {
                sortedList.add(event);
            } else if (event.getLongDesc().toLowerCase().contains((keyWord))) {
                sortedList.add(event);
            }
        }
    }

    private void sortByName() {
        sortBy("name");
    }

    private void sortByLike() {
        sortBy("like");
    }

    private void sortByDate() {
        sortBy("date");
    }

    private void sortBy(String type) {
        CheckBox clickedCheckBox = getCheckBoxFor(type);
        Comparator<Event> comparator = getComparatorFor(type);
        List<CheckBox> checkBoxes = new ArrayList<>(Arrays.asList(checkbox_event_sort_like, checkbox_event_sort_name, checkbox_event_sort_date));
        clickedCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (CheckBox box : checkBoxes) {
                    boolean isClickedBox = box.equals(clickedCheckBox);
                    box.setChecked(isClickedBox);
                    box.setEnabled(!isClickedBox);
                }

                clearTextAndFocus();

                sortEventLists(comparator);

                if (isFavDisplayed) event_adapter = new EventArrayAdapter(getContext(), event_fav, mListener, user);
                else event_adapter = new EventArrayAdapter(getContext(), event_all, mListener, user);

                listview_event.setAdapter(event_adapter);
                event_adapter.notifyDataSetChanged();

                clickedCheckBox.setEnabled(false);
                clickedCheckBox.setChecked(true);
            }
        });
    }

    private void clearTextAndFocus() {
        event_fragment_from_date.getText().clear();
        event_fragment_from_date.clearFocus();
        event_fragment_to_date.getText().clear();
        event_fragment_to_date.clearFocus();
        event_search_bar.getText().clear();
        event_search_bar.clearFocus();
    }

    private CheckBox getCheckBoxFor(String type) {
        CheckBox checkBox;
        switch (type) {
            case "like":
                checkBox = checkbox_event_sort_like;
                break;
            case "name":
                checkBox = checkbox_event_sort_name;
                break;
            case "date":
                checkBox = checkbox_event_sort_date;
                break;
            default:
                throw new IllegalArgumentException("The type " + type + " is invalid");
        }
        return checkBox;
    }

    private Comparator<Event> getComparatorFor(String type) {
        Comparator<Event> comparator;
        switch (type) {
            case "like":
                comparator = Event.likeComparator();
                break;
            case "name":
                comparator = Event.assoNameComparator();
                break;
            case "date":
                comparator = Event.dateComparator();
                break;
            default:
                throw new IllegalArgumentException("The type " + type + " is invalid");
        }
        return comparator;
    }

    public void sortEventLists(Comparator<Event> comparator){
        Collections.sort(event_all, comparator);
        Collections.sort(event_fav, comparator);
    }

    private void fillEventArchiveLists(String sortOption) {
        FirebaseFirestore.getInstance().collection("events_archive").orderBy(sortOption).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> snap_list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot snap : snap_list) {
                            FirebaseMapDecorator fmap = new FirebaseMapDecorator(snap);
                            if (fmap.hasFields(Event.FIELDS)) {
                                Event event = new Event.EventBuilder().build(fmap);
                                event_archive.add(event);

                                event_adapter.notifyDataSetChanged();
                            }
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(getView(), "Loading error, check your connection", 5000).show();
                        Log.e("EVENT_LIST", "Error fetching event date\n" + e.getMessage());
                    }
                });
    }

    private void updateEventDataBase(String collectionPath){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(collectionPath).orderBy("name").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots){
                        for(Event e: event_archive) {
                            Map<String, Object> docData = new HashMap<>();
                            docData.put("icon_uri", e.getBannerUri());
                            docData.put("id", e.getId());
                            docData.put("likes", e.getLikes());
                            docData.put("long_desc", e.getLongDesc());
                            docData.put("name", e.getName());
                            docData.put("organizer", e.getOrganizer());
                            docData.put("place", e.getPlace());
                            docData.put("short_desc", e.getShortDesc());
                            docData.put("start_date", e.getStartDate());
                            DatabaseFactory.getDependency().collection(collectionPath).document("event" + e.getId()).set(docData);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //error_message(e);
                    }
                });
    }

    private void keepOnlyDateBeforeNow() {
        for(Event e: event_all) {
            if(e.getStartDate().compareTo(new Date()) <= 0) {
                event_archive.add(e);
                event_all.remove(e);
                event_adapter.notifyDataSetChanged();
            }
        }
    }

    private void printOldEvent() {
        archive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event_adapter = new EventArrayAdapter(getContext(), event_archive, mListener, user);
                listview_event.setAdapter(event_adapter);
            }
        });

    }
}