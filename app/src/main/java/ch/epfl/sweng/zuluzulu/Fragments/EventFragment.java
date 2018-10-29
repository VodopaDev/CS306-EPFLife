package ch.epfl.sweng.zuluzulu.Fragments;

import android.content.Context;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.zuluzulu.Structure.DateInputMask;
import ch.epfl.sweng.zuluzulu.Structure.Event;
import ch.epfl.sweng.zuluzulu.OnFragmentInteractionListener;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.Structure.User;
import ch.epfl.sweng.zuluzulu.Adapters.EventAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventFragment extends Fragment {
    private static final String TAG = "EVENT_TAG";
    private static final String ARG_USER = "ARG_USER";

    private User user;
    private OnFragmentInteractionListener mListener;

    private ArrayList<Event> event_all;
    private ArrayList<Event> event_fav;
    private EventAdapter event_adapter;

    private ListView listview_event;
    private Button button_event_all;
    private Button button_event_fav;

    private CheckBox checkbox_event_sort_name;
    private CheckBox checkbox_event_sort_date;
    private String default_sort_option;

    private EditText event_fragment_from_date;
    private EditText event_fragment_to_date;
    private DateInputMask event_fragment_from_date_mask;
    private DateInputMask getEvent_fragment_to_date_mask;

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
        }

        event_all = new ArrayList<>();
        event_fav = new ArrayList<>();
        event_adapter = new EventAdapter(getContext(), event_all, mListener);

        default_sort_option = "name";

        fillEventLists(default_sort_option);

        event_all_sorted = new ArrayList<>();
        event_fav_sorted = new ArrayList<>();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);

        listview_event = view.findViewById(R.id.event_fragment_listview);
        listview_event.setAdapter(event_adapter);

        button_event_fav = view.findViewById(R.id.event_fragment_fav_button);
        button_event_all = view.findViewById(R.id.event_fragment_all_button);

        button_event_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.isConnected())
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

        checkbox_event_sort_name.setChecked(true);
        checkbox_event_sort_name.setEnabled(false);

        event_fragment_from_date = view.findViewById(R.id.event_fragment_from_date);
        event_fragment_from_date_mask = new DateInputMask(event_fragment_from_date);
        event_fragment_to_date = view.findViewById(R.id.event_fragment_to_date);
        getEvent_fragment_to_date_mask = new DateInputMask(event_fragment_to_date);

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

        checkbox_event_sort_name.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                checkbox_event_sort_name.setEnabled(false);
                event_all.sort(Event.assoNameComparator());
                event_fav.sort(Event.assoNameComparator());
                event_adapter.notifyDataSetChanged();
                checkbox_event_sort_date.setChecked(false);
                checkbox_event_sort_date.setEnabled(true);
            }
        });

        checkbox_event_sort_date.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if (event_fragment_from_date.length() == 0 && event_fragment_to_date.length() == 0){
                    checkbox_event_sort_date.setEnabled(false);
                    event_all.sort(Event.dateComparator());
                    event_fav.sort(Event.dateComparator());
                    event_adapter.notifyDataSetChanged();
                    checkbox_event_sort_name.setChecked(false);
                    checkbox_event_sort_name.setEnabled(true);
                }
//                else if ( event_fragment_from_date.length() > 0 && event_fragment_to_date.length() == 0){
//                    event_all.sort(Event.dateComparator());
//                    event_fav.sort(Event.dateComparator());
//
//                    Boolean test = true;
//                    int counter = 0;
//                    while(test && counter != event_all.size()){
//                        if event_all.get(counter).getStart_date() // TODO check if start_date is smaller than the user input
//                    }
//
//
//                    System.arraycopy(event_all, 0, event_all_sorted, 0, event_all.size());
//                    System.arraycopy(event_fav, 0, event_fav_sorted, 0, event_fav.size());
//                    event_all_sorted.sort(Event.dateComparator());
//                    event_all_sorted.
//                }
            }
        });


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

//    private void emptyEventList(){
//        event_all.clear();
//        event_fav.clear();
//    }

    private void fillEventLists(String sortOption){
       FirebaseFirestore.getInstance().collection("events_info")
                .orderBy(sortOption)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> snap_list = queryDocumentSnapshots.getDocuments();
                        for (int i = 0; i < snap_list.size(); i++) {
                            Event event = new Event(snap_list.get(i));
                            Log.d("! TEST DATE EVENT !", event.getStart_date().toString());
                            event_all.add(event);

                            if (user.isConnected() && ((AuthenticatedUser) user).isFollowedEvent(event))
                                event_fav.add(event);
                        }
                        event_adapter.notifyDataSetChanged();
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

    private void updateListView(Button new_selected, Button new_unselected, ArrayList<Event> data, ListView list){
        new_selected.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
        new_unselected.setBackgroundColor(getResources().getColor(R.color.colorGrayDarkTransparent));
        event_adapter = new EventAdapter(getContext(), data, mListener);
        list.setAdapter(event_adapter);
        event_adapter.notifyDataSetChanged();
    }
}
