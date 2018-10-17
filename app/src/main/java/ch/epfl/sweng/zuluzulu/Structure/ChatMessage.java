package ch.epfl.sweng.zuluzulu.Structure;

public class ChatMessage {

    private String senderName;
    private String sciper;
    private String message;
    private boolean ownMessage;

    public ChatMessage(String senderName, String sciper, String message, boolean ownMessage) {
        this.senderName = senderName;
        this.sciper = sciper;
        this.message = message;
        this.ownMessage = ownMessage;
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
}
