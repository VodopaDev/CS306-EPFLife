package ch.epfl.sweng.zuluzulu.Structure;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;

/**
 * Class that represents a post in a view
 */
public class Post {

    public static final List<String> FIELDS = Arrays.asList("senderName", "sciper", "message", "time", "color", "nbUps", "nbResponses");
    private String senderName;
    private String sciper;
    private String message;
    private Date time;
    private String color;
    private int nbUps;
    private int nbResponses;

    private boolean anonymous;

    public Post(FirebaseMapDecorator data) {
        senderName = data.getString("senderName");
        sciper = data.getString("sciper");
        message = data.getString("message");
        time = data.getDate("time");
        color = data.getString("color");
        nbUps = data.getInteger("nbUps");
        nbResponses = data.getInteger("nbResponses");

        anonymous = senderName.isEmpty();
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
     *
     * @param senderName the new senderName
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
     *
     * @param sciper the new sciper
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
     *
     * @param message the new message
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
     *
     * @param time the new time
     */
    public void setTime(Date time) {
        this.time = time;
    }

    /**
     * Getter for the color
     *
     * @return the color
     */
    public String getColor() {
        return color;
    }

    /**
     * Setter for the color
     *
     * @param color the new color
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Getter for the number of ups
     *
     * @return the number of ups
     */
    public int getNbUps() { return nbUps; }

    /**
     * Setter for the number of ups
     *
     * @param nbUps the new number of ups
     */
    public void setNbUps(int nbUps) { this.nbUps = nbUps; }

    /**
     * Getter for the number of responses
     *
     * @return the number of responses
     */
    public int getNbResponses() { return nbResponses; }

    /**
     * Setter for the number of responses
     *
     * @param nbResponses the new number of responses
     */
    public void setNbResponses(int nbResponses) { this.nbResponses = nbResponses; }

    /**
     * Getter for anonymous
     *
     * @return Whether the post is anonymous or not
     */
    public boolean isAnonymous() {
        return anonymous;
    }
}
