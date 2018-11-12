package ch.epfl.sweng.zuluzulu.Structure;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;

/**
 * Class that represents a post in a view
 */
public class Post {

    public static final List<String> FIELDS = Arrays.asList("senderName", "sciper", "message", "time");
    private String senderName;
    private String sciper;
    private String message;
    private Date time;
    private boolean anonym;

    public Post(FirebaseMapDecorator data) {
        senderName = data.getString("senderName");
        sciper = data.getString("sciper");
        message = data.getString("message");
        time = data.getDate("time");
        anonym = senderName.isEmpty();
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
     * Getter for post creation time
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
     * Getter for anonym
     *
     * @return Whether the post is anonymous or not
     */
    public boolean isAnonym() { return anonym; }
}
