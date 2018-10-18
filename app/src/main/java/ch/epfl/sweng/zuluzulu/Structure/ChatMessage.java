package ch.epfl.sweng.zuluzulu.Structure;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Arrays;
import java.util.List;

public class ChatMessage {

    private static final List<String> fields = Arrays.asList("senderName", "sciper", "message", "time");
    private String senderName;
    private String sciper;
    private String message;
    private Timestamp time;
    private boolean ownMessage;

    public ChatMessage(DocumentSnapshot snap, String userSciper) {
        if (!ToolBox.isValidSnapshot(snap, fields)) {
            throw new IllegalArgumentException("This is not a chat message snapshot");
        }
        senderName = snap.getString("senderName");
        sciper = snap.getString("sciper");
        message = snap.getString("message");
        time = snap.getTimestamp("time");
        ownMessage = sciper.equals(userSciper);
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSciper() { return sciper; }

    public void setSciper(String sciper) { this.sciper = sciper; }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isOwnMessage() { return ownMessage; }

    public void setOwnMessage(boolean ownMessage) { this.ownMessage = ownMessage; }

    public Timestamp getTime() { return time; }

    public void setTime(Timestamp time) { this.time = time; }
}
