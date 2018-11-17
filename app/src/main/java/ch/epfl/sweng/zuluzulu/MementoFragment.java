package ch.epfl.sweng.zuluzulu;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ch.epfl.sweng.zuluzulu.Fragments.AssociationsGeneratorFragment;
import ch.epfl.sweng.zuluzulu.Fragments.SuperFragment;
import ch.epfl.sweng.zuluzulu.URLTools.AssociationsParser;
import ch.epfl.sweng.zuluzulu.URLTools.MementoParser;
import ch.epfl.sweng.zuluzulu.URLTools.UrlHandler;
import ch.epfl.sweng.zuluzulu.User.User;
import ch.epfl.sweng.zuluzulu.User.UserRole;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MementoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MementoFragment extends SuperFragment {
    private static final String TAG = "MEMENTO_FRAGMENT";
    final static public String MEMENTO_URL = "https://memento.epfl.ch/api/jahia/mementos/associations/events/fr/";
    private static final UserRole ROLE_REQUIRED = UserRole.ADMIN;

    public MementoFragment() {
        // Required empty public constructor
    }

    /**
     * New instance
     * @param user User
     * @return The fragment
     */
    public static MementoFragment newInstance(User user) {
        if (!user.hasRole(ROLE_REQUIRED)) {
            return null;
        }

        return new MementoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mListener.onFragmentInteraction(CommunicationTag.SET_TITLE, "Associations Generator");
        UrlHandler urlHandler = new UrlHandler(this::handleMemento, new AssociationsParser());
        urlHandler.execute(MEMENTO_URL);

        // Send increment to wait async execution in test
        mListener.onFragmentInteraction(CommunicationTag.INCREMENT_IDLING_RESOURCE, true);
    }

    /**
     * Handle memento data
     * @param result Json aray datas
     */
    private void handleMemento(List<String> result) {
        if(result != null && !result.isEmpty() && !result.get(0).isEmpty()){
            String datas = result.get(0);
            JSONArray jsonarray = null;
            try {
                jsonarray = new JSONArray(datas);
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    String title = jsonobject.getString("title");
                    System.out.println(title);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d(TAG, "Could not parse json");
            }
        }

        mListener.onFragmentInteraction(CommunicationTag.DECREMENT_IDLING_RESOURCE, true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_memento, container, false);
    }
}
