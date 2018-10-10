package ch.epfl.sweng.zuluzulu.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import ch.epfl.sweng.zuluzulu.Structure.Association;
import ch.epfl.sweng.zuluzulu.OnFragmentInteractionListener;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.View.AssociationAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AssociationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssociationFragment extends Fragment {
    private static final String TAG = "ASSOCIATION_TAG";

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

    // TODO: Rename and change types and number of parameters
    public static AssociationFragment newInstance(String param1, String param2) {
        AssociationFragment fragment = new AssociationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assos_all = new ArrayList<>();
        assos_adapter = new AssociationAdapter(getContext(), assos_all);

        FirebaseFirestore.getInstance().document("assos_info/all_assos")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                List<DocumentReference> assos_all_ref = (ArrayList<DocumentReference>)task.getResult().get("all_ids");

                for(int i = 0; i < assos_all_ref.size(); i++){
                    assos_all_ref.get(i).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Association asso = new Association(documentSnapshot);
                            assos_all.add(asso);
                            assos_adapter.sort(Association.getComparator());
                        }
                    });
                }

            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_association, container, false);

        listview_assos = view.findViewById(R.id.listview_assos);
        button_assos_all = view.findViewById(R.id.button_assos_all);
        button_assos_fav = view.findViewById(R.id.button_assos_fav);

        listview_assos.setAdapter(assos_adapter);



        // TODO: add a check if the User is authenticated and load favorites
        /*
        if()
            FirebaseFirestore.getInstance().document("assos_info/all_assos")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                List<DocumentReference> assos_all_fav = (ArrayList<DocumentReference>)task.getResult().get("all_ids");
                loadAssociationsList(assos_all_fav, vlayout_assos_fav);
            }
        });
        */

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

}
