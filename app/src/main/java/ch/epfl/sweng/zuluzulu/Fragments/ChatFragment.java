package ch.epfl.sweng.zuluzulu.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import ch.epfl.sweng.zuluzulu.Adapters.ChatMessageAdapter;
import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.OnFragmentInteractionListener;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.Structure.ChatMessage;
import ch.epfl.sweng.zuluzulu.Structure.User;
import ch.epfl.sweng.zuluzulu.Structure.Utils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {
    public static final String TAG = "CHAT_TAG";
    private static final String ARG_USER = "ARG_USER";
    private static final String ARG_CHANNEL_ID = "ARG_CHANNEL_ID";

    private static final String CHANNEL_DOCUMENT_NAME = "channels/channel";
    private static final String MESSAGES_COLLECTION_NAME = "messages";

    private FirebaseFirestore db;

    private Button sendButton;
    private EditText textEdit;
    private ListView listView;
    private List<ChatMessage> messages = new ArrayList<>();
    private ChatMessageAdapter adapter;
    private String collection_path;

    private User user;
    private int channelID;

    private OnFragmentInteractionListener mListener;

    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ChatFragment.
     */
    public static ChatFragment newInstance(User user, int channelID) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        args.putInt(ARG_CHANNEL_ID, channelID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (AuthenticatedUser) getArguments().getSerializable(ARG_USER);
            channelID = getArguments().getInt(ARG_CHANNEL_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        sendButton = view.findViewById(R.id.chat_send_button);
        textEdit = view.findViewById(R.id.chat_message_edit);
        listView = view.findViewById(R.id.chat_list_view);

        collection_path = CHANNEL_DOCUMENT_NAME + channelID + "/" + MESSAGES_COLLECTION_NAME;

        adapter = new ChatMessageAdapter(view.getContext(), messages);
        listView.setAdapter(adapter);

        setUpDataOnChangeListener();
        setUpSendButton();
        setUpEditText();

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

    /**
     * Add a onEventChange listener on the message list in the database
     */
    private void setUpDataOnChangeListener() {
        db = FirebaseFirestore.getInstance();
        db.collection(collection_path)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        updateChat();
                    }
                });
    }

    /**
     * Add an onClick listener on the button to send the message to the database
     */
    private void setUpSendButton() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String senderName = user.isConnected() ? user.getFirstNames() : "Guest";
                String message = textEdit.getText().toString();
                Timestamp time = Timestamp.now();
                String sciper = user.isConnected() ? user.getSciper() : "000000";

                Map<String, Object> data = new HashMap<>();
                data.put("senderName", senderName);
                data.put("message", message);
                data.put("time", time);
                data.put("sciper", sciper);

                addDataToFirestore(data);
            }
        });
    }

    /**
     * Send the data to the database
     *
     * @param data the data to send to the database
     */
    private void addDataToFirestore(Map data) {
        db.collection(collection_path)
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference ref) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + ref.getId());
                        textEdit.setText("");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);
            }
        });
    }

    /**
     * Add a listener on the edit text view to check that the message is not empty
     */
    private void setUpEditText() {
        textEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                sendButton.setEnabled(count > 0);
            }

            @Override
            public void afterTextChanged(Editable text) {
            }
        });
    }

    /**
     * Refresh the chat by reading all the messages in the database
     */
    private void updateChat() {
        db.collection(collection_path)
                .orderBy("time", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            messages.clear();
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                FirebaseMapDecorator fmap = new FirebaseMapDecorator(document);
                                if (fmap.hasFields(ChatMessage.FIELDS)) {
                                    ChatMessage message = new ChatMessage(fmap, user.getSciper());
                                    messages.add(message);
                                }
                            }
                            adapter.notifyDataSetChanged();
                            listView.setSelection(adapter.getCount() - 1);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
