package ch.epfl.sweng.zuluzulu.Structure;

import com.google.firebase.firestore.DocumentSnapshot;

public class ChatMessage {

    private String senderName;
    private String sciper;
    private String message;
    private boolean ownMessage;

    public ChatMessage(DocumentSnapshot snap, String userSciper) {
        if (!snapshotIsValid(snap)) {
            throw new IllegalArgumentException("This is not a chat message snapshot");
        }
        senderName = snap.getString("senderName");
        sciper = snap.getString("sciper");
        message = snap.getString("message");
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

    /**
     * Check if a DocumentSnapshot corresponds to a chat message
     * @param snap the DocumentSnapshot
     * @return true if it is a valid snapshot, false otherwise
     */
    private boolean snapshotIsValid(DocumentSnapshot snap){
        return !(snap == null
                || snap.getString("senderName") == null
                || snap.getString("sciper") == null
                || snap.getString("message") == null
        );
    }
}
