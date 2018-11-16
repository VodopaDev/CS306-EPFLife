package ch.epfl.sweng.zuluzulu.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.Firebase.Database.Database;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.OnFragmentInteractionListener;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.URLTools.AssociationsParser;
import ch.epfl.sweng.zuluzulu.URLTools.IconParser;
import ch.epfl.sweng.zuluzulu.URLTools.UrlHandler;
import ch.epfl.sweng.zuluzulu.URLTools.UrlResultListener;
import ch.epfl.sweng.zuluzulu.User.User;
import ch.epfl.sweng.zuluzulu.User.UserRole;


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
    private static final UserRole ROLE_REQUIRED = UserRole.ADMIN;

    private List<String> datas;
    private Database db = DatabaseFactory.getDependency();

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
     */
    private void handleAssociations(List<String> results) {
        mListener.onFragmentInteraction(CommunicationTag.DECREMENT_IDLING_RESOURCE, true);

        if (results != null) {
            this.datas = results;
            updateView();
        }
    }

    /**
     * Load the associations icons
     *
     * @param name
     */
    private void requestIcon(String name) {
        if (name == null || name.length() == 0) {
            return;
        }

        int index = 0;
        for (String data : datas) {
            if (data.toLowerCase().contains(name.toLowerCase())) {
                // Tell tests the async execution is finished
                mListener.onFragmentInteraction(CommunicationTag.INCREMENT_IDLING_RESOURCE, true);

                String url = data.split(",")[0];

                int finalIndex = index;
                UrlHandler urlHandler = new UrlHandler(new UrlResultListener<List<String>>() {
                    @Override
                    public void onFinished(List<String> result) {
                        handleIcon(finalIndex, result);
                    }
                }, new IconParser());

                urlHandler.execute(url);
            }
            index++;
        }
    }

    private void addDatabase(String icon_url, int index) {
        if (index < 0 || index >= this.datas.size()) {
            return;
        }
        try {
            String base_url = datas.get(index).split(",")[0];
            URL url = new URL(base_url);
            URL iconUrl = new URL(url, icon_url);
            String final_icon_url = iconUrl.toString();

            if (final_icon_url.contains("www.epfl.ch/favicon.ico")) {
                final_icon_url = "https://mediacom.epfl.ch/files/content/sites/mediacom/files/EPFL-Logo.jpg";
            }

            datas.set(index, datas.get(index) + "," + final_icon_url);

            //put db
            Map<String, Object> docData = new HashMap<>();
            docData.put("channel_id", 1);
            docData.put("events", new ArrayList<>());
            docData.put("icon_uri", final_icon_url);
            docData.put("name", datas.get(index).split(",")[1]);
            docData.put("short_desc", datas.get(index).split(",")[2]);
            docData.put("long_desc", datas.get(index).split(",")[2]);
            docData.put("id", index + 100);

            db.collection("assos_info").document(Integer.toString(index + 100)).set(docData);
            updateView();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Add the loaded icon to database
     * @param index
     * @param result
     */
    private void handleIcon(final int index, List<String> result) {
        if (result != null && index >= 0 && index < this.datas.size()) {
            String value = result.get(0);

            addDatabase(value, index);
        }
        mListener.onFragmentInteraction(CommunicationTag.DECREMENT_IDLING_RESOURCE, true);
    }



    /**
     * Fill the datas in the view
     */
    private void updateView() {
        TextView view = Objects.requireNonNull(getView()).findViewById(R.id.associations_generator_list_values);

        if (datas != null && datas.size() > 0) {
            view.setText(datas.size() + " ASSOCIATIONS FOUND : \n\n");
        }

        for (String data : datas) {
            view.append(data.replaceAll(",", "\n"));
            view.append("\n-----------\n");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.datas = null;

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
        if (view == null) {
            return null;
        }
        Button button = view.findViewById(R.id.load_icon_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView text = view.findViewById(R.id.nbr_icon);
                requestIcon(text.getText().toString());
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}
