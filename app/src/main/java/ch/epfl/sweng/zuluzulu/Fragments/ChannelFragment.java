package ch.epfl.sweng.zuluzulu.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.zuluzulu.OnFragmentInteractionListener;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.User;
import ch.epfl.sweng.zuluzulu.View.ChannelView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChannelFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChannelFragment extends Fragment {
    public static final String TAG = "CHANNEL_TAG";
    private static final String ARG_USER = "ARG_USER";

    private User user;
    private List<ChannelView> channelViewList;
    private LinearLayout vlayout_channels;
    private ScrollView scroll_channels;

    private OnFragmentInteractionListener mListener;

    public ChannelFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ChannelFragment.
     */
    public static ChannelFragment newInstance(User user) {
        ChannelFragment fragment = new ChannelFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_channel, container, false);
        vlayout_channels = view.findViewById(R.id.vlayout_channels);
        scroll_channels = view.findViewById(R.id.scroll_channels);

        channelViewList = new ArrayList<>();

        FirebaseApp.initializeApp(getContext());
        FirebaseFirestore.getInstance().document("channels_info/all_channels")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                List<DocumentReference> channels_all_ref = (ArrayList<DocumentReference>)task.getResult().get("all_ids");
                for(int i = 0; i < channels_all_ref.size(); i++){
                    ChannelView channelView = new ChannelView(getContext(), channels_all_ref.get(i));
                    channelViewList.add(channelView);
                    vlayout_channels.addView(channelView);
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
