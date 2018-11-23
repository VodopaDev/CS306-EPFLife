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
    private String sciper;
    private String message;
    private Date time;
    private String channelId;

    public ChatMessage(FirebaseMapDecorator data) {
        super(data);
        if(!data.hasFields(requiredFields()))
            throw new IllegalArgumentException();

        senderName = data.getString("sender_name");
        sciper = data.getString("sender_sciper");
        message = data.getString("message");
        time = data.getDate("time");
        channelId = data.getString("channel_id");
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
     * Setter for the sender name
     */
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    /**
     * Getter for the sciper
     *
     * @return the sciper
     */
    public String getSciper() {
        return sciper;
    }

    /**
     * Setter for the sciper
     */
    public void setSciper(String sciper) {
        this.sciper = sciper;
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
     * Setter for the message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Getter for the fact the message is viewed by his owner
     *
     * @return whether the message is viewed by his owner or not
     */
    public boolean isOwnMessage(String sciper) {
        return sciper.equals(this.sciper);
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
     * Setter for the creation time
     */
    public void setTime(Date time) {
        this.time = time;
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
        map.put("sender_sciper", sciper);
        map.put("sender_name", senderName);
        map.put("message", message);
        map.put("time", time);
        return map;
    }

    public static List<String> requiredFields(){
        return Arrays.asList("sender_name", "sender_sciper", "message", "time", "id", "channel_id");
    }
}
