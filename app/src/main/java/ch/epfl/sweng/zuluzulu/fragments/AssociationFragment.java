package ch.epfl.sweng.zuluzulu.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.sweng.zuluzulu.adapters.AssociationArrayAdapter;
import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.OnFragmentInteractionListener;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.structure.Association;
import ch.epfl.sweng.zuluzulu.structure.user.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.structure.user.User;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AssociationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssociationFragment extends SuperFragment {
    private static final String ARG_USER = "ARG_USER";

    private User user;

    private List<Association> assosAll;
    private List<Association> assosFav;
    private List<Association> assosToFilter;
    private List<Association> assosFiltered;
    private AssociationArrayAdapter assosAdapter;

    private TextView plainTextFilter;

    public AssociationFragment() {
        // Required empty public constructor
    }

    public static AssociationFragment newInstance(User user) {
        if(user == null)
            throw new IllegalArgumentException("user can't be null");
        AssociationFragment fragment = new AssociationFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable(ARG_USER);
            mListener.onFragmentInteraction(CommunicationTag.SET_TITLE, getResources().getString(R.string.drawer_associations));
        }


        assosAll = new ArrayList<>();
        assosFav = new ArrayList<>();
        assosToFilter = new ArrayList<>();
        assosFiltered = new ArrayList<>();

        assosAdapter = new AssociationArrayAdapter(getContext(), assosFiltered, mListener);

        fillAssociationLists();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_association, container, false);

        ListView listviewAssos = view.findViewById(R.id.association_fragment_listview);
        listviewAssos.setAdapter(assosAdapter);

        Button buttonAssosFav = view.findViewById(R.id.association_fragment_fav_button);
        Button buttonAssosAll = view.findViewById(R.id.association_fragment_all_button);

        buttonAssosFav.setOnClickListener(v -> {
            if (user.isConnected())
                updateListView(buttonAssosFav, buttonAssosAll, assosFav);
            else
                Snackbar.make(getView(), "Login to access your favorite associations", 5000).show();
        });
        buttonAssosAll.setOnClickListener(v -> updateListView(buttonAssosAll, buttonAssosFav, assosAll));

        plainTextFilter = view.findViewById(R.id.association_fragment_search_text);
        plainTextFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String firstLetters = s.toString().toLowerCase();
                assosFiltered.clear();
                for (Association assos : assosToFilter) {
                    if (assos.getName().toLowerCase().contains(firstLetters))
                        assosFiltered.add(assos);
                }
                assosAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

        return view;
    }

    /**
     * Fill the all and followed association list from the database
     */
    private void fillAssociationLists() {
        assosAll.clear();
        assosFiltered.clear();
        assosToFilter.clear();
        assosFav.clear();
        DatabaseFactory.getDependency().getAllAssociations(result -> {
            if (!result.isEmpty()) {
                for (Association asso : result) {
                    assosAll.add(asso);
                    if (user.isConnected() && ((AuthenticatedUser) user).isFollowedAssociation(asso.getId()))
                        assosFav.add(asso);
                }
                Collections.sort(assosAll);
                Collections.sort(assosFav);
                assosToFilter = assosAll;
                assosFiltered.addAll(assosToFilter);
                assosAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Select the new list of associations to filter and change the buttons appearance accordingly
     *
     * @param newSelected   new button to be selected
     * @param newUnselected new button to unselect
     * @param newToFilter   new association list to filter
     */
    private void updateListView(Button newSelected, Button newUnselected, List<Association> newToFilter) {
        newSelected.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
        newUnselected.setBackgroundColor(getResources().getColor(R.color.colorGrayDarkTransparent));
        plainTextFilter.setText("");
        assosToFilter = newToFilter;
        assosFiltered.clear();
        assosFiltered.addAll(assosToFilter);
        assosAdapter.notifyDataSetChanged();
    }
}
