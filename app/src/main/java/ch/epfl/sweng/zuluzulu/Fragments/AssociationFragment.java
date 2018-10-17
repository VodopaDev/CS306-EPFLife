package ch.epfl.sweng.zuluzulu.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.zuluzulu.Adapters.AssociationAdapter;
import ch.epfl.sweng.zuluzulu.OnFragmentInteractionListener;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Association;
import ch.epfl.sweng.zuluzulu.Structure.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.Structure.User;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AssociationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssociationFragment extends Fragment {
    private static final String TAG = "ASSOCIATIONS_TAG";
    private static final String ARG_USER = "ARG_USER";

    private User user;
    private OnFragmentInteractionListener mListener;

    private ArrayList<Association> assos_all;
    private ArrayList<Association> assos_fav;
    private AssociationAdapter assos_adapter;

    private ListView listview_assos;
    private Button button_assos_all;
    private Button button_assos_fav;

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
        }

        assos_all = new ArrayList<>();
        assos_fav = new ArrayList<>();
        assos_adapter = new AssociationAdapter(getContext(), assos_all, mListener);

        fillAssociationLists();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_association, container, false);

        listview_assos = view.findViewById(R.id.association_fragment_listview);
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

    public ListView getListviewAssos() {
        return listview_assos;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void fillAssociationLists() {
        FirebaseFirestore.getInstance().collection("assos_info")
                .orderBy("name")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> snap_list = queryDocumentSnapshots.getDocuments();
                        for (int i = 0; i < snap_list.size(); i++) {
                            Association asso = new Association(snap_list.get(i));
                            assos_all.add(asso);

                            if (user.isConnected() && ((AuthenticatedUser) user).isFavAssociation(asso))
                                assos_fav.add(asso);
                        }
                        assos_adapter.notifyDataSetChanged();
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

    private void updateListView(Button new_selected, Button new_unselected, ArrayList<Association> data, ListView list) {
        new_selected.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
        new_unselected.setBackgroundColor(getResources().getColor(R.color.colorGrayDarkTransparent));
        assos_adapter = new AssociationAdapter(getContext(), data, mListener);
        list.setAdapter(assos_adapter);
        assos_adapter.notifyDataSetChanged();
    }

}
