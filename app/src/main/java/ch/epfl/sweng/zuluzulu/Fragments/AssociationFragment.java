package ch.epfl.sweng.zuluzulu.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.zuluzulu.View.AssociationCard;
import ch.epfl.sweng.zuluzulu.OnFragmentInteractionListener;
import ch.epfl.sweng.zuluzulu.R;

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

    private List<AssociationCard> card_list;
    private Button button_assos_all;
    private Button button_assos_fav;
    private LinearLayout vlayout_assos_all;
    private LinearLayout vlayout_assos_fav;
    private ScrollView scroll_assos_all;
    private ScrollView scroll_assos_fav;

    public AssociationFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AssociationFragment newInstance() {
        AssociationFragment fragment = new AssociationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_association, container, false);
        card_list = new ArrayList<>();
        vlayout_assos_all = view.findViewById(R.id.vlayout_assos_all);
        vlayout_assos_fav = view.findViewById(R.id.vlayout_assos_fav);
        button_assos_all = view.findViewById(R.id.button_assos_all);
        button_assos_fav = view.findViewById(R.id.button_assos_fav);
        scroll_assos_all = view.findViewById(R.id.scroll_assos_all);
        scroll_assos_fav = view.findViewById(R.id.scroll_assos_fav);

        button_assos_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scroll_assos_all.setVisibility(View.VISIBLE);
                scroll_assos_fav.setVisibility(View.INVISIBLE);
            }
        });

        button_assos_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scroll_assos_all.setVisibility(View.INVISIBLE);
                scroll_assos_fav.setVisibility(View.VISIBLE);
            }
        });


        FirebaseApp.initializeApp(getContext());
        FirebaseFirestore.getInstance().document("assos_info/all_assos")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                List<DocumentReference> assos_all_ref = (ArrayList<DocumentReference>)task.getResult().get("all_ids");
                for(int i = 0; i < assos_all_ref.size(); i++){
                    AssociationCard card = new AssociationCard(getContext(), assos_all_ref.get(i));
                    card_list.add(card);
                    vlayout_assos_all.addView(card);
                }
            }
        });

        // TODO: add a check if the User is authentificated and load favorites
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

    public boolean hasLoaded(){
        for(int i = 0; i < card_list.size(); i++){
            if(!card_list.get(i).hasLoaded())
                return false;
        }
        return true;
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
