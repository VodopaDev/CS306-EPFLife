package ch.epfl.sweng.zuluzulu.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import ch.epfl.sweng.zuluzulu.Adapters.EventArrayAdapter;
import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.OnFragmentInteractionListener;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.DateInputMask;
import ch.epfl.sweng.zuluzulu.Structure.Event;
import ch.epfl.sweng.zuluzulu.Structure.Utils;
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
    private Button button_event_add;

    private CheckBox checkbox_event_sort_name;
    private CheckBox checkbox_event_sort_date;
    private CheckBox checkbox_event_sort_like;
    private String default_sort_option;

    private EditText event_fragment_from_date;
    private EditText event_fragment_to_date;
    private DateInputMask event_fragment_from_date_mask;
    private DateInputMask event_fragment_to_date_mask;

    private ArrayList<Event> event_all_sorted;
    private ArrayList<Event> event_fav_sorted;

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
        event_adapter = new EventArrayAdapter(getContext(), event_all, mListener, user);

        default_sort_option = "name";

        fillEventLists(default_sort_option);

        event_all_sorted = new ArrayList<>();
        event_fav_sorted = new ArrayList<>();
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
        event_fragment_from_date_mask = new DateInputMask(event_fragment_from_date);
        event_fragment_to_date = view.findViewById(R.id.event_fragment_to_date);
        event_fragment_to_date_mask = new DateInputMask(event_fragment_to_date);


//        checkbox_event_sort_date.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                checkbox_event_sort_date.setEnabled(false);
//                emptyEventList();
//                fillEventLists("start_date");
//                checkbox_event_sort_name.setChecked(false);
//                checkbox_event_sort_name.setEnabled(true);
//            }
//        });


//        checkbox_event_sort_name.setEnabled(false);
//        event_all.sort(Event.nameComparator());
//        event_fav.sort(Event.nameComparator());
//        event_adapter.notifyDataSetChanged();
//        checkbox_event_sort_date.setChecked(false);
//        checkbox_event_sort_date.setEnabled(true);

        checkbox_event_sort_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkbox_event_sort_name.setEnabled(false);

                Collections.sort(event_all, Event.nameComparator());
                Collections.sort(event_fav, Event.nameComparator());

                event_adapter = new EventArrayAdapter(getContext(), event_all, mListener, user);

                listview_event.setAdapter(event_adapter);
                event_adapter.notifyDataSetChanged();

                checkbox_event_sort_date.setChecked(false);
                checkbox_event_sort_date.setEnabled(true);
                checkbox_event_sort_like.setChecked(false);
                checkbox_event_sort_like.setEnabled(true);
            }
        });

        checkbox_event_sort_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkbox_event_sort_like.setEnabled(false);

                Collections.sort(event_all, Event.likeComparator());
                Collections.sort(event_fav, Event.likeComparator());

                event_adapter = new EventArrayAdapter(getContext(), event_all, mListener, user);

                listview_event.setAdapter(event_adapter);
                event_adapter.notifyDataSetChanged();

                checkbox_event_sort_name.setChecked(false);
                checkbox_event_sort_name.setEnabled(true);
                checkbox_event_sort_date.setChecked(false);
                checkbox_event_sort_date.setEnabled(true);
            }
        });

        checkbox_event_sort_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (event_fragment_from_date.getText().toString().contains("D") || event_fragment_from_date.getText().toString().contains("M") ||
                        event_fragment_from_date.getText().toString().contains("Y")) {

                    checkbox_event_sort_date.setEnabled(false);
                    Collections.sort(event_all, Event.dateComparator());
                    Collections.sort(event_fav, Event.dateComparator());

                    event_adapter = new EventArrayAdapter(getContext(), event_all, mListener, user);

                    listview_event.setAdapter(event_adapter);
                    event_adapter.notifyDataSetChanged();
                    checkbox_event_sort_name.setChecked(false);
                    checkbox_event_sort_name.setEnabled(true);
                    checkbox_event_sort_like.setChecked(false);
                    checkbox_event_sort_like.setEnabled(true);
                } else if (event_fragment_to_date.getText().toString().contains("D") || event_fragment_to_date.getText().toString().contains("M") ||
                        event_fragment_to_date.getText().toString().contains("Y")) {
                    checkbox_event_sort_date.setEnabled(false);
                    Collections.sort(event_all, Event.dateComparator());
                    Collections.sort(event_fav, Event.dateComparator());

                    emptySortedEventList();

                    Date tempDate = null;
                    try {
                        tempDate = Utils.stringToDateFormat.parse(event_fragment_from_date.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < event_all.size(); i++) {
                        if (event_all.get(i).getStartDate().compareTo(tempDate) >= 0) {
                            event_all_sorted.add(event_all.get(i));
                        }
                    }
                    for (int i = 0; i < event_fav.size(); i++) {
                        if (event_fav.get(i).getStartDate().compareTo(tempDate) >= 0) {
                            event_fav_sorted.add(event_fav.get(i));
                        }
                    }

                    event_adapter = new EventArrayAdapter(getContext(), event_all_sorted, mListener, user);
                    listview_event.setAdapter(event_adapter);
                    event_adapter.notifyDataSetChanged();

                    checkbox_event_sort_name.setChecked(false);
                    checkbox_event_sort_name.setEnabled(true);
                    checkbox_event_sort_like.setChecked(false);
                    checkbox_event_sort_like.setEnabled(true);
                } else {
                    checkbox_event_sort_date.setEnabled(false);
                    Collections.sort(event_all, Event.dateComparator());
                    Collections.sort(event_fav, Event.dateComparator());

                    emptySortedEventList();

                    Date tempDateFrom = null;
                    Date tempDateTo = null;
                    try {
                        tempDateFrom = Utils.stringToDateFormat.parse(event_fragment_from_date.getText().toString());
                        tempDateTo = Utils.stringToDateFormat.parse(event_fragment_to_date.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < event_all.size(); i++) {
                        if (event_all.get(i).getStartDate().compareTo(tempDateFrom) >= 0 && event_all.get(i).getStartDate().compareTo(tempDateTo) <= 0) {
                            event_all_sorted.add(event_all.get(i));
                        }
                    }
                    for (int i = 0; i < event_fav.size(); i++) {
                        if (event_fav.get(i).getStartDate().compareTo(tempDateFrom) >= 0 && event_all.get(i).getStartDate().compareTo(tempDateTo) <= 0) {
                            event_fav_sorted.add(event_fav.get(i));
                        }
                    }

                    event_adapter = new EventArrayAdapter(getContext(), event_all_sorted, mListener, user);
                    listview_event.setAdapter(event_adapter);
                    event_adapter.notifyDataSetChanged();

                    checkbox_event_sort_name.setChecked(false);
                    checkbox_event_sort_name.setEnabled(true);
                    checkbox_event_sort_like.setChecked(false);
                    checkbox_event_sort_like.setEnabled(true);
                }
            }
        });

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
                            if (fmap.hasFields(Event.requiredFields())) {
                                Event event = new Event(fmap);
                                event_all.add(event);
                                if (user.isConnected() && ((AuthenticatedUser) user).isFollowedEvent(event))
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
        event_adapter = new EventArrayAdapter(getContext(), data, mListener, user);
        list.setAdapter(event_adapter);
        event_adapter.notifyDataSetChanged();
    }
}
