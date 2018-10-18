package ch.epfl.sweng.zuluzulu.Structure;

import com.google.firebase.firestore.DocumentSnapshot;

public class Channel {

    private int id;
    private String name;
    private String description;

    public Channel(DocumentSnapshot snap) {
        if (!snapshotIsValid(snap)) {
            throw new IllegalArgumentException("This is not a channel snapshot");
        }
        this.id = snap.getLong("id").intValue();
        this.name = snap.getString("name");
        this.description = snap.getString("description");
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Check if a DocumentSnapshot corresponds to a channel
     * @param snap the DocumentSnapshot
     * @return true if it is a valid snapshot, false otherwise
     */
    private boolean snapshotIsValid(DocumentSnapshot snap){
        return !(snap == null
                || snap.getLong("id") == null
                || snap.getString("name") == null
                || snap.getString("description") == null
        );
    }
}
