package ch.epfl.sweng.zuluzulu.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ch.epfl.sweng.zuluzulu.OnFragmentInteractionListener;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.AssociationsUrlHandler;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AssociationsGeneratorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssociationsGeneratorFragment extends Fragment {
    private static final String ASSOCIATIONS_GENERATOR_TAG = "ASSOCIATIONS_GENERATOR_TAG";

    // The URL we will connect to
    final static private String EPFL_URL = "https://associations.epfl.ch/page-16300-fr-html/";


    private OnFragmentInteractionListener mListener;

    public AssociationsGeneratorFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment AssociationsGeneratorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AssociationsGeneratorFragment newInstance() {
        AssociationsGeneratorFragment fragment = new AssociationsGeneratorFragment();
        return fragment;
    }

    private Void handleData(ArrayList<String> datas){
        for (String data : datas) {
            System.out.println(data.split(",").toString());
        }

        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // handle hargs
        }

        AssociationsUrlHandler urlHandler = new AssociationsUrlHandler(this::handleData);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_associations_generator, container, false);
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
