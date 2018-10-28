package ch.epfl.sweng.zuluzulu.Structure;

import android.util.Log;

import com.google.firebase.firestore.GeoPoint;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;

/**
 * Class that represents a channel in a view
 */
public class Channel {

    public static final List<String> FIELDS = Arrays.asList("id", "name", "description", "restrictions");
    private static final double MAX_DISTANCE = 3.0e-3;
    private int id;
    private String name;
    private String description;
    private Map<String, Object> restrictions;

    public Channel(FirebaseMapDecorator data) {
        this.id = data.getInteger("id");
        this.name = data.getString("name");
        this.description = data.getString("description");
        this.restrictions = data.getMap("restrictions");
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

    /**
     * Check whether a user can access to this channel or not
     *
     * @param user The user who wants to enter the channel
     * @return whether the user can access it or not
     */
    public boolean canBeAccessedBy(AuthenticatedUser user, GeoPoint userLocation) {
        boolean hasAccess = true;
        String section = (String) restrictions.get("section");
        GeoPoint channelLocation = (GeoPoint) restrictions.get("location");
        if (section != null) {
            hasAccess = section.equals(user.getSection());
        }
        if (channelLocation != null) {
            if (userLocation == null) {
                return false;
            }
            double distance = Utils.distanceBetween(channelLocation, userLocation);
            Log.d("Channel", getName());
            Log.d("Distance: ", "" + distance);
            hasAccess = hasAccess && distance < MAX_DISTANCE;
        }
        return hasAccess;
    }
}
