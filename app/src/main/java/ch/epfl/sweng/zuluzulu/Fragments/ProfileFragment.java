package ch.epfl.sweng.zuluzulu.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.OnFragmentInteractionListener;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.User.User;
import ch.epfl.sweng.zuluzulu.User.UserRole;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends SuperFragment {
    private static final String PROFILE_TAG = "PROFILE_TAG";

    private User user;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param user User
     * @return A new instance of fragment ProfileFragment.
     */
    public static ProfileFragment newInstance(User user) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(PROFILE_TAG, user);

        // Transmit data
        ProfileFragment profileFragment = new ProfileFragment();
        profileFragment.setArguments(bundle);

        return profileFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.user = (User) getArguments().getSerializable(PROFILE_TAG);
            mListener.onFragmentInteraction(CommunicationTag.SET_TITLE, user.getFirstNames() + "'s Profile");
        } else {
            throw new AssertionError("No argument");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView user_view = view.findViewById(R.id.profile_name_text);

        StringBuilder builder = new StringBuilder();
        if (user.getFirstNames() != null && user.getFirstNames().length() > 1) {
            builder.append(user.getFirstNames().substring(0, 1).toUpperCase());
            builder.append(user.getFirstNames().substring(1));
        }
        if (user.getLastNames() != null && user.getLastNames().length() > 1) {
            builder.append(" ").append(user.getLastNames().substring(0, 1).toUpperCase());
            builder.append(user.getLastNames().substring(1));
        }
        if (user.hasRole(UserRole.ADMIN)) {
            builder.append(" - ADMIN");
        }

        String username = builder.toString();

        user_view.setText(username);


        TextView gaspar = view.findViewById(R.id.profile_gaspar_text);
        gaspar.setText(user.getGaspar());


        TextView email = view.findViewById(R.id.profile_email_edit);
        email.setText(user.getEmail());


        TextView sciper = view.findViewById(R.id.profile_sciper_edit);
        sciper.setText(user.getSciper());

        TextView unit = view.findViewById(R.id.profile_unit_edit);
        unit.setText(new StringBuilder().append(user.getSection()).append("-").append(user.getSemester()));

        return view;
    }
}
