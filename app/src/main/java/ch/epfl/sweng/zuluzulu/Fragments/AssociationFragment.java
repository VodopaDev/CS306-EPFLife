package ch.epfl.sweng.zuluzulu.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.zuluzulu.OnFragmentInteractionListener;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Association;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AssociationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssociationFragment extends Fragment {
    public static final String TAG = "ASSOCIATION_TAG";

    private OnFragmentInteractionListener mListener;
    private List<DocumentReference> assos_all_ref;
    private List<Association> assos_all;
    private Button button_all;
    private Button button_fav;
    private LinearLayout buttons_layout;

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
        assos_all_ref = new ArrayList<>();

        FirebaseApp.initializeApp(getContext());
        DocumentReference db = FirebaseFirestore.getInstance().document("assos_info/all_assos");
        db.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                assos_all_ref = (ArrayList<DocumentReference>)task.getResult().get("all_ids");

                for(int i = 0; i < assos_all_ref.size(); i++){
                    Association asso = new Association(assos_all_ref.get(i));
                    assos_all.add(asso);
                    Log.d("Boucle iteration","#"+i);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_association, container, false);
        buttons_layout = view.findViewById(R.id.assos_layout);

        for(int i = 0; i < 100; i++){
            TextView text = new TextView(getContext());
            text.setText("text test" + i);
            buttons_layout.addView(text);
        }

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(TAG, uri);
        }
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
