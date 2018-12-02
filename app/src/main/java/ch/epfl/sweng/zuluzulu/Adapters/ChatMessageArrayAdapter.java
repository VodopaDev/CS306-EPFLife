package ch.epfl.sweng.zuluzulu.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
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
        boolean isAnonymous = currentChatMessage.isAnonymous();

        boolean mustHideName = isOwnMessage || isAnonymous;
        View view = LayoutInflater.from(mContext).inflate(R.layout.chat_message, parent, false);

        LinearLayout linearLayout = view.findViewById(R.id.chat_message_linearLayout);
        LinearLayout messageContent = view.findViewById(R.id.chat_message_content);
        TextView message = view.findViewById(R.id.chat_message_msg);
        TextView senderName = view.findViewById(R.id.chat_message_senderName);

        int backgroundResource = isOwnMessage ? R.drawable.chat_message_background_ownmessage : R.drawable.chat_message_background;
        messageContent.setBackgroundResource(backgroundResource);
        senderName.setText(currentChatMessage.getSenderName());

        if (isOwnMessage) {
            linearLayout.setGravity(Gravity.END);
        }

        if (mustHideName) {
            senderName.setVisibility(View.GONE);
        }

        message.setText(currentChatMessage.getMessage());

        return view;
    }
}
