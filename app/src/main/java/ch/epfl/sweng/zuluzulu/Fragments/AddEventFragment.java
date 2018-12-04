package ch.epfl.sweng.zuluzulu.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Association;
import ch.epfl.sweng.zuluzulu.Structure.Event;
import ch.epfl.sweng.zuluzulu.Structure.EventBuilder;
import ch.epfl.sweng.zuluzulu.Structure.EventDate;

public class AddEventFragment extends SuperFragment {

    private final static int DAYSTOSEC = 86400;
    private static final String EPFL_LOGO = "https://mediacom.epfl.ch/files/content/sites/mediacom/files/EPFL-Logo.jpg";
    //for association name
    private Map<String, String> association_map = new HashMap<>();
    private Spinner spinner;

    //for date (number of days adapt depending on the month chosen)
    private DatePicker date_pick;

    //for time
    private List<String> hours = new ArrayList<>();
    private List<String> minutes = new ArrayList<>();
    private Spinner spinner_hours;
    private Spinner spinner_minutes;

    private Spinner spinner_end_hours;
    private Spinner spinner_end_minutes;

    //for description_view
    private TextView title_view;
    private TextView description_view;
    private TextView place;
    private TextView organizer;
    private TextView website;
    private TextView duration;
    private TextView speaker;
    private TextView category;
    private TextView contact;

    //for validating and create the event
    private Button create_event;



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
    private int getIntSpinnerContent(Spinner spinner) {
        return Integer.parseInt(spinner.getSelectedItem().toString());
    }

    /**
     * Set the onClick of the button with sending the event to the database
     */
    private void setUpCreateEventButton() {
        create_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Set the starting date
                int hour = getIntSpinnerContent(spinner_hours)-1;
                int minute = getIntSpinnerContent(spinner_minutes);
                int day = date_pick.getDayOfMonth();
                int month = date_pick.getMonth();
                int year = date_pick.getYear() - 1900;
                Date date = new Date(year, month, day, hour, minute);

                //Set the end date (to avoid adding another calendar to the layout I compute the duration in days and add it to the date
                // to find the end date)
                int dur = Integer.parseInt(duration.getText().toString());
                dur = dur * DAYSTOSEC * 1000;
                long d = date.getTime();
                d = d + dur;
                Date end_date = new Date();
                end_date.setTime(d);
                end_date.setHours(getIntSpinnerContent(spinner_end_hours)-1);
                end_date.setMinutes(getIntSpinnerContent(spinner_end_minutes));

                //Set the other fields
                String name = spinner.getSelectedItem().toString();
                String tit = title_view.getText().toString();
                String desc = description_view.getText().toString();
                String pla = place.getText().toString();
                String org = organizer.getText().toString();
                String web = website.getText().toString();
                String speak = speaker.getText().toString();
                String cat = category.getText().toString();
                String cont = contact.getText().toString();

                if (!checkIfValid(tit, desc)) {
                    return;
                }

                Event event = new EventBuilder().
                        setId(DatabaseFactory.getDependency().getNewEventId()).
                        setName(name).
                        setShortDesc(tit).
                        setLongDesc(desc).
                        setChannelId(DatabaseFactory.getDependency().getNewChannelId()).
                        setAssosId(association_map.get(name)).
                        setDate(new EventDate(date, end_date)).
                        setFollowers(new ArrayList<>()).
                        setOrganizer(org).
                        setPlace(pla).
                        setIconUri(EPFL_LOGO).
                        setBannerUri(EPFL_LOGO).
                        setUrlPlaceAndRoom("https://plan.epfl.ch/?room=" + pla.replaceAll("\\s+","")).
                        setWebsite(web).
                        setContact(cont).
                        setCategory(cat).
                        setSpeaker(speak).
                        build();

                DatabaseFactory.getDependency().addEvent(event);

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
        website = view.findViewById(R.id.website);
        duration = view.findViewById(R.id.duration);
        speaker = view.findViewById(R.id.speaker);
        category = view.findViewById(R.id.category);
        contact = view.findViewById(R.id.contact);

        //the button "create event" that when clicked gather the data from all spinners and
        //textviews and push an event on the database
        create_event = view.findViewById(R.id.create_event_button);
        setUpCreateEventButton();

        spinner_hours = view.findViewById(R.id.spinner_hour);
        setSpinner(spinner_hours, hours);

        spinner_minutes = view.findViewById(R.id.spinner_minute);
        setSpinner(spinner_minutes, minutes);

        spinner_end_hours = view.findViewById(R.id.end_spinner_hour);
        setSpinner(spinner_end_hours, hours);

        spinner_end_minutes = view.findViewById(R.id.end_spinner_minute);
        setSpinner(spinner_end_minutes, minutes);

        //fill the spinner for associations.
        spinner = view.findViewById(R.id.spinner);
        fillAssociationNames();

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
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean checkIfValid(String title, String description) {
        if (title.length() > 30)
            return viewSetError(title_view, "title is too long");
        if (title.isEmpty())
            return viewSetError(title_view, "please write a title");
        if (description.length() > 80)
            return viewSetError(description_view, "description is too long");
        if (description.isEmpty())
            return viewSetError(description_view, "please write a description");

        return true;
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
        DatabaseFactory.getDependency().getAllAssociations(result -> {
            for (Association association : result) {
                association_map.put(association.getName(), association.getId());
                Log.d("EVENT_CREATOR", "added association " + association.getName());
            }
            setSpinner(spinner, new ArrayList<>(association_map.keySet()));
        });
    }
}
