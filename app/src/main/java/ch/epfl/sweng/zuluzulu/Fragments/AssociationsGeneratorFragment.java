package ch.epfl.sweng.zuluzulu.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.zuluzulu.Adapters.AddAssociationAdapter;
import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.Firebase.Proxy;
import ch.epfl.sweng.zuluzulu.IdlingResource.IdlingResourceFactory;
import ch.epfl.sweng.zuluzulu.OnFragmentInteractionListener;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Association;
import ch.epfl.sweng.zuluzulu.URLTools.AssociationsParser;
import ch.epfl.sweng.zuluzulu.URLTools.IconParser;
import ch.epfl.sweng.zuluzulu.URLTools.OnClickRecyclerView;
import ch.epfl.sweng.zuluzulu.URLTools.UrlHandler;
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
    private Proxy db = DatabaseFactory.getDependency();
    private List<Association> associations;
    private AddAssociationAdapter adapter;

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
            this.datas.addAll(results);
            int index = 0;
            for (String data : datas
                    ) {
                // Tell tests the async execution is finished
                IdlingResourceFactory.incrementCountingIdlingResource();

                String url = data.split(",")[0];

                int finalIndex = index;
                UrlHandler urlHandler = new UrlHandler(result -> handleIcon(finalIndex, result), new IconParser());
                urlHandler.execute(url);
                index++;

                Association association = new Association(
                        DatabaseFactory.getDependency().getNewAssociationId(), //id
                        data.split(",")[1], // name
                        data.split(",")[2], // short description
                        data.split(",")[2], // long description
                        EPFL_LOGO, // icon uri
                        EPFL_LOGO, // banner_icon
                        new ArrayList<>(), // events
                        DatabaseFactory.getDependency().getNewChannelId() // channel id
                        );
                associations.add(association);
            }
        }

        adapter.notifyDataSetChanged();
        IdlingResourceFactory.decrementCountingIdlingResource();
    }

    /**
     * Add the loaded icon to database
     *
     * @param index  index
     * @param result Icon found
     */
    private void handleIcon(final int index, List<String> result) {
        String value = EPFL_LOGO;
        if (result != null && !result.isEmpty() && checkBound(index)) {
            value = result.get(0);
            value = createUrl(datas.get(index).split(",")[0], value);
        }

        Association asso = this.associations.get(index);
        Association newAssociation = new Association(
                asso.getId(), // id
                asso.getName(), // name
                asso.getShortDescription(), // short description
                asso.getLongDescription(), // long description
                value, // icon uri
                asso.getBannerUri().toString(), // banner uri
                new ArrayList<>(), // events
                asso.getChannelId() // channel id
                );
        associations.set(index, newAssociation);
        adapter.notifyDataSetChanged();

        IdlingResourceFactory.decrementCountingIdlingResource();
    }

    private String createUrl(String base, String icon_path) {
        String value = EPFL_LOGO;
        try {
            URL url = new URL(base);
            URL iconUrl = new URL(url, icon_path);

            value = iconUrl.toString();
            if (value.contains("www.epfl.ch/favicon.ico")) {
                value = EPFL_LOGO;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return value;
    }

    private boolean checkBound(int index) {
        return index >= 0 && index < datas.size() && datas.size() == associations.size();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.datas = new ArrayList<>();
        this.associations = new ArrayList<>();
        this.adapter = new AddAssociationAdapter(this.getContext(), this.associations, new OnClickRecyclerView() {
            @Override
            public void onClick(int i) {
                if (checkBound(i)) {
                    DatabaseFactory.getDependency().addAssociation(associations.get(i));
                    Snackbar.make(getView(), associations.get(i).getName() + " added", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

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

        RecyclerView mRecyclerView = view.findViewById(R.id.associations_generator_recyclerview);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(false);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mRecyclerView.setAdapter(adapter);

        // Inflate the layout for this fragment
        return view;
    }
}
