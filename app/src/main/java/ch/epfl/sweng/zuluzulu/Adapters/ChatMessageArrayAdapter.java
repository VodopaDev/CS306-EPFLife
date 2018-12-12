package ch.epfl.sweng.zuluzulu.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.ChatMessage;
import ch.epfl.sweng.zuluzulu.Structure.SuperMessage;
import ch.epfl.sweng.zuluzulu.User.User;

public class ChatMessageArrayAdapter extends ArrayAdapter<SuperMessage> {

    private Context mContext;
    private List<SuperMessage> messages;
    private User user;

    public ChatMessageArrayAdapter(@NonNull Context context, List<SuperMessage> list, User user) {
        super(context, 0, list);
        mContext = context;
        messages = list;
        this.user = user;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ChatMessage currentChatMessage = (ChatMessage) messages.get(position);
        boolean isOwnMessage = currentChatMessage.isOwnMessage(user.getSciper());
        boolean isAnonymous = currentChatMessage.isAnonymous();

        boolean mustHideName = isOwnMessage || isAnonymous;
        View view = LayoutInflater.from(mContext).inflate(R.layout.chat_message, parent, false);

        LinearLayout linearLayout = view.findViewById(R.id.chat_message_linearLayout);
        LinearLayout messageContent = view.findViewById(R.id.chat_message_content);
        TextView message = view.findViewById(R.id.chat_message_msg);
        TextView senderName = view.findViewById(R.id.chat_message_senderName);
        TextView timeView = view.findViewById(R.id.chat_message_time);

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

        Date time = currentChatMessage.getTime();
        setUpTimeView(timeView, time);

        return view;
    }

    /**
     * Set up the correct time field on each message
     *
     * @param timeView The view to set up
     * @param time     The time of the message
     */
    private void setUpTimeView(TextView timeView, Date time) {
        boolean sameDay = DateUtils.isToday(time.getTime());
        if (sameDay) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(time);
            String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
            String minute = String.valueOf(calendar.get(Calendar.MINUTE));
            if (hour.length() < 2) {
                hour = "0" + hour;
            }
            if (minute.length() < 2) {
                minute = "0" + minute;
            }
            timeView.setText(hour + ":" + minute);
        } else {
            timeView.setVisibility(View.GONE);
        }
    }
}
