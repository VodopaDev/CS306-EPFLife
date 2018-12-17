package ch.epfl.sweng.zuluzulu.structure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.epfl.sweng.zuluzulu.firebase.FirebaseMapDecorator;

/**
 * Class that represents a post in a view
 */
public class Post extends SuperMessage {

    private String color;
    private Set<String> replies;
    private Set<String> upScipers;
    private Set<String> downScipers;
    private String originalPostId;

    public Post(String id, String channelId, String originalPostId, String message, String senderName, String senderSciper, Date time, String color, List<String> replies, List<String> upScipers, List<String> downScipers) {
        super(id, channelId, message, senderName, senderSciper, time);
        this.originalPostId = originalPostId;
        this.color = color;

        this.replies = new HashSet<>(replies);
        this.upScipers = new HashSet<>(upScipers);
        this.downScipers = new HashSet<>(downScipers);
    }

    public Post(FirebaseMapDecorator data) {
        super(data);
        if (!data.hasFields(requiredFields()))
            throw new IllegalArgumentException();

        channelId = data.getString("channel_id");
        senderName = data.getString("sender_name");
        senderSciper = data.getString("sender_sciper");
        message = data.getString("message");
        time = data.getDate("time");
        color = data.getString("color");
        originalPostId = data.getString("original_post_id");
        replies = new HashSet<>(data.getStringList("replies"));
        upScipers = new HashSet<>(data.getStringList("up_scipers"));
        downScipers = new HashSet<>(data.getStringList("down_scipers"));
    }

    public static List<String> requiredFields() {
        return Arrays.asList("sender_name", "sender_sciper", "message", "time", "color", "replies", "up_scipers", "down_scipers", "id", "channel_id");
    }

    /**
     * Comparator to compare posts with number of ups
     *
     * @return Comparator to compare posts with number of ups
     */
    public static Comparator<Post> decreasingNbUpsComparator() {
        return (o1, o2) -> o2.getNbUps() - o1.getNbUps();
    }

    /**
     * Comparator to compare posts with number of replies
     *
     * @return Comparator to compare posts with number of replies
     */
    public static Comparator<Post> decreasingNbRepliesComparator() {
        return (o1, o2) -> o2.getNbReplies() - o1.getNbReplies();
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
        return upScipers.size() - downScipers.size();
    }

    /**
     * Getter for the number of responses
     *
     * @return the number of responses
     */
    public int getNbReplies() {
        return replies.size();
    }

    /**
     *
     */
    public List<String> getReplies() {
        return new ArrayList<>(replies);
    }

    public boolean addReply(String replyId) {
        return originalPostId == null && replies.add(replyId);
    }

    /**
     * Getter for upByUser
     *
     * @return whether the user has liked this post or not
     */
    public boolean isUpByUser(String userID) {
        return upScipers.contains(userID);
    }

    /**
     * Getter for downByUser
     *
     * @return whether the user has disliked this post or not
     */
    public boolean isDownByUser(String userID) {
        return downScipers.contains(userID);
    }

    public boolean upvoteWithUser(String userId) {
        return !isDownByUser(userId) && upScipers.add(userId);
    }

    public boolean downvoteWithUser(String userId) {
        return !isUpByUser(userId) && downScipers.add(userId);
    }

    public Map<String, Object> getData() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", getId());
        map.put("channel_id", channelId);
        map.put("original_post_id", originalPostId);
        map.put("sender_name", senderName);
        map.put("message", message);
        map.put("time", time);
        map.put("sender_sciper", senderSciper);
        map.put("color", color);
        map.put("replies", new ArrayList<>(replies));
        map.put("up_scipers", new ArrayList<>(upScipers));
        map.put("down_scipers", new ArrayList<>(downScipers));
        return map;
    }

    /**
     * Getter for the fact that the post is a reply or not
     *
     * @return Whether the post is a reply or not
     */
    public boolean isReply() {
        return originalPostId != null;
    }

    /**
     * Getter for the original post
     *
     * @return The original post
     */
    public String getOriginalPostId() {
        return originalPostId;
    }
}
