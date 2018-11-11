package ch.epfl.sweng.zuluzulu.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.ChatMessage;

public class ChatMessageArrayAdapter extends ArrayAdapter<ChatMessage> {

    private Context mContext;
    private List<ChatMessage> messages;

    public ChatMessageArrayAdapter(@NonNull Context context, List<ChatMessage> list) {
        super(context, 0, list);
        mContext = context;
        messages = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ChatMessage currentChatMessage = messages.get(position);
        boolean isOwnMessage = currentChatMessage.isOwnMessage();
        boolean isAnonym = currentChatMessage.isAnonym();

        boolean mustHideName = isOwnMessage || isAnonym;
        int layoutRessource = mustHideName ? R.layout.chat_message_noname : R.layout.chat_message;
        View messageView = LayoutInflater.from(mContext).inflate(layoutRessource, parent ,false);

        LinearLayout linearLayout = messageView.findViewById(R.id.chat_message_linearLayout);
        TextView message = messageView.findViewById(R.id.chat_message_msg);

        if (mustHideName) {
            if (isOwnMessage) {
                linearLayout.setBackgroundResource(R.drawable.chat_message_background_ownmessage);
            }
        } else {
            TextView senderName = messageView.findViewById(R.id.chat_message_senderName);
            senderName.setText(currentChatMessage.getSenderName());
            linearLayout.setBackgroundResource(R.drawable.chat_message_background);
        }

        message.setText(currentChatMessage.getMessage());

        return messageView;
    }
}
