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

    public static AddEventFragment newInstance() {
        return new AddEventFragment();
    }

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        association_names = new ArrayList<>();
        fillAssociationNames("name");
        /*association_names.add("hello how are you todaay it is super fun to write asdasdklasdklashdalksdahlas");
        association_names.add("selected");*/


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_event, container, false);

        spinner =  view.findViewById(R.id.spinner);
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, association_names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);


        return view;
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
