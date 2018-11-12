package ch.epfl.sweng.zuluzulu.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Association;

public class AddEventFragment extends SuperFragment {

    //for association name
    private List<String> association_names = new ArrayList<>();
    private Spinner spinner;
    private int numberOfEvents;

    //for date (number of days adapt depending on the month chosen)
    private List<String> months = new ArrayList<>();
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

    //for validating and create the event
    private Button create_event;

    public static AddEventFragment newInstance() {
        return new AddEventFragment();
    }

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        months.add("January");
        months.add("February");
        months.add("March");
        months.add("April");
        months.add("May");
        months.add("June");
        months.add("July");
        months.add("August");
        months.add("September");
        months.add("October");
        months.add("November");
        months.add("December");

        for(String m : months){
            short_months.add(m.substring(0,3));
        }

        int[] indices = {0, 2, 4, 6,7,9,11};
        fillMonthsSublist(thirty_one_days_months,indices);

        for (int i = 1; i <= 31; i++){
            thirty_one_days.add(String.valueOf(i));
        }

        thirty_days = thirty_one_days.subList(0,30);
        feb_days = thirty_one_days.subList(0,28);

        for(int i = 0; i <= 10; i++){
            years.add(String.valueOf(2018+i));
        }

        for(int i = 0; i < 24; i++){
            if(i < 10){
                hours.add("0"+String.valueOf(i));
            }else{
                hours.add(String.valueOf(i));
            }
        }

        for(int i = 0; i < 60; i++){
            if(i<10){
                minutes.add("0"+String.valueOf(i));
            }
            else{
            minutes.add(String.valueOf(i));
            }
        }

    }

    private void fillMonthsSublist(List<String> months, int[] array){
        for(int i : array){
            months.add(short_months.get(i));
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_event, container, false);

        title_view = view.findViewById(R.id.event_title);
        description_view = view.findViewById(R.id.long_desc_text);

        create_event = view.findViewById(R.id.create_event_button);
        create_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String hour = getSpinnerContent(spinner_hours);
                String minute = getSpinnerContent(spinner_minutes);
                String day = getSpinnerContent(spinner_days);
                String month = months.get(short_months.indexOf(getSpinnerContent(spinner_months)));
                String year = getSpinnerContent(spinner_years);

                String name = spinner.getSelectedItem().toString();
                String tit = title_view.getText().toString();
                String desc = description_view.getText().toString();
                String date = day + " " + month + " " + year + " à " + hour + ":" + minute + ":00 UTC+1";

                if(!checkIfValid(tit, desc)){
                    return;
                }
                /*FirebaseFirestore db = FirebaseFirestore.getInstance();

                db.collection("events_info").orderBy("name").get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots){
                                List<DocumentSnapshot> snap_list = queryDocumentSnapshots.getDocuments();
                                numberOfEvents = snap_list.size();
                                Map<String, Object> docData = new HashMap<>();
                                docData.put("icon_uri", "https://mediacom.epfl.ch/files/content/sites/mediacom/files/EPFL-Logo.jpg");
                                docData.put("id", numberOfEvents + 1);
                                docData.put("long_desc", desc);
                                docData.put("name", name);
                                docData.put("short_desc", tit);
                                docData.put("start_date", date);
                                db.collection("events_info").document("event"+Integer.toString(numberOfEvents+1)).set(docData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        mListener.onFragmentInteraction(CommunicationTag.OPEN_EVENT_FRAGMENT, null);
                                    }
                                });

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Snackbar.make(getView(), "Loading error, check your connection", 5000).show();
                                Log.e("EVENT_LIST", "Error fetching event data\n" + e.getMessage());
                            }
                        });*/



                System.out.println(name);
                System.out.println(tit);
                System.out.println(desc);
                System.out.println(date);
                getActivity().onBackPressed();
            }
        });


        spinner_days = view.findViewById(R.id.spinner_day);
        setSpinner(spinner_days, thirty_one_days);

        spinner_months = view.findViewById(R.id.spinner_month);
        setSpinner(spinner_months, short_months);
        spinner_months.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, int pos,
                                       long id) {
                String selected_mon = ((TextView) view).getText().toString();
                if(selected_mon.equals("Feb")){
                    setSpinner(spinner_days, feb_days);
                }else{
                    if(thirty_one_days_months.contains(selected_mon)){
                        setSpinner(spinner_days, thirty_one_days);
                    }
                    else{
                        setSpinner(spinner_days, thirty_days);
                    }
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        spinner_years = view.findViewById(R.id.spinner_year);
        setSpinner(spinner_years, years);

        spinner_hours = view.findViewById(R.id.spinner_hour);
        setSpinner(spinner_hours,hours);

        spinner_minutes = view.findViewById(R.id.spinner_minute);
        setSpinner(spinner_minutes, minutes);

        spinner =  view.findViewById(R.id.spinner);
        fillAssociationNames("name");



        return view;
    }

    private boolean checkIfValid(String title, String description){
        boolean valid = true;

        if(title.length() > 30){
            valid = viewSetError(title_view,"title is too long");
        }
        if(title.isEmpty()){
            valid = viewSetError(title_view, "please write a title");
        }
        if(description.length() > 80){
            valid = viewSetError(description_view, "description is too long");
        }
        if(description.isEmpty()){
            valid = viewSetError(description_view, "please write a description");
        }

        return valid;
    }

    private boolean viewSetError(TextView t, String errorMessage){
        t.requestFocus();
        t.setError(errorMessage);
        return false;
    }

    private String getSpinnerContent(Spinner spinner){
        return spinner.getSelectedItem().toString();
    }
    private void setSpinner(Spinner spinner, List<String> list){
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void fillAssociationNames(String sortOption) {
        FirebaseFirestore.getInstance().collection("assos_info").orderBy(sortOption).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots){
                        List<DocumentSnapshot> snap_list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot snap : snap_list) {
                            FirebaseMapDecorator data = new FirebaseMapDecorator(snap);
                            if (data.hasFields(Association.FIELDS)) {
                                association_names.add((String) data.get("name"));
                            }
                        }
                        setSpinner(spinner, association_names);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(getView(), "Loading error, check your connection", 5000).show();
                        Log.e("ASSO_LIST", "Error fetching association data\n" + e.getMessage());
                    }
                });
    }
}
