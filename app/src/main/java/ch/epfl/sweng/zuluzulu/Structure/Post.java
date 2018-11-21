package ch.epfl.sweng.zuluzulu.Structure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;

/**
 * Class that represents a post in a view
 */
public class Post {

    public static final List<String> FIELDS = Arrays.asList("senderName", "sciper", "message", "time", "color", "nbUps", "nbResponses", "upScipers", "downScipers");
    String id;
    private String senderName;
    private String sciper;
    private String message;
    private Date time;
    private String color;
    private int nbUps;
    private int nbResponses;
    private List<String> upScipers;
    private List<String> downScipers;
    private boolean upByUser;
    private boolean downByUser;

    private boolean anonymous;
    private int channelId;
    private String userSciper;

    public Post(FirebaseMapDecorator data, String userSciper, int channelId) {
        senderName = data.getString("senderName");
        sciper = data.getString("sciper");
        message = data.getString("message");
        time = data.getDate("time");
        color = data.getString("color");
        nbUps = data.getInteger("nbUps");
        nbResponses = data.getInteger("nbResponses");
        upScipers = data.getStringList("upScipers");
        downScipers = data.getStringList("downScipers");
        id = data.getId();

        anonymous = senderName.isEmpty();
        this.channelId = channelId;
        this.userSciper = userSciper;

        upByUser = upScipers.contains(userSciper);
        downByUser = downScipers.contains(userSciper);

        if (upByUser && downByUser) {
            throw new IllegalStateException("A post cannot be liked and disliked at the same time");
        }
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
     * Getter for the sciper
     *
     * @return the sciper
     */
    public String getSciper() {
        return sciper;
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
     * Getter for post creation time
     *
     * @return the creation time
     */
    public Date getTime() {
        return time;
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
     * Getter for the number of ups
     *
     * @return the number of ups
     */
    public int getNbUps() {
        return nbUps;
    }

    /**
     * Getter for the number of responses
     *
     * @return the number of responses
     */
    public int getNbResponses() {
        return nbResponses;
    }

    /**
     * Getter for anonymous
     *
     * @return Whether the post is anonymous or not
     */
    public boolean isAnonymous() {
        return anonymous;
    }

    /**
     * Getter for upByUser
     *
     * @return whether the user has liked this post or not
     */
    public boolean isUpByUser() {
        return upByUser;
    }

    /**
     * Setter of upByUser
     *
     * @param upByUser the new upByUser value
     */
    public void setUpByUser(boolean upByUser) {
        if (downByUser) {
            throw new IllegalArgumentException("This post is already up by the user");
        }
        this.upByUser = upByUser;
    }

    /**
     * Getter for downByUser
     *
     * @return whether the user has disliked this post or not
     */
    public boolean isDownByUser() {
        return downByUser;
    }

    /**
     * Setter for downByUser
     *
     * @param downByUser the new downByUser value
     */
    public void setDownByUser(boolean downByUser) {
        if (upByUser) {
            throw new IllegalArgumentException("This post is already down by the user");
        }
        this.downByUser = downByUser;
    }

    /**
     * Getter for the channelId of the post
     *
     * @return the channelId
     */
    public int getChannelId() {
        return channelId;
    }

    /**
     * Getter for the user reading the post
     *
     * @return the user reading the post
     */
    public String getUserSciper() {
        return userSciper;
    }

    /**
     * Getter for the up scipers
     *
     * @return the up scipers
     */
    public List<String> getUpScipers() {
        return new ArrayList<>(Collections.unmodifiableCollection(upScipers));
    }

    /**
     * Getter for the down scipers
     *
     * @return the down scipers
     */
    public List<String> getDownScipers() {
        return new ArrayList<>(Collections.unmodifiableCollection(downScipers));
    }

    /**
     * Getter for the id
     *
     * @return the id
     */
    public String getId() {
        return id;
    }
}
