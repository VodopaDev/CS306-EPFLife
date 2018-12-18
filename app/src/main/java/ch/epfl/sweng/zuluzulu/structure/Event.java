package ch.epfl.sweng.zuluzulu.structure;

import android.net.Uri;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.epfl.sweng.zuluzulu.firebase.FirebaseMapDecorator;

public class Event extends FirebaseStructure {
    private final String name;
    private final String shortDescription;
    private final String longDescription;
    private final String channelId;
    private final String associationId;

    private final EventDate date;
    private final String organizer;
    private final String place;
    private final String bannerUri;
    private final String iconUri;
    private final String url_place_and_room;
    private final String website;
    private final String contact;
    private final String category;
    private final String speaker;

    private final Set<String> followers;

    protected Event(String id, String name, String shortDesc, String longDesc, String channelId, String associationId, EventDate date,
                    List<String> followers, String organizer, String place, String iconUri, String bannerUri,
                    String url_place_and_room, String website, String contact, String category, String speaker) {
        super(id);
        this.name = name;
        this.channelId = channelId;
        this.associationId = associationId;
        this.shortDescription = shortDesc;
        this.longDescription = longDesc;
        this.date = date;
        this.followers = new HashSet<>(followers);
        this.organizer = organizer;
        this.place = place;
        this.bannerUri = bannerUri;
        this.iconUri = iconUri;
        this.website = website;
        this.url_place_and_room = url_place_and_room;
        this.contact = contact;
        this.category = category;
        this.speaker = speaker;
    }

    /**
     * Create an event using a FirebaseMap
     *
     * @param data the FirebaseMap
     * @throws IllegalArgumentException if the FirebaseMap isn't an Event's FirebaseMap
     */
    public Event(FirebaseMapDecorator data) {
        super(data);
        if (!data.hasFields(requiredFields()))
            throw new IllegalArgumentException();

        name = data.getString("name");
        shortDescription = data.getString("short_description");
        longDescription = data.getString("long_description");
        channelId = data.getString("channel_id");
        associationId = data.getString("association_id");
        followers = new HashSet<>(data.getStringList("followers"));
        organizer = data.getString("organizer");
        place = data.getString("place");
        date = new EventDate(data.getDate("start_date"), data.getDate("end_date"));

        url_place_and_room = (data.getString("url_place_and_room"));
        website = (data.getString("website"));
        contact = (data.getString("contact"));
        category = (data.getString("category"));
        speaker = (data.getString("speaker"));

        iconUri = data.getString("icon_uri");
        bannerUri = data.getString("banner_uri");
    }

    /**
     * Return a name-increasing comparator
     *
     * @return name-increasing comparator
     */
    public static Comparator<Event> nameComparator() {
        return (o1, o2) -> o1.getName().compareTo(o2.getName());
    }

    /**
     * Return a date-increasing comparator
     *
     * @return date-increasing comparator
     */
    public static Comparator<Event> dateComparator() {
        return (o1, o2) -> o1.date.getStartDate().compareTo(o2.date.getStartDate());
    }

    /**
     * Return a like-increasing comparator
     *
     * @return like-increasing comparator
     */
    public static Comparator<Event> likeComparator() {
        return (o1, o2) -> o2.getLikes().compareTo(o1.getLikes());
    }

    public static List<String> requiredFields() {
        return Arrays.asList(
                "id", "name", "short_description", "long_description", "category", "icon_uri",
                "banner_uri", "followers", "channel_id", "association_id", "start_date", "end_date",
                "place", "organizer", "url_place_and_room", "website", "contact", "speaker");
    }

    /**
     * Return the event's association id
     *
     * @return event's association id
     */
    public String getAssociationId() {
        return associationId;
    }

    /**
     * Return the event's name
     *
     * @return event's name
     */
    public String getName() {
        return name;
    }

    /**
     * Return the event's short description
     *
     * @return event's short description
     */
    public String getShortDescription() {
        return shortDescription;
    }

    /**
     * Return the event's long description
     *
     * @return event's long description
     */
    public String getLongDescription() {
        return longDescription;
    }

    /**
     * Return the event's channel id
     *
     * @return event's channel id
     */
    public String getChannelId() {
        return channelId;
    }

    /**
     * Return the Association's icon Uri
     *
     * @return the icon Uri
     */
    public Uri getIconUri() {
        return Uri.parse(iconUri);
    }

    /**
     * Return the Association's banner Uri
     *
     * @return the banner Uri
     */
    public Uri getBannerUri() {
        return Uri.parse(bannerUri);
    }

    /**
     * Return the event's organizer
     *
     * @return event's organizer
     */
    public String getOrganizer() {
        return organizer;
    }

    /**
     * Return the event's place
     *
     * @return event's place
     */
    public String getPlace() {
        return place;
    }

    /**
     * Return the event's likes
     *
     * @return event's likes
     */
    public Integer getLikes() {
        return followers.size();
    }

    /**
     * Add a user to the event followers
     *
     * @param userId user to add to followers
     * @return true if the user wasn't already in the event's followers
     */
    public boolean addFollower(String userId) {
        return followers.add(userId);
    }

    /**
     * Remove a user from the event followers
     *
     * @param userId user to remove from followers
     * @return true if the user was already in the event's followers
     */
    public boolean removeFollower(String userId) {
        return followers.remove(userId);
    }

    public Map<String, Object> getData() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", getId());
        map.put("name", name);
        map.put("short_description", shortDescription);
        map.put("long_description", longDescription);
        map.put("category", category);

        map.put("icon_uri", iconUri);
        map.put("banner_uri", bannerUri);

        map.put("followers", new ArrayList<>(followers));
        map.put("channel_id", channelId);
        map.put("association_id", associationId);

        map.put("start_date", date.getStartDate());
        map.put("end_date", date.getEndDate());
        map.put("place", place);
        map.put("organizer", organizer);
        map.put("url_place_and_room", url_place_and_room);
        map.put("website", website);
        map.put("contact", contact);
        map.put("speaker", speaker);
        return map;
    }

    /**
     * Return the event's url_place_and_room
     *
     * @return event's url_place_and_room
     */
    public String getUrlPlaceAndRoom() {
        return url_place_and_room;
    }

    /**
     * Return the event's website
     *
     * @return event's website
     */
    public String getWebsite() {
        return website;
    }

    /**
     * Return the event's contact
     *
     * @return event's contact
     */
    public String getContact() {
        return contact;
    }

    /**
     * Return the event's category
     *
     * @return event's category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Return the event's speaker
     *
     * @return event's speaker
     */
    public String getSpeaker() {
        return speaker;
    }

    /**
     * Return the event's starting date
     *
     * @return event's starting date
     */
    public Date getStartDate() {
        return date.getStartDate();
    }

    /**
     * Return the event's ending date
     *
     * @return event's ending date
     */
    public Date getEndDate() {
        return date.getEndDate();
    }

    /**
     * Return the event's starting date as a string
     *
     * @return event's starting date as a string
     */
    public String getStartDateString() {
        return date.getStartDateString();
    }

    /**
     * Return the event's ending date as a string
     *
     * @return event's ending date as a string
     */
    public String getEndDateString() {
        return date.getEndDateString();
    }

    public String getDateTimeUser(boolean fullDate) {
        return date.getDateTimeUser(fullDate);
    }
}
