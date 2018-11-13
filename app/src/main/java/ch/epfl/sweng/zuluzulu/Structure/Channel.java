package ch.epfl.sweng.zuluzulu.Structure;

import android.net.Uri;
import android.support.annotation.Nullable;

import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.R;

/**
 * Class that represents a channel in a view
 */
public class Channel implements Serializable {

    public static final List<String> FIELDS = Arrays.asList("id", "name", "description", "restrictions");
    private static final double MAX_DISTANCE_TO_ACCESS_CHANNEL = 30;
    private static final double MAX_DISTANCE_TO_SEE_CHANNEL = 500;
    private int id;
    private String name;
    private String description;
    private Map<String, Object> restrictions;

    private Uri icon_uri;

    private boolean isClickable;

    public Channel(FirebaseMapDecorator data) {
        if (!data.hasFields(FIELDS))
            throw new IllegalArgumentException();

        this.id = data.getInteger("id");
        this.name = data.getString("name");
        this.description = data.getString("description");
        this.restrictions = data.getMap("restrictions");
        this.isClickable = true;

        String icon_str = data.getString("icon_uri");
        icon_uri = icon_str == null ?
                Uri.parse("android.resource://ch.epfl.sweng.zuluzulu/" + R.drawable.default_icon) :
                Uri.parse(icon_str);
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
     * Return the Association's icon Uri
     *
     * @return the icon Uri
     */
    @Nullable
    public Uri getIconUri() {
        return icon_uri;
    }

    public boolean isClickable() { return isClickable; }

    /**
     * Check whether a user can access to this channel or not
     *
     * @param user The user who wants to enter the channel
     * @return whether the user can access it or not
     */
    public boolean canBeSeenBy(AuthenticatedUser user, GeoPoint userLocation) {
        boolean isVisible = true;
        String section = (String) restrictions.get("section");
        GeoPoint channelLocation = (GeoPoint) restrictions.get("location");
        if (section != null) {
            isVisible = section.equals(user.getSection());
        }
        if (channelLocation != null) {
            if (userLocation == null) {
                isClickable = false;
                return false;
            }
            double distance = Utils.distanceBetween(channelLocation, userLocation);
            double diff_distance = distance - MAX_DISTANCE_TO_ACCESS_CHANNEL;
            if (diff_distance > MAX_DISTANCE_TO_SEE_CHANNEL) {
                isClickable = false;
            }
            isVisible = isVisible && distance < MAX_DISTANCE_TO_ACCESS_CHANNEL;
        }
        isClickable = isClickable && isVisible;
        return isVisible;
    }
}
