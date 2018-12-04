package ch.epfl.sweng.zuluzulu.Structure;

import android.net.Uri;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;

public class Event extends FirebaseStructure {
    private String name;
    private String shortDescription;
    private String longDescription;
    private String channelId;
    private String associationId;

    private EventDate date;
    private String organizer;
    private String place;
    private String bannerUri;
    private String iconUri;
    private String url_place_and_room;
    private String website;
    private String contact;
    private String category;
    private String speaker;

    // TODO: make it in the cloud :)
    private List<String> followers;

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
        this.followers = new ArrayList<>(followers);
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
        followers = new ArrayList<>(data.getStringList("followers"));
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


    public static Comparator<Event> nameComparator() {
        return (o1, o2) -> o1.getName().compareTo(o2.getName());
    }

    public static Comparator<Event> dateComparator() {
        return (o1, o2) -> o1.date.getStartDate().compareTo(o2.date.getStartDate());
    }

    public static Comparator<Event> likeComparator() {
        return (o1, o2) -> o2.getLikes().compareTo(o1.getLikes());
    }

    public static List<String> requiredFields() {
        return Arrays.asList(
                "id", "name", "short_description", "long_description", "category", "icon_uri",
                "banner_uri", "followers", "channel_id", "association_id", "start_date", "end_date",
                "place", "organizer", "url_place_and_room", "website", "contact", "speaker");
    }

    public String getAssociationId() {
        return associationId;
    }

    public String getName() {
        return name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

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

    public String getOrganizer() {
        return organizer;
    }

    public String getPlace() {
        return place;
    }

    // TODO: change with followers.size() when it is implemented
    public Integer getLikes() {
        return followers.size();
    }

    public boolean addFollower(String userId) {
        return !followers.contains(userId) && followers.add(userId);
    }

    public boolean removeFollower(String userId) {
        return followers.contains(userId) && followers.remove(userId);
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

        map.put("followers", followers);
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

    public String getUrlPlaceAndRoom() {
        return url_place_and_room;
    }

    public String getWebsite() {
        return website;
    }

    public String getContact() {
        return contact;
    }

    public String getCategory() {
        return category;
    }

    public String getSpeaker() {
        return speaker;
    }

    public Date getStartDate() {
        return date.getStartDate();
    }

    public Date getEndDate() {
        return date.getEndDate();
    }

    public String getStartDateString() {
        return date.getStartDateString();
    }

    public String getEndDateString() {
        return date.getEndDateString();
    }

    public String getDateTimeUser() {
        return date.getDateTimeUser();
    }
}
