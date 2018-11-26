package ch.epfl.sweng.zuluzulu.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.IdlingResource.IdlingResourceFactory;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Association;

public class AddEventFragment extends SuperFragment {
    private static final int[] INDICES = {0, 2, 4, 6, 7, 9, 11};
    //for association name
    private List<String> association_names = new ArrayList<>();
    private List<Integer> association_ids = new ArrayList<>();
    private Spinner spinner;
    private int numberOfEvents;

    //for date (number of days adapt depending on the month chosen)
    private DatePicker date_pick;

    //for time
    private List<String> hours = new ArrayList<>();
    private List<String> minutes = new ArrayList<>();
    private Spinner spinner_hours;
    private Spinner spinner_minutes;

    //for description_view
    private TextView title_view;
    private TextView description_view;
    private TextView place;
    private TextView organizer;

    //for validating and create the event
    private Button create_event;

    // for the map
    private MapView mMapView;
    private GoogleMap googleMap;


    public static AddEventFragment newInstance() {
        return new AddEventFragment();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //fills the hour list for the spinner
        addIntsToList(hours, 0, 24, 1);

        //fills the minute list for the spinner
        addIntsToList(minutes, 0, 60, 10);


    }

    /**
     * fill lists requiring double digits numbers
     *
     * @param list              , the list we want to fill
     * @param startingValue     the first value that the list will have
     * @param exclusiveMaxValue the max value (exclusive) that the list will have
     * @param increment         of how much we increment per iteration
     */
    private void addIntsToList(List<String> list, int startingValue, int exclusiveMaxValue, int increment) {
        for (int i = startingValue; i < exclusiveMaxValue; i += increment) {
            if (i < 10) {
                list.add("0" + String.valueOf(i));
            } else {
                list.add(String.valueOf(i));
            }
        }

    }

    /**
     * Takes the current selected Item of a spinner with numbers and convert it to int
     *
     * @return the int value of the selected content of the spinner
     */
    private int getNumberSpinnerContent(Spinner spinner) {
        return Integer.parseInt(spinner.getSelectedItem().toString());
    }

    /**
     * Set the onClick of the button with sending the event to the database
     */
    private void setUpCreateEventButton() {
        create_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int hour = getNumberSpinnerContent(spinner_hours);
                int minute = getNumberSpinnerContent(spinner_minutes);
                int day = date_pick.getDayOfMonth();
                int month = date_pick.getMonth();
                int year = date_pick.getYear() - 1900;
                Date date = new Date(year, month, day, hour, minute);


                String name = spinner.getSelectedItem().toString();
                String tit = title_view.getText().toString();
                String desc = description_view.getText().toString();
                String pla = place.getText().toString();
                String org = organizer.getText().toString();

                if (!checkIfValid(tit, desc)) {
                    return;
                }

                IdlingResourceFactory.incrementCountingIdlingResource();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("events_info").orderBy("name").get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                List<DocumentSnapshot> snap_list = queryDocumentSnapshots.getDocuments();
                                numberOfEvents = snap_list.size();

                                int id_channel = 170 + numberOfEvents + 1;

                                //the map for the corresponding channel
                                Map<String, Object> docDataChannel = new HashMap<>();
                                docDataChannel.put("description", "chat of the event : " + tit + " from " + name);
                                docDataChannel.put("icon_uri", "https://mediacom.epfl.ch/files/content/sites/mediacom/files/EPFL-Logo.jpg");
                                docDataChannel.put("id", id_channel);
                                docDataChannel.put("name", name + "'s event");

                                Map<String, Object> restrictions = new HashMap<>();
                                restrictions.put("location", null);
                                restrictions.put("section", null);

                                docDataChannel.put("restrictions", restrictions);
                                db.collection("channels").document("channel" + Integer.toString(id_channel)).set(docDataChannel);

                                //the map for the event
                                Map<String, Object> docData = new HashMap<>();
                                docData.put("icon_uri", "https://mediacom.epfl.ch/files/content/sites/mediacom/files/EPFL-Logo.jpg");
                                docData.put("id", numberOfEvents + 1);
                                docData.put("assos_id", association_ids.get(association_names.indexOf(name)));
                                docData.put("channel_id", id_channel);
                                docData.put("likes", 0);
                                docData.put("long_desc", desc);
                                docData.put("name", name);
                                docData.put("organizer", org);
                                docData.put("place", pla);
                                docData.put("short_desc", tit);
                                docData.put("start_date", date);
                                DatabaseFactory.getDependency().collection("events_info").document("event" + Integer.toString(numberOfEvents + 1)).set(docData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        mListener.onFragmentInteraction(CommunicationTag.OPEN_EVENT_FRAGMENT, null);
                                    }
                                });

                                IdlingResourceFactory.decrementCountingIdlingResource();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                error_message(e);
                                IdlingResourceFactory.decrementCountingIdlingResource();
                            }
                        });
            }
        });
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_event, container, false);

        title_view = view.findViewById(R.id.event_title);
        description_view = view.findViewById(R.id.long_desc_text);
        organizer = view.findViewById(R.id.organizer);
        place = view.findViewById(R.id.place);

        //the button "create event" that when clicked gather the data from all spinners and
        //textviews and push an event on the database
        create_event = view.findViewById(R.id.create_event_button);
        setUpCreateEventButton();

        spinner_hours = view.findViewById(R.id.spinner_hour);
        setSpinner(spinner_hours, hours);

        spinner_minutes = view.findViewById(R.id.spinner_minute);
        setSpinner(spinner_minutes, minutes);

        //fill the spinner for associations.
        spinner = view.findViewById(R.id.spinner);
        fillAssociationNames();

        mMapView = (MapView) view.findViewById(R.id.mapViewAdd);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                } else {
                    // For showing a move to my location button
                    googleMap.setMyLocationEnabled(true);
                }

                // For dropping a marker at a point on the Map
                LatLng epfl = new LatLng(46.520537, 6.570930);

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(epfl).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

        date_pick = (DatePicker) view.findViewById(R.id.date_for_add);

        return view;


    }

    /**
     * check if the arguments are valid for creating an event
     *
     * @param title       , the title of the event
     * @param description , the long description of the event
     * @return if the arguments are valid
     */
    private boolean checkIfValid(String title, String description) {
        boolean valid = true;

        if (title.length() > 30) {
            valid = viewSetError(title_view, "title is too long");
        }
        if (title.isEmpty()) {
            valid = viewSetError(title_view, "please write a title");
        }
        if (description.length() > 80) {
            valid = viewSetError(description_view, "description is too long");
        }
        if (description.isEmpty()) {
            valid = viewSetError(description_view, "please write a description");
        }

        return valid;
    }

    /**
     * Set an error message in a textview.
     *
     * @param t            , the textview we take care of
     * @param errorMessage , the message we want to show
     * @return
     */
    private boolean viewSetError(TextView t, String errorMessage) {
        t.requestFocus();
        t.setError(errorMessage);
        return false;
    }

    /**
     * Set the content of a spinner
     *
     * @param spinner , the spinner we want to set
     * @param list    , the content of the spinner
     */
    private void setSpinner(Spinner spinner, List<String> list) {
        ArrayAdapter adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    /**
     * fill the list for the association spinner with the associations
     * on the database.
     */
    private void fillAssociationNames() {
        IdlingResourceFactory.incrementCountingIdlingResource();
        FirebaseFirestore.getInstance().collection("assos_info").orderBy("name").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> snap_list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot snap : snap_list) {
                            FirebaseMapDecorator data = new FirebaseMapDecorator(snap);
                            if (data.hasFields(Association.FIELDS)) {
                                association_names.add((String) data.get("name"));
                                association_ids.add(data.getInteger("id"));
                            }
                        }
                        setSpinner(spinner, association_names);
                        IdlingResourceFactory.decrementCountingIdlingResource();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        error_message(e);
                        IdlingResourceFactory.decrementCountingIdlingResource();
                    }
                });
    }

    private void error_message(Exception e) {
        Snackbar.make(getView(), "Loading error, check your connection", 5000).show();
        Log.e("ASSO_LIST", "Error fetching association data\n" + e.getMessage());
    }
}
