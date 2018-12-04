package ch.epfl.sweng.zuluzulu.Fragments.AdminFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.Fragments.SuperFragment;
import ch.epfl.sweng.zuluzulu.Structure.Event;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Association;
import ch.epfl.sweng.zuluzulu.Structure.Event;
import ch.epfl.sweng.zuluzulu.Structure.EventBuilder;
import ch.epfl.sweng.zuluzulu.Structure.EventDate;

public class AddEventFragment extends SuperFragment {
    private static final String EPFL_LOGO = "https://mediacom.epfl.ch/files/content/sites/mediacom/files/EPFL-Logo.jpg";
    private static final int[] INDICES = {0, 2, 4, 6, 7, 9, 11};
    //for association name
    private Map<String, String> association_map = new HashMap<>();
    private Spinner spinner;
    private int numberOfEvents;

    //for date (number of days adapt depending on the month chosen)
    private List<String> short_months = new ArrayList<>();
    private List<String> thirty_one_days = new ArrayList<>();
    private List<String> thirty_days;
    private List<String> feb_days;
    private List<String> years = new ArrayList();
    private List<String> thirty_one_days_months = new ArrayList<>();
    private Spinner spinner_days;
    private Spinner spinner_months;
    private Spinner spinner_years;

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

    public static AddEventFragment newInstance() {
        return new AddEventFragment();
    }

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //fill the list for the months spinner
        populateShortMonths();

        //makes a sublist containing all the months with 31 days
        fillMonthsSublist(thirty_one_days_months, INDICES);

        //a list of 31 days
        for (int i = 1; i <= 31; i++) {
            thirty_one_days.add(String.valueOf(i));
        }
        //goes to 30 for months with a day less
        thirty_days = thirty_one_days.subList(0, 30);
        //special case for february
        feb_days = thirty_one_days.subList(0, 28);

        //fills the year list for the spinner
        for (int i = 0; i <= 10; i++) {
            years.add(String.valueOf(2018 + i));
        }

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
     * simply fill short_months with the 12 months of the year
     */
    private void populateShortMonths() {
        short_months.add("Jan");
        short_months.add("Feb");
        short_months.add("Mar");
        short_months.add("Apr");
        short_months.add("May");
        short_months.add("Jun");
        short_months.add("Jul");
        short_months.add("Aug");
        short_months.add("Sep");
        short_months.add("Oct");
        short_months.add("Nov");
        short_months.add("Dec");
    }

    /**
     * create a sublist of months containing only those we select
     *
     * @param months the sublist we want to fill
     * @param array  the array of indices that indicate the months we want
     */
    private void fillMonthsSublist(List<String> months, int[] array) {
        for (int i : array) {
            months.add(short_months.get(i));
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
                int day = getNumberSpinnerContent(spinner_days);
                int month = short_months.indexOf(spinner_months.getSelectedItem().toString());
                int year = getNumberSpinnerContent(spinner_years) - 1900;
                Date date = new Date(year, month, day, hour, minute);

                String name = spinner.getSelectedItem().toString();
                String tit = title_view.getText().toString();
                String desc = description_view.getText().toString();
                String pla = place.getText().toString();
                String org = organizer.getText().toString();

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
                        setDate(new EventDate(date, date)).
                        setFollowers(new ArrayList<>()).
                        setOrganizer(org).
                        setPlace(pla).
                        setIconUri(EPFL_LOGO).
                        setBannerUri(EPFL_LOGO).
                        setUrlPlaceAndRoom("EPFL").
                        setWebsite("http://epfl.ch").
                        setContact("contact@epfl.ch").
                        setCategory("none").
                        setSpeaker("speaker").
                        build();

                DatabaseFactory.getDependency().addEvent(event);
            }
        });
    }

    /**
     * Method that setup the fact that there are different days depending on the month
     * and update the spinners values accordingly
     */
    private void setUpDayMonthInteraction() {
        spinner_months.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos,
                                       long id) {

                String selected_mon = ((TextView) view).getText().toString();
                int selected = Integer.parseInt(spinner_days.getSelectedItem().toString());

                if (selected_mon.equals("Feb")) {
                    //case we select february
                    setSpinner(spinner_days, feb_days);
                    helperSetup(selected, 28, 27);

                } else {
                    if (thirty_one_days_months.contains(selected_mon)) {
                        //case we select a 31 days month
                        setSpinner(spinner_days, thirty_one_days);
                        spinner_days.setSelection(selected - 1);
                    } else {
                        //case we select a 30 days month
                        setSpinner(spinner_days, thirty_days);
                        helperSetup(selected, 30, 29);
                    }
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }

        });
    }

    /**
     * little method that check the selected day is within the boundaries
     * of the days of the month and adapt the value
     *
     * @param selected       current selected value
     * @param conditionValue the max value (exclusive) selected can have for the current month
     * @param setValue       set the spinner at this value if selected is out of bound
     */
    private void helperSetup(int selected, int conditionValue, int setValue) {
        if (selected < conditionValue) {
            spinner_days.setSelection(selected - 1);
        } else {
            spinner_days.setSelection(setValue);
        }

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


        //fill the different spinners for the dates.
        spinner_days = view.findViewById(R.id.spinner_day);
        setSpinner(spinner_days, thirty_one_days);

        spinner_months = view.findViewById(R.id.spinner_month);
        setSpinner(spinner_months, short_months);
        setUpDayMonthInteraction();

        spinner_years = view.findViewById(R.id.spinner_year);
        setSpinner(spinner_years, years);

        spinner_hours = view.findViewById(R.id.spinner_hour);
        setSpinner(spinner_hours, hours);

        spinner_minutes = view.findViewById(R.id.spinner_minute);
        setSpinner(spinner_minutes, minutes);

        //fill the spinner for associations.
        spinner = view.findViewById(R.id.spinner);
        fillAssociationNames();

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
