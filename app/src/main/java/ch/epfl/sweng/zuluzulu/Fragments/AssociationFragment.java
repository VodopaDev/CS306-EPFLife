package ch.epfl.sweng.zuluzulu.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.zuluzulu.Adapters.AssociationArrayAdapter;
import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.OnFragmentInteractionListener;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Association;
import ch.epfl.sweng.zuluzulu.Structure.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.Structure.User;

import static ch.epfl.sweng.zuluzulu.CommunicationTag.DECREMENT_IDLING_RESOURCE;
import static ch.epfl.sweng.zuluzulu.CommunicationTag.INCREMENT_IDLING_RESOURCE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AssociationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssociationFragment extends SuperFragment {
    private static final String ARG_USER = "ARG_USER";

    private User user;

    private List<Association> assos_all;
    private List<Association> assos_filtered;
    private List<Association> assos_fav;
    private AssociationArrayAdapter assos_adapter;

    private ListView listview_assos;
    private Button button_assos_all;
    private Button button_assos_fav;

    private CheckBox checkbox_assos_sort_name;
    private CheckBox checkbox_assos_sort_date;
    private String default_sort_option;

    private TextView plainText_filter;

    public AssociationFragment() {
        // Required empty public constructor
    }

    public static AssociationFragment newInstance(User user) {
        AssociationFragment fragment = new AssociationFragment();
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
            mListener.onFragmentInteraction(CommunicationTag.SET_TITLE, "Associations");
        }

        assos_all = new ArrayList<>();
        assos_filtered = new ArrayList<>();
        assos_fav = new ArrayList<>();
        default_sort_option = "name";
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_association, container, false);

        fillAssociationLists(default_sort_option);
        listview_assos = view.findViewById(R.id.association_fragment_listview);

        assos_adapter = new AssociationArrayAdapter(getContext(), assos_filtered, mListener);
        listview_assos.setAdapter(assos_adapter);

        button_assos_fav = view.findViewById(R.id.association_fragment_fav_button);
        button_assos_all = view.findViewById(R.id.association_fragment_all_button);

        button_assos_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.isConnected())
                    updateListView(button_assos_fav, button_assos_all, assos_fav, listview_assos);
                else
                    Snackbar.make(getView(), "Login to access your favorite associations", 5000).show();
            }
        });

        button_assos_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateListView(button_assos_all, button_assos_fav, assos_all, listview_assos);
            }
        });

        checkbox_assos_sort_name = view.findViewById(R.id.assos_fragment_checkbox_sort_Name);
        checkbox_assos_sort_date = view.findViewById(R.id.assos_fragment_checkbox_sort_date);

        checkbox_assos_sort_name.setChecked(true);
        checkbox_assos_sort_name.setEnabled(false);

        checkbox_assos_sort_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkbox_assos_sort_date.setEnabled(false);
                assos_filtered.clear();
                assos_all.clear();
                assos_fav.clear();
                fillAssociationLists("id");
                checkbox_assos_sort_name.setChecked(false);
                checkbox_assos_sort_name.setEnabled(true);
            }
        });

        checkbox_assos_sort_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkbox_assos_sort_name.setEnabled(false);
                assos_filtered.clear();
                assos_all.clear();
                assos_fav.clear();
                fillAssociationLists("name");
                checkbox_assos_sort_date.setChecked(false);
                checkbox_assos_sort_date.setEnabled(true);
            }
        });

        plainText_filter = view.findViewById(R.id.assos_fragment_plainText_filter);

        plainText_filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String firstLetters = s.toString().toLowerCase();
                assos_filtered.clear();
                for(Association assos : assos_all) {
                    if (assos.getName().toLowerCase().contains(firstLetters)) {
                        assos_filtered.add(assos);
                    }
                }
                assos_adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });
        return view;
    }

    private void fillAssociationLists(String sortOption) {
        mListener.onFragmentInteraction(INCREMENT_IDLING_RESOURCE, null);
        FirebaseFirestore.getInstance().collection("assos_info").orderBy(sortOption).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> snap_list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot snap : snap_list) {
                            FirebaseMapDecorator data = new FirebaseMapDecorator(snap);
                            if (data.hasFields(Association.FIELDS)) {
                                Association asso = new Association(data);
                                assos_all.add(asso);
                                assos_filtered.add(asso);

                                if (user.isConnected() && ((AuthenticatedUser) user).isFavAssociation(asso))
                                    assos_fav.add(asso);

                                assos_adapter.notifyDataSetChanged();
                            }
                        }
                        mListener.onFragmentInteraction(DECREMENT_IDLING_RESOURCE, null);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(getView(), "Loading error, check your connection", 5000).show();
                        Log.e("ASSO_LIST", "Error fetching association date\n" + e.getMessage());
                        mListener.onFragmentInteraction(DECREMENT_IDLING_RESOURCE, null);
                    }
                });
    }

    private void updateListView(Button new_selected, Button new_unselected, List<Association> data, ListView list) {
        new_selected.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
        new_unselected.setBackgroundColor(getResources().getColor(R.color.colorGrayDarkTransparent));
        assos_adapter = new AssociationArrayAdapter(getContext(), data, mListener);
        list.setAdapter(assos_adapter);
        assos_adapter.notifyDataSetChanged();
    }
}
