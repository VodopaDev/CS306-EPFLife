package ch.epfl.sweng.zuluzulu.Structure;

import android.net.Uri;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.R;

/**
 * A simple class describing an Association
 * Has diverse getters and some functions to create views
 */
public class Association extends SuperStructure{
    public final static List<String> REQUIRED_FIELDS = SuperStructure.REQUIRED_FIELDS;

    private Uri bannerUri;
    private List<Map<String, Object>> registeredEvents;
    private String longDesc;
    private Long mainChannelId;
    private Long closestEventId;

    /**
     * Create an association using a Firebase adapted map
     *
     * @param data the adapted map containing the association data
     * @throws IllegalArgumentException if the map isn't an Association's map
     */
    public Association(FirebaseMapDecorator data) {
        super(data);
        if (!data.hasFields(REQUIRED_FIELDS)) {
            throw new IllegalArgumentException();
        }

        longDesc = data.getString("long_desc");

        // Init the main chat id
        mainChannelId = data.get("channel_id") == null ?
                0L :
                data.getLong("channel_id");

        // Init the upcoming event
        registeredEvents = data.get("registeredEvents") == null ?
                Collections.EMPTY_LIST :
                (List<Map<String, Object>>) data.get("registeredEvents");
        computeClosestEvent();

        // Init the Banner URI
        String banner_str = data.getString("bannerUri");
        bannerUri = banner_str == null ?
                Uri.parse("android.resource://ch.epfl.sweng.zuluzulu/" + R.drawable.default_banner) :
                Uri.parse(banner_str);
    }

    /**
     * Return a Comparator for two Associations using their names
     * @return compareTo of two Associations names
     */
    public static Comparator<Association> getComparator() {
        return new Comparator<Association>() {
            @Override
            public int compare(Association o1, Association o2) {
                return o1.getName().compareTo(o2.getName());
            }
        };
    }

    /**
     * Return the association's long description
     * @return the association's long description
     */
    public String getLongDesc() {
        return longDesc;
    }

    /**
     * Set the association's long description
     * @param newLongDesc new long description to use
     * @return the new structure's icon uri
     */
    public String setLongDesc(String newLongDesc){
        if(newLongDesc == null)
            throw new NullPointerException("newLongDesc must be initialized");
        else
            longDesc = newLongDesc;
        return longDesc;
    }


    /**
     * Return the association's main chat id
     * @return the association's main chat id
     */
    public Long getMainChannelId() {
        return mainChannelId;
    }

    /**
     * Set the association's main channel id
     * @param newMainChannelId new icon uro to use
     * @return the new structure's icon uri
     */
    public Long setMainChannelId(Long newMainChannelId){
        if(mainChannelId == null)
            throw new NullPointerException("newMainChannelId must be initialized");
        else
            mainChannelId = newMainChannelId;
        return mainChannelId;
    }

    /**
     * Return the association's closest event happening id
     * @return the association's closest event happening id
     */
    public Long getClosestEventId() {
        return closestEventId;
    }

    /**
     * Set the association's main channel id
     * @param newClosestEventId new icon uro to use
     * @return the new structure's icon uri
     */
    public Long setClosestEventId(Long newClosestEventId){
        if(mainChannelId == null)
            throw new NullPointerException("newClosestEventId must be initialized");
        else
            closestEventId = newClosestEventId;
        return closestEventId;
    }

    /**
     * Return the association's banner uri
     * @return the association's banner uri
     */
    public Uri getBannerUri() {
        return bannerUri;
    }

    /**
     * Set the association's main channel id
     * @param newBannerUri new icon uro to use
     * @return the new structure's icon uri
     */
    public Uri setBannerUri(Uri newBannerUri){
        if(newBannerUri == null)
            throw new NullPointerException("newBannerUri must be initialized");
        else
            bannerUri = newBannerUri;
        return bannerUri;
    }

    /**
     * Compute the closest event id and store it in the class
     */
    private void computeClosestEvent() {
        if (registeredEvents.isEmpty())
            closestEventId = 0L;
        else {
            Long closest = (Long)registeredEvents.get(0).get("id");
            java.util.Date closest_time = (java.util.Date) registeredEvents.get(0).get("start");
            for (int i = 1; i < registeredEvents.size(); i++) {
                java.util.Date current = (java.util.Date) registeredEvents.get(i).get("start");
                if (current.before(closest_time)) {
                    closestEventId = (Long)registeredEvents.get(i).get("id");
                }
            }
            closestEventId = closest;
        }
    }
}
