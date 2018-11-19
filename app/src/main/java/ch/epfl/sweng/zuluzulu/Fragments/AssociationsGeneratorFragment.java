package ch.epfl.sweng.zuluzulu.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ch.epfl.sweng.zuluzulu.Adapters.AddAssociationArrayAdapter;
import ch.epfl.sweng.zuluzulu.Adapters.AssociationArrayAdapter;
import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.Firebase.Database.Database;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.IdlingResource.IdlingResourceFactory;
import ch.epfl.sweng.zuluzulu.OnFragmentInteractionListener;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Association;
import ch.epfl.sweng.zuluzulu.URLTools.AssociationsParser;
import ch.epfl.sweng.zuluzulu.URLTools.IconParser;
import ch.epfl.sweng.zuluzulu.URLTools.MementoParser;
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

    private AddAssociationArrayAdapter adapter = null;


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
                if(index == 10)
                    break;
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

            Association association = new Association(
                        index,
                        datas.get(index).split(",")[1]
                        , datas.get(index).split(",")[2],
                        datas.get(index).split(",")[2],
                        Uri.parse(final_icon_url),
                        Uri.parse(final_icon_url),
                        new ArrayList<>(),
                        1,
                        0);

            this.associations.add(association);

            ListView list = Objects.requireNonNull(getView()).findViewById(R.id.associations_generator_listview);
            if(adapter == null) {
                adapter = new AddAssociationArrayAdapter(getContext(), this.associations, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        System.out.println("ADD");
                    }
                });
                System.out.println("ADAPTER CREATED");
            }
            list.setAdapter(adapter);

            adapter.notifyDataSetChanged();
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
