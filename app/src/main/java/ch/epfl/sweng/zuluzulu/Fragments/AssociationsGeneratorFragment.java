package ch.epfl.sweng.zuluzulu.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.OnFragmentInteractionListener;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.URLTools.IconParser;
import ch.epfl.sweng.zuluzulu.URLTools.UrlHandler;
import ch.epfl.sweng.zuluzulu.Structure.User;
import ch.epfl.sweng.zuluzulu.Structure.UserRole;
import ch.epfl.sweng.zuluzulu.URLTools.AssociationsParser;
import ch.epfl.sweng.zuluzulu.URLTools.UrlReader;
import ch.epfl.sweng.zuluzulu.URLTools.UrlReaderFactory;


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

    private List<String> datas;

    public AssociationsGeneratorFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment AssociationsGeneratorFragment.
     */
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
     * @param results Received datas
     * @return void
     */
    private Void handleAssociations(Pair<String, List<String>> results) {
        mListener.onFragmentInteraction(CommunicationTag.DECREMENT_IDLING_RESOURCE, true);

        if (results != null) {
            this.datas = results.second;
            updateView();
        }

        return null;
    }

    /**
     * Load the associations icons
     * @param nbr
     */
    private void requestIcon(int nbr) {
        if(nbr <= 0 || nbr > this.datas.size()){
            return;
        }

        // Tell tests the async execution is finished
        for (int i = 0; i < nbr; i++) {
            mListener.onFragmentInteraction(CommunicationTag.INCREMENT_IDLING_RESOURCE, true);
            UrlHandler urlHandler = new UrlHandler(this::handleIcon, new IconParser());

            urlHandler.execute(this.datas.get(i).split(",")[0]);
        }
    }

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Void handleIcon(Pair<String, List<String>> result){
        if(result != null && result.second.size() > 0){
            String key = result.first;
            String value = result.second.get(0);

            for(int i = 0; i < datas.size(); i++){
                String data = datas.get(i);
                if(data.contains(key)){
                    try {
                        URL url = new URL(key);
                        URL anURI = new URL(url, value);

                        datas.set(i, datas.get(i) + "," + anURI.toString());

                        //put db
                        Map<String, Object> docData = new HashMap<>();
                        docData.put("channel_id", 1);
                        docData.put("events", new ArrayList<>());
                        if(anURI.toString().contains("www.epfl.ch/favicon.ico")){
                            docData.put("icon_uri", "https://mediacom.epfl.ch/files/content/sites/mediacom/files/EPFL-Logo.jpg");
                        } else {
                            docData.put("icon_uri", anURI.toString());
                        }
                        docData.put("name", datas.get(i).split(",")[1]);
                        docData.put("short_desc", datas.get(i).split(",")[2]);
                        docData.put("long_desc", datas.get(i).split(",")[2]);
                        docData.put("id", i + 100);
                        db.collection("assos_info").document(Integer.toString(i + 100)).delete();

                        updateView();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    break;
                }
            }
        }
        mListener.onFragmentInteraction(CommunicationTag.DECREMENT_IDLING_RESOURCE, true);

        return null;
    }

    /**
     * Fill the datas in the view
     */
    private void updateView() {
        TextView view = Objects.requireNonNull(getView()).findViewById(R.id.associations_generator_list_values);

        // TEMPORARY CODE
        // Need to be replace by fill the database
        view.setText(datas.size() + " ASSOCIATIONS FOUND : \n\n");

        for (String data : datas) {
            view.append(data.replaceAll(",", "\n"));
            view.append("\n-----------\n");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mListener.onFragmentInteraction(CommunicationTag.SET_TITLE, "Associations Generator");
        UrlHandler urlHandler = new UrlHandler(this::handleAssociations, new AssociationsParser());
        urlHandler.execute(EPFL_URL);

        // Send increment to wait async execution in test
        mListener.onFragmentInteraction(CommunicationTag.INCREMENT_IDLING_RESOURCE, true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_associations_generator, container, false);
        if(view == null){
            return null;
        }
        Button button = view.findViewById(R.id.load_icon_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView text = view.findViewById(R.id.nbr_icon);
                try {
                    int number = Integer.parseInt(text.getText().toString());
                    requestIcon(number);
                } catch (NumberFormatException e) {
                    return;
                }
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}
