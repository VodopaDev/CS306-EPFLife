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
        boolean isAnonym = currentChatMessage.isAnonymous();

        boolean mustHideName = isOwnMessage || isAnonym;
        int layoutResource = mustHideName ? R.layout.chat_message_noname : R.layout.chat_message;
        View view = LayoutInflater.from(mContext).inflate(layoutResource, parent, false);

        LinearLayout linearLayout = view.findViewById(R.id.chat_message_linearLayout);
        TextView message = view.findViewById(R.id.chat_message_msg);

        int backgroundResource = isOwnMessage ? R.drawable.chat_message_background_ownmessage : R.drawable.chat_message_background;
        linearLayout.setBackgroundResource(backgroundResource);

        if (!mustHideName) {
            TextView senderName = view.findViewById(R.id.chat_message_senderName);
            senderName.setText(currentChatMessage.getSenderName());
        }

        message.setText(currentChatMessage.getMessage());

        return view;
    }
}
