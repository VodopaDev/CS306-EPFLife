package ch.epfl.sweng.zuluzulu.Structure;

import java.sql.Time;
import java.util.Comparator;
import java.util.Date;

import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;

public abstract class SuperMessage extends FirebaseStructure {

    protected String senderName;
    protected String senderSciper;
    protected String message;
    protected Date time;
    protected String channelId;

    public SuperMessage(String id, String channelId, String message, String senderName, String senderSciper, Date time) {
        super(id);
        this.channelId = channelId;
        this.message = message;
        this.senderName = senderName;
        this.senderSciper = senderSciper;
        this.time = time;
    }

    public SuperMessage(FirebaseMapDecorator data) {
        super(data);
    }

    public boolean isAnonymous() {
        return senderName.isEmpty();
    }

    public boolean isOwnMessage(String readerSciper) {
        return senderSciper.equals(readerSciper);
    }

    public String getMessage() {
        return message;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getSenderSciper() {
        return senderSciper;
    }

    public Date getTime() {
        return time;
    }

    public String getChannelId() {
        return channelId;
    }

    /**
     * Comparator to compare messages with time
     *
     * @return Comparator to compare messages with time
     */
    public static Comparator<? extends SuperMessage> decreasingTimeComparator() {
        return (Comparator<SuperMessage>) (o1, o2) -> o2.getTime().compareTo(o1.getTime());
    }
}
