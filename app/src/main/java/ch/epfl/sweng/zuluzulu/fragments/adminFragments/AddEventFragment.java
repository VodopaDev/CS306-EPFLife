package ch.epfl.sweng.zuluzulu.fragments.adminFragments;

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
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.fragments.superFragments.SuperFragment;
import ch.epfl.sweng.zuluzulu.structure.Association;
import ch.epfl.sweng.zuluzulu.structure.Event;
import ch.epfl.sweng.zuluzulu.structure.EventBuilder;
import ch.epfl.sweng.zuluzulu.structure.EventDate;

public class AddEventFragment extends SuperFragment {

    private static final String EPFL_LOGO = "https://mediacom.epfl.ch/files/content/sites/mediacom/files/EPFL-Logo.jpg";

    //for association name
    private final Map<String, String> association_map = new HashMap<>();
    private Spinner spinner;

    //for date (number of days adapt depending on the month chosen)
    private DatePicker start_date_pick;
    private DatePicker end_date_pick;
    private final Date today = new Date();

    //for time
    private final List<String> hours = new ArrayList<>();
    private final List<String> minutes = new ArrayList<>();
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
        addIntsToList(hours, 24, 1);

        //fills the minute list for the spinner
        addIntsToList(minutes, 60, 10);
    }

    /**
     * fill lists of integers requiring double digits numbers
     *  @param list              , the list we want to fill
     * @param exclusiveMaxValue the max value (exclusive) that the list will have
     * @param increment         of how much we increment per iteration
     */
    private void addIntsToList(List<String> list, int exclusiveMaxValue, int increment) {
        for (int i = 0; i < exclusiveMaxValue; i += increment) {
            if (i < 10) {
                list.add("0" + String.valueOf(i));
            } else {
                list.add(String.valueOf(i));
            }
        }

    }



    /**
     * Set the onClick of the button, gathering all the informations on the fragment and
     * sending the event to the database
     */
    private void setUpCreateEventButton() {
        create_event.setOnClickListener(v -> {
            //Set the starting date
            Date date = getDateAndTime(start_date_pick,spinner_hours,spinner_minutes);
            Date end_date = getDateAndTime(end_date_pick,spinner_end_hours,spinner_end_minutes);

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

            if (!checkIfValid(tit, desc, date,end_date)) {
                return;
            }

            String mapUrl;
            if(!pla.isEmpty()){
                mapUrl = "https://plan.epfl.ch/?room=" + pla.replaceAll("\\s+","");
            } else {
                mapUrl = "https://plan.epfl.ch";
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
                    setUrlPlaceAndRoom(mapUrl).
                    setWebsite(web).
                    setContact(cont).
                    setCategory(cat).
                    setSpeaker(speak).
                    build();

            DatabaseFactory.getDependency().addEvent(event);
            mListener.onFragmentInteraction(CommunicationTag.OPEN_EVENT_FRAGMENT, null);

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
        speaker = view.findViewById(R.id.speaker);
        category = view.findViewById(R.id.category);
        contact = view.findViewById(R.id.contact);

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

        start_date_pick = view.findViewById(R.id.date_for_add);
        end_date_pick = view.findViewById(R.id.end_date_for_add);



        return view;


    }

    /**
     * Helper method that gets the date and time from a date picker and two spinners representing the hours and minutes
     * @param datePick the DatePicker that contains the date
     * @param hours the hours
     * @param minutes the minutes
     * @return the full date
     */
    private Date getDateAndTime(DatePicker datePick, Spinner hours, Spinner minutes){
        int hour = getIntSpinnerContent(hours)-1;
        int minute = getIntSpinnerContent(minutes);
        int day = datePick.getDayOfMonth();
        int month = datePick.getMonth();
        int year = datePick.getYear() - 1900;
        return new Date(year, month, day, hour, minute);
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
     *
     * check if the arguments are valid for creating an event
     *
     * @param title       , the title of the event
     * @param description , the long description of the event
     * @param start , the starting date of the event
     * @param end , the end date of the event
     * @return if the arguments are valid
     */
    private boolean checkIfValid(String title, String description, Date start, Date end) {
        boolean isValid = true;
        if (title.isEmpty())
            isValid = viewSetError(title_view, "please write a title");
        if (description.isEmpty())
            isValid = viewSetError(description_view, "please write a description");
        if (start.before(today) || end.before(start)){
            Toast.makeText(getActivity(), "Set a correct date", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        return isValid;
    }

    /**
     * Set an error message in a textview.
     *
     * @param t            , the textview we take care of
     * @param errorMessage , the message we want to show
     * @return false
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
        ArrayAdapter adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_item, list);
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
            List<String> names = new ArrayList<>(association_map.keySet());
            java.util.Collections.sort(names);
            setSpinner(spinner, names);
        });
    }
}