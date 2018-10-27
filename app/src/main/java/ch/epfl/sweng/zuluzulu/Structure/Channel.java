package ch.epfl.sweng.zuluzulu.Structure;

import android.location.Location;

import com.google.firebase.firestore.GeoPoint;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

    public Channel(Map data) {
        this.id = ((Long) data.get("id")).intValue();
        this.name = (String) data.get("name");
        this.description = (String) data.get("description");
        this.restrictions = (Map) data.get("restrictions");
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
    public boolean canBeAccessedBy(AuthenticatedUser user, Location userLocation) {
        boolean hasAccess = true;
        String section = (String) restrictions.get("section");
        GeoPoint channelLocation = (GeoPoint) restrictions.get("location");
        if (section != null) {
            hasAccess = section.equals(user.getSection());
        }
        if (channelLocation != null) {
            double userLatitude = userLocation.getLatitude();
            double userLongitude = userLocation.getLongitude();
            GeoPoint userPoint = new GeoPoint(userLatitude, userLongitude);

            double distance = Utils.distanceBetween(channelLocation, userPoint);
            System.out.println(getName());
            System.out.println("User: (" + userLatitude + ", " + userLongitude + ")");
            System.out.println("Channel: (" + channelLocation.getLatitude() + ", " + channelLocation.getLongitude() + ")");
            System.out.println("Distance: " + distance);
            hasAccess = hasAccess && distance < MAX_DISTANCE;
        }
        return hasAccess;
    }
}
