package ch.epfl.sweng.zuluzulu.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.Adapters.PostArrayAdapter;
import ch.epfl.sweng.zuluzulu.Firebase.Database.Database;
import ch.epfl.sweng.zuluzulu.Firebase.Database.DatabaseCollection;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Post;
import ch.epfl.sweng.zuluzulu.Structure.Utils;
import ch.epfl.sweng.zuluzulu.User.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.User.User;

public class ReplyFragment extends SuperFragment {

    public static final List<String> FIELDS = Arrays.asList("senderName", "sciper", "message", "time", "color", "nbUps", "upScipers", "downScipers");
    private static final String TAG = "REPLY_TAG";
    private static final String ARG_USER = "ARG_USER";
    private static final String ARG_POST = "ARG_POST";
    private static final int REPLY_MAX_LENGTH = 100;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference;
    private DatabaseCollection mockableCollectionReplies;
    private DatabaseCollection mockableCollectionOriginalPost;

    private List<Post> replies = new ArrayList<>();
    private PostArrayAdapter adapter;

    private EditText replyText;
    private Button sendButton;

    private AuthenticatedUser user;
    private Post postOriginal;

    private boolean anonymous;

    public ReplyFragment() {
        // Required empty public constructor
    }

    public static ReplyFragment newInstance(User user, Post postOriginal) {
        ReplyFragment fragment = new ReplyFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        args.putSerializable(ARG_POST, postOriginal);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (AuthenticatedUser) getArguments().getSerializable(ARG_USER);
            postOriginal = (Post) getArguments().getSerializable(ARG_POST);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reply, container, false);

        ListView postView = view.findViewById(R.id.reply_original_post);
        ListView listView = view.findViewById(R.id.reply_list_view);
        replyText = view.findViewById(R.id.reply_text_edit);
        sendButton = view.findViewById(R.id.reply_send_button);

        String collectionPath = "channels/channel" + postOriginal.getChannelId() + "/posts/" + postOriginal.getId() + "/replies";
        collectionReference = db.collection(collectionPath);
        mockableCollectionReplies = DatabaseFactory.getDependency().collection(collectionPath);
        String collectionPathOriginalPost = "channels/channel" + postOriginal.getChannelId() + "/posts";
        mockableCollectionOriginalPost = DatabaseFactory.getDependency().collection(collectionPathOriginalPost);

        PostArrayAdapter adapterOriginalPost = new PostArrayAdapter(view.getContext(), new ArrayList<>(Arrays.asList(postOriginal)));
        postView.setAdapter(adapterOriginalPost);
        adapter = new PostArrayAdapter(view.getContext(), replies);
        listView.setAdapter(adapter);

        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        anonymous = preferences.getBoolean(SettingsFragment.PREF_KEY_ANONYM, false);

        sendButton.setEnabled(false);

        setUpReplyText();
        setUpSendButton();
        updateReplies();

        return view;
    }

    private void setUpReplyText() {
        replyText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.toString().length();
                sendButton.setEnabled(0 < length && length < REPLY_MAX_LENGTH);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setUpSendButton() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String senderName = anonymous ? "" : user.getFirstNames();
                String message = replyText.getText().toString();
                Timestamp time = Timestamp.now();
                String sciper = user.getSciper();
                replyText.setText("");

                Map<String, Object> data = new HashMap();
                data.put("senderName", senderName);
                data.put("message", message);
                data.put("time", time);
                data.put("sciper", sciper);
                data.put("color", postOriginal.getColor());
                data.put("nbUps", 0);
                data.put("upScipers", new ArrayList<>());
                data.put("downScipers", new ArrayList<>());

                Utils.addDataToFirebase(data, mockableCollectionReplies, TAG);

                int nbResponses = postOriginal.getNbResponses() + 1;
                mockableCollectionOriginalPost.document(postOriginal.getId()).update("nbResponses", nbResponses);

                updateReplies();
            }
        });
    }

    /**
     * Refresh the posts by reading in the database
     */
    private void updateReplies() {
        collectionReference
                .orderBy("time", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            replies.clear();
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                FirebaseMapDecorator fmap = new FirebaseMapDecorator(document);
                                if (fmap.hasFields(FIELDS)) {
                                    Post reply = new Post(fmap, user.getSciper(), postOriginal.getChannelId(), postOriginal);
                                    replies.add(reply);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

}
