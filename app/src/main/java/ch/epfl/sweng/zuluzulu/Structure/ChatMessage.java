package ch.epfl.sweng.zuluzulu.Structure;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;

/**
 * Class that represents a chat message in a view
 */
public class ChatMessage extends SuperMessage {

    public ChatMessage(FirebaseMapDecorator data) {
        super(data);
        if (!data.hasFields(requiredFields()))
            throw new IllegalArgumentException();

        senderName = data.getString("sender_name");
        senderSciper = data.getString("sender_sciper");
        message = data.getString("message");
        time = data.getDate("time");
        channelId = data.getString("channel_id");
    }

    public ChatMessage(String id, String channelId, String message, Date time, String senderName, String senderSciper) {
        super(id, channelId, message, senderName, senderSciper, time);
        this.channelId = channelId;
        this.message = message;
        this.time = time;
        this.senderName = senderName;
        this.senderSciper = senderSciper;
    }

    public static List<String> requiredFields() {
        return Arrays.asList("sender_name", "sender_sciper", "message", "time", "id", "channel_id");
    }

    @Override
    public Map<String, Object> getData() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", getId());
        map.put("channel_id", channelId);
        map.put("sender_sciper", senderSciper);
        map.put("sender_name", senderName);
        map.put("message", message);
        map.put("time", time);
        return map;
    }
}
