package ch.epfl.sweng.zuluzulu.Fragments.AdminFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.Fragments.SuperFragment;
import ch.epfl.sweng.zuluzulu.R;

import static ch.epfl.sweng.zuluzulu.CommunicationTag.OPEN_CREATE_EVENT;
import static ch.epfl.sweng.zuluzulu.CommunicationTag.OPEN_MANAGE_ASSOCIATION;
import static ch.epfl.sweng.zuluzulu.CommunicationTag.OPEN_MANAGE_CHANNEL;
import static ch.epfl.sweng.zuluzulu.CommunicationTag.OPEN_MANAGE_USER;
import static ch.epfl.sweng.zuluzulu.CommunicationTag.OPEN_MEMENTO;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminPanelFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminPanelFragment extends SuperFragment {

    public AdminPanelFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AdminPanelFragment.
     */
    public static AdminPanelFragment newInstance() {
        AdminPanelFragment fragment = new AdminPanelFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_panel, container, false);

        view.findViewById(R.id.panel_association).setOnClickListener(sendTagOnClick(OPEN_MANAGE_ASSOCIATION));
        view.findViewById(R.id.panel_channel).setOnClickListener(sendTagOnClick(OPEN_MANAGE_CHANNEL));
        view.findViewById(R.id.panel_create_event).setOnClickListener(sendTagOnClick(OPEN_CREATE_EVENT));
        view.findViewById(R.id.panel_memento).setOnClickListener(sendTagOnClick(OPEN_MEMENTO));
        view.findViewById(R.id.panel_user).setOnClickListener(sendTagOnClick(OPEN_MANAGE_USER));
        return view;
    }

    private View.OnClickListener sendTagOnClick(CommunicationTag tag) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFragmentInteraction(tag, null);
            }
        };
    }
}
