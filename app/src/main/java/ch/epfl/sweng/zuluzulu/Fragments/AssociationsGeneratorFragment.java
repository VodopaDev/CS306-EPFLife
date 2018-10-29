package ch.epfl.sweng.zuluzulu.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ch.epfl.sweng.zuluzulu.MainActivity;
import ch.epfl.sweng.zuluzulu.OnFragmentInteractionListener;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.AssociationsUrlHandler;
import ch.epfl.sweng.zuluzulu.Structure.User;
import ch.epfl.sweng.zuluzulu.Structure.UserRole;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AssociationsGeneratorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssociationsGeneratorFragment extends SuperFragment {
    // The URL we will connect to
    final static public String EPFL_URL = "https://associations.epfl.ch/page-16300-fr-html/";
    private static final String ASSOCIATIONS_GENERATOR_TAG = "ASSOCIATIONS_GENERATOR_TAG";
    private static final UserRole ROLE_REQUIRED = UserRole.ADMIN;

    public AssociationsGeneratorFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment AssociationsGeneratorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AssociationsGeneratorFragment newInstance(User user) {
        if (!user.hasRole(ROLE_REQUIRED)) {
            return null;
        }

        AssociationsGeneratorFragment fragment = new AssociationsGeneratorFragment();
        return fragment;
    }

    /**
     * This function will handle the generated datas
     * We expect a arraylist of strings. Each information is separated by a coma in the string
     *
     * @param datas Received datas
     * @return void
     */
    private Void handleData(List<String> datas) {
        TextView view = getView().findViewById(R.id.associations_generator_list_values);
        if (datas != null) {
            // TEMPORARY CODE
            // Need to be replace by fill the database
            view.setText(datas.size() + " ASSOCIATIONS FOUND : \n\n");

            for (String data : datas) {
                view.append(data.replaceAll(",", "\n"));
                view.append("\n-----------\n");
            }
        } else {
            view.setText("ERROR NETWORK");
        }

        // Tell tests the async execution is finished
        mListener.onFragmentInteraction(MainActivity.DECREMENT, true);

        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AssociationsUrlHandler urlHandler = new AssociationsUrlHandler<List<String>>(this::handleData, AssociationsUrlHandler::parseAssociationsData);
        urlHandler.execute(EPFL_URL);

        // Send increment to wait async execution in test
        mListener.onFragmentInteraction(MainActivity.INCREMENT, true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_associations_generator, container, false);
    }
}
