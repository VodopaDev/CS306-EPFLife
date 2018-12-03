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

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.sweng.zuluzulu.Adapters.ChatMessageArrayAdapter;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Channel;
import ch.epfl.sweng.zuluzulu.Structure.ChatMessage;
import ch.epfl.sweng.zuluzulu.User.User;

import static ch.epfl.sweng.zuluzulu.CommunicationTag.OPEN_POST_FRAGMENT;

/**
 * A {@link SuperChatPostsFragment} subclass.
 * This fragment is used to display the chat and to write in it
 */
public class ChatFragment extends SuperChatPostsFragment {
    private static final int MAX_MESSAGE_LENGTH = 100;
    private Button sendButton;
    private EditText textEdit;

    private List<ChatMessage> messages = new ArrayList<>();
    private ChatMessageArrayAdapter adapter;

    public ChatFragment() {
        // Required empty public constructor
    }

    public static ChatFragment newInstance(User user, Channel channel) {
        return (ChatFragment) newInstanceOf("chat", user, channel);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        sendButton = view.findViewById(R.id.chat_send_button);
        textEdit = view.findViewById(R.id.chat_message_edit);
        listView = view.findViewById(R.id.chat_list_view);
        chatButton = view.findViewById(R.id.chat_button);
        postsButton = view.findViewById(R.id.posts_button);

        chatButton.setEnabled(false);
        postsButton.setEnabled(true);

        sendButton.setEnabled(false);

        adapter = new ChatMessageArrayAdapter(view.getContext(), messages, user);
        listView.setAdapter(adapter);

        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        anonymous = preferences.getBoolean(SettingsFragment.PREF_KEY_ANONYM, false);

        loadInitialMessages();
        setUpDataOnChangeListener();
        setUpSendButton();
        setUpEditText();
        setUpPostsButton();

        return view;
    }

    /**
     * Add an onClick listener on the button to send the message to the database
     */
    private void setUpSendButton() {
        sendButton.setOnClickListener(v -> {
            ChatMessage chatMessage = new ChatMessage(
                    DatabaseFactory.getDependency().getNewMessageId(channel.getId()),
                    channel.getId(),
                    textEdit.getText().toString(),
                    Timestamp.now().toDate(),
                    anonymous ? "" : user.getFirstNames(),
                    user.getSciper());
            DatabaseFactory.getDependency().addMessage(chatMessage);
            textEdit.setText("");
        });
    }

    /**
     * Add an onClick listener on the button to switch to the posts fragment
     */
    private void setUpPostsButton() {
        postsButton.setOnClickListener(v -> mListener.onFragmentInteraction(OPEN_POST_FRAGMENT, channel));
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
                int length = text.toString().length();
                sendButton.setEnabled(0 < length && length < MAX_MESSAGE_LENGTH);
            }

            @Override
            public void afterTextChanged(Editable text) {
            }
        });
    }

    /**
     * Refresh the chat by reading all the messages in the database
     */
    private void loadInitialMessages() {
        DatabaseFactory.getDependency().getMessagesFromChannel(channel.getId(), result -> {
            Log.d("TEST", result.size() + " messages");
            messages.clear();
            messages.addAll(result);
            Collections.sort(messages, (o1, o2) -> {
                if (o1.getTime().before(o2.getTime()))
                    return -1;
                else
                    return 1;
            });
            adapter.notifyDataSetChanged();
            listView.setSelection(adapter.getCount() - 1);
        });
    }

    private void setUpDataOnChangeListener() {
        DatabaseFactory.getDependency().updateOnNewMessagesFromChannel(channel.getId(), result -> {
            messages.clear();
            messages.addAll(result);
            Collections.sort(messages, (o1, o2) -> {
                if (o1.getTime().before(o2.getTime()))
                    return -1;
                else
                    return 1;
            });
            adapter.notifyDataSetChanged();
            listView.setSelection(adapter.getCount() - 1);
        });
    }
}
