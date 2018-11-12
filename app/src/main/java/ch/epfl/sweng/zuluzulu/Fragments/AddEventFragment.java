package ch.epfl.sweng.zuluzulu.Fragments;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Association;

public class AddEventFragment extends SuperFragment {

    private List<String> association_names;
    private Spinner spinner;

    private Spinner spinner_days;
    private Spinner spinner_months;
    private Spinner spinner_years;

    private List<String> months = new ArrayList<>();
    private List<String> even_months_days = new ArrayList<>();
    private List<String> odd_months_days;
    private List<String> feb_days;
    private List<String> years = new ArrayList();
    private List<String> even_months = new ArrayList<>();


    public static AddEventFragment newInstance() {
        return new AddEventFragment();
    }

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        association_names = new ArrayList<>();
        fillAssociationNames("name");


        months.add("Jan");
        months.add("Feb");
        months.add("Mar");
        months.add("Apr");
        months.add("May");
        months.add("Jun");
        months.add("Jul");
        months.add("Aug");
        months.add("Sep");
        months.add("Oct");
        months.add("Nov");
        months.add("Dec");

        for (int i = 0; i < 12; i+=2){
            even_months.add(months.get(i));
        }

        for (int i = 1; i <= 31; i++){
            even_months_days.add(String.valueOf(i));
        }

        odd_months_days = even_months_days.subList(0,30);
        feb_days = even_months_days.subList(0,28);

        for(int i = 0; i <= 10; i++){
            years.add(String.valueOf(2018+i));
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_event, container, false);

        spinner_days = view.findViewById(R.id.spinner2);
        setSpinner(spinner_days, even_months_days);

        spinner_months = view.findViewById(R.id.spinner3);
        setSpinner(spinner_months, months);
        spinner_months.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, int pos,
                                       long id) {
                String selected_mon = ((TextView) view).getText().toString();
                if(selected_mon == "Feb"){
                    setSpinner(spinner_days, feb_days);
                }else{
                    if(even_months.contains(selected_mon)){
                        setSpinner(spinner_days,even_months_days);
                    }
                    else{
                        setSpinner(spinner_days, odd_months_days);
                    }
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        spinner_years = view.findViewById(R.id.spinner4);
        setSpinner(spinner_years, years);

        spinner =  view.findViewById(R.id.spinner);
        setSpinner(spinner, association_names);


        return view;
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
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(getView(), "Loading error, check your connection", 5000).show();
                        Log.e("ASSO_LIST", "Error fetching association date\n" + e.getMessage());
                    }
                });
    }
}
