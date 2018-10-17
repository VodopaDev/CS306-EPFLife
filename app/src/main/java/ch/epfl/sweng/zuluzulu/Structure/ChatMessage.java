package ch.epfl.sweng.zuluzulu.Structure;

public class ChatMessage {

    private String senderName;
    private int sciper;
    private String message;

    public ChatMessage(String senderName, int sciper, String message) {
        this.senderName = senderName;
        this.sciper = sciper;
        this.message = message;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public int getSciper() { return sciper; }

    public void setSciper(int sciper) { this.sciper = sciper; }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
