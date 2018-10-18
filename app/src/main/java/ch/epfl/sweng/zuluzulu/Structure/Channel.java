package ch.epfl.sweng.zuluzulu.Structure;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Arrays;
import java.util.List;

public class Channel {

    private static final List<String> fields = Arrays.asList("id", "name", "description");
    private int id;
    private String name;
    private String description;

    public Channel(DocumentSnapshot snap) {
        if (!Utils.isValidSnapshot(snap, fields)) {
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
}
