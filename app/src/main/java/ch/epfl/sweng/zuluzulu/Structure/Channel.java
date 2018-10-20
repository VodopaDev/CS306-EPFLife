package ch.epfl.sweng.zuluzulu.Structure;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Arrays;
import java.util.List;

/**
 * Class that represents a channel in a view
 */
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

    /**
     * Getter for the id
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Setter for the id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter for the name
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for the description
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for the description
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
