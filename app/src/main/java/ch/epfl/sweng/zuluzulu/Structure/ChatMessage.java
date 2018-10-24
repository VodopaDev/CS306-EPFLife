package ch.epfl.sweng.zuluzulu.Structure;

import com.google.firebase.Timestamp;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Class that represents a chat message in a view
 */
public class ChatMessage {

    public static final List<String> FIELDS = Arrays.asList("senderName", "sciper", "message", "time");
    private String senderName;
    private String sciper;
    private String message;
    private Timestamp time;
    private boolean ownMessage;

    public ChatMessage(Map data, String userSciper) {
        senderName = (String) data.get("senderName");
        sciper = (String) data.get("sciper");
        message = (String) data.get("message");
        time = (Timestamp) data.get("time") ;
        ownMessage = sciper.equals(userSciper);
    }

    /**
     * Getter for the sender name
     *
     * @return the sender name
     */
    public String getSenderName() {
        return senderName;
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
    public boolean isOwnMessage() {
        return ownMessage;
    }

    /**
     * Setter for the fact that the message is viewed by his owner
     */
    public void setOwnMessage(boolean ownMessage) {
        this.ownMessage = ownMessage;
    }

    /**
     * Getter for message creation time
     *
     * @return the creation time
     */
    public Timestamp getTime() {
        return time;
    }

    /**
     * Setter for the creation time
     */
    public void setTime(Timestamp time) {
        this.time = time;
    }
}
