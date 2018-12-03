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
public class ChatMessage extends FirebaseStructure{
    private String senderName;
    private String senderSciper;
    private String message;
    private Date time;
    private String channelId;

    public ChatMessage(FirebaseMapDecorator data) {
        super(data);
        if(!data.hasFields(requiredFields()))
            throw new IllegalArgumentException();

        senderName = data.getString("sender_name");
        senderSciper = data.getString("sender_sciper");
        message = data.getString("message");
        time = data.getDate("time");
        channelId = data.getString("channel_id");
    }

    public ChatMessage(String id, String channelId, String message, Date time, String senderName, String senderSciper){
        super(id);
        this.channelId = channelId;
        this.message = message;
        this.time = time;
        this.senderName = senderName;
        this.senderSciper = senderSciper;
    }

    /**
     * Getter for the sender name
     *
     * @return the sender name
     */
    public String getSenderName() {
        return senderName;
    }

    public String getChannelId(){
        return channelId;
    }

    /**
     * Getter for the senderSciper
     *
     * @return the senderSciper
     */
    public String getSenderSciper() {
        return senderSciper;
    }

    /**
     * Getter for the message
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Getter for the fact the message is viewed by his owner
     *
     * @return whether the message is viewed by his owner or not
     */
    public boolean isOwnMessage(String sciper) {
        return sciper.equals(senderSciper);
    }

    /**
     * Getter for message creation time
     *
     * @return the creation time
     */
    public Date getTime() {
        return time;
    }

    /**
     * Getter for anonymous
     *
     * @return Whether the message is anonymous or not
     */
    public boolean isAnonymous() {
        return senderName.isEmpty();
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

    public static List<String> requiredFields(){
        return Arrays.asList("sender_name", "sender_sciper", "message", "time", "id", "channel_id");
    }
}
