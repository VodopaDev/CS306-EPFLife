package ch.epfl.sweng.zuluzulu.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.OnFragmentInteractionListener;
import ch.epfl.sweng.zuluzulu.R;

import static android.support.design.widget.Snackbar.LENGTH_SHORT;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends SuperFragment {
    public static final String TAG = "SETTINGS_TAG";
    public final static String PREF_KEY_ANONYM = "PREF_KEY_ANONYM";

    private SharedPreferences preferences;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SettingsFragment.
     */
    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListener.onFragmentInteraction(CommunicationTag.SET_TITLE, "Settings");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        preferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        Button button_clear = view.findViewById(R.id.button_clear_cache);
        button_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar clear_snackbar = Snackbar.make(v, "Cache cleared", LENGTH_SHORT);
                clear_snackbar.show();
            }
        });

        Switch switchAnonym = view.findViewById(R.id.switch_chat_anonym);
        switchAnonym.setChecked(preferences.getBoolean(PREF_KEY_ANONYM, false));
        switchAnonym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences.edit().putBoolean(PREF_KEY_ANONYM, switchAnonym.isChecked()).apply();
            }
        });

        return view;
    }
}
