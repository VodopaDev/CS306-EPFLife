package ch.epfl.sweng.zuluzulu.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ch.epfl.sweng.zuluzulu.Adapters.AddAssociationAdapter;
import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.Firebase.Database.Database;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.IdlingResource.IdlingResourceFactory;
import ch.epfl.sweng.zuluzulu.OnFragmentInteractionListener;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Association;
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
    private static final String EPFL_LOGO = "https://mediacom.epfl.ch/files/content/sites/mediacom/files/EPFL-Logo.jpg";

    private List<String> datas;
    private Database db = DatabaseFactory.getDependency();
    private List<Association> associations = new ArrayList<>();

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
        if (results != null) {
            this.datas = results;
            int index = 0;
            for (String data : datas
                    ) {
                if (index < 0) {
                    // Tell tests the async execution is finished
                    IdlingResourceFactory.incrementCountingIdlingResource();

                    String url = data.split(",")[0];

                    int finalIndex = index;
                    UrlHandler urlHandler = new UrlHandler(new UrlResultListener<List<String>>() {
                        @Override
                        public void onFinished(List<String> result) {
                            handleIcon(finalIndex, result);
                        }
                    }, new IconParser());

                    urlHandler.execute(url);
                    index++;
                }

                Association association = new Association(
                        index,
                        data.split(",")[1],
                        data.split(",")[2],
                        data.split(",")[2],
                        Uri.parse(EPFL_LOGO),
                        Uri.parse(EPFL_LOGO),
                        new ArrayList<>(),
                        1,
                        0);

                this.associations.add(association);
            }

            RecyclerView mRecyclerView = (RecyclerView) getView().findViewById(R.id.associations_generator_recyclerview);

            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            mRecyclerView.setHasFixedSize(true);

            // use a linear layout manager
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
            mRecyclerView.setLayoutManager(mLayoutManager);

            // specify an adapter (see also next example)
            AddAssociationAdapter adapter = new AddAssociationAdapter(this.getContext(), this.associations);
            mRecyclerView.setAdapter(adapter);


        }

        IdlingResourceFactory.decrementCountingIdlingResource();
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
                final_icon_url = EPFL_LOGO;
            }

            datas.set(index, datas.get(index) + "," + final_icon_url);

            //put db
            Map<String, Object> docData = new HashMap<>();
            docData.put("channel_id", "1");
            docData.put("events", new ArrayList<>());
            docData.put("icon_uri", final_icon_url);
            docData.put("name", datas.get(index).split(",")[1]);
                docData.put("short_desc", datas.get(index).split(",")[2]);
                docData.put("long_desc", datas.get(index).split(",")[2]);
            docData.put("id", Integer.toString(index));

            System.out.println(datas.get(index).split(",")[1] + " added");
            db.collection("assos_info").document(Integer.toString(index)).set(docData);
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
        String value;
        if (result != null && !result.isEmpty() && index >= 0 && index < this.datas.size()) {
            value = result.get(0);

        } else {
            value = EPFL_LOGO;
        }
        addDatabase(value, index);
        IdlingResourceFactory.decrementCountingIdlingResource();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.datas = null;

        mListener.onFragmentInteraction(CommunicationTag.SET_TITLE, "Associations Generator");
        UrlHandler urlHandler = new UrlHandler(this::handleAssociations, new AssociationsParser());
        urlHandler.execute(EPFL_URL);

        // Send increment to wait async execution in test
        IdlingResourceFactory.incrementCountingIdlingResource();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_associations_generator, container, false);
        if (view == null) {
            return null;
        }
        // Inflate the layout for this fragment
        return view;
    }
}
