package ch.epfl.sweng.zuluzulu.Structure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;

/**
 * Class that represents a post in a view
 */
public class Post extends FirebaseStructure{

    private String senderName;
    private String sciper;
    private String message;
    private Date time;
    private String color;
    private int nbUps;
    private int nbResponses;
    private List<String> upScipers;
    private List<String> downScipers;
    private String channelId;

    //TODO: fill the constructor
    public Post(String id){
        super(id);
    }

    public Post(FirebaseMapDecorator data) {
        super(data);
        if(!data.hasFields(requiredFields()))
            throw new IllegalArgumentException();

        channelId = data.getString("channel_id");
        senderName = data.getString("sender_name");
        sciper = data.getString("sciper");
        message = data.getString("message");
        time = data.getDate("time");
        color = data.getString("color");
        nbUps = data.getInteger("nb_ups");
        nbResponses = data.getInteger("nb_responses");
        upScipers = data.getStringList("up_scipers");
        downScipers = data.getStringList("down_scipers");
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
        return senderName.isEmpty();
    }

    /**
     * Getter for upByUser
     *
     * @return whether the user has liked this post or not
     */
    public boolean isUpByUser(String userID) { return upScipers.contains(userID); }

    /**
     * Getter for downByUser
     *
     * @return whether the user has disliked this post or not
     */
    public boolean isDownByUser(String userID) { return downScipers.contains(userID); }

    public String getChannelId(){
        return channelId;
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

    public boolean upvoteWithUser(String userId){
        return !downScipers.contains(userId) && upScipers.add(userId);
    }

    public boolean downvoteWithUser(String userId){
        return !upScipers.contains(userId) && downScipers.add(userId);
    }

    public static List<String> requiredFields(){
        return Arrays.asList("sender_name", "sciper", "message", "time", "color", "nb_ups", "nb_responses", "up_scipers", "down_scipers", "id", "channel_id");
    }

    public Map<String, Object> getData() {
        Map<String, Object> map = new HashMap<>();
        map.put("channel_id", channelId);
        map.put("sender_name", senderName);
        map.put("message", message);
        map.put("time", time);
        map.put("sciper", sciper);
        map.put("color", color);
        map.put("nb_ups", nbUps);
        map.put("nb_responses", nbResponses);
        map.put("up_scipers", upScipers);
        map.put("down_scipers", downScipers);
        return map;
    }
}
